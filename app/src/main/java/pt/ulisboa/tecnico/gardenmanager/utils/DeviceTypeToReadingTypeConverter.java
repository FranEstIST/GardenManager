package pt.ulisboa.tecnico.gardenmanager.utils;

import pt.ulisboa.tecnico.gardenmanager.domain.DeviceType;
import pt.ulisboa.tecnico.gardenmanager.domain.ReadingType;

public class DeviceTypeToReadingTypeConverter {
    public static ReadingType convertToReadingType(DeviceType deviceType) {
        switch(deviceType)  {
            case TEMPERATURE_SENSOR:
                return ReadingType.CELSIUS;
            case LIGHT_SENSOR:
                return ReadingType.PERCENTAGE;
            case HUMIDITY_SENSOR:
                return ReadingType.PERCENTAGE;
            default:
                return ReadingType.STATE;
        }
    }
}
