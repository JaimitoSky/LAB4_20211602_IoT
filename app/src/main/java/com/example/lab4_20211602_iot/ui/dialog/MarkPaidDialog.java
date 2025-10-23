package com.example.lab4_20211602_iot.ui.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.example.lab4_20211602_iot.R;
import com.example.lab4_20211602_iot.data.model.ServiceReminder;
import com.example.lab4_20211602_iot.util.DateUtils;

public final class MarkPaidDialog {
    private MarkPaidDialog(){}

    public interface OnOk { void run(); }

    public static void show(Context ctx, ServiceReminder s, OnOk onOk) {
        View content = LayoutInflater.from(ctx).inflate(R.layout.dialog_mark_paid, null, false);
        TextView tv = content.findViewById(R.id.tvResumen);
        String resumen = ctx.getString(R.string.paid_summary, s.nombre, s.monto, DateUtils.formatDate(s.fechaVencimientoMs));
        tv.setText(resumen);

        new AlertDialog.Builder(ctx)
                .setTitle(R.string.confirm_paid_title)
                .setView(content)
                .setPositiveButton(R.string.ok, (d, w) -> { if (onOk != null) onOk.run(); })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }
}
