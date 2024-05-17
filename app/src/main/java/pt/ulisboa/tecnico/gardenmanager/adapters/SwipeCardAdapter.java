package pt.ulisboa.tecnico.gardenmanager.adapters;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import pt.ulisboa.tecnico.gardenmanager.domain.DeviceType;
import pt.ulisboa.tecnico.gardenmanager.domain.Reading;
import pt.ulisboa.tecnico.gardenmanager.fragments.viewmodels.GardenDashboardViewModel;
import pt.ulisboa.tecnico.gardenmanager.fragments.viewmodels.ViewModelFactory;
import pt.ulisboa.tecnico.gardenmanager.db.GardenDatabase;
import pt.ulisboa.tecnico.gardenmanager.domain.DeviceWithReadings;
import pt.ulisboa.tecnico.gardenmanager.fragments.SwipeCardFragment;

public class SwipeCardAdapter extends FragmentStateAdapter {
    private DeviceType deviceType;
    private static final String TAG = "SwipeCardAdapter";
    private final CompositeDisposable disposable = new CompositeDisposable();
    private Context context;

    private ArrayList<DeviceWithReadings> devicesWithReadings;
    private HashMap<Integer, SwipeCardFragment> swipeCardFragments;

    public SwipeCardAdapter(@NonNull Fragment fragment, DeviceType deviceType) {
        super(fragment);
        this.context = fragment.getContext();
        this.deviceType = deviceType;
        this.devicesWithReadings = new ArrayList<>();
        this.swipeCardFragments = new HashMap<>();
    }

    public SwipeCardAdapter(@NonNull Fragment fragment, DeviceType deviceType, ArrayList<DeviceWithReadings> devicesWithReadings) {
        super(fragment);
        this.context = fragment.getContext();
        this.deviceType = deviceType;
        this.devicesWithReadings = devicesWithReadings;
        this.swipeCardFragments = new HashMap<>();
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

        int deviceId = deviceWithReadings.device.getDeviceId();

        List<Reading> readings = deviceWithReadings.readings;

        List<Reading> orderedReadings = readings.stream()
                .sorted((readingOne, readingTwo) -> (int) (readingOne.getTimestamp() - readingTwo.getTimestamp()))
                .collect(Collectors.toList());

        long value = -1;

        if(deviceWithReadings.readings.size() > 0) {
            // The latest reading can only be displayed if this device has any readings
            value = orderedReadings.get(orderedReadings.size() - 1).getValue();
        }

        SwipeCardFragment swipeCardFragment = SwipeCardFragment.newInstance(this.deviceType
                , deviceName
                , deviceId
                , value);

        swipeCardFragments.put(position, swipeCardFragment);

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
        return itemId == -1;
    }

    @Override
    public int getItemCount() {
        return this.devicesWithReadings.size() + 1;
    }

    private long getMostRecentValue(List<Reading> readings) {
        List<Reading> orderedReadings = readings.stream()
                .sorted((readingOne, readingTwo) -> (int) (readingOne.getTimestamp() - readingTwo.getTimestamp()))
                .collect(Collectors.toList());

        return orderedReadings.size() > 0 ? orderedReadings.get(orderedReadings.size() - 1).getValue() : -1;
    }

    private void updateSwipeCardFragments() {
        for(int i = 0; i < getItemCount() - 1; i++) {
            SwipeCardFragment swipeCardFragment = swipeCardFragments.get(i);

            if(swipeCardFragment != null) {
                DeviceWithReadings deviceWithReadings = devicesWithReadings.get(i);
                long value = getMostRecentValue(deviceWithReadings.readings);
                swipeCardFragment.setValue(value);
            }
        }
    }

    public void setDevicesWithReadings(ArrayList<DeviceWithReadings> devicesWithReadings) {
        this.devicesWithReadings = devicesWithReadings;
        this.notifyDataSetChanged();
        this.updateSwipeCardFragments();
    }
}
