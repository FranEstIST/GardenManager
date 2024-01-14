package pt.ulisboa.tecnico.gardenmanager;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import pt.ulisboa.tecnico.gardenmanager.db.GardenDatabase;

public class GlobalClass extends Application {
    private GardenDatabase gardenDatabase;
    private int currentGardenId;
    private RequestQueue requestQueue;

    public GlobalClass() {
        this.currentGardenId = -1;
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

    public int getCurrentGardenId() {
        return currentGardenId;
    }

    public void setCurrentGardenId(int currentGardenId) {
        this.currentGardenId = currentGardenId;
    }

    public RequestQueue getRequestQueue() {
        if(this.requestQueue == null) {
            this.requestQueue = Volley.newRequestQueue(this.getApplicationContext());
        }

        return this.requestQueue;
    }
}
