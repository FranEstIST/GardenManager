package pt.ulisboa.tecnico.gardenmanager.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import pt.ulisboa.tecnico.gardenmanager.R;
import pt.ulisboa.tecnico.gardenmanager.constants.ViewModes;
import pt.ulisboa.tecnico.gardenmanager.databinding.FragmentCreateNewBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateNewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateNewFragment extends Fragment {
    private FragmentCreateNewBinding binding;

    private int mode;

    private static final String ARG_MODE = "mode";

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
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (getArguments() != null) {
            mode = getArguments().getInt(ARG_MODE);
        }

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCreateNewBinding.inflate(getLayoutInflater());

        String createNewTitle;
        String hint;
        String submitButtonText;

        if(mode == ViewModes.DEVICE_MODE) {
            createNewTitle = getString(R.string.create_new_device);
            hint = getString(R.string.device_name);
            submitButtonText = getString(R.string.create_device);
        } else {
            createNewTitle = getString(R.string.create_new_garden);
            hint = getString(R.string.garden_name);
            submitButtonText = getString(R.string.create_garden);
        }

        TextView createNewTitleTextView = binding.createNewTitleTextView;
        TextView createNewNameTextView = binding.createNewNameTextView;
        Button createNewSubmitButton = binding.createNewSubmitButton;

        createNewTitleTextView.setText(createNewTitle);
        createNewNameTextView.setHint(hint);
        createNewSubmitButton.setText(submitButtonText);

        return binding.getRoot();
    }
}