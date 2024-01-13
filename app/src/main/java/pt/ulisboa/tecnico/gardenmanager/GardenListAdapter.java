package pt.ulisboa.tecnico.gardenmanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.gardenmanager.domain.Device;
import pt.ulisboa.tecnico.gardenmanager.domain.Garden;
import pt.ulisboa.tecnico.gardenmanager.domain.GardenWithDevices;

public class GardenListAdapter extends RecyclerView.Adapter<GardenListAdapter.GardenListItemViewHolder> {
    private ArrayList<GardenWithDevices> gardensWithDevices;

    public GardenListAdapter(ArrayList<GardenWithDevices> gardensWithDevices) {
        this.gardensWithDevices = gardensWithDevices;
    }

    public GardenListAdapter() {
        this.gardensWithDevices = new ArrayList<>();
    }

    @NonNull
    @Override
    public GardenListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.garden_list_item, parent, false);
        return new GardenListItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull GardenListItemViewHolder holder, int position) {
        GardenWithDevices gardenWithDevices = this.gardensWithDevices.get(position);

        Garden garden = gardenWithDevices.garden;

        List<Device> devices = gardenWithDevices.devices;

        String gardenName = garden.getName();
        int numberOfDevicesInGarden = devices.size();

        holder.gardenNameTextView.setText(gardenName);
        holder.numberOfDevicesTextView.setText(numberOfDevicesInGarden + "");
    }

    @Override
    public int getItemCount() {
        return gardensWithDevices.size();
    }

    public void setGardensWithDevices(ArrayList<GardenWithDevices> gardensWithDevices) {
        this.gardensWithDevices = gardensWithDevices;
        this.notifyDataSetChanged();
    }

    class GardenListItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView gardenNameTextView;
        public TextView numberOfDevicesTextView;

        public GardenListItemViewHolder(@NonNull View itemView) {
            super(itemView);
            this.gardenNameTextView = itemView.findViewById(R.id.gardenNameTextView);
            this.numberOfDevicesTextView = itemView.findViewById(R.id.numberOfDevicesTextView);
        }

        @Override
        public void onClick(View v) {
            // TODO: Navigate to the respective garden dashboard
            // getAdapterPosition() will come in handy
        }
    }
}
