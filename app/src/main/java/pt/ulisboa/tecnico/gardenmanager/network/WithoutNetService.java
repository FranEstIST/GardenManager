package pt.ulisboa.tecnico.gardenmanager.network;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import pt.ulisboa.tecnico.gardenmanager.GlobalClass;
import pt.ulisboa.tecnico.gardenmanager.constants.ErrorMessages;
import pt.ulisboa.tecnico.gardenmanager.constants.StatusCodes;
import pt.ulisboa.tecnico.gardenmanager.domain.Reading;
import pt.ulisboa.tecnico.gardenmanager.network.dto.DeviceDto;
import pt.ulisboa.tecnico.gardenmanager.network.dto.GardenDto;

public class WithoutNetService {
    private GlobalClass globalClass;
    private RequestQueue requestQueue;

    private static final String GET_MESSAGES_URL_SUFFIX = "get-most-recent-messages-by-sender-and-receiver";
    private static final String ADD_MESSAGE_URL_SUFFIX = "add-message";
    private static final String REMOVE_MESSAGE_URL_SUFFIX = "remove-message";
    private static final String GET_NETWORK_BY_ID_BASE_URL_SUFFIX = "get-network-by-id/";
    private static final String FIND_NODES_BY_NETWORK_ID_AND_SEARCH_TERM_BASE_URL_SUFFIX = "find-nodes-by-network-id-and-search-term/";
    private static final String FIND_NETWORKS_BY_SEARCH_TERM_BASE_URL_SUFFIX = "find-networks-by-search-term/";
    private static final String ADD_NETWORK_URL_SUFFIX = "add-network";
    private static final String ADD_NODE_URL_SUFFIX = "add-node";

    public WithoutNetService(GlobalClass globalClass) {
        this.globalClass = globalClass;
        this.requestQueue = globalClass.getRequestQueue();
    }

    public void getReadings(int senderId, WithoutNetServiceResponseListener responseListener) {
        String url = globalClass.getServerURL() + GET_MESSAGES_URL_SUFFIX + "/" + senderId + "/" + globalClass.getGardenManagerId();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET ,url, null, new Response.Listener<JSONObject>() {
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
                        JSONArray readingsJsonArray = response.getJSONArray("messages");

                        ArrayList<Reading> receivedReadings = new ArrayList<>();

                        for(int i = 0; i < readingsJsonArray.length(); i++) {
                            Reading reading = new Reading(readingsJsonArray.getJSONObject(i));
                            receivedReadings.add(reading);
                        }

                        responseListener.onResponse(receivedReadings);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        responseListener.onError("Error");
                    } catch (IllegalArgumentException e1) {
                        e1.printStackTrace();
                        responseListener.onError("Error");
                    }
                } else {
                    // TODO: Handle error status codes
                    responseListener.onError("Error");
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

    public void getAllDevicesInGarden(int gardenId, WithoutNetServiceResponseListener responseListener) {
        String url = globalClass.getServerURL() + GET_NETWORK_BY_ID_BASE_URL_SUFFIX + gardenId;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
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
                        JSONObject networkJson = response.getJSONObject("network");
                        JSONArray deviceJsonArray = networkJson.getJSONArray("nodes");
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

    public void getAllDevicesInGardenContainingSubstring(int gardenId, String substring, WithoutNetServiceResponseListener responseListener) {
        String url = globalClass.getServerURL() + FIND_NODES_BY_NETWORK_ID_AND_SEARCH_TERM_BASE_URL_SUFFIX + gardenId + "/" + substring;

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
        String url = globalClass.getServerURL() + FIND_NETWORKS_BY_SEARCH_TERM_BASE_URL_SUFFIX + substring;

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

    public void addGarden(String gardenName, WithoutNetServiceResponseListener responseListener) {
        String url = globalClass.getServerURL() + ADD_NETWORK_URL_SUFFIX;

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

    public void addDevice(String deviceName, int gardenId, WithoutNetServiceResponseListener responseListener) {
        String url = globalClass.getServerURL() + ADD_NODE_URL_SUFFIX;

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

    private byte[] longToByteArrayRev(long value) {
        byte[] bytes = new byte[Integer.BYTES];
        int length = bytes.length;
        for (int i = 0; i < length; i++) {
            bytes[i] = (byte) (value & 0xFF);
            value >>= 8;
        }

        return bytes;
    }

    public void sendMessage(int receiverId, long value, WithoutNetServiceResponseListener responseListener){
        String url = globalClass.getServerURL() + ADD_MESSAGE_URL_SUFFIX;

        JSONObject jsonRequest = new JSONObject();

        try {
            jsonRequest.put("length", 17);
            jsonRequest.put("timestamp", globalClass.getSendCounter());
            jsonRequest.put("messageType", 0);
            jsonRequest.put("sender", globalClass.getGardenManagerId());
            jsonRequest.put("receiver", receiverId);
            jsonRequest.put("payload", Base64
                    .getEncoder()
                    .encodeToString(longToByteArrayRev(value)));
        } catch (JSONException e) {
            e.printStackTrace();
            responseListener.onError(ErrorMessages.JSON_ERROR);
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonRequest, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int status = response.getInt("status");
                    if(status == StatusCodes.OK) {
                        responseListener.onResponse(status);
                    } else {
                        responseListener.onError(ErrorMessages.getErrorMessage(status));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    responseListener.onError(ErrorMessages.JSON_ERROR);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                responseListener.onError(ErrorMessages.VOLLEY_ERROR);
            }
        });

        this.requestQueue.add(request);
    }

    public void removeReading(Reading reading, WithoutNetServiceResponseListener responseListener) {
        String url = globalClass.getServerURL() + REMOVE_MESSAGE_URL_SUFFIX;

        JSONObject jsonRequest = new JSONObject();

        try {
            jsonRequest.put("senderId", reading.getSenderId());
            jsonRequest.put("receiverId", globalClass.getGardenManagerId());
            jsonRequest.put("timestamp", reading.getTimestamp());
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
                    responseListener.onResponse(StatusCodes.OK);
                } else {
                    // TODO: Handle error status codes
                    responseListener.onError("Error");
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

    public interface WithoutNetServiceResponseListener {
        void onResponse(Object response);
        void onError(String errorMessage);
    }
}
