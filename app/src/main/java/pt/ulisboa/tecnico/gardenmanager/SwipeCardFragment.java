package pt.ulisboa.tecnico.gardenmanager;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SwipeCardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SwipeCardFragment extends Fragment {
    private static final String DEVICE_TYPE = "device_type";
    private static final String DEVICE_NAME = "device_name";
    private static final String VALUE = "value";

    // TODO: Change types of parameters
    private DeviceType deviceType;
    private String deviceName;
    private String value;

    public SwipeCardFragment() {
        // Required empty public constructor
    }

    /**
     * @param deviceType The type of the device associated with this fragment (TEMPERATURE_SENSOR, LIGHT_SENSOR, etc.).
     * @param deviceName The common name of the device associated with this fragment.
     * @param value The value to be displayed on this fragment.
     * @return A new instance of fragment SwipeCardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SwipeCardFragment newInstance(DeviceType deviceType, String deviceName, String value) {
        SwipeCardFragment fragment = new SwipeCardFragment();
        Bundle args = new Bundle();
        args.putString(DEVICE_TYPE, deviceType.name());
        args.putString(DEVICE_NAME, deviceName);
        args.putString(VALUE, value);
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
            this.value = getArguments().getString(VALUE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_swipe_card, container, false);
        TextView deviceNameTextView = view.findViewById(R.id.deviceNameTextView);
        TextView valueTextView = view.findViewById(R.id.valueTextView);

        // Alter the child views, in order to adjust this fragment to the specific measurement
        // type that it is used for

        CardView swipeCardView = view.findViewById(R.id.swipeCard);
        ImageView deviceIconImageView = view.findViewById(R.id.deviceIconImageView);
        ImageButton removeDeviceImageButton = view.findViewById(R.id.removeDeviceImageButton);

        switch(this.deviceType) {
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
        }
        deviceNameTextView.setText(this.deviceName);
        valueTextView.setText(this.value);

        return view;
    }
}

