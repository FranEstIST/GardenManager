package pt.ulisboa.tecnico.gardenmanager;

import android.app.Application;
import android.content.SharedPreferences;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import pt.ulisboa.tecnico.gardenmanager.db.GardenDatabase;

public class GlobalClass extends Application {
    private static final String DEFAULT_SERVER_URL= "https://192.168.1.102:8081/";
    private static final int DEFAULT_SERVER_POLLING_INTERVAL = 10000;
    private static final int DEFAULT_MAX_NUM_OF_READINGS = 1000;

    private GardenDatabase gardenDatabase;
    private int currentGardenId;
    private RequestQueue requestQueue;

    private String serverURL;
    private int serverPollingInterval;
    private int maxNumOfReadings;

    private SharedPreferences GMSharedPrefs;

    public GlobalClass() {
        this.currentGardenId = -1;
        this.serverURL = DEFAULT_SERVER_URL;
        this.serverPollingInterval = DEFAULT_SERVER_POLLING_INTERVAL;
        this.maxNumOfReadings = DEFAULT_MAX_NUM_OF_READINGS;
    }

    public GlobalClass(GardenDatabase gardenDatabase) {
        this.gardenDatabase = gardenDatabase;
        this.currentGardenId = -1;
        this.serverURL = DEFAULT_SERVER_URL;
        this.serverPollingInterval = DEFAULT_SERVER_POLLING_INTERVAL;
        this.maxNumOfReadings = DEFAULT_MAX_NUM_OF_READINGS;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        GMSharedPrefs = getSharedPreferences("GMSharedPrefs", MODE_PRIVATE);
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

    public int getMaxNumOfReadings() {
        return GMSharedPrefs
                .getInt("maxNumOfReadings"
                        , DEFAULT_MAX_NUM_OF_READINGS);
    }

    public void setMaxNumOfReadings(int maxNumOfReadings) {
        GMSharedPrefs
                .edit()
                .putInt("maxNumOfReadings"
                        , maxNumOfReadings)
                .apply();
    }

    public String getServerURL() {
        return GMSharedPrefs
                .getString("serverURL"
                        , DEFAULT_SERVER_URL);
    }

    public void setServerURL(String serverURL) {
        GMSharedPrefs
                .edit()
                .putString("serverURL"
                        , serverURL)
                .apply();
    }

    public int getServerPollingInterval() {
        return GMSharedPrefs
                .getInt("serverPollingInterval"
                        , DEFAULT_SERVER_POLLING_INTERVAL);
    }

    public void setServerPollingInterval(int serverPollingInterval) {
        GMSharedPrefs
                .edit()
                .putInt("serverPollingInterval"
                        , serverPollingInterval)
                .apply();
    }
}
