package pt.ulisboa.tecnico.gardenmanager.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import io.reactivex.rxjava3.schedulers.Schedulers;
import pt.ulisboa.tecnico.gardenmanager.GlobalClass;
import pt.ulisboa.tecnico.gardenmanager.activities.DeviceDetailsPopUpActivity;
import pt.ulisboa.tecnico.gardenmanager.constants.ViewModes;
import pt.ulisboa.tecnico.gardenmanager.databinding.FragmentSwipeCardBinding;
import pt.ulisboa.tecnico.gardenmanager.domain.DeviceType;
import pt.ulisboa.tecnico.gardenmanager.R;
import pt.ulisboa.tecnico.gardenmanager.activities.AddNewPopUpActivity;

public class SwipeCardFragment extends Fragment {
    private static final String TAG = "SwipeCardFragment";

    private static final String DEVICE_TYPE = "device_type";
    private static final String DEVICE_NAME = "device_name";
    private static final String DEVICE_ID = "device_id";
    private static final String VALUE = "value";
    private static final String IS_LAST_SWIPE_CARD = "is_last_swipe_card";

    private GlobalClass globalClass;

    private DeviceType deviceType;
    private String deviceName;
    private int deviceId;
    private long value;

    private boolean isLastSwipeCard = false;

    private FragmentSwipeCardBinding binding;
    private TextView valueTextView;

    public SwipeCardFragment() {
    }

    /**
     * @param deviceType The type of the device associated with this fragment (TEMPERATURE_SENSOR, LIGHT_SENSOR, etc.).
     * @param deviceName The common name of the device associated with this fragment.
     * @param value      The value to be displayed on this fragment.
     * @return A new instance of fragment SwipeCardFragment.
     */
    public static SwipeCardFragment newInstance(DeviceType deviceType, String deviceName, int deviceId, long value) {
        SwipeCardFragment fragment = new SwipeCardFragment();
        Bundle args = new Bundle();
        args.putString(DEVICE_TYPE, deviceType.name());
        args.putString(DEVICE_NAME, deviceName);
        args.putInt(DEVICE_ID, deviceId);
        args.putLong(VALUE, value);
        args.putBoolean(IS_LAST_SWIPE_CARD, false);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * @param deviceType The type of the device associated with this fragment (TEMPERATURE_SENSOR, LIGHT_SENSOR, etc.).
     * @return A new instance of fragment SwipeCardFragment corresponding to the last swipe card.
     */
    public static SwipeCardFragment newLastSwipeCardInstance(DeviceType deviceType) {
        SwipeCardFragment fragment = new SwipeCardFragment();
        Bundle args = new Bundle();
        args.putString(DEVICE_TYPE, deviceType.name());
        args.putString(DEVICE_NAME, "N/A");
        args.putLong(VALUE, -1);
        args.putBoolean(IS_LAST_SWIPE_CARD, true);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String deviceTypeName = getArguments().getString(DEVICE_TYPE);

            this.deviceType = DeviceType.valueOf(deviceTypeName);
            this.deviceName = getArguments().getString(DEVICE_NAME);
            this.deviceId = getArguments().getInt(DEVICE_ID);
            this.value = getArguments().getLong(VALUE);
            this.isLastSwipeCard = getArguments().getBoolean(IS_LAST_SWIPE_CARD);
        }

        globalClass = (GlobalClass) getActivity().getApplicationContext();
    }

    private void createRegularSwipeCardView(View view) {
        View regularSwipeCardLayout = view.findViewById(R.id.regular_swipe_card);
        regularSwipeCardLayout.setVisibility(View.VISIBLE);

        View addNewDeviceSwipeCardLayout = view.findViewById(R.id.add_new_device_swipe_card);
        addNewDeviceSwipeCardLayout.setVisibility(View.GONE);

        TextView deviceNameTextView = regularSwipeCardLayout.findViewById(R.id.deviceNameTextView);
        valueTextView = regularSwipeCardLayout.findViewById(R.id.valueTextView);

        // Alter the child views, in order to adjust this fragment to the specific measurement
        // type that it is used for

        CardView swipeCardView = regularSwipeCardLayout.findViewById(R.id.swipeCard);
        ImageView deviceIconImageView = regularSwipeCardLayout.findViewById(R.id.deviceIconImageView);
        ImageButton removeDeviceImageButton = regularSwipeCardLayout.findViewById(R.id.removeDeviceImageButton);

        switch (this.deviceType) {
            case TEMPERATURE_SENSOR:
                deviceIconImageView.setImageResource(R.drawable.thermometer_icon_white);
                //deviceIconImageView.setMaxHeight(2);
                deviceNameTextView.setTextColor(getResources().getColor(R.color.white));
                valueTextView.setTextColor(getResources().getColor(R.color.white));
                removeDeviceImageButton.setBackground(getResources().getDrawable(R.drawable.delete_button_white_small));
                swipeCardView.setCardBackgroundColor(getResources().getColor(R.color.temperature_red));
                break;
            case LIGHT_SENSOR:
                deviceIconImageView.setImageResource(R.drawable.day_cloudy_icon);
                swipeCardView.setCardBackgroundColor(getResources().getColor(R.color.light_gray));
                break;
            case HUMIDITY_SENSOR:
                deviceIconImageView.setImageResource(R.drawable.water_drop_teardrop_icon);
                deviceIconImageView.setMaxHeight(20);
                deviceIconImageView.setMaxWidth(20);
                swipeCardView.setCardBackgroundColor(getResources().getColor(R.color.humidity_blue));
                break;
            case MONITOR:
                deviceIconImageView.setImageResource(R.drawable.led_television_white_icon);
                deviceNameTextView.setTextColor(getResources().getColor(R.color.white));
                valueTextView.setTextColor(getResources().getColor(R.color.white));
                removeDeviceImageButton.setBackground(getResources().getDrawable(R.drawable.delete_button_white_small));
                swipeCardView.setCardBackgroundColor(getResources().getColor(R.color.humidity_blue));
                break;
            case LAMP:
                deviceIconImageView.setImageResource(R.drawable.bulb_icon);
                swipeCardView.setCardBackgroundColor(getResources().getColor(R.color.light_gray));
                break;
            case SPRINKLER:
                deviceIconImageView.setImageResource(R.drawable.watering_can_icon);
                swipeCardView.setCardBackgroundColor(getResources().getColor(R.color.monitor_blue));
                break;
        }

        deviceNameTextView.setText(deviceName);
        valueTextView.setText(getValueString());

        regularSwipeCardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SwipeCardFragment.this.getContext(), DeviceDetailsPopUpActivity.class);
                intent.putExtra("deviceId", deviceId);
                intent.putExtra("deviceCommonName", deviceName);
                intent.putExtra("deviceTypeString", deviceType.name());

                switch (deviceType) {
                    case MONITOR:
                        boolean toggled = (value == 0 || value == -1) ? false : true;
                        intent.putExtra("toggled", toggled);
                        break;
                    case LAMP:
                    case SPRINKLER:
                        intent.putExtra("progress", value);
                        break;
                }

                startActivity(intent);
            }
        });

        removeDeviceImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                globalClass.getGardenDatabase()
                        .deviceDao()
                        .deleteById(deviceId)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(Schedulers.newThread())
                        .subscribe(() -> {
                            Log.d(TAG, "Deleted node with ID: " + deviceId);
                        });
            }
        });
    }

    private void createLastSwipeCardView(View view) {
        View regularSwipeCardLayout = view.findViewById(R.id.regular_swipe_card);
        regularSwipeCardLayout.setVisibility(View.GONE);

        View addNewDeviceSwipeCardLayout = view.findViewById(R.id.add_new_device_swipe_card);
        addNewDeviceSwipeCardLayout.setVisibility(View.VISIBLE);

        TextView addNewDeviceTextView = addNewDeviceSwipeCardLayout.findViewById(R.id.addNewDeviceTextView);

        // Alter the child views, in order to adjust this fragment to the specific measurement
        // type that it is used for

        CardView swipeCardView = addNewDeviceSwipeCardLayout.findViewById(R.id.swipeCard);
        ImageView deviceIconImageView = addNewDeviceSwipeCardLayout.findViewById(R.id.deviceIconImageView);
        ImageButton addNewDeviceImageButton = addNewDeviceSwipeCardLayout.findViewById(R.id.addNewDeviceImageButton);

        switch (this.deviceType) {
            case TEMPERATURE_SENSOR:
                deviceIconImageView.setImageResource(R.drawable.thermometer_icon_white);
                //deviceIconImageView.setMaxHeight(2);
                addNewDeviceTextView.setTextColor(getResources().getColor(R.color.white));
                addNewDeviceImageButton.setBackground(getResources().getDrawable(R.drawable.add_button_white_plain));
                swipeCardView.setCardBackgroundColor(getResources().getColor(R.color.temperature_red));
                break;
            case LIGHT_SENSOR:
                deviceIconImageView.setImageResource(R.drawable.day_cloudy_icon);
                swipeCardView.setCardBackgroundColor(getResources().getColor(R.color.light_gray));
                break;
            case HUMIDITY_SENSOR:
                deviceIconImageView.setImageResource(R.drawable.water_drop_teardrop_icon);
                deviceIconImageView.setMaxHeight(20);
                deviceIconImageView.setMaxWidth(20);
                swipeCardView.setCardBackgroundColor(getResources().getColor(R.color.humidity_blue));
                break;
            case MONITOR:
                deviceIconImageView.setImageResource(R.drawable.led_television_white_icon);
                addNewDeviceTextView.setTextColor(getResources().getColor(R.color.white));
                addNewDeviceImageButton.setBackground(getResources().getDrawable(R.drawable.add_button_white_plain));
                swipeCardView.setCardBackgroundColor(getResources().getColor(R.color.humidity_blue));
                break;
            case LAMP:
                deviceIconImageView.setImageResource(R.drawable.bulb_icon);
                swipeCardView.setCardBackgroundColor(getResources().getColor(R.color.light_gray));
                break;
            case SPRINKLER:
                deviceIconImageView.setImageResource(R.drawable.watering_can_icon);
                swipeCardView.setCardBackgroundColor(getResources().getColor(R.color.monitor_blue));
                break;
        }

        addNewDeviceImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Steps:
                //  1. Open an overlay where the user can input the device name (and at this point also the reading's value)
                //  2. After the user submits the new device, save it to the db

                Intent intent = new Intent(getContext(), AddNewPopUpActivity.class);
                intent.putExtra("mode", ViewModes.DEVICE_MODE);
                intent.putExtra("deviceTypeString", deviceType.name());
                startActivity(intent);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSwipeCardBinding.inflate(getLayoutInflater(), container, false);

        // Inflate the layout for this fragment
        View view = binding.getRoot();

        if (this.isLastSwipeCard) {
            createLastSwipeCardView(view);
        } else {
            createRegularSwipeCardView(view);
        }

        return view;
    }

    private String getValueString() {
        if(value == -1) {
            return getActivity().getString(R.string.not_available);
        }

        switch (this.deviceType) {
            case TEMPERATURE_SENSOR:
                return value + "ºC";
            case LIGHT_SENSOR:
            case HUMIDITY_SENSOR:
            case LAMP:
            case SPRINKLER:
                return value + "%";
            case MONITOR:
                if (value == 0) {
                    return "OFF";
                } else {
                    return "ON";
                }
            default:
                return value + "";
        }
    }

    public void setValue(long value) {
        this.value = value;

        if (valueTextView != null) {
            valueTextView.setText(getValueString());
        }
    }
}

