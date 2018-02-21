package marvin.babyphone.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import marvin.babyphone.model.BabyEntry;

/**
 * DAO class used to read from and write to the database.
 *
 * @author Marvin Suhr
 */
@Dao
interface BabyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(BabyEntry entry);

    @Query("SELECT * FROM baby_entries ORDER BY timeStamp DESC")
    List<BabyEntry> getAll();

    @Query("DELETE FROM baby_entries")
    void deleteAll();

}
