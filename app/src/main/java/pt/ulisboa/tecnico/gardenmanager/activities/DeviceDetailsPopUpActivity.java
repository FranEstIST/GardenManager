package pt.ulisboa.tecnico.gardenmanager.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;

import io.reactivex.rxjava3.schedulers.Schedulers;
import pt.ulisboa.tecnico.gardenmanager.GlobalClass;
import pt.ulisboa.tecnico.gardenmanager.R;
import pt.ulisboa.tecnico.gardenmanager.databinding.ActivityDeviceDetailsPopUpBinding;
import pt.ulisboa.tecnico.gardenmanager.domain.DeviceType;
import pt.ulisboa.tecnico.gardenmanager.domain.Reading;
import pt.ulisboa.tecnico.gardenmanager.network.WithoutNetService;

public class DeviceDetailsPopUpActivity extends AppCompatActivity {
    private final static String TAG = "DeviceDetailsPopUpActivity";

    private GlobalClass globalClass;
    private WithoutNetService withoutNetService;

    private ActivityDeviceDetailsPopUpBinding binding;
    private int deviceId;
    private String deviceCommonName;
    private DeviceType deviceType;

    private boolean toggled;
    private int progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDeviceDetailsPopUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        getWindow().setLayout((int) (dm.widthPixels * 0.5), (int) (dm.widthPixels * 0.5));

        globalClass = (GlobalClass) getApplicationContext();
        withoutNetService = new WithoutNetService(globalClass);

        Intent receivedIntent = getIntent();

        deviceId = receivedIntent.getIntExtra("deviceId", -1);

        if(deviceId == -1) {
            Log.e(TAG, "No device id provided in intent");
            finish();
            return;
        }

        deviceCommonName = receivedIntent.getStringExtra("deviceCommonName");

        if(deviceCommonName == null) {
            Log.e(TAG, "No device common name provided in intent");
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

        switch(deviceType) {
            case TEMPERATURE_SENSOR:
            case LIGHT_SENSOR:
            case HUMIDITY_SENSOR:
                binding.activityDeviceDetailsSeeHistoryButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(DeviceDetailsPopUpActivity.this, ReadingsHistoryActivity.class);
                        intent.putExtra("deviceId", deviceId);
                        intent.putExtra("deviceTypeString", deviceType.name());

                        startActivity(intent);

                        DeviceDetailsPopUpActivity.this.finish();
                    }
                });
                break;
            case MONITOR:
                binding.activityDeviceDetailsSeeHistoryButton.setVisibility(View.GONE);
                binding.activityDeviceDetailsToggleButton.setVisibility(View.VISIBLE);

                if(!receivedIntent.hasExtra("toggled")) {
                    finish();
                    return;
                }

                toggled = receivedIntent.getBooleanExtra("toggled", false);

                updateToggleButton();

                binding.activityDeviceDetailsToggleButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toggled = !toggled;

                        updateToggleButton();

                        Log.d(TAG, "Toggled: " + toggled);

                        long toggledLong = toggled ? 1 : 0;

                        Reading reading = new Reading(System.currentTimeMillis(), deviceId, toggledLong);

                        globalClass.getGardenDatabase()
                                .readingDao()
                                .insertAll(reading)
                                .observeOn(Schedulers.newThread())
                                .subscribeOn(Schedulers.newThread())
                                .subscribe();

                        WithoutNetService.WithoutNetServiceResponseListener sendMessageResponseListener = new WithoutNetService.WithoutNetServiceResponseListener() {
                            @Override
                            public void onResponse(Object response) {
                                Log.d(TAG, "Successfully sent message to node with id " + deviceId);
                            }

                            @Override
                            public void onError(String errorMessage) {
                                Log.e(TAG, "Error sending message: " + errorMessage);
                            }
                        };

                        withoutNetService.sendMessage(deviceId, toggledLong, sendMessageResponseListener);
                    }
                });
                break;
            case LAMP:
            case SPRINKLER:
                binding.activityDeviceDetailsSeeHistoryButton.setVisibility(View.GONE);
                binding.activityDeviceDetailsSeekBar.setVisibility(View.VISIBLE);

                if(!receivedIntent.hasExtra("progress")) {
                    finish();
                    return;
                }

                progress = (int) receivedIntent.getLongExtra("progress", 0);

                if(progress == -1) progress = 0;

                binding.activityDeviceDetailsSeekBar.setProgress(progress);

                binding.activityDeviceDetailsSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        DeviceDetailsPopUpActivity.this.progress = progress;
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        Log.d(TAG, "Progress: " + progress);

                        Reading reading = new Reading(System.currentTimeMillis(), deviceId, progress);

                        globalClass.getGardenDatabase()
                                .readingDao()
                                .insertAll(reading)
                                .observeOn(Schedulers.newThread())
                                .subscribeOn(Schedulers.newThread())
                                .subscribe();

                        WithoutNetService.WithoutNetServiceResponseListener sendMessageResponseListener = new WithoutNetService.WithoutNetServiceResponseListener() {
                            @Override
                            public void onResponse(Object response) {
                                Log.d(TAG, "Successfully sent message to node with id " + deviceId);
                            }

                            @Override
                            public void onError(String errorMessage) {
                                Log.e(TAG, "Error sending message: " + errorMessage);
                            }
                        };

                        withoutNetService.sendMessage(deviceId, progress, sendMessageResponseListener);
                    }
                });
                break;

        }

        binding.activityDeviceDetailsDeviceNameTextView.setText(deviceCommonName);
        binding.activityDeviceDetailsDeviceId.setText("ID: " + deviceId);
        binding.activityDeviceDetailsSeeHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DeviceDetailsPopUpActivity.this, ReadingsHistoryActivity.class);
                intent.putExtra("deviceId", deviceId);
                intent.putExtra("deviceTypeString", deviceType.name());

                startActivity(intent);

                DeviceDetailsPopUpActivity.this.finish();
            }
        });
    }

    private void updateToggleButton() {
        binding.activityDeviceDetailsToggleButton.setChecked(toggled);

        if(toggled) {
            binding.activityDeviceDetailsToggleButton
                    .setBackgroundColor(getResources()
                            .getColor(R.color.vibrant_plant_green));
        } else {
            binding.activityDeviceDetailsToggleButton
                    .setBackgroundColor(getResources()
                            .getColor(R.color.white));
        }
    }
}