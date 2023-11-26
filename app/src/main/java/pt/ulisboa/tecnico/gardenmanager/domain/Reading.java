package pt.ulisboa.tecnico.gardenmanager.domain;

public class Reading {
    private Device sender;
    private long value;
    private ReadingType readingType;
}

enum ReadingType {
    CELSIUS,
    FAHRENHEIT,
    PERCENTAGE,
    STATE
}
