package com.example.lab4_20211602_iot.data.local;

import android.content.Context;

import com.example.lab4_20211602_iot.data.model.PaymentHistoryItem;
import com.example.lab4_20211602_iot.data.model.ServiceReminder;
import com.example.lab4_20211602_iot.util.IdGenerator;
import com.example.lab4_20211602_iot.util.JsonUtils;
import com.example.lab4_20211602_iot.notif.AlarmScheduler;
import com.example.lab4_20211602_iot.util.DateUtils;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Repository {

    private final PrefsDataSource prefs;
    private final IdGenerator idGen;
    private final Type listServiceType = JsonUtils.listTypeOf(ServiceReminder.class);
    private final Type listHistoryType = JsonUtils.listTypeOf(PaymentHistoryItem.class);

    public Repository(Context ctx) {
        this.prefs = new PrefsDataSource(ctx.getApplicationContext());
        this.idGen = new IdGenerator(prefs);
    }

    public List<ServiceReminder> getAllServices() {
        String json = prefs.getString(PrefsKeys.SERVICES_JSON);
        List<ServiceReminder> list = JsonUtils.fromJson(json, listServiceType);
        return list != null ? list : new ArrayList<>();
    }



    public List<PaymentHistoryItem> getAllHistory() {
        String json = prefs.getString(PrefsKeys.HISTORY_JSON);
        List<PaymentHistoryItem> list = JsonUtils.fromJson(json, listHistoryType);
        return list != null ? list : new ArrayList<>();
    }

    public void addHistory(PaymentHistoryItem item) {
        if (item.id == 0) item.id = idGen.nextHistoryId();
        List<PaymentHistoryItem> list = getAllHistory();
        list.add(item);
        prefs.putString(PrefsKeys.HISTORY_JSON, JsonUtils.toJson(list));
    }

    public void replaceHistory(List<PaymentHistoryItem> items) {
        prefs.putString(PrefsKeys.HISTORY_JSON, JsonUtils.toJson(items));
    }

    public void upsertService(ServiceReminder s) {
        List<ServiceReminder> list = getAllServices();
        if (s.id == 0) s.id = idGen.nextServiceId();
        boolean updated = false;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).id == s.id) { list.set(i, s); updated = true; break; }
        }
        if (!updated) list.add(s);
        prefs.putString(PrefsKeys.SERVICES_JSON, JsonUtils.toJson(list));

        if (s.activo && s.fechaVencimientoMs > System.currentTimeMillis()) {
            AlarmScheduler.schedule24hBefore(appCtx(), s);
        }
    }

    public void deleteService(long id) {
        List<ServiceReminder> list = getAllServices();
        for (Iterator<ServiceReminder> it = list.iterator(); it.hasNext(); ) {
            if (it.next().id == id) { it.remove(); break; }
        }
        prefs.putString(PrefsKeys.SERVICES_JSON, JsonUtils.toJson(list));
        AlarmScheduler.cancel(appCtx(), id);
    }

    private Context appCtx() {
        try {
            java.lang.reflect.Field f = PrefsDataSource.class.getDeclaredField("prefs");
            f.setAccessible(true);
            android.content.SharedPreferences sp = (android.content.SharedPreferences) f.get(prefs);
            assert sp != null;
            android.content.Context ctx = (android.content.Context)
                    sp.getClass().getMethod("getContext").invoke(sp);
            assert ctx != null;
            return ctx.getApplicationContext();
        } catch (Throwable t) {
            return null;
        }
    }
}
