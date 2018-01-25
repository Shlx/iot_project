package marvin.babyphone.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import marvin.babyphone.db.BabyStateConverter;
import marvin.babyphone.db.SitterStateConverter;

/**
 * Object that describes the state of the baby at a certain time.
 */
@Entity(tableName = "baby_entries")
public class BabyEntry {

    public enum BabyState {
        CALM,   // 0
        NOISY,  // 1
        CRYING  // 2
    }

    public enum SitterState {
        NOT_PRESENT,    // 0
        PRESENT         // 1
    }

    @PrimaryKey
    private long timeStamp;

    @TypeConverters(BabyStateConverter.class)
    @ColumnInfo(name = "baby_state")
    private BabyState babyState;

    @TypeConverters(SitterStateConverter.class)
    @ColumnInfo(name = "sitter_state")
    private SitterState sitterState;

    public BabyEntry(long timeStamp, int volume, int movement) {
        this.timeStamp = timeStamp;
        this.babyState = BabyState.values()[volume];
        this.sitterState = SitterState.values()[movement];
    }

    public BabyEntry(long timeStamp, BabyState babyState, SitterState sitterState) {
        this.timeStamp = timeStamp;
        this.babyState = babyState;
        this.sitterState = sitterState;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public BabyState getBabyState() {
        return babyState;
    }

    public SitterState getSitterState() {
        return sitterState;
    }

}
