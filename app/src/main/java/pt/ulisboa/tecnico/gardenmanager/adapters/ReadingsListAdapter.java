package pt.ulisboa.tecnico.gardenmanager.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import pt.ulisboa.tecnico.gardenmanager.R;
import pt.ulisboa.tecnico.gardenmanager.domain.DeviceType;
import pt.ulisboa.tecnico.gardenmanager.domain.Reading;

public class ReadingsListAdapter extends RecyclerView.Adapter<ReadingsListAdapter.ReadingsListItemViewHolder> {
    private List<Reading> readings;
    private DeviceType deviceType;

    public ReadingsListAdapter(List<Reading> readings, DeviceType deviceType) {
        this.readings = readings;
        this.deviceType = deviceType;
    }

    @NonNull
    @Override
    public ReadingsListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.readings_list_item, parent, false);
        return new ReadingsListItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReadingsListItemViewHolder holder, int position) {
        Reading reading = readings.get(position);

        TextView readingValueTextView = holder.getReadingValueTextView();
        TextView readingTimestampTextView = holder.getReadingTimestampTextView();

        readingValueTextView.setText(reading.getValue() + "");

        String readingValueString;

        switch(this.deviceType) {
            case TEMPERATURE_SENSOR:
                readingValueString = reading.getValue() + "ºC";
                break;
            case LIGHT_SENSOR:
            case HUMIDITY_SENSOR:
            case LAMP:
            case SPRINKLER:
                readingValueString = reading.getValue() + "%";
                break;
            case MONITOR:
                if(reading.getValue() == 0) {
                    readingValueString = "OFF";
                } else {
                    readingValueString = "ON";
                }
                break;
            default:
                readingValueString = reading.getValue() + "";
                break;
        }

        readingValueTextView.setText(readingValueString);

        if(position == 0) {
            readingTimestampTextView.setText(R.string.most_recent);
        } else {
            Reading prevReading = readings.get(position - 1);

            long timeDif = reading.getTimestamp() - prevReading.getTimestamp();

            Date date = new Date(timeDif);
            DateFormat formatter = new SimpleDateFormat("HH:mm:ss.SSS");
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            String formattedTimeDif = formatter.format(date);
            
            readingTimestampTextView.setText(formattedTimeDif);
        }
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class ReadingsListItemViewHolder extends RecyclerView.ViewHolder {
        private TextView readingValueTextView;
        private TextView readingTimestampTextView;

        public ReadingsListItemViewHolder(@NonNull View itemView) {
            super(itemView);
            this.readingValueTextView = itemView.findViewById(R.id.readingValueTextView);
            this.readingTimestampTextView = itemView.findViewById(R.id.readingTimestampTextView);
        }

        public TextView getReadingValueTextView() {
            return readingValueTextView;
        }

        public TextView getReadingTimestampTextView() {
            return readingTimestampTextView;
        }
    }
}
