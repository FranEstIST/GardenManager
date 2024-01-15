package pt.ulisboa.tecnico.gardenmanager.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.observers.DisposableCompletableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import pt.ulisboa.tecnico.gardenmanager.R;
import pt.ulisboa.tecnico.gardenmanager.databinding.ActivityAddNewDevicePopUp2Binding;
import pt.ulisboa.tecnico.gardenmanager.databinding.AddOptionsButtonBinding;
import pt.ulisboa.tecnico.gardenmanager.domain.DeviceType;
import pt.ulisboa.tecnico.gardenmanager.utils.DeviceTypeToReadingTypeConverter;
import pt.ulisboa.tecnico.gardenmanager.GlobalClass;
import pt.ulisboa.tecnico.gardenmanager.databinding.ActivityAddNewDevicePopUpBinding;
import pt.ulisboa.tecnico.gardenmanager.db.GardenDatabase;
import pt.ulisboa.tecnico.gardenmanager.domain.Device;
import pt.ulisboa.tecnico.gardenmanager.domain.Reading;
import pt.ulisboa.tecnico.gardenmanager.domain.ReadingType;

public class AddNewDevicePopUpActivity extends AppCompatActivity {
    public static final String TAG = "AddNewDevicePopUpActivity";
    public ActivityAddNewDevicePopUp2Binding binding;
    private GlobalClass globalClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddNewDevicePopUp2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);

        this.getWindow().setLayout((int) (dm.widthPixels * 0.5), (int) (dm.heightPixels * 0.5));

        AddOptionsButtonBinding addExistingDeviceButtonBinding = binding.addExistingDeviceButton;

        TextView addExistingDeviceTextView = addExistingDeviceButtonBinding.addOptionsButtonTextView;
        ImageView addExistingDeviceIconImageView = addExistingDeviceButtonBinding.addOptionsButtonIconImageView;

        addExistingDeviceTextView.setText(R.string.add_existing_device);
        addExistingDeviceIconImageView.setImageResource(R.drawable.ic_baseline_search_black_24);

        AddOptionsButtonBinding createNewDeviceButtonBinding = binding.createNewDeviceButton;

        TextView createNewDeviceTextView = createNewDeviceButtonBinding.addOptionsButtonTextView;
        ImageView createNewDeviceIconImageView = createNewDeviceButtonBinding.addOptionsButtonIconImageView;

        createNewDeviceTextView.setText(R.string.create_new_device);
        createNewDeviceIconImageView.setImageResource(R.drawable.create_new_icon_v2);

        /*TextView editDeviceIdTextView = binding.editDeviceIdTextView;
        TextView editDeviceNameTextView = binding.editDeviceNameTextView;
        TextView editLatestReadingTextView = binding.editReadingValueTextView;
        Button submitButton = binding.submitButton;

        String deviceTypeString = getIntent().getStringExtra("deviceType");

        DeviceType deviceType;

        if (deviceTypeString != null) {
            try {
                deviceType = DeviceType.valueOf(deviceTypeString);
            } catch (IllegalArgumentException e) {
                // TODO: Handle this exception better
                deviceType = DeviceType.LIGHT_SENSOR;
            }
        } else {
            deviceType = DeviceType.LIGHT_SENSOR;
        }

        DeviceType finalDeviceType = deviceType;

        this.globalClass = (GlobalClass) this.getApplicationContext();

        GardenDatabase gardenDatabase = this.globalClass.getGardenDatabase();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String deviceId = editDeviceIdTextView.getText().toString();
                int deviceIdInt;

                try {
                    deviceIdInt = Integer.parseInt(deviceId);
                } catch (NumberFormatException e) {
                    Log.e(TAG, "Invalid device Id");
                    return;
                }

                String deviceName = editDeviceNameTextView.getText().toString();
                String latestReadingString = editLatestReadingTextView.getText().toString();

                Device newDevice = new Device(deviceIdInt, deviceName, finalDeviceType);

                int currentGardenId = globalClass.getCurrentGardenId();

                if(currentGardenId != -1) {
                    newDevice.setParentGardenId(currentGardenId);
                }

                long readingLong;

                try {
                    readingLong = Long.parseLong(latestReadingString);
                } catch (NumberFormatException e) {
                    Log.e(TAG, "Invalid reading");
                    return;
                }

                ReadingType readingType = DeviceTypeToReadingTypeConverter.convertToReadingType(finalDeviceType);

                Reading latestReading = new Reading(deviceIdInt, readingLong, readingType);

                // Save the device and latest reading to the db

                gardenDatabase.deviceDao().insertAll(newDevice)
                        .observeOn(Schedulers.newThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new DisposableCompletableObserver() {
                            @Override
                            public void onComplete() {
                                Log.d(TAG, "Added device");
                                gardenDatabase.readingDao().insertAll(latestReading)
                                        .observeOn(Schedulers.newThread())
                                        .subscribeOn(Schedulers.io())
                                        .subscribe(new DisposableCompletableObserver() {
                                            @Override
                                            public void onComplete() {
                                                Log.d(TAG, "Added reading");
                                            }

                                            @Override
                                            public void onError(@NonNull Throwable e) {
                                                e.printStackTrace();
                                            }
                                        });
                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                            }
                        });
            }
        });*/
    }
}