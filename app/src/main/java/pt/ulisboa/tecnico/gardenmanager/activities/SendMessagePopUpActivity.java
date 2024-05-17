package pt.ulisboa.tecnico.gardenmanager.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import pt.ulisboa.tecnico.gardenmanager.databinding.ActivitySendMessagePopUpBinding;
import pt.ulisboa.tecnico.gardenmanager.domain.DeviceType;

public class SendMessagePopUpActivity extends AppCompatActivity {
    private static final String TAG = "SendMessagePopUpActivity";

    private ActivitySendMessagePopUpBinding binding;

    private DeviceType deviceType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySendMessagePopUpBinding.inflate(getLayoutInflater(), null, false);
        setContentView(binding.getRoot());

        //if(deviceType)

        deviceType = getIntent().getParcelableExtra("device-type");

        switch(deviceType) {
            case MONITOR:
                //deviceType
                break;
            case LAMP:

                break;
            case SPRINKLER:

                break;
        }
    }

}