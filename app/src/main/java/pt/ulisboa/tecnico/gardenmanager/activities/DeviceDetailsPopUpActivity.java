package pt.ulisboa.tecnico.gardenmanager.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

import pt.ulisboa.tecnico.gardenmanager.databinding.ActivityDeviceDetailsPopUpBinding;

public class DeviceDetailsPopUpActivity extends AppCompatActivity {
    private ActivityDeviceDetailsPopUpBinding binding;
    private int deviceId;
    private String deviceCommonName;

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
        deviceCommonName = receivedIntent.getStringExtra("deviceCommonName");

        binding.activityDeviceDetailsDeviceNameTextView.setText(deviceCommonName);
        binding.activityDeviceDetailsDeviceId.setText("ID: " + deviceId);
        binding.activityDeviceDetailsSeeHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO
            }
        });
    }
}