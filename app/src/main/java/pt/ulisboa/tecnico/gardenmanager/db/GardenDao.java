package pt.ulisboa.tecnico.gardenmanager.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.Comparator;
import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import pt.ulisboa.tecnico.gardenmanager.domain.Garden;
import pt.ulisboa.tecnico.gardenmanager.domain.GardenWithDevices;

@Dao
public interface GardenDao {
    @Query("SELECT * FROM garden")
    Single<List<Garden>> getAll();

    @Query("SELECT * FROM garden WHERE gardenId IN (:gardenIds)")
    Single<List<Garden>> loadAllByIds(int[] gardenIds);

    @Query("SELECT * FROM garden WHERE name LIKE :name LIMIT 1")
    Single<Garden> findByName(String name);

    @Query("SELECT * FROM garden WHERE gardenId = (:gardenId)")
    Single<Garden> findById(int gardenId);

    @Transaction
    @Query("SELECT * FROM garden")
    LiveData<List<GardenWithDevices>> getAllGardensWithDevices();

    @Insert
    Completable insertAll(Garden... gardens);

    @Delete
    Completable delete(Garden garden);
}
