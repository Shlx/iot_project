package marvin.babyphone.db;

import android.arch.persistence.room.TypeConverter;

import marvin.babyphone.model.BabyEntry;

/**
 * A class used by Room to convert an integer to a BabyState and vice versa.
 *
 * @author Marvin Suhr
 */
@SuppressWarnings("WeakerAccess")
public class BabyStateConverter {

    /**
     * Convert an integer value to the corresponding BabyState.
     *
     * @param value An integer (between 0 and 2) representing a BabyState.
     * @return The corresponding BabyState.
     */
    @TypeConverter
    public static BabyEntry.BabyState fromInteger(int value) {
        // Check if the passed integer is valid
        if (value < 0 || value > (BabyEntry.BabyState.values().length - 1)) {
            throw new IllegalArgumentException();
        } else {
            return BabyEntry.BabyState.values()[value];
        }
    }

    /**
     * Convert a BabyState to an integer representation.
     *
     * @param state The BabyState to be converted.
     * @return An integer representation of that BabyState.
     */
    @TypeConverter
    public static int BabyStateToInt(BabyEntry.BabyState state) {
        return state.ordinal();
    }

}
