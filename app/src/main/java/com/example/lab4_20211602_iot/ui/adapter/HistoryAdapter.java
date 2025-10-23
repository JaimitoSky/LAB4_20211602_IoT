package com.example.lab4_20211602_iot.ui.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab4_20211602_iot.R;
import com.example.lab4_20211602_iot.data.model.PaymentHistoryItem;
import com.example.lab4_20211602_iot.util.DateUtils;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.VH> {

    private final List<PaymentHistoryItem> data;
    public HistoryAdapter(List<PaymentHistoryItem> d) { data = d; }

    @NonNull @Override public VH onCreateViewHolder(@NonNull ViewGroup p, int v) {
        return new VH(LayoutInflater.from(p.getContext()).inflate(R.layout.item_history, p, false));
    }

    @SuppressLint("SetTextI18n")
    @Override public void onBindViewHolder(@NonNull VH h, int pos) {
        PaymentHistoryItem it = data.get(pos);
        h.tvTitle.setText(it.nombreServicio + " • S/. " + it.montoPagado);
        String sub = "Pagado el " + DateUtils.formatDate(it.fechaPagoMs)
                + " • " + it.anticipacionDias + " días antes";
        h.tvSubtitle.setText(sub);
    }

    @Override public int getItemCount() { return data.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvTitle, tvSubtitle;
        VH(@NonNull View v) {
            super(v);
            tvTitle = v.findViewById(R.id.tvHistTitle);
            tvSubtitle = v.findViewById(R.id.tvHistSubtitle);
        }
    }
}
