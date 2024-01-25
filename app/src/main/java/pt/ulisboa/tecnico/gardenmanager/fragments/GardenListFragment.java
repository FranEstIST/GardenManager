package pt.ulisboa.tecnico.gardenmanager.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.gardenmanager.activities.AddNewPopUpActivity;
import pt.ulisboa.tecnico.gardenmanager.adapters.GardenListAdapter;
import pt.ulisboa.tecnico.gardenmanager.constants.ViewModes;
import pt.ulisboa.tecnico.gardenmanager.fragments.viewmodels.GardenListViewModel;
import pt.ulisboa.tecnico.gardenmanager.GlobalClass;
import pt.ulisboa.tecnico.gardenmanager.R;
import pt.ulisboa.tecnico.gardenmanager.fragments.viewmodels.ViewModelFactory;
import pt.ulisboa.tecnico.gardenmanager.activities.MainActivity;
import pt.ulisboa.tecnico.gardenmanager.databinding.FragmentGardenListBinding;
import pt.ulisboa.tecnico.gardenmanager.db.GardenDatabase;
import pt.ulisboa.tecnico.gardenmanager.domain.GardenWithDevices;

public class GardenListFragment extends Fragment {

    private FragmentGardenListBinding binding;
    private final String TAG = "GardenListFragment";

    private GlobalClass globalClass;

    private GardenDatabase gardenDatabase;
    private GardenListViewModel gardenListViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.globalClass = (GlobalClass) this.getActivity().getApplicationContext();

        this.gardenDatabase = this.globalClass.getGardenDatabase();

        this.gardenListViewModel = new ViewModelFactory(
                gardenDatabase.gardenDao(),
                gardenDatabase.deviceDao(),
                gardenDatabase.readingDao()
        ).create(GardenListViewModel.class);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentGardenListBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.addNewGardenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GardenListFragment.this.getActivity().getApplicationContext(),
                        AddNewPopUpActivity.class);
                intent.putExtra("mode", ViewModes.GARDEN_MODE);
                startActivity(intent);
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        binding.gardenListRecyclerView.setLayoutManager(layoutManager);

        GardenListAdapter gardenListAdapter = new GardenListAdapter(this.globalClass);
        binding.gardenListRecyclerView.setAdapter(gardenListAdapter);

        MainActivity mainActivity = (MainActivity) getActivity();

        if(mainActivity != null && mainActivity.binding != null) {
            mainActivity.binding.appBarMain.appBarTitle.setText(R.string.my_gardens);
        }

        this.gardenListViewModel.getAllGardensWithDevices().observe(getViewLifecycleOwner(), new Observer<List<GardenWithDevices>>() {
            @Override
            public void onChanged(@Nullable List<GardenWithDevices> gardensWithDevices) {
                Log.d(TAG, "List of gardens has been updated");
                gardenListAdapter.setGardensWithDevices(new ArrayList<>(gardensWithDevices));
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}