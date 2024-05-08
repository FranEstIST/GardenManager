package pt.ulisboa.tecnico.gardenmanager.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import pt.ulisboa.tecnico.gardenmanager.GlobalClass;
import pt.ulisboa.tecnico.gardenmanager.R;
import pt.ulisboa.tecnico.gardenmanager.adapters.ReadingsListAdapter;
import pt.ulisboa.tecnico.gardenmanager.databinding.ActivityReadingsHistoryBinding;
import pt.ulisboa.tecnico.gardenmanager.domain.DeviceType;
import pt.ulisboa.tecnico.gardenmanager.domain.Reading;

public class ReadingsHistoryActivity extends AppCompatActivity {
    private static final String TAG = "ReadingsHistoryActivity";

    private GlobalClass globalClass;

    private int deviceId;
    private DeviceType deviceType;

    private ActivityReadingsHistoryBinding binding;

    private RecyclerView readingsListRecyclerView;
    private TextView readingsListStatusTextView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readings_history);

        globalClass = (GlobalClass) getApplicationContext();

        Intent receivedIntent = getIntent();

        deviceId = receivedIntent.getIntExtra("deviceId", -1);

        if(deviceId == -1) {
            Log.e(TAG, "No device id provided in intent");
            finish();
            return;
        }

        String deviceTypeString = receivedIntent.getStringExtra("deviceTypeString");

        if(deviceTypeString == null) {
            Log.e(TAG, "No device type provided in intent");
            finish();
            return;
        }

        deviceType = DeviceType.valueOf(deviceTypeString);

        binding = ActivityReadingsHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        TextView appBarTitleTextView = binding.appBarTitle;
        appBarTitleTextView.setText(R.string.readings_history);

        readingsListRecyclerView = binding.readingsListRecyclerView;

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        readingsListRecyclerView.setLayoutManager(layoutManager);

        readingsListStatusTextView = binding.readingsListStatusTextView;

        binding.deleteReadingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                globalClass.getGardenDatabase()
                        .readingDao()
                        .deleteReadingsBySenderId(deviceId)
                        .observeOn(Schedulers.newThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(() -> {
                            runOnUiThread(() -> {
                                readingsListStatusTextView.setText(R.string.no_readings_available);
                                readingsListStatusTextView.setVisibility(View.VISIBLE);
                                readingsListStatusTextView.setVisibility(View.GONE);
                            });
                        });
            }
        });

        globalClass.getGardenDatabase()
                .readingDao()
                .getAllBySenderId(deviceId)
                .observeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new DisposableSingleObserver<List<Reading>>() {
                    @Override
                    public void onSuccess(@NonNull List<Reading> readings) {
                        runOnUiThread(() -> {
                            List<Reading> orderedReadings = readings.stream()
                                    .sorted((readingOne, readingTwo) -> (int) (readingTwo.getTimestamp() - readingOne.getTimestamp()))
                                    .collect(Collectors.toList());

                            ReadingsListAdapter readingsListAdapter = new ReadingsListAdapter(orderedReadings, deviceType);
                            readingsListRecyclerView.setAdapter(readingsListAdapter);

                            if(readings.size() == 0) {
                                readingsListStatusTextView.setText(R.string.no_readings_available);
                            } else {
                                readingsListStatusTextView.setVisibility(View.GONE);
                                readingsListRecyclerView.setVisibility(View.VISIBLE);
                            }
                        });
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(TAG, "Readings could not be loaded", e);
                        finish();
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(@androidx.annotation.NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) // Press Back Icon
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}