package pt.ulisboa.tecnico.gardenmanager;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import pt.ulisboa.tecnico.gardenmanager.db.DeviceDao;
import pt.ulisboa.tecnico.gardenmanager.db.GardenDao;
import pt.ulisboa.tecnico.gardenmanager.domain.Device;
import pt.ulisboa.tecnico.gardenmanager.domain.DeviceWithReadings;

public class GardenDashboardViewModel extends ViewModel {
    private GardenDao gardenDao;
    private DeviceDao deviceDao;

    public GardenDashboardViewModel(GardenDao gardenDao, DeviceDao deviceDao) {
        this.gardenDao = gardenDao;
        this.deviceDao = deviceDao;
    }

    public Single<List<Device>> getAllDevices() {
        return this.deviceDao.getAll();
    }

    public Single<List<DeviceWithReadings>> getAllDevicesWithReadings() {
        return this.deviceDao.getAllDevicesWithReadings();
    }

    public LiveData<List<DeviceWithReadings>> getDevicesWithReadingsByType(DeviceType deviceType) {
        return this.deviceDao.getDevicesWithReadingsByType(deviceType);
    }
}
