package pt.ulisboa.tecnico.gardenmanager.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import pt.ulisboa.tecnico.gardenmanager.DeviceType;
import pt.ulisboa.tecnico.gardenmanager.domain.Device;
import pt.ulisboa.tecnico.gardenmanager.domain.DeviceWithReadings;
import pt.ulisboa.tecnico.gardenmanager.domain.GardenWithDevices;

@Dao
public interface DeviceDao {
    @Query("SELECT * FROM device")
    Single<List<Device>> getAll();

    @Query("SELECT * FROM device WHERE deviceId IN (:deviceIds)")
    Single<List<Device>> loadAllByIds(int[] deviceIds);

    @Query("SELECT * FROM device WHERE name LIKE :name LIMIT 1")
    Single<Device> findByName(String name);

    @Transaction
    @Query("SELECT * FROM device")
    Single<List<DeviceWithReadings>> getAllDevicesWithReadings();

    @Transaction
    @Query("SELECT * FROM device WHERE deviceType = (:deviceType)")
    LiveData<List<DeviceWithReadings>> getDevicesWithReadingsByType(DeviceType deviceType);

    @Insert
    Completable insertAll(Device... devices);

    @Delete
    Completable delete(Device device);
}
