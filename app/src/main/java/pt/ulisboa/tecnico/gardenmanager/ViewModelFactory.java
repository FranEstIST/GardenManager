package pt.ulisboa.tecnico.gardenmanager;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import pt.ulisboa.tecnico.gardenmanager.db.DeviceDao;
import pt.ulisboa.tecnico.gardenmanager.db.GardenDao;
import pt.ulisboa.tecnico.gardenmanager.db.ReadingDao;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private final GardenDao gardenDao;
    private final DeviceDao deviceDao;
    private final ReadingDao readingDao;

    public ViewModelFactory(GardenDao gardenDao, DeviceDao deviceDao, ReadingDao readingDao) {
        this.gardenDao = gardenDao;
        this.deviceDao = deviceDao;
        this.readingDao = readingDao;
    }

    @Override
    @NonNull
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(GardenDashboardViewModel.class)) {
            return (T) new GardenDashboardViewModel(this.gardenDao, this.deviceDao);
        } else if (modelClass.isAssignableFrom(GardenListViewModel.class)) {
            return (T) new GardenListViewModel(this.gardenDao);
        }
        //noinspection unchecked
        throw new IllegalArgumentException("Unknown ViewModel class");
    }

}
