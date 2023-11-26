package pt.ulisboa.tecnico.gardenmanager;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import pt.ulisboa.tecnico.gardenmanager.databinding.FragmentFirstBinding;

public class SensorsDashboardFragment extends Fragment {

    private FragmentFirstBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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

        setUpSwipeCards();
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

        setUpSwipeCards();
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
    }
}