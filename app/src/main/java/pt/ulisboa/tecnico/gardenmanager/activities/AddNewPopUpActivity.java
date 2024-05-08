package pt.ulisboa.tecnico.gardenmanager.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;

import pt.ulisboa.tecnico.gardenmanager.R;
import pt.ulisboa.tecnico.gardenmanager.constants.ViewModes;
import pt.ulisboa.tecnico.gardenmanager.databinding.ActivityAddNewPopUpBinding;
import pt.ulisboa.tecnico.gardenmanager.fragments.AddOptionsFragment;
import pt.ulisboa.tecnico.gardenmanager.fragments.CreateNewFragment;
import pt.ulisboa.tecnico.gardenmanager.GlobalClass;

public class AddNewPopUpActivity extends AppCompatActivity implements AddOptionsFragment.SelectAddOptionListener {
    public static final String TAG = "AddNewDevicePopUpActivity";
    public ActivityAddNewPopUpBinding binding;
    private GlobalClass globalClass;
    private int mode;
    private String deviceTypeString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddNewPopUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent receivedIntent = getIntent();
        mode = receivedIntent.getIntExtra("mode", -1);

        if(mode == ViewModes.DEVICE_MODE) {
            deviceTypeString = receivedIntent.getStringExtra("deviceTypeString");
        }

        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);

        this.getWindow().setLayout((int) (dm.widthPixels * 0.5), (int) (dm.heightPixels * 0.5));

        Fragment addOptionsFragment = AddOptionsFragment.newInstance(mode);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.addNewFragmentContainerView, addOptionsFragment)
                .commit();
    }

    @Override
    public void onAddExistingClick() {
        // This code calls the search activity directly
        Intent intent = new Intent(this, SearchActivity.class);
        intent.putExtra("mode", mode);

        if(mode == ViewModes.DEVICE_MODE) {
            intent.putExtra("deviceTypeString", deviceTypeString);
        }

        startActivity(intent);

        finish();
    }

    @Override
    public void onCreateNewClick() {
        Fragment createNewFragment;

        if(mode == ViewModes.DEVICE_MODE) {
            createNewFragment = CreateNewFragment.newInstance(mode, deviceTypeString);
        } else {
            createNewFragment = CreateNewFragment.newInstance(mode);
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.addNewFragmentContainerView, createNewFragment)
                .commit();
    }
}