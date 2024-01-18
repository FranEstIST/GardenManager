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

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String MODE = "mode";

    private int mode;

    private FragmentAddOptionsBinding binding;

    private SelectAddOptionListener selectAddOptionListener;

    public AddOptionsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param mode The mode to create this fragment in (this can be either device or garden mode)
     * @return A new instance of fragment AddOptionsFragment.
     */
    // TODO: Rename and change types and number of parameters
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
        public void onAddExistingClick();

        public void onCreateNewClick();

        public static SelectAddOptionListener getDefaultInstance() {
            return new SelectAddOptionListener() {
                @Override
                public void onAddExistingClick() {
                }

                @Override
                public void onCreateNewClick() {
                }
            };
        }
    }

}