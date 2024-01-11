package pt.ulisboa.tecnico.gardenmanager.domain;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class GardenWithDevicesAndReadings {
    @Embedded
    public Garden garden;

    @Relation(
            parentColumn = "gardenId",
            entityColumn = "parentGardenId"
    )
    public List<DeviceWithReadings> devicesWithReadings;
}
