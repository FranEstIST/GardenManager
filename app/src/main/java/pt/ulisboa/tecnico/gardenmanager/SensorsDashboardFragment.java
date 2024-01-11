package pt.ulisboa.tecnico.gardenmanager;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.fragment.NavHostFragment;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
//import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import pt.ulisboa.tecnico.gardenmanager.databinding.FragmentFirstBinding;
import pt.ulisboa.tecnico.gardenmanager.db.GardenDatabase;
import pt.ulisboa.tecnico.gardenmanager.domain.Device;
import pt.ulisboa.tecnico.gardenmanager.domain.DeviceWithReadings;
import pt.ulisboa.tecnico.gardenmanager.domain.GardenWithDevices;
import pt.ulisboa.tecnico.gardenmanager.domain.Reading;

public class SensorsDashboardFragment extends Fragment {

    private FragmentFirstBinding binding;
    private final CompositeDisposable disposable = new CompositeDisposable();
    private final String TAG = "SensorsDashboardFragment";
    private HashMap<DeviceType, SwipeCardAdapter> swipeCardAdapterHashMap = new HashMap<>();

    private GardenDatabase gardenDatabase;
    private GardenDashboardViewModel gardenDashboardViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        this.gardenDatabase = Room
                .databaseBuilder(this.getActivity().getApplicationContext(), GardenDatabase.class, "GardenDatabase")
                .fallbackToDestructiveMigration()
                .build();

        this.gardenDashboardViewModel = new ViewModelFactory(
                gardenDatabase.gardenDao(),
                gardenDatabase.deviceDao(),
                gardenDatabase.readingDao()
        ).create(GardenDashboardViewModel.class);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /*binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SensorsDashboardFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });*/

        setUpSwipeCardsTwo();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        setUpSwipeCardsTwo();
    }
    
    private void setUpSwipeCard(DeviceWithReadings deviceWithReadings) {
        Device device = deviceWithReadings.device;
        Reading mostRecentReading = deviceWithReadings.readings.get(deviceWithReadings.readings.size() - 1);

        DeviceType deviceType = device.getDeviceType();

        SwipeCardAdapter swipeCardAdapter = new SwipeCardAdapter(this, deviceType);

        switch(deviceType) {
            case TEMPERATURE_SENSOR:
                binding.framedSwipeCardOne.swipeCardViewPager.setAdapter(swipeCardAdapter);
                break;
            case LIGHT_SENSOR:
                binding.framedSwipeCardTwo.swipeCardViewPager.setAdapter(swipeCardAdapter);
                break;
            case HUMIDITY_SENSOR:
                binding.framedSwipeCardThree.swipeCardViewPager.setAdapter(swipeCardAdapter);
                break;
        }
    }
    
    void setUpSwipeCards() {
        SwipeCardAdapter swipeCardAdapterOne = new SwipeCardAdapter(this, DeviceType.TEMPERATURE_SENSOR);
        SwipeCardAdapter swipeCardAdapterTwo = new SwipeCardAdapter(this, DeviceType.LIGHT_SENSOR);
        SwipeCardAdapter swipeCardAdapterThree = new SwipeCardAdapter(this, DeviceType.HUMIDITY_SENSOR);

        binding.framedSwipeCardOne.swipeCardViewPager.setAdapter(swipeCardAdapterOne);
        binding.framedSwipeCardTwo.swipeCardViewPager.setAdapter(swipeCardAdapterTwo);
        binding.framedSwipeCardThree.swipeCardViewPager.setAdapter(swipeCardAdapterThree);

        Button sensorsButton = binding.sensorsButton;
        Button actuatorsButton = binding.actuatorsButton;

        //sensorsButton.setTypeface(Typeface.create("times new roman", Typeface.NORMAL));

        //Typeface roboto = Typeface.createFromAsset(getResources().getAssets(), get)
        Typeface roboto = ResourcesCompat.getFont(getContext(), R.font.roboto_regular);
        actuatorsButton.setTypeface(roboto);

        MainActivity mainActivity = (MainActivity) getActivity();

        if(mainActivity != null && mainActivity.binding != null) {
            mainActivity.binding.appBarMain.appBarTitle.setText("Alameda Garden 1");
        }

        GardenDatabase gardenDatabase = GardenDatabase.getInstance(this.getContext());
        GardenDashboardViewModel gardenDashboardViewModel = new ViewModelFactory(
                gardenDatabase.gardenDao(),
                gardenDatabase.deviceDao(),
                gardenDatabase.readingDao()
        ).create(GardenDashboardViewModel.class);

        this.disposable.add(gardenDashboardViewModel.getAllDevicesWithReadings()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(devicesWithReadings -> Log.d(TAG, devicesWithReadings + "")));
    }

    private void getDeviceWithReadingsByType(DeviceType deviceType) {
        this.gardenDashboardViewModel.getDevicesWithReadingsByType(deviceType).observe(getViewLifecycleOwner(), new Observer<List<DeviceWithReadings>>() {
            @Override
            public void onChanged(@Nullable List<DeviceWithReadings> deviceWithReadings) {
                SwipeCardAdapter swipeCardAdapter = swipeCardAdapterHashMap.get(deviceType);
                if(swipeCardAdapter == null) {
                    Log.e(TAG, "Swipe card adapter is null");
                } else {
                  swipeCardAdapter.setDevicesWithReadings(new ArrayList<>(deviceWithReadings));
                }
            }
        });

        /*this.disposable.add(gardenDashboardViewModel.getDevicesWithReadingsByType(deviceType)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(devicesWithReadings -> {
                    Log.d(TAG, devicesWithReadings + "");
                    this.devicesWithReadings.addAll(devicesWithReadings);
                }));*/
    }

    void setUpSwipeCardsTwo() {
        SwipeCardAdapter tempSwipeCardAdapter = new SwipeCardAdapter(this, DeviceType.TEMPERATURE_SENSOR);
        SwipeCardAdapter lightSwipeCardAdapter = new SwipeCardAdapter(this, DeviceType.LIGHT_SENSOR);
        SwipeCardAdapter humSwipeCardAdapter = new SwipeCardAdapter(this, DeviceType.HUMIDITY_SENSOR);

        this.swipeCardAdapterHashMap.put(DeviceType.TEMPERATURE_SENSOR, tempSwipeCardAdapter);
        this.swipeCardAdapterHashMap.put(DeviceType.LIGHT_SENSOR, lightSwipeCardAdapter);
        this.swipeCardAdapterHashMap.put(DeviceType.HUMIDITY_SENSOR, humSwipeCardAdapter);

        getDeviceWithReadingsByType(DeviceType.TEMPERATURE_SENSOR);
        getDeviceWithReadingsByType(DeviceType.LIGHT_SENSOR);
        getDeviceWithReadingsByType(DeviceType.HUMIDITY_SENSOR);

        binding.framedSwipeCardOne.swipeCardViewPager.setAdapter(tempSwipeCardAdapter);
        binding.framedSwipeCardTwo.swipeCardViewPager.setAdapter(lightSwipeCardAdapter);
        binding.framedSwipeCardThree.swipeCardViewPager.setAdapter(humSwipeCardAdapter);

        Button sensorsButton = binding.sensorsButton;
        Button actuatorsButton = binding.actuatorsButton;

        //sensorsButton.setTypeface(Typeface.create("times new roman", Typeface.NORMAL));

        //Typeface roboto = Typeface.createFromAsset(getResources().getAssets(), get)
        Typeface roboto = ResourcesCompat.getFont(getContext(), R.font.roboto_regular);
        actuatorsButton.setTypeface(roboto);

        MainActivity mainActivity = (MainActivity) getActivity();

        if(mainActivity != null && mainActivity.binding != null) {
            mainActivity.binding.appBarMain.appBarTitle.setText("Alameda Garden 1");
        }

        /*GardenDatabase gardenDatabase = GardenDatabase.getInstance(this.getContext());
        GardenDashboardViewModel gardenDashboardViewModel = new ViewModelFactory(
                gardenDatabase.gardenDao(),
                gardenDatabase.deviceDao(),
                gardenDatabase.readingDao()
        ).create(GardenDashboardViewModel.class);

        this.disposable.add(gardenDashboardViewModel.getAllDevicesWithReadings()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(devicesWithReadings -> Log.d(TAG, devicesWithReadings + "")));*/
    }
}