package pt.ulisboa.tecnico.gardenmanager.network.dto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GardenDto {
    private int id;
    private String name;
    private List<Integer> nodeIds;

    public GardenDto(int id, String name, List<Integer> nodeIds) {
        this.id = id;
        this.name = name;
        this.nodeIds = nodeIds;
    }

    public GardenDto(JSONObject gardenJson) throws JSONException {
        this.id = gardenJson.getInt("id");
        this.name = gardenJson.getString("name");
        this.nodeIds = new ArrayList<>();

        JSONArray nodeIdsJsonArray = gardenJson.getJSONArray("nodes");
        for(int i = 0; i < nodeIdsJsonArray.length(); i++) {
            this.nodeIds.add(nodeIdsJsonArray.getJSONObject(i).getInt("id"));
        }
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Integer> getNodeIds() {
        return nodeIds;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject gardenJson = new JSONObject();
        gardenJson.put("id", id);
        gardenJson.put("name", name);

        JSONArray nodeIdsJsonArray = new JSONArray();

        for(int nodeId : nodeIds) {
            nodeIdsJsonArray.put(nodeId);
        }

        gardenJson.put("nodes", nodeIdsJsonArray);

        return gardenJson;
    }
}
