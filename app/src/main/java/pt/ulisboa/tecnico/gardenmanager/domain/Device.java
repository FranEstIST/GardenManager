package pt.ulisboa.tecnico.gardenmanager.domain;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import pt.ulisboa.tecnico.gardenmanager.network.dto.DeviceDto;

@Entity
public class Device {
    @PrimaryKey
    private int deviceId;

    private int parentGardenId;

    private String name;

    private DeviceType deviceType;

    public Device(int deviceId, String name, DeviceType deviceType) {
        this.deviceId = deviceId;
        this.name = name;
        this.deviceType = deviceType;
    }

    public Device(DeviceDto deviceDto, DeviceType deviceType) {
        this.deviceId = deviceDto.getId();
        this.name = deviceDto.getCommonName();
        this.deviceType = deviceType;
        this.parentGardenId = deviceDto.getGardenId();
    }

    public Device(DeviceDto deviceDto) {
        this.deviceId = deviceDto.getId();
        this.name = deviceDto.getCommonName();
        this.deviceType = null;
        this.parentGardenId = deviceDto.getGardenId();
    }

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public int getParentGardenId() {
        return parentGardenId;
    }

    public void setParentGardenId(int parentGardenId) {
        this.parentGardenId = parentGardenId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(DeviceType deviceType) {
        this.deviceType = deviceType;
    }
}
