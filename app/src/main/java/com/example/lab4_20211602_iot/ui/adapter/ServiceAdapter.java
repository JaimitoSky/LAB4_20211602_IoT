package com.example.lab4_20211602_iot.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab4_20211602_iot.R;
import com.example.lab4_20211602_iot.data.model.ServiceReminder;
import com.example.lab4_20211602_iot.util.DateUtils;

import java.util.List;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.VH> {

    public interface Listener {
        void onEdit(ServiceReminder s);
        void onDelete(ServiceReminder s);
        void onPaid(ServiceReminder s);
    }

    private final List<ServiceReminder> data;
    private final Listener listener;

    public ServiceAdapter(List<ServiceReminder> data, Listener l) {
        this.data = data; this.listener = l;
    }

    @NonNull @Override public VH onCreateViewHolder(@NonNull ViewGroup p, int v) {
        View view = LayoutInflater.from(p.getContext()).inflate(R.layout.item_service, p, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int pos) {
        ServiceReminder s = data.get(pos);

        h.tvName.setText(s.nombre);

        String hora = new java.text.SimpleDateFormat("HH:mm",
                java.util.Locale.getDefault()).format(new java.util.Date(s.fechaVencimientoMs));

        String sub = "S/. " + String.format(java.util.Locale.US, "%.2f", s.monto)
                + " • vence " + DateUtils.formatDate(s.fechaVencimientoMs)
                + " " + hora;

        h.tvAmountDate.setText(sub);

        h.btnEdit.setOnClickListener(v -> listener.onEdit(s));
        h.btnDelete.setOnClickListener(v -> listener.onDelete(s));
        h.btnPaid.setOnClickListener(v -> listener.onPaid(s));
    }


    @Override public int getItemCount() { return data.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvName, tvAmountDate;
        Button btnEdit, btnPaid, btnDelete;
        VH(@NonNull View v) {
            super(v);
            tvName = v.findViewById(R.id.tvName);
            tvAmountDate = v.findViewById(R.id.tvAmountDate);
            btnEdit = v.findViewById(R.id.btnEdit);
            btnPaid = v.findViewById(R.id.btnPaid);
            btnDelete = v.findViewById(R.id.btnDelete);
        }
    }
}
