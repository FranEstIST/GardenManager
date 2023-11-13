package pt.ulisboa.tecnico.gardenmanager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class SwipeCardAdapter extends FragmentStateAdapter {
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
        SwipeCardFragment swipeCardFragment = SwipeCardFragment.newInstance(""
                , "device " + (position + 1)
                , "" + (position*10));

        return swipeCardFragment;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
