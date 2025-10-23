package com.example.lab4_20211602_iot.ui;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lab4_20211602_iot.R;
import com.example.lab4_20211602_iot.data.local.Repository;
import com.example.lab4_20211602_iot.data.model.Importance;
import com.example.lab4_20211602_iot.data.model.Periodicity;
import com.example.lab4_20211602_iot.data.model.ServiceReminder;
import com.example.lab4_20211602_iot.util.DateUtils;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EditServiceActivity extends AppCompatActivity {

    private Repository repo;
    private EditText etName, etAmount, etDate;
    private MaterialAutoCompleteTextView spPeriod, spImportance;

    private long editingId = 0L;
    private long dueMs = 0L;

    private int selHour = 9, selMin = 0;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_service);

        repo = new Repository(this);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) MaterialToolbar tb = findViewById(R.id.toolbar);
        if (tb != null) setSupportActionBar(tb);

        etName = findViewById(R.id.etName);
        etAmount = findViewById(R.id.etAmount);
        etDate = findViewById(R.id.etDate);
        spPeriod = findViewById(R.id.spPeriodicity);
        spImportance = findViewById(R.id.spImportance);
        Button btnSave = findViewById(R.id.btnSave);

        spPeriod.setAdapter(ArrayAdapter.createFromResource(
                this, R.array.periodicity_labels, android.R.layout.simple_list_item_1));
        spImportance.setAdapter(ArrayAdapter.createFromResource(
                this, R.array.importance_labels, android.R.layout.simple_list_item_1));

        etDate.setOnClickListener(v -> {
            final Calendar base = Calendar.getInstance();
            if (dueMs > 0) base.setTimeInMillis(dueMs);

            int y = base.get(Calendar.YEAR);
            int m = base.get(Calendar.MONTH);
            int d = base.get(Calendar.DAY_OF_MONTH);

            new DatePickerDialog(this, (view, yy, mm, dd) -> {
                final Calendar now = Calendar.getInstance();
                int defaultH = (dueMs > 0) ? base.get(Calendar.HOUR_OF_DAY) : now.get(Calendar.HOUR_OF_DAY);
                int defaultMin = (dueMs > 0) ? base.get(Calendar.MINUTE) : now.get(Calendar.MINUTE);

                new TimePickerDialog(this, (tp, hour, minute) -> {
                    selHour = hour;
                    selMin = minute;

                    Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.YEAR, yy);
                    cal.set(Calendar.MONTH, mm);
                    cal.set(Calendar.DAY_OF_MONTH, dd);
                    cal.set(Calendar.HOUR_OF_DAY, selHour);
                    cal.set(Calendar.MINUTE, selMin);
                    cal.set(Calendar.SECOND, 0);
                    cal.set(Calendar.MILLISECOND, 0);
                    dueMs = cal.getTimeInMillis();

                    etDate.setText(formatDateTime(dueMs));
                }, defaultH, defaultMin, true).show();

            }, y, m, d).show();
        });

        // Modo edición
        editingId = getIntent().getLongExtra("id", 0L);
        if (editingId != 0L) {
            List<ServiceReminder> all = repo.getAllServices();
            for (ServiceReminder s : all) {
                if (s.id == editingId) {
                    etName.setText(s.nombre);
                    etAmount.setText(String.valueOf(s.monto));
                    dueMs = s.fechaVencimientoMs;

                    etDate.setText(formatDateTime(dueMs));

                    Calendar c = Calendar.getInstance();
                    c.setTimeInMillis(dueMs);
                    selHour = c.get(Calendar.HOUR_OF_DAY);
                    selMin = c.get(Calendar.MINUTE);

                    spPeriod.setText(labelOf(s.periodicidad), false);
                    spImportance.setText(labelOf(s.importancia), false);
                    break;
                }
            }
        }

        btnSave.setOnClickListener(v -> save());
    }

    private void save() {
        String name = etName.getText().toString().trim();
        String amountStr = etAmount.getText().toString().trim();
        String periodStr = spPeriod.getText().toString().trim();
        String impStr = spImportance.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(amountStr) || dueMs == 0L
                || TextUtils.isEmpty(periodStr) || TextUtils.isEmpty(impStr)) {
            etName.setError(TextUtils.isEmpty(name) ? getString(R.string.hint_name) : null);
            etAmount.setError(TextUtils.isEmpty(amountStr) ? getString(R.string.hint_amount) : null);
            if (dueMs == 0L) etDate.setError(getString(R.string.hint_date));
            return;
        }

        double amount = Double.parseDouble(amountStr);
        ServiceReminder s = new ServiceReminder();
        s.id = editingId;
        s.nombre = name;
        s.monto = amount;
        s.fechaVencimientoMs = dueMs;
        s.periodicidad = parsePeriod(periodStr);
        s.importancia = parseImportance(impStr);
        s.activo = true;

        repo.upsertService(s);
        finish();
    }

    private String labelOf(Periodicity p) {
        switch (p) {
            case MENSUAL: return "Mensual";
            case BIMESTRAL: return "Bimestral";
            case TRIMESTRAL: return "Trimestral";
            case ANUAL: return "Anual";
            default: return "Una vez";
        }
    }

    private String labelOf(Importance i) {
        switch (i) {
            case ALTA: return "Alta";
            case MEDIA: return "Media";
            default: return "Baja";
        }
    }

    private Periodicity parsePeriod(String s) {
        if (s.equalsIgnoreCase("Mensual")) return Periodicity.MENSUAL;
        if (s.equalsIgnoreCase("Bimestral")) return Periodicity.BIMESTRAL;
        if (s.equalsIgnoreCase("Trimestral")) return Periodicity.TRIMESTRAL;
        if (s.equalsIgnoreCase("Anual")) return Periodicity.ANUAL;
        return Periodicity.UNA_VEZ;
    }

    private Importance parseImportance(String s) {
        if (s.equalsIgnoreCase("Alta")) return Importance.ALTA;
        if (s.equalsIgnoreCase("Media")) return Importance.MEDIA;
        return Importance.BAJA;
    }

    private String formatDateTime(long millis) {
        String d = DateUtils.formatDate(millis); // dd/MM/yyyy
        String h = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date(millis));
        return d + " • " + h;
    }
}
