package pt.ulisboa.tecnico.gardenmanager.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Toast;

import pt.ulisboa.tecnico.gardenmanager.GlobalClass;
import pt.ulisboa.tecnico.gardenmanager.R;
import pt.ulisboa.tecnico.gardenmanager.databinding.ActivityEditSettingPopUpBinding;

public class EditSettingPopUpActivity extends AppCompatActivity {
    private ActivityEditSettingPopUpBinding binding;

    private GlobalClass globalClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        binding = ActivityEditSettingPopUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);

        this.getWindow().setLayout((int) (dm.widthPixels * 0.6), (int) (dm.heightPixels * 0.5));

        globalClass = (GlobalClass) getApplicationContext();

        Intent receivedIntent = getIntent();

        if (!receivedIntent.hasExtra("setting-type")) {
            finish();
            return;
        }

        SettingType settingType = (SettingType) receivedIntent.getSerializableExtra("setting-type");

        switch (settingType) {
            case SERVER_URL:
                binding.changeSettingValueTextView.setText(getResources().getText(R.string.change)
                        + " "
                        + getResources().getText(R.string.server_url));
                binding.newSettingValueInputText.setHint(getResources().getText(R.string.enter_new)
                        + " "
                        + getResources().getText(R.string.server_url));
                binding.newSettingValueInputText.setText(globalClass.getServerURL());
                break;
            case GM_ID:
                binding.changeSettingValueTextView.setText(getResources().getText(R.string.change)
                        + " "
                        + getResources().getText(R.string.garden_manager_id));
                binding.newSettingValueInputText.setHint(getResources().getText(R.string.enter_new)
                        + " "
                        + getResources().getText(R.string.garden_manager_id));
                binding.newSettingValueInputText.setText(globalClass.getGardenManagerId() + "");
                break;
            case SERVER_POLLING_INTERVAL:
                binding.changeSettingValueTextView.setText(getResources().getText(R.string.change)
                        + " "
                        + getResources().getText(R.string.server_polling_interval));
                binding.newSettingValueInputText.setHint(getResources().getText(R.string.enter_new)
                        + " "
                        + getResources().getText(R.string.server_polling_interval));
                binding.newSettingValueInputText.setText(globalClass.getServerPollingInterval() + "");
                break;
        }

        binding.submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.newSettingValueInputText.getText() == null) {
                    return;
                }

                String newSettingValueString = binding.newSettingValueInputText.getText().toString();

                switch (settingType) {
                    case SERVER_URL:
                        globalClass.setServerURL(newSettingValueString);
                        break;
                    case GM_ID:
                        try {
                            int newGardenManagerId = Integer.valueOf(newSettingValueString);
                            globalClass.setGardenManagerId(newGardenManagerId);
                        } catch (NumberFormatException e) {
                            Toast.makeText(EditSettingPopUpActivity.this, "Invalid id. It should be an integer.", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case SERVER_POLLING_INTERVAL:
                        try {
                            int newServerPollingInterval = Integer.valueOf(newSettingValueString);
                            globalClass.setServerPollingInterval(newServerPollingInterval);
                        } catch (NumberFormatException e) {
                            Toast.makeText(EditSettingPopUpActivity.this, "Invalid interval. It should be an integer.", Toast.LENGTH_SHORT).show();
                        }
                        break;
                }

                Intent resultIntent = new Intent();

                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }

    public enum SettingType {
        SERVER_URL,
        GM_ID,
        SERVER_POLLING_INTERVAL,
    }
}


