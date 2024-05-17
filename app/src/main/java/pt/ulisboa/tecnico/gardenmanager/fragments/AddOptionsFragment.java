package pt.ulisboa.tecnico.gardenmanager.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import pt.ulisboa.tecnico.gardenmanager.R;
import pt.ulisboa.tecnico.gardenmanager.constants.ViewModes;
import pt.ulisboa.tecnico.gardenmanager.databinding.AddOptionsButtonBinding;
import pt.ulisboa.tecnico.gardenmanager.databinding.FragmentAddOptionsBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddOptionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddOptionsFragment extends Fragment {
    private static final String MODE = "mode";

    private int mode;

    private FragmentAddOptionsBinding binding;

    private SelectAddOptionListener selectAddOptionListener;

    public AddOptionsFragment() {
    }

    public static AddOptionsFragment newInstance(int mode) {
        AddOptionsFragment fragment = new AddOptionsFragment();
        Bundle args = new Bundle();
        args.putInt(MODE, mode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mode = getArguments().getInt(MODE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddOptionsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        createAddNewView();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.selectAddOptionListener = (SelectAddOptionListener) context;
    }

    private void createAddNewView() {
        String addExistingText;
        String createNewText;

        if(mode == ViewModes.DEVICE_MODE) {
            addExistingText = getString(R.string.add_existing_device);
            createNewText = getString(R.string.create_new_device);
        } else {
            addExistingText = getString(R.string.add_existing_garden);
            createNewText = getString(R.string.create_new_garden);
        }

        // Create the "add existing device" button
        AddOptionsButtonBinding addExistingDeviceButtonBinding = binding.addExistingButton;

        TextView addExistingDeviceTextView = addExistingDeviceButtonBinding.addOptionsButtonTextView;
        ImageView addExistingDeviceIconImageView = addExistingDeviceButtonBinding.addOptionsButtonIconImageView;

        addExistingDeviceTextView.setText(addExistingText);
        addExistingDeviceIconImageView.setImageResource(R.drawable.ic_baseline_search_black_24);

        CardView addExistingDeviceCardView = addExistingDeviceButtonBinding.addOptionsButtonCardView;

        addExistingDeviceCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectAddOptionListener != null) {
                    selectAddOptionListener.onAddExistingClick();
                }
            }
        });

        // Create the "create new device" button
        AddOptionsButtonBinding createNewDeviceButtonBinding = binding.createNewButton;

        TextView createNewDeviceTextView = createNewDeviceButtonBinding.addOptionsButtonTextView;
        ImageView createNewDeviceIconImageView = createNewDeviceButtonBinding.addOptionsButtonIconImageView;

        createNewDeviceTextView.setText(createNewText);
        createNewDeviceIconImageView.setImageResource(R.drawable.create_new_icon_v2);

        CardView createNewDeviceCardView = createNewDeviceButtonBinding.addOptionsButtonCardView;

        createNewDeviceCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectAddOptionListener != null) {
                    selectAddOptionListener.onCreateNewClick();
                }
            }
        });
    }

    public interface SelectAddOptionListener {
        void onAddExistingClick();
        void onCreateNewClick();
    }

}