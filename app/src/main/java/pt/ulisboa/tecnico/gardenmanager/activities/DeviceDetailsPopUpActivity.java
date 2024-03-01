package pt.ulisboa.tecnico.gardenmanager.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import pt.ulisboa.tecnico.gardenmanager.databinding.ActivityDeviceDetailsPopUpBinding;
import pt.ulisboa.tecnico.gardenmanager.domain.DeviceType;

public class DeviceDetailsPopUpActivity extends AppCompatActivity {
    private final static String TAG = "DeviceDetailsPopUpActivity";

    private ActivityDeviceDetailsPopUpBinding binding;
    private int deviceId;
    private String deviceCommonName;
    private DeviceType deviceType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDeviceDetailsPopUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);

        this.getWindow().setLayout((int) (dm.widthPixels * 0.5), (int) (dm.widthPixels * 0.5));

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

        binding.activityDeviceDetailsDeviceNameTextView.setText(deviceCommonName);
        binding.activityDeviceDetailsDeviceId.setText("ID: " + deviceId);
        binding.activityDeviceDetailsSeeHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DeviceDetailsPopUpActivity.this, ReadingsHistoryActivity.class);
                intent.putExtra("deviceId", deviceId);
                intent.putExtra("deviceTypeString", deviceType.name());

                startActivity(intent);
            }
        });
    }
}