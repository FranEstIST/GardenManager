package pt.ulisboa.tecnico.gardenmanager.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import pt.ulisboa.tecnico.gardenmanager.domain.Reading;

@Dao
public interface ReadingDao {
    @Query("SELECT * FROM reading")
    Single<List<Reading>> getAll();

    @Query("SELECT * FROM reading WHERE `sender-id` = (:senderId)")
    Single<List<Reading>> getAllBySenderId(int senderId);

    @Query("SELECT * FROM reading WHERE readingId IN (:readingIds)")
    Single<List<Reading>> loadAllByIds(int[] readingIds);

    @Insert
    Completable insertAll(Reading... readings);

    @Delete
    Completable delete(Reading reading);
}
