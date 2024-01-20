package pt.ulisboa.tecnico.gardenmanager.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import pt.ulisboa.tecnico.gardenmanager.R;
import pt.ulisboa.tecnico.gardenmanager.constants.ViewModes;
import pt.ulisboa.tecnico.gardenmanager.domain.Device;
import pt.ulisboa.tecnico.gardenmanager.domain.Garden;

public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.SearchListItemViewHolder> implements Filterable {
    private int mode;

    private List<Garden> gardens;
    private List<Device> devices;

    private List<Garden> filteredGardens;
    private List<Device> filteredDevices;

    public SearchListAdapter(int mode) {
        this.mode = mode;
        this.devices = new ArrayList<>();
        this.gardens = new ArrayList<>();
        this.filteredDevices = new ArrayList<>();
        this.filteredGardens = new ArrayList<>();
    }

    public void setGardens(List<Garden> gardens) {
        this.gardens = gardens;
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }

    private void fillOutDeviceViewHolder(View itemView, int position) {
        Device device = filteredDevices.get(position);

        View deviceItemView = itemView.findViewById(R.id.deviceSearchListItem);
        TextView deviceNameTextView = itemView.findViewById(R.id.deviceNameTextView);
        TextView deviceIdTextView = itemView.findViewById(R.id.deviceIdTextView);
        TextView deviceStatusTextView = itemView.findViewById(R.id.deviceStatusTextView);

        deviceNameTextView.setText(device.getName());
        deviceIdTextView.setText(device.getDeviceId());

        if(device.getParentGardenId() == -1) {
            deviceItemView.setBackgroundResource(R.drawable.device_search_list_item_no_network_bg);
        } else {
            deviceStatusTextView.setText(R.string.in_garden);
        }
    }

    private void fillOutGardenViewHolder(View itemView, int position) {

    }

    @NonNull
    @Override
    public SearchListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int itemLayoutResource;

        if(mode == ViewModes.DEVICE_MODE) {
            itemLayoutResource = R.layout.device_search_list_item;
        } else {
            itemLayoutResource = R.layout.garden_search_list_item;
        }

        View itemView = LayoutInflater.from(parent.getContext()).inflate(itemLayoutResource, parent, false);
        return new SearchListAdapter.SearchListItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchListItemViewHolder holder, int position) {
        View itemView = holder.itemView;

        if(mode == ViewModes.DEVICE_MODE) {
            fillOutDeviceViewHolder(itemView, position);
        } else {
            fillOutGardenViewHolder(itemView, position);
        }
    }

    @Override
    public int getItemCount() {
        if(mode == ViewModes.DEVICE_MODE) {
            return filteredDevices.size();
        } else {
            return filteredGardens.size();
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                List resultingList;

                if(constraint == null || constraint.equals("")) {

                    if(mode == ViewModes.DEVICE_MODE) {
                        resultingList = gardens;
                    } else {
                        resultingList = devices;
                    }


                } else {
                    //resultingList = new ArrayList();

                    if(mode == ViewModes.DEVICE_MODE) {
                        resultingList = devices.stream().filter(device -> {
                            return device.getName().toLowerCase().contains(constraint.toString().toLowerCase());
                        }).collect(Collectors.toList());
                    } else {
                        resultingList = gardens.stream().filter(garden -> {
                            return garden.getName().toLowerCase().contains(constraint.toString().toLowerCase());
                        }).collect(Collectors.toList());
                    }
                }

                results.values = resultingList;
                results.count = resultingList.size();

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredDevices = (List<Device>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    class SearchListItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public View itemView;

        public SearchListItemViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
