package com.example.lab4_20211602_iot.ui.dialog;

import android.content.Context;

import androidx.appcompat.app.AlertDialog;

import com.example.lab4_20211602_iot.R;

public final class ConfirmDeleteDialog {
    private ConfirmDeleteDialog(){}

    public interface OnOk { void run(); }

    public static void show(Context ctx, OnOk onOk) {
        new AlertDialog.Builder(ctx)
                .setView(R.layout.dialog_confirm_delete)
                .setPositiveButton(R.string.ok, (d, w) -> { if (onOk != null) onOk.run(); })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }
}
