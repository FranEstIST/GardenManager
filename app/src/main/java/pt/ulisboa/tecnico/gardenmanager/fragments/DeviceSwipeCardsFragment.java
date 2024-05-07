package pt.ulisboa.tecnico.gardenmanager.fragments;

//import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.lifecycle.Observer;

import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import pt.ulisboa.tecnico.gardenmanager.domain.Device;
import pt.ulisboa.tecnico.gardenmanager.domain.Reading;
import pt.ulisboa.tecnico.gardenmanager.domain.ReadingType;
import pt.ulisboa.tecnico.gardenmanager.fragments.viewmodels.DeviceSwipeCardsViewModel;
import pt.ulisboa.tecnico.gardenmanager.domain.DeviceType;
import pt.ulisboa.tecnico.gardenmanager.fragments.viewmodels.GardenDashboardViewModel;
import pt.ulisboa.tecnico.gardenmanager.GlobalClass;
import pt.ulisboa.tecnico.gardenmanager.adapters.SwipeCardAdapter;
import pt.ulisboa.tecnico.gardenmanager.fragments.viewmodels.ViewModelFactory;
import pt.ulisboa.tecnico.gardenmanager.activities.MainActivity;
import pt.ulisboa.tecnico.gardenmanager.databinding.FragmentDeviceSwipeCardsBinding;
import pt.ulisboa.tecnico.gardenmanager.db.GardenDatabase;
import pt.ulisboa.tecnico.gardenmanager.domain.DeviceWithReadings;
import pt.ulisboa.tecnico.gardenmanager.domain.Garden;

public class DeviceSwipeCardsFragment extends Fragment {
    private GlobalClass globalClass;
    private GardenDatabase gardenDatabase;

    private static final String TAG = "DeviceSwipeCardsFragment";

    public static final int SENSOR_MODE = 0;
    public static final int ACTUATOR_MODE = 1;

    private DeviceSwipeCardsViewModel mViewModel;
    private GardenDashboardViewModel gardenDashboardViewModel;

    private FragmentDeviceSwipeCardsBinding binding;

    private HashMap<DeviceType, SwipeCardAdapter> swipeCardAdapterHashMap = new HashMap<>();

    private int mode;

    private boolean isInDemoMode = false;

    public static DeviceSwipeCardsFragment newInstance() {
        return new DeviceSwipeCardsFragment();
    }

    public DeviceSwipeCardsFragment() {
        this.mode = SENSOR_MODE;
    }

    public DeviceSwipeCardsFragment(int mode) {
        this.mode = mode;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.globalClass = (GlobalClass) this.getActivity().getApplicationContext();

        this.gardenDatabase = this.globalClass.getGardenDatabase();

        this.gardenDashboardViewModel = new ViewModelFactory(
                gardenDatabase.gardenDao(),
                gardenDatabase.deviceDao(),
                gardenDatabase.readingDao()
        ).create(GardenDashboardViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDeviceSwipeCardsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /*binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SensorsDashboardFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });*/

        setUpSwipeCards();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //binding.framedSwipeCardOne.swipeCardViewPager.setAdapter(null);
        binding = null;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        setUpSwipeCards();
    }

    private void getDeviceWithReadingsByGardenAndType(int parentGardenId, DeviceType deviceType) {
        if(isInDemoMode) {
            SwipeCardAdapter swipeCardAdapter;
            Device device;
            DeviceWithReadings deviceWithReadings;
            Reading reading;
            ArrayList<Reading> readingList;
            ArrayList<DeviceWithReadings> deviceWithReadingsList;

            switch(deviceType) {
                case TEMPERATURE_SENSOR:
                    // Demo temperature sensor
                    swipeCardAdapter = swipeCardAdapterHashMap.get(DeviceType.TEMPERATURE_SENSOR);

                    device = new Device(1, "NE_Temp_1", DeviceType.TEMPERATURE_SENSOR);
                    deviceWithReadings = new DeviceWithReadings();

                    reading = new Reading(2, 24, ReadingType.CELSIUS);
                    readingList = new ArrayList<>();
                    readingList.add(reading);

                    deviceWithReadings.device = device;
                    deviceWithReadings.readings = readingList;
                    deviceWithReadingsList = new ArrayList<>();
                    deviceWithReadingsList.add(deviceWithReadings);

                    swipeCardAdapter.setDevicesWithReadings(deviceWithReadingsList);
                    break;
                case LIGHT_SENSOR:
                    // Demo light sensor
                    swipeCardAdapter = swipeCardAdapterHashMap.get(DeviceType.LIGHT_SENSOR);

                    device = new Device(1, "SW_Light_3", DeviceType.LIGHT_SENSOR);
                    deviceWithReadings = new DeviceWithReadings();

                    reading = new Reading(2, 80, ReadingType.PERCENTAGE);
                    readingList = new ArrayList<>();
                    readingList.add(reading);

                    deviceWithReadings.device = device;
                    deviceWithReadings.readings = readingList;
                    deviceWithReadingsList = new ArrayList<>();
                    deviceWithReadingsList.add(deviceWithReadings);

                    swipeCardAdapter.setDevicesWithReadings(deviceWithReadingsList);
                    break;
                case HUMIDITY_SENSOR:
                    // Demo humidity sensor
                    swipeCardAdapter = swipeCardAdapterHashMap.get(DeviceType.HUMIDITY_SENSOR);

                    device = new Device(1, "E_Hum_5", DeviceType.HUMIDITY_SENSOR);
                    deviceWithReadings = new DeviceWithReadings();

                    reading = new Reading(2, 55, ReadingType.PERCENTAGE);
                    readingList = new ArrayList<>();
                    readingList.add(reading);

                    deviceWithReadings.device = device;
                    deviceWithReadings.readings = readingList;
                    deviceWithReadingsList = new ArrayList<>();
                    deviceWithReadingsList.add(deviceWithReadings);

                    swipeCardAdapter.setDevicesWithReadings(deviceWithReadingsList);
                    break;
                case MONITOR:
                    // Demo monitor
                    swipeCardAdapter = swipeCardAdapterHashMap.get(DeviceType.MONITOR);

                    device = new Device(4, "Monitor_3", DeviceType.MONITOR);
                    deviceWithReadings = new DeviceWithReadings();

                    reading = new Reading(2, 1, ReadingType.STATE);
                    readingList = new ArrayList<>();
                    readingList.add(reading);

                    deviceWithReadings.device = device;
                    deviceWithReadings.readings = readingList;
                    deviceWithReadingsList = new ArrayList<>();
                    deviceWithReadingsList.add(deviceWithReadings);

                    swipeCardAdapter.setDevicesWithReadings(deviceWithReadingsList);
                    break;
                case LAMP:
                    // Demo lamp
                    swipeCardAdapter = swipeCardAdapterHashMap.get(DeviceType.LAMP);

                    device = new Device(4, "NW_Lamp_1", DeviceType.LAMP);
                    deviceWithReadings = new DeviceWithReadings();

                    reading = new Reading(2, 0, ReadingType.PERCENTAGE);
                    readingList = new ArrayList<>();
                    readingList.add(reading);

                    deviceWithReadings.device = device;
                    deviceWithReadings.readings = readingList;
                    deviceWithReadingsList = new ArrayList<>();
                    deviceWithReadingsList.add(deviceWithReadings);

                    swipeCardAdapter.setDevicesWithReadings(deviceWithReadingsList);
                    break;
                case SPRINKLER:
                    // Demo sprinkler
                    swipeCardAdapter = swipeCardAdapterHashMap.get(DeviceType.SPRINKLER);

                    device = new Device(4, "NE_Spr_2", DeviceType.SPRINKLER);
                    deviceWithReadings = new DeviceWithReadings();

                    reading = new Reading(2, 26, ReadingType.PERCENTAGE);
                    readingList = new ArrayList<>();
                    readingList.add(reading);

                    deviceWithReadings.device = device;
                    deviceWithReadings.readings = readingList;
                    deviceWithReadingsList = new ArrayList<>();
                    deviceWithReadingsList.add(deviceWithReadings);

                    swipeCardAdapter.setDevicesWithReadings(deviceWithReadingsList);
                    break;
            }

            return;
        }

        this.gardenDashboardViewModel.getDevicesWithReadingsByGardenAndType(parentGardenId, deviceType)
                .observe(getViewLifecycleOwner(), new Observer<List<DeviceWithReadings>>() {
                    @Override
                    public void onChanged(@Nullable List<DeviceWithReadings> deviceWithReadings) {
                        Log.d(TAG, "List of " + deviceType.name() + " devices has been updated");

                        SwipeCardAdapter swipeCardAdapter = swipeCardAdapterHashMap.get(deviceType);
                        if(swipeCardAdapter == null) {
                            Log.e(TAG, "Swipe card adapter is null");
                        } else {
                            swipeCardAdapter.setDevicesWithReadings(new ArrayList<>(deviceWithReadings));
                        }
                    }
                });
    }

    void setUpAppBar(int currentGardenId) {
        MainActivity mainActivity = (MainActivity) getActivity();

        if(mainActivity != null && mainActivity.binding != null) {
            TextView appBarTitleTextView = mainActivity.binding.appBarMain.appBarTitle;

            if(currentGardenId == -1) {
                appBarTitleTextView.setText("");
            } else {
                globalClass.getGardenDatabase().gardenDao().findById(currentGardenId)
                        .observeOn(Schedulers.newThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new DisposableSingleObserver<Garden>() {
                            @Override
                            public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull Garden garden) {
                                appBarTitleTextView.setText(garden.getName());
                            }

                            @Override
                            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                                e.printStackTrace();
                            }
                        });
            }

            //mainActivity.binding.appBarMain.appBarTitle.setText("Alameda Garden 1");
        }
    }

    void setUpSensors(int currentGardenId) {
        SwipeCardAdapter tempSwipeCardAdapter = new SwipeCardAdapter(this, DeviceType.TEMPERATURE_SENSOR);
        SwipeCardAdapter lightSwipeCardAdapter = new SwipeCardAdapter(this, DeviceType.LIGHT_SENSOR);
        SwipeCardAdapter humSwipeCardAdapter = new SwipeCardAdapter(this, DeviceType.HUMIDITY_SENSOR);

        this.swipeCardAdapterHashMap.put(DeviceType.TEMPERATURE_SENSOR, tempSwipeCardAdapter);
        this.swipeCardAdapterHashMap.put(DeviceType.LIGHT_SENSOR, lightSwipeCardAdapter);
        this.swipeCardAdapterHashMap.put(DeviceType.HUMIDITY_SENSOR, humSwipeCardAdapter);

        getDeviceWithReadingsByGardenAndType(currentGardenId, DeviceType.TEMPERATURE_SENSOR);
        getDeviceWithReadingsByGardenAndType(currentGardenId, DeviceType.LIGHT_SENSOR);
        getDeviceWithReadingsByGardenAndType(currentGardenId, DeviceType.HUMIDITY_SENSOR);

        binding.framedSwipeCardOne.swipeCardViewPager.setAdapter(tempSwipeCardAdapter);
        binding.framedSwipeCardTwo.swipeCardViewPager.setAdapter(lightSwipeCardAdapter);
        binding.framedSwipeCardThree.swipeCardViewPager.setAdapter(humSwipeCardAdapter);
    }

    void setUpActuators(int currentGardenId) {
        SwipeCardAdapter monitorSwipeCardAdapter = new SwipeCardAdapter(this, DeviceType.MONITOR);
        SwipeCardAdapter lampSwipeCardAdapter = new SwipeCardAdapter(this, DeviceType.LAMP);
        SwipeCardAdapter sprinklerSwipeCardAdapter = new SwipeCardAdapter(this, DeviceType.SPRINKLER);

        this.swipeCardAdapterHashMap.put(DeviceType.MONITOR, monitorSwipeCardAdapter);
        this.swipeCardAdapterHashMap.put(DeviceType.LAMP, lampSwipeCardAdapter);
        this.swipeCardAdapterHashMap.put(DeviceType.SPRINKLER, sprinklerSwipeCardAdapter);

        getDeviceWithReadingsByGardenAndType(currentGardenId, DeviceType.MONITOR);
        getDeviceWithReadingsByGardenAndType(currentGardenId, DeviceType.LAMP);
        getDeviceWithReadingsByGardenAndType(currentGardenId, DeviceType.SPRINKLER);

        binding.framedSwipeCardOne.swipeCardViewPager.setAdapter(monitorSwipeCardAdapter);
        binding.framedSwipeCardTwo.swipeCardViewPager.setAdapter(lampSwipeCardAdapter);
        binding.framedSwipeCardThree.swipeCardViewPager.setAdapter(sprinklerSwipeCardAdapter);
    }

    void setUpSwipeCards() {
        int currentGardenId = this.globalClass.getCurrentGardenId();

        setUpAppBar(currentGardenId);

        if(currentGardenId == -1) {
            binding.framedSwipeCardOne.getRoot().setVisibility(View.GONE);
            binding.framedSwipeCardTwo.getRoot().setVisibility(View.GONE);
            binding.framedSwipeCardThree.getRoot().setVisibility(View.GONE);
            return;
        }

        if(mode == SENSOR_MODE) {
            setUpSensors(currentGardenId);
        } else {
            setUpActuators(currentGardenId);
        }
    }

    /*@Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(DeviceSwipeCardsViewModel.class);
        // TODO: Use the ViewModel
    }*/

}