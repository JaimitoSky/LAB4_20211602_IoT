package com.example.lab4_20211602_iot.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab4_20211602_iot.R;
import com.example.lab4_20211602_iot.data.local.Repository;
import com.example.lab4_20211602_iot.data.model.ServiceReminder;
import com.example.lab4_20211602_iot.notif.NotificationHelper;
import com.example.lab4_20211602_iot.ui.adapter.ServiceAdapter;
import com.example.lab4_20211602_iot.ui.dialog.ConfirmDeleteDialog;
import com.example.lab4_20211602_iot.ui.dialog.MarkPaidDialog;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ServiceAdapter.Listener {

    private Repository repo;
    private RecyclerView rv;
    private View tvEmpty;
    private ServiceAdapter adapter;
    private final List<ServiceReminder> data = new ArrayList<>();

    private final ActivityResultLauncher<Intent> editLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    r -> refresh());

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NotificationHelper.ensureChannels(this);

        repo = new Repository(this);
        MaterialToolbar tb = findViewById(R.id.toolbar);
        setSupportActionBar(tb);

        tvEmpty = findViewById(R.id.tvEmpty);
        rv = findViewById(R.id.rvServices);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ServiceAdapter(data, this);
        rv.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.fabAdd);
        fab.setOnClickListener(v -> {
            Intent i = new Intent(this, EditServiceActivity.class);
            editLauncher.launch(i);
        });

        refresh();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void refresh() {
        data.clear();
        data.addAll(repo.getAllServices());
        adapter.notifyDataSetChanged();
        tvEmpty.setVisibility(data.isEmpty() ? View.VISIBLE : View.GONE);
    }

    // ServiceAdapter.Listener
    @Override public void onEdit(ServiceReminder s) {
        Intent i = new Intent(this, EditServiceActivity.class);
        i.putExtra("id", s.id);
        editLauncher.launch(i);
    }

    @Override public void onDelete(ServiceReminder s) {
        ConfirmDeleteDialog.show(this, () -> {
            repo.deleteService(s.id);
            refresh();
        });
    }

    @Override public void onPaid(ServiceReminder s) {
        MarkPaidDialog.show(this, s, () -> {
            Context ctx = getApplicationContext();
            new Repository(ctx).markAsPaid(s.id, System.currentTimeMillis());
            Toast.makeText(this, R.string.paid_done_toast, Toast.LENGTH_SHORT).show();
            refresh();
        });
    }

    // Menú
    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_history) {
            startActivity(new Intent(this, HistoryActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
