package pt.ulisboa.tecnico.gardenmanager.domain;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class DeviceWithReadings {
    @Embedded
    public Device device;

    @Relation(
            parentColumn = "deviceId",
            entityColumn = "sender-id"
    )
    public List<Reading> readings;
}
