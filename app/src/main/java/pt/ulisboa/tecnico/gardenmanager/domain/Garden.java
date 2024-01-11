package pt.ulisboa.tecnico.gardenmanager.domain;

import androidx.room.Entity;
import androidx.room.MapInfo;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

@Entity
public class Garden {
    @PrimaryKey
    private int gardenId;

    private String name;

    public Garden(int gardenId, String name) {
        this.gardenId = gardenId;
        this.name = name;
    }

    public int getGardenId() {
        return gardenId;
    }

    public void setGardenId(int gardenId) {
        this.gardenId = gardenId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
