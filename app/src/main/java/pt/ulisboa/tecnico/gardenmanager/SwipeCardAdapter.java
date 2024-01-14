package pt.ulisboa.tecnico.gardenmanager;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import pt.ulisboa.tecnico.gardenmanager.db.GardenDatabase;
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
            SwipeCardFragment swipeCardFragment = SwipeCardFragment.newLastSwipeCardInstance(this.deviceType);
            return swipeCardFragment;
        }

        DeviceWithReadings deviceWithReadings = this.devicesWithReadings.get(position);

        String deviceName = deviceWithReadings.device.getName();

        String valueString = "N/A";

        if(deviceWithReadings.readings.size() > 0) {
            // The latest reading can only be displayed if this device has any readings

            long value = deviceWithReadings.readings.get(deviceWithReadings.readings.size() - 1).getValue();

            switch(this.deviceType) {
                case TEMPERATURE_SENSOR:
                    valueString = value + "ºC";
                    break;
                case LIGHT_SENSOR:
                case HUMIDITY_SENSOR:
                case LAMP:
                case SPRINKLER:
                    valueString = value + "%";
                    break;
                case MONITOR:
                    if(value == 0) {
                        valueString = "OFF";
                    } else {
                        valueString = "ON";
                    }
                    break;
                default:
                    valueString = value + "";
                    break;
            }
        }

        SwipeCardFragment swipeCardFragment = SwipeCardFragment.newInstance(this.deviceType
                , deviceName
                , valueString);

        return swipeCardFragment;
    }

    @Override
    public long getItemId(int position) {
        if(position < (this.getItemCount() - 1)) {
            return this.devicesWithReadings.get(position).device.getDeviceId();
        } else {
            return -1;
        }
    }

    @Override
    public boolean containsItem(long itemId) {
        // TODO: Make this method return false iff the deviceWithReadings corresponding to itemId
        // has been changed, so that each fragment is only reloaded if necessary

        return itemId == -1;
        /*|| this.devicesWithReadings
                .stream()
                .map(deviceWithReadings -> deviceWithReadings.device.getDeviceId())
                .anyMatch(deviceId -> deviceId == itemId);*/
    }

    @Override
    public int getItemCount() {
        return this.devicesWithReadings.size() + 1;
    }

    public void setDevicesWithReadings(ArrayList<DeviceWithReadings> devicesWithReadings) {
        this.devicesWithReadings = devicesWithReadings;
        this.notifyDataSetChanged();
        //this.notifyItemChanged(0);
    }
}
