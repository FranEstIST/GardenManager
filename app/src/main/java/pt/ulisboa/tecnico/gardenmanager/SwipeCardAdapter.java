package pt.ulisboa.tecnico.gardenmanager;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import pt.ulisboa.tecnico.gardenmanager.db.GardenDatabase;
import pt.ulisboa.tecnico.gardenmanager.domain.Device;
import pt.ulisboa.tecnico.gardenmanager.domain.DeviceWithReadings;

public class SwipeCardAdapter extends FragmentStateAdapter {
    private DeviceType deviceType;
    private static final String TAG = "SwipeCardAdapter";
    private final CompositeDisposable disposable = new CompositeDisposable();
    private Context context;

    private ArrayList<DeviceWithReadings> devicesWithReadings;

    public SwipeCardAdapter(@NonNull Fragment fragment, DeviceType deviceType) {
        super(fragment);
        this.context = fragment.getContext();
        this.deviceType = deviceType;
        this.devicesWithReadings = new ArrayList<>();
    }

    public SwipeCardAdapter(@NonNull Fragment fragment, DeviceType deviceType, ArrayList<DeviceWithReadings> devicesWithReadings) {
        super(fragment);
        this.context = fragment.getContext();
        this.deviceType = deviceType;
        this.devicesWithReadings = devicesWithReadings;
    }

    public SwipeCardAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    public SwipeCardAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public SwipeCardAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    private void getDeviceWithReadings(DeviceType deviceType, int position) {
        GardenDatabase gardenDatabase = GardenDatabase.getInstance(this.context);
        GardenDashboardViewModel gardenDashboardViewModel = new ViewModelFactory(
                gardenDatabase.gardenDao(),
                gardenDatabase.deviceDao(),
                gardenDatabase.readingDao()
        ).create(GardenDashboardViewModel.class);

        this.disposable.add(gardenDashboardViewModel.getAllDevicesWithReadings()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(devicesWithReadings -> {
                    Log.d(TAG, devicesWithReadings + "");
                    this.devicesWithReadings.addAll(devicesWithReadings);
                }));
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if(position == (this.getItemCount() - 1)) {
            // TODO: The last swipe card in the swipe card pager should show a button to
            // add a new device (i.e. a new swipe card) to this pager
            SwipeCardFragment swipeCardFragment = SwipeCardFragment.newLastSwipeCardInstance(this.deviceType);
            return swipeCardFragment;
        }

        //String deviceName = "" + (position + 1);
        //String value = "" + (position*10);

        DeviceWithReadings deviceWithReadings = this.devicesWithReadings.get(position);

        String deviceName = deviceWithReadings.device.getName();

        String value = "N/A";

        if(deviceWithReadings.readings.size() > 0) {
            // Check if this device has any readings
            value = deviceWithReadings.readings.get(deviceWithReadings.readings.size() - 1).getValue() + "";
        }

        switch(this.deviceType) {
            case TEMPERATURE_SENSOR:
                deviceName = "TempSensor" + deviceName;
                value = value + "ºC";
                break;
            case LIGHT_SENSOR:
                deviceName = "LightSensor" + deviceName;
                value = value + "%";
                break;
            case HUMIDITY_SENSOR:
                deviceName = "HumSensor" + deviceName;
                value = value + "%";
                break;
            default:
                deviceName = "Device " + deviceName;
                break;
        }

        SwipeCardFragment swipeCardFragment = SwipeCardFragment.newInstance(this.deviceType
                , deviceName
                , value);

        return swipeCardFragment;
    }

    @Override
    public int getItemCount() {
        return this.devicesWithReadings.size() + 1;
    }

    public void setDevicesWithReadings(ArrayList<DeviceWithReadings> devicesWithReadings) {
        this.devicesWithReadings = devicesWithReadings;
        this.notifyDataSetChanged();
    }
}
