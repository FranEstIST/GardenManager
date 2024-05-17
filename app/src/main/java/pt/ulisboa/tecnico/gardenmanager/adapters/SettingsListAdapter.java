package pt.ulisboa.tecnico.gardenmanager.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import pt.ulisboa.tecnico.gardenmanager.GlobalClass;
import pt.ulisboa.tecnico.gardenmanager.R;
import pt.ulisboa.tecnico.gardenmanager.activities.EditSettingPopUpActivity;

public class SettingsListAdapter  extends RecyclerView.Adapter<SettingsListAdapter.ViewHolder> {
    private static final int NUM_OF_SETTINGS = 3;

    private Activity activity;
    private GlobalClass globalClass;
    private ActivityResultLauncher<Intent> changeSettingValueResultLauncher;

    public SettingsListAdapter(GlobalClass globalClass, ActivityResultLauncher<Intent> changeSettingValueResultLauncher) {
        this.activity = activity;
        this.globalClass = globalClass;
        this.changeSettingValueResultLauncher = changeSettingValueResultLauncher;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View itemView = inflater.inflate(R.layout.settings_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        switch(position) {
            case 0:
                holder.settingNameTextView.setText(R.string.server_url);
                holder.settingValueTextView.setText(globalClass.getServerURL());
                break;
            case 1:
                holder.settingNameTextView.setText(R.string.garden_manager_id);
                holder.settingValueTextView.setText(globalClass.getGardenManagerId() + "");
                break;
            case 2:
                holder.settingNameTextView.setText(R.string.server_polling_interval);
                holder.settingValueTextView.setText(globalClass.getServerPollingInterval() + "");
                break;
            default:
                holder.settingNameTextView.setText(R.string.not_available);
                break;
        }

        holder.editSettingValueButton.setOnClickListener(new View.OnClickListener() {
            Intent intent = new Intent(globalClass, EditSettingPopUpActivity.class);

            @Override
            public void onClick(View v) {
                switch(holder.getAdapterPosition()) {
                    case 0:
                        intent.putExtra("setting-type"
                                , EditSettingPopUpActivity.SettingType.SERVER_URL);

                        break;
                    case 1:
                        intent.putExtra("setting-type"
                                , EditSettingPopUpActivity.SettingType.GM_ID);

                        break;
                    case 2:
                        intent.putExtra("setting-type"
                                , EditSettingPopUpActivity.SettingType.SERVER_POLLING_INTERVAL);

                        break;
                    default:
                        return;
                }

                changeSettingValueResultLauncher.launch(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return NUM_OF_SETTINGS;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView settingNameTextView;
        public TextView settingValueTextView;
        public ImageButton editSettingValueButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            settingNameTextView = itemView.findViewById(R.id.settingName);
            settingValueTextView = itemView.findViewById(R.id.settingValue);
            editSettingValueButton = itemView.findViewById(R.id.editSettingValueButton);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
