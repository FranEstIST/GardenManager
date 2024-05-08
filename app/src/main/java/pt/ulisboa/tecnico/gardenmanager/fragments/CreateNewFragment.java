package pt.ulisboa.tecnico.gardenmanager.fragments;

import static pt.ulisboa.tecnico.gardenmanager.constants.ViewModes.DEVICE_MODE;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import io.reactivex.rxjava3.observers.DisposableCompletableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import pt.ulisboa.tecnico.gardenmanager.GlobalClass;
import pt.ulisboa.tecnico.gardenmanager.R;
import pt.ulisboa.tecnico.gardenmanager.constants.ViewModes;
import pt.ulisboa.tecnico.gardenmanager.databinding.FragmentCreateNewBinding;
import pt.ulisboa.tecnico.gardenmanager.db.GardenDatabase;
import pt.ulisboa.tecnico.gardenmanager.domain.Device;
import pt.ulisboa.tecnico.gardenmanager.domain.DeviceType;
import pt.ulisboa.tecnico.gardenmanager.domain.Garden;
import pt.ulisboa.tecnico.gardenmanager.network.WithoutNetService;
import pt.ulisboa.tecnico.gardenmanager.network.dto.DeviceDto;
import pt.ulisboa.tecnico.gardenmanager.network.dto.GardenDto;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateNewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateNewFragment extends Fragment {
    private static final String TAG = "CreateNewFragment";

    private static final String ARG_MODE = "mode";
    private static final String ARG_DEVICE_TYPE_STRING = "deviceMode";

    private int mode;
    private DeviceType deviceType;

    private FragmentCreateNewBinding binding;

    private GlobalClass globalClass;

    private WithoutNetService WNService;

    public CreateNewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param mode The view mode for this fragment
     * @return A new instance of fragment CreateNewDeviceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateNewFragment newInstance(int mode) {
        CreateNewFragment fragment = new CreateNewFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_MODE, mode);
        args.putInt(ARG_DEVICE_TYPE_STRING, -1);
        fragment.setArguments(args);
        return fragment;
    }

    public static CreateNewFragment newInstance(int mode, String deviceTypeString) {
        CreateNewFragment fragment = new CreateNewFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_MODE, mode);
        args.putString(ARG_DEVICE_TYPE_STRING, deviceTypeString);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (getArguments() != null) {
            mode = getArguments().getInt(ARG_MODE);
            if(mode == DEVICE_MODE) {
                deviceType = DeviceType.valueOf(getArguments().getString(ARG_DEVICE_TYPE_STRING));
            }
        }

        globalClass = (GlobalClass) getActivity().getApplicationContext();
        WNService = new WithoutNetService(globalClass);

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCreateNewBinding.inflate(getLayoutInflater());

        TextView createNewTitleTextView = binding.createNewTitleTextView;
        TextView createNewNameTextView = binding.createNewNameTextView;
        Button createNewSubmitButton = binding.createNewSubmitButton;

        String createNewTitle;
        String hint;
        String submitButtonText;
        View.OnClickListener submitButtonOnClickListener;

        if(mode == DEVICE_MODE) {
            createNewTitle = getString(R.string.create_new_device);
            hint = getString(R.string.device_name);
            submitButtonText = getString(R.string.create_device);
            submitButtonOnClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WithoutNetService.WithoutNetServiceResponseListener responseListener = new WithoutNetService.WithoutNetServiceResponseListener() {
                        @Override
                        public void onResponse(Object response) {
                            DeviceDto deviceDto = (DeviceDto) response;

                            Device newDevice = new Device(deviceDto, deviceType);

                            int currentGardenId = globalClass.getCurrentGardenId();

                            newDevice.setParentGardenId(currentGardenId);

                            GardenDatabase gardenDatabase = globalClass.getGardenDatabase();

                            gardenDatabase.deviceDao().insertAll(newDevice)
                                    .observeOn(Schedulers.newThread())
                                    .subscribeOn(Schedulers.io())
                                    .subscribe(new DisposableCompletableObserver() {
                                        @Override
                                        public void onComplete() {
                                            Log.d(TAG, "Added device");
                                            CreateNewFragment.this.getActivity().finish();
                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            e.printStackTrace();
                                        }
                                    });
                        }

                        @Override
                        public void onError(String errorMessage) {

                        }
                    };

                    String newDeviceName = createNewNameTextView.getText().toString();
                    int currentGardenId = globalClass.getCurrentGardenId();

                    WNService.addDevice(newDeviceName, currentGardenId, responseListener);
                    //WNService.addDeviceToGarden(globalClass.getCurrentGardenId());
                }
            };
        } else {
            createNewTitle = getString(R.string.create_new_garden);
            hint = getString(R.string.garden_name);
            submitButtonText = getString(R.string.create_garden);
            submitButtonOnClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WithoutNetService.WithoutNetServiceResponseListener responseListener = new WithoutNetService.WithoutNetServiceResponseListener() {
                        @Override
                        public void onResponse(Object response) {
                            GardenDto gardenDto = (GardenDto) response;

                            // Create and save the new garden to the database

                            Garden newGarden = new Garden(gardenDto);

                            GardenDatabase gardenDatabase = globalClass.getGardenDatabase();

                            gardenDatabase.gardenDao().insertAll(newGarden)
                                    .observeOn(Schedulers.newThread())
                                    .subscribeOn(Schedulers.io())
                                    .subscribe(new DisposableCompletableObserver() {
                                        @Override
                                        public void onComplete() {
                                            Log.d(TAG, "Added garden");
                                            CreateNewFragment.this.getActivity().finish();
                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            e.printStackTrace();
                                        }
                                    });

                        }

                        @Override
                        public void onError(String errorMessage) {
                            return;
                        }
                    };

                    WNService.addGarden(createNewNameTextView.getText().toString(), responseListener);
                }
            };
        }

        createNewTitleTextView.setText(createNewTitle);
        createNewNameTextView.setHint(hint);
        createNewSubmitButton.setText(submitButtonText);
        createNewSubmitButton.setOnClickListener(submitButtonOnClickListener);

        return binding.getRoot();
    }
}