package pt.ulisboa.tecnico.gardenmanager.network;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pt.ulisboa.tecnico.gardenmanager.GlobalClass;

public class WithoutNetService {
    private GlobalClass globalClass;
    private RequestQueue requestQueue;

    private static final String BASE_URL = "http://192.168.1.102:8081/";
    private static final String GET_MESSAGES_URL = BASE_URL + "get-most-recent-messages-by-sender-and-receiver";
    private static final String GET_NETWORK_BY_ID_BASE_URL = BASE_URL + "get-network-by-id/";
    private static final String GET_NODES_WITHOUT_A_NETWORK_URL = BASE_URL + "get-nodes-without-a-network";
    private static final String ADD_NODE_URL = BASE_URL + "add-node";
    private static final String ADD_NODE_TO_NETWORK_URL = BASE_URL + "add-node-to-network";
    private static final String REMOVE_NODE_FROM_NETWORK_URL = BASE_URL + "remove-node-from-network";
    private static final String DELETE_NODE_BASE_URL = BASE_URL + "delete-node/";

    private static final int GARDEN_MANAGER_NODE_ID = 21342;

    private static final int MAX_NUM_OF_READINGS = 20;

    public WithoutNetService(GlobalClass globalClass) {
        this.globalClass = globalClass;
        this.requestQueue = globalClass.getRequestQueue();
    }

    public void getReadings(int senderId) {
        String url = GET_MESSAGES_URL;

        JSONObject jsonRequest = new JSONObject();

        try {
            jsonRequest.put("senderId", senderId);
            jsonRequest.put("receiverId", GARDEN_MANAGER_NODE_ID);
            jsonRequest.put("maxNumOfMessages", MAX_NUM_OF_READINGS);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST ,url, jsonRequest, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        this.requestQueue.add(request);
    }

    public void getAllDevicesInGarden(int gardenId) {
        String url = GET_NETWORK_BY_ID_BASE_URL + gardenId;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        this.requestQueue.add(request);
    }

    public void getAllGardenlessDevices() {
    }

    public void getAllDevicesInGardenContainingSubstring(int gardenId, String substring) {

    }

    public void getAllGardenlessDevicesContainingSubstring(String substring) {

    }

    public void addDeviceToGarden(String deviceName, int gardenId) {
        // Step 1: Add node to the server

        String url = ADD_NODE_URL;

        JSONObject jsonRequest = new JSONObject();

        try {
            jsonRequest.put("commonName", deviceName);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST ,url, jsonRequest, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // Step 2: Add node to the network
                JSONObject nodeJson = null;
                int nodeId = 0;

                try {
                    nodeJson = response.getJSONObject("node");
                    nodeId = nodeJson.getInt("id");
                } catch (JSONException e) {
                    e.printStackTrace();
                    return;
                }

                String url = ADD_NODE_TO_NETWORK_URL;

                JSONObject jsonRequest = new JSONObject();

                try {
                    jsonRequest.put("nodeId", nodeId);
                    jsonRequest.put("gardenId", gardenId);
                } catch (JSONException e) {
                    e.printStackTrace();
                    return;
                }

                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST ,url, jsonRequest, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

                requestQueue.add(request);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        this.requestQueue.add(request);
    }

    public void removeDeviceFromGarden(int deviceId, int gardenId) {
        String url = REMOVE_NODE_FROM_NETWORK_URL;

        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("nodeId", deviceId);
            jsonRequest.put("networkId", gardenId);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonRequest, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        this.requestQueue.add(request);
    }

    // public void getReadingsAfterTimestamp(int senderId, long timestamp) { }

    // public void deleteDevice(int deviceId) { }

    // public void getDeviceReadings(int deviceId) { }
}
