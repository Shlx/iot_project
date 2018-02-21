package marvin.babyphone.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import marvin.babyphone.db.BabyStateConverter;
import marvin.babyphone.db.SitterStateConverter;

/**
 * Object that describes the state of the baby and the sitter at a certain time.
 *
 * @author Marvin Suhr
 */
@Entity(tableName = "baby_entries")
public class BabyEntry {

    /**
     * Enum representing the state of the baby.
     */
    public enum BabyState {
        CALM,   // 0
        NOISY,  // 1
        CRYING  // 2
    }

    /**
     * Enum representing the state of the babysitter.
     */
    public enum SitterState {
        NOT_PRESENT,    // 0
        PRESENT         // 1
    }

    /////////////
    // MEMBERS //
    /////////////

    /**
     * Timestamp at which the entry was logged.
     */
    @PrimaryKey
    private long timeStamp;

    /**
     * State of the baby.
     */
    @TypeConverters(BabyStateConverter.class)
    @ColumnInfo(name = "baby_state")
    private BabyState babyState;

    /**
     * State of the babysitter.
     */
    @TypeConverters(SitterStateConverter.class)
    @ColumnInfo(name = "sitter_state")
    private SitterState sitterState;

    //////////////////
    // CONSTRUCTORS //
    //////////////////

    public BabyEntry(long timeStamp, int volume, int movement) {
        // Check if passed integers are valid
        if (movement < 0 || movement > (SitterState.values().length - 1)
                || volume < 0 || volume > (BabyState.values().length - 1)) {
            throw new IllegalArgumentException();
        } else {
            this.timeStamp = timeStamp;
            this.babyState = BabyState.values()[volume];
            this.sitterState = SitterState.values()[movement];
        }
    }

    public BabyEntry(long timeStamp, BabyState babyState, SitterState sitterState) {
        this.timeStamp = timeStamp;
        this.babyState = babyState;
        this.sitterState = sitterState;
    }

    ///////////////////////
    // GETTERS / SETTERS //
    ///////////////////////

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
