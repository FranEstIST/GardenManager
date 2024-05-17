package pt.ulisboa.tecnico.gardenmanager.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.AutoMigration;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.AutoMigrationSpec;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import pt.ulisboa.tecnico.gardenmanager.domain.Device;
import pt.ulisboa.tecnico.gardenmanager.domain.DeviceWithReadings;
import pt.ulisboa.tecnico.gardenmanager.domain.Garden;
import pt.ulisboa.tecnico.gardenmanager.domain.GardenWithDevices;
import pt.ulisboa.tecnico.gardenmanager.domain.GardenWithDevicesAndReadings;
import pt.ulisboa.tecnico.gardenmanager.domain.Reading;

@Database(entities = {
            Garden.class,
            Device.class,
            Reading.class,
        },
        version = 3,
        exportSchema = false
        )
public abstract class GardenDatabase extends RoomDatabase {
    private static volatile GardenDatabase INSTANCE;

    public abstract GardenDao gardenDao();
    public abstract DeviceDao deviceDao();
    public abstract ReadingDao readingDao();

    public static GardenDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (GardenDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    GardenDatabase.class, "garden_db")
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    static class AutoMigration implements AutoMigrationSpec{};
}
