package pt.ulisboa.tecnico.gardenmanager;

import android.util.Log;
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
    public static final String TAG = "GardenListAdapter";
    private ArrayList<GardenWithDevices> gardensWithDevices;
    private GlobalClass globalClass;

    public GardenListAdapter(GlobalClass globalClass, ArrayList<GardenWithDevices> gardensWithDevices) {
        this.globalClass = globalClass;
        this.gardensWithDevices = gardensWithDevices;
    }

    public GardenListAdapter(GlobalClass globalClass) {
        this.globalClass = globalClass;
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

        int gardenId = garden.getGardenId();

        if(gardenId == globalClass.getCurrentGardenId()) {
            holder.gardenListItem.setBackgroundResource(R.drawable.garden_list_item_selected_bg);
        } else {
            holder.gardenListItem.setBackgroundResource(R.drawable.garden_list_item_bg);
        }

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
        public View gardenListItem;
        public TextView gardenNameTextView;
        public TextView numberOfDevicesTextView;

        public GardenListItemViewHolder(@NonNull View itemView) {
            super(itemView);
            this.gardenListItem = itemView.findViewById(R.id.gardenListItem);
            this.gardenNameTextView = itemView.findViewById(R.id.gardenNameTextView);
            this.numberOfDevicesTextView = itemView.findViewById(R.id.numberOfDevicesTextView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            Garden selectedGarden = gardensWithDevices.get(clickedPosition).garden;
            int selectedGardenId = selectedGarden.getGardenId();
            globalClass.setCurrentGardenId(selectedGardenId);

            Log.d(TAG, "Clicked garden at position: " + clickedPosition);

            notifyDataSetChanged();
        }
    }
}
