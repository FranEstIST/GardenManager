package pt.ulisboa.tecnico.gardenmanager.network;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;

import pt.ulisboa.tecnico.gardenmanager.GlobalClass;

public class WithoutNetService {
    private GlobalClass globalClass;
    private RequestQueue requestQueue;

    private static final String BASE_URL = "https://127.0.0.1/";
    private static final String GET_MESSAGES_BASE_URL = BASE_URL + "get-messages-by-sender-and-receiver/";

    private static final int GARDEN_MANAGER_NODE_ID = 21342;

    public WithoutNetService(GlobalClass globalClass) {
        this.globalClass = globalClass;
        this.requestQueue = globalClass.getRequestQueue();
    }

    public void getReadings(int senderId) {
        String url = GET_MESSAGES_BASE_URL + senderId + "/" + GARDEN_MANAGER_NODE_ID;
        JsonArrayRequest request = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        this.requestQueue.add(request);
    }

    public void getReadingsAfterTimestamp(int senderId, long timestamp) {

    }

    public void getAllDevicesInGarden(int gardenId) {

    }

    public void getAllGardenlessDevices() {

    }

    public void getAllDevicesInGardenContainingSubstring(int gardenId, String substring) {

    }

    public void getAllGardenlessDevicesContainingSubstring(String substring) {

    }

    public void addDeviceToGarden(String deviceName, int gardenId) {

    }

    public void removeDeviceFromGarden(int deviceId, int gardenId) {

    }

    public void deleteDevice(int deviceId) {

    }

    public void getDeviceReadings(int deviceId) {

    }
}
