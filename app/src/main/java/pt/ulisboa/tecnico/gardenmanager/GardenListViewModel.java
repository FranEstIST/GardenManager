package pt.ulisboa.tecnico.gardenmanager;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import io.reactivex.rxjava3.core.Single;
import pt.ulisboa.tecnico.gardenmanager.db.GardenDao;
import pt.ulisboa.tecnico.gardenmanager.domain.Garden;
import pt.ulisboa.tecnico.gardenmanager.domain.GardenWithDevices;

public class GardenListViewModel extends ViewModel {
    private GardenDao gardenDao;

    public GardenListViewModel(GardenDao gardenDao) {
        this.gardenDao = gardenDao;
    }

    public Single<List<Garden>> getAllGardens() {
        return this.gardenDao.getAll();
    }

    public LiveData<List<GardenWithDevices>> getAllGardensWithDevices() {
        return this.gardenDao.getAllGardensWithDevices();
    }
}
