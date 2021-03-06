package marvin.babyphone.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import java.util.List;

import marvin.babyphone.model.BabyEntry;
import timber.log.Timber;

/**
 * Singleton class that handles access to the local database.
 *
 * @author Marvin Suhr
 */
@Database(entities = {BabyEntry.class}, version = 1, exportSchema = false)
public abstract class BabyDatabase extends RoomDatabase {

    private static BabyDatabase sInstance;
    private static final String DATABASE_NAME = "babyphone_db";

    public abstract BabyDao babyDao();

    public static BabyDatabase getInstance(Context context) {
        if (sInstance == null) {
            sInstance = Room.databaseBuilder(context.getApplicationContext(), BabyDatabase.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .build();
        }
        return sInstance;
    }

    //////////////////////
    // DATABASE METHODS //
    //////////////////////

    /**
     * Get all BabyEntries.
     *
     * @return A list containing every BabyEntry in the local database.
     */
    public List<BabyEntry> getAll() {
        List<BabyEntry> entries = babyDao().getAll();

        if (entries.size() > 0) {
            Timber.i("Loading " + entries.size() + " entries from the local database");
        } else {
            Timber.i("No entries found, local database seems to be empty");
        }

        return entries;
    }

    /**
     * Add multiple BabyEntries to the local database.
     *
     * @param entries A list of BabyEntries to be added to the database.
     */
    public void addAll(List<BabyEntry> entries) {
        Timber.i("Adding " + entries.size() + " entries to the local database");
        for (BabyEntry entry : entries) {
            babyDao().insert(entry);
        }
    }

    /**
     * Delete all BabyEntries from the local database.
     */
    public void deleteAll() {
        Timber.i("Deleting all entries from the local database");
        babyDao().deleteAll();
    }

}
