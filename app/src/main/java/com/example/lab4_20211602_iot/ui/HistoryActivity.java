package com.example.lab4_20211602_iot.ui;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab4_20211602_iot.R;
import com.example.lab4_20211602_iot.data.local.Repository;
import com.example.lab4_20211602_iot.data.model.PaymentHistoryItem;
import com.example.lab4_20211602_iot.ui.adapter.HistoryAdapter;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private final List<PaymentHistoryItem> data = new ArrayList<>();
    private View tvEmpty;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        tvEmpty = findViewById(R.id.tvEmptyHistory);
        RecyclerView rv = findViewById(R.id.rvHistory);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(new HistoryAdapter(data));

        refresh();
    }

    private void refresh() {
        Repository repo = new Repository(this);
        data.clear();
        data.addAll(repo.getAllHistory());
        tvEmpty.setVisibility(data.isEmpty() ? View.VISIBLE : View.GONE);
    }
}
