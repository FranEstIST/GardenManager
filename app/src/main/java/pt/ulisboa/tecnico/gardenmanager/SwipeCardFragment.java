package pt.ulisboa.tecnico.gardenmanager;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private String deviceType;
    private String deviceName;
    private String value;

    public SwipeCardFragment() {
        // Required empty public constructor
    }

    /**
     * @param deviceType The type of the device associated with this fragment (thermometer, light sensor, etc.).
     * @param deviceName The common name of the device associated with this fragment.
     * @param value The value to be displayed on this fragment.
     * @return A new instance of fragment SwipeCardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SwipeCardFragment newInstance(String deviceType, String deviceName, String value) {
        SwipeCardFragment fragment = new SwipeCardFragment();
        Bundle args = new Bundle();
        args.putString(DEVICE_TYPE, deviceType);
        args.putString(DEVICE_NAME, deviceName);
        args.putString(VALUE, value);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.deviceType = getArguments().getString(DEVICE_TYPE);
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

        deviceNameTextView.setText(this.deviceName);
        valueTextView.setText(this.value);

        return view;
    }
}