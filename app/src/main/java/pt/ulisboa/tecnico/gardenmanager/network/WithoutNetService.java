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
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.gardenmanager.GlobalClass;
import pt.ulisboa.tecnico.gardenmanager.constants.StatusCodes;
import pt.ulisboa.tecnico.gardenmanager.network.dto.DeviceDto;
import pt.ulisboa.tecnico.gardenmanager.network.dto.GardenDto;

public class WithoutNetService {
    private GlobalClass globalClass;
    private RequestQueue requestQueue;

    private static final String BASE_URL = "https://192.168.1.102:8081/";
    private static final String GET_MESSAGES_URL = BASE_URL + "get-most-recent-messages-by-sender-and-receiver";
    private static final String GET_NETWORK_BY_ID_BASE_URL = BASE_URL + "get-network-by-id/";
    private static final String GET_NODES_WITHOUT_A_NETWORK_URL = BASE_URL + "get-nodes-without-a-network";
    private static final String FIND_NODES_BY_NETWORK_ID_AND_SEARCH_TERM_BASE_URL = BASE_URL + "find-nodes-by-network-id-and-search-term/";
    private static final String FIND_NETWORKS_BY_SEARCH_TERM_BASE_URL = BASE_URL + "find-networks-by-search-term/";
    private static final String ADD_NETWORK_URL = BASE_URL + "add-network";
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

    public void getAllDevicesInGardenContainingSubstring(int gardenId, String substring, WithoutNetServiceResponseListener responseListener) {
        String url = FIND_NODES_BY_NETWORK_ID_AND_SEARCH_TERM_BASE_URL + gardenId + "/" + substring;

        JsonObjectRequest request = new JsonObjectRequest(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                int status = StatusCodes.UNKNOWN_ERROR;

                try {
                    status = response.getInt("status");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(status == StatusCodes.OK) {
                    try {
                        JSONArray deviceJsonArray = response.getJSONArray("nodes");
                        List<DeviceDto> deviceDtos = new ArrayList<>();

                        for(int i = 0; i < deviceJsonArray.length(); i++) {
                            JSONObject deviceJson = deviceJsonArray.getJSONObject(i);
                            DeviceDto deviceDto  = new DeviceDto(deviceJson);
                            deviceDtos.add(deviceDto);
                        }

                        responseListener.onResponse(deviceDtos);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    // TODO: Handle error status codes
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                responseListener.onError(error.getMessage());
            }
        });

        this.requestQueue.add(request);
    }

    public void getAllGardensContainingSubstring(String substring, WithoutNetServiceResponseListener responseListener) {
        String url = FIND_NETWORKS_BY_SEARCH_TERM_BASE_URL + substring;

        JsonObjectRequest request = new JsonObjectRequest(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                int status = StatusCodes.UNKNOWN_ERROR;

                try {
                    status = response.getInt("status");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(status == StatusCodes.OK) {
                    try {
                        JSONArray gardenJsonArray = response.getJSONArray("networks");
                        List<GardenDto> gardenDtos = new ArrayList<>();

                        for(int i = 0; i < gardenJsonArray.length(); i++) {
                            JSONObject gardenJson = gardenJsonArray.getJSONObject(i);
                            GardenDto gardenDto = new GardenDto(gardenJson);
                            gardenDtos.add(gardenDto);
                        }

                        responseListener.onResponse(gardenDtos);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    // TODO: Handle error status codes
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                responseListener.onError(error.getMessage());
            }
        });

        this.requestQueue.add(request);
    }

    public void getAllGardenlessDevicesContainingSubstring(String substring) {

    }

    public void addGarden(String gardenName, WithoutNetServiceResponseListener responseListener) {
        String url = ADD_NETWORK_URL;

        JSONObject jsonRequest = new JSONObject();

        try {
            jsonRequest.put("name", gardenName);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonRequest, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                int status = StatusCodes.UNKNOWN_ERROR;

                try {
                    status = response.getInt("status");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(status == StatusCodes.OK) {
                    try {
                        JSONObject gardenJsonObject = response.getJSONObject("network");
                        responseListener.onResponse(new GardenDto(gardenJsonObject));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    // TODO: Handle error status codes
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                responseListener.onError(error.getMessage());
            }
        });

        this.requestQueue.add(request);
    }

    public void addDevice(String deviceName, WithoutNetServiceResponseListener responseListener) {
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
                int status = StatusCodes.UNKNOWN_ERROR;

                try {
                    status = response.getInt("status");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(status == StatusCodes.OK) {
                    try {
                        JSONObject deviceJsonObject = response.getJSONObject("device");
                        responseListener.onResponse(new DeviceDto(deviceJsonObject));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    // TODO: Handle error status codes
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                responseListener.onError(error.getMessage());
            }
        });

        this.requestQueue.add(request);
    }

    public void addDevice(String deviceName, int gardenId, WithoutNetServiceResponseListener responseListener) {
        String url = ADD_NODE_URL;

        JSONObject jsonRequest = new JSONObject();

        try {
            jsonRequest.put("commonName", deviceName);
            jsonRequest.put("networkId", gardenId);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST ,url, jsonRequest, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                int status = StatusCodes.UNKNOWN_ERROR;

                try {
                    status = response.getInt("status");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(status == StatusCodes.OK) {
                    try {
                        JSONObject deviceJsonObject = response.getJSONObject("node");
                        responseListener.onResponse(new DeviceDto(deviceJsonObject));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    // TODO: Handle error status codes
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                responseListener.onError(error.getMessage());
            }
        });

        this.requestQueue.add(request);
    }

    public void addDeviceToGarden(String deviceName, int gardenId, WithoutNetServiceResponseListener responseListener) {
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
                        int status = StatusCodes.UNKNOWN_ERROR;

                        try {
                            status = response.getInt("status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if(status == StatusCodes.OK) {
                            responseListener.onResponse(response);
                        } else {
                            // TODO: Handle error status codes
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        responseListener.onError(error.getMessage());
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

    public interface WithoutNetServiceResponseListener {
        public void onResponse(Object response);
        public void onError(String errorMessage);
    }
}
