package pt.ulisboa.tecnico.gardenmanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class GardenListAdapter extends RecyclerView.Adapter<GardenListAdapter.GardenListItemViewHolder> {
    @NonNull
    @Override
    public GardenListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.garden_list_item, parent, false);
        return new GardenListItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull GardenListItemViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 3;
    }

    class GardenListItemViewHolder extends RecyclerView.ViewHolder {
        public GardenListItemViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
