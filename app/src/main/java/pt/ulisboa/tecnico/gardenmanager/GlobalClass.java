package pt.ulisboa.tecnico.gardenmanager;

import android.app.Application;

import pt.ulisboa.tecnico.gardenmanager.db.GardenDatabase;

public class GlobalClass extends Application {
    private GardenDatabase gardenDatabase;

    public GlobalClass() {
    }

    public GlobalClass(GardenDatabase gardenDatabase) {
        this.gardenDatabase = gardenDatabase;
    }

    public GardenDatabase getGardenDatabase() {
        return gardenDatabase;
    }

    public void setGardenDatabase(GardenDatabase gardenDatabase) {
        this.gardenDatabase = gardenDatabase;
    }
}
