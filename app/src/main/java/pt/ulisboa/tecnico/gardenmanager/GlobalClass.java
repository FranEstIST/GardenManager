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
    private static final int DEFAULT_GM_ID = 64;

    private GardenDatabase gardenDatabase;
    private RequestQueue requestQueue;

    private SharedPreferences GMSharedPrefs;

    public GlobalClass() {
    }

    public GlobalClass(GardenDatabase gardenDatabase) {
        this.gardenDatabase = gardenDatabase;
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
        return GMSharedPrefs
                .getInt("currentGardenId",
                        -1);
    }

    public void setCurrentGardenId(int currentGardenId) {
        GMSharedPrefs
                .edit()
                .putInt("currentGardenId"
                        , currentGardenId)
                .apply();
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

    public int getGardenManagerId() {
        return GMSharedPrefs
                .getInt("gardenManagerId"
                        , DEFAULT_GM_ID);
    }

    public void setGardenManagerId(int gardenManagerId) {
        GMSharedPrefs
                .edit()
                .putInt("gardenManagerId"
                        , gardenManagerId)
                .apply();
    }

    public int getSendCounter() {
        int sendCounter = GMSharedPrefs
                .getInt("sendCounter"
                    , 0);

        sendCounter++;

        GMSharedPrefs
                .edit()
                .putInt("sendCounter"
                        , sendCounter)
                .apply();

        return sendCounter;
    }
}
