package pt.ulisboa.tecnico.gardenmanager;

import static pt.ulisboa.tecnico.gardenmanager.DeviceSwipeCardsFragment.ACTUATOR_MODE;
import static pt.ulisboa.tecnico.gardenmanager.DeviceSwipeCardsFragment.SENSOR_MODE;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import pt.ulisboa.tecnico.gardenmanager.databinding.FragmentGardenDashboardBinding;
import pt.ulisboa.tecnico.gardenmanager.db.GardenDatabase;
import pt.ulisboa.tecnico.gardenmanager.domain.Device;
import pt.ulisboa.tecnico.gardenmanager.domain.DeviceWithReadings;
import pt.ulisboa.tecnico.gardenmanager.domain.Garden;
import pt.ulisboa.tecnico.gardenmanager.domain.GardenWithDevices;
import pt.ulisboa.tecnico.gardenmanager.domain.Reading;

public class GardenDashboardFragment extends Fragment {

    private FragmentGardenDashboardBinding binding;
    private final CompositeDisposable disposable = new CompositeDisposable();
    private final String TAG = "SensorsDashboardFragment";
    private HashMap<DeviceType, SwipeCardAdapter> swipeCardAdapterHashMap = new HashMap<>();

    private GlobalClass globalClass;

    private GardenDatabase gardenDatabase;
    private GardenDashboardViewModel gardenDashboardViewModel;

    private int mode;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        this.globalClass = (GlobalClass) this.getActivity().getApplicationContext();

        this.gardenDatabase = this.globalClass.getGardenDatabase();

        this.gardenDashboardViewModel = new ViewModelFactory(
                gardenDatabase.gardenDao(),
                gardenDatabase.deviceDao(),
                gardenDatabase.readingDao()
        ).create(GardenDashboardViewModel.class);

        this.mode = SENSOR_MODE;
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentGardenDashboardBinding.inflate(inflater, container, false);
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

        setUpDashboard();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
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
        setUpDashboard();
    }

    void moveToSensorsDashboard() {
        Fragment deviceSwipeCardsFragment = new DeviceSwipeCardsFragment(SENSOR_MODE);

        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.devicesSwipeCardsFragmentContainerView, deviceSwipeCardsFragment)
                .commit();

        Button sensorsButton = binding.sensorsButton;
        Button actuatorsButton = binding.actuatorsButton;

        Typeface roboto_bold = ResourcesCompat.getFont(getContext(), R.font.roboto_bold);
        sensorsButton.setTypeface(roboto_bold);

        Typeface roboto_regular = ResourcesCompat.getFont(getContext(), R.font.roboto_regular);
        actuatorsButton.setTypeface(roboto_regular);
    }

    void moveToActuatorsDashboard() {
        Fragment deviceSwipeCardsFragment = new DeviceSwipeCardsFragment(ACTUATOR_MODE);

        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.devicesSwipeCardsFragmentContainerView, deviceSwipeCardsFragment)
                .commit();

        Button sensorsButton = binding.sensorsButton;
        Button actuatorsButton = binding.actuatorsButton;

        Typeface roboto_bold = ResourcesCompat.getFont(getContext(), R.font.roboto_bold);
        actuatorsButton.setTypeface(roboto_bold);

        Typeface roboto_regular = ResourcesCompat.getFont(getContext(), R.font.roboto_regular);
        sensorsButton.setTypeface(roboto_regular);
    }

    void setUpDashboard() {
        int currentGardenId = this.globalClass.getCurrentGardenId();

        MainActivity mainActivity = (MainActivity) getActivity();

        if(mainActivity != null && mainActivity.binding != null) {
            TextView appBarTitleTextView = mainActivity.binding.appBarMain.appBarTitle;

            if(currentGardenId == -1) {
                appBarTitleTextView.setText("");
            } else {
                globalClass.getGardenDatabase().gardenDao().findById(currentGardenId)
                        .observeOn(Schedulers.newThread())
                        //.subscribeOn(Schedulers.io())
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

        if(currentGardenId == -1) {
            binding.sensorsButton.setVisibility(View.GONE);
            binding.actuatorsButton.setVisibility(View.GONE);

            binding.selectAGardenTextView.setVisibility(View.VISIBLE);

            return;
        }

        moveToSensorsDashboard();

        Button sensorsButton = binding.sensorsButton;
        Button actuatorsButton = binding.actuatorsButton;

        sensorsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToSensorsDashboard();
            }
        });

        actuatorsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToActuatorsDashboard();
            }
        });
    }

}