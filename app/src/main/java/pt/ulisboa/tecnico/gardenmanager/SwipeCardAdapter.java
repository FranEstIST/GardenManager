package pt.ulisboa.tecnico.gardenmanager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class SwipeCardAdapter extends FragmentStateAdapter {
    private DeviceType deviceType;

    public SwipeCardAdapter(@NonNull Fragment fragment, DeviceType deviceType) {
        super(fragment);
        this.deviceType = deviceType;
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

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if(position == (this.getItemCount() - 1)) {
            // TODO: The last swipe card in the swipe card pager should show a button to
            // add a new device (i.e. a new swipe card) to this pager
        }

        String deviceName = "" + (position + 1);
        String value = "" + (position*10);

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
        return 3;
    }
}
