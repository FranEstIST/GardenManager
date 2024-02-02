package pt.ulisboa.tecnico.gardenmanager.network.dto;

import org.json.JSONException;
import org.json.JSONObject;

import pt.ulisboa.tecnico.gardenmanager.domain.Device;

public class DeviceDto {
    private int id;
    private String commonName;
    private int gardenId;

    public DeviceDto(int id, String commonName, int gardenId) {
        this.id = id;
        this.commonName = commonName;
        this.gardenId = gardenId;
    }

    public DeviceDto(Device device) {
        this.id = device.getDeviceId();
        this.commonName = device.getName();
        this.gardenId = device.getParentGardenId();
    }

    public DeviceDto(JSONObject deviceJson) throws JSONException {
        this.id = deviceJson.getInt("id");
        this.commonName = deviceJson.getString("common-name");
        this.gardenId = deviceJson.getInt("network-id");
    }

    public int getId() {
        return id;
    }

    public String getCommonName() {
        return commonName;
    }

    public int getGardenId() {
        return gardenId;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject deviceJson = new JSONObject();
        
        deviceJson.put("id", this.id);
        deviceJson.put("common-name", this.commonName);
        deviceJson.put("garden-id", this.gardenId);

        return  deviceJson;
    }
}
