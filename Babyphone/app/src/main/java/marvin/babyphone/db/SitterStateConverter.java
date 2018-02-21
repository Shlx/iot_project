package marvin.babyphone.db;

import android.arch.persistence.room.TypeConverter;

import marvin.babyphone.model.BabyEntry;

/**
 * A class used by Room to convert an integer to a SitterState and vice versa.
 *
 * @author Marvin Suhr
 */
@SuppressWarnings("WeakerAccess")
public class SitterStateConverter {

    /**
     * Convert an integer value to the corresponding SitterState.
     *
     * @param value An integer (between 0 and 1) representing a SitterState.
     * @return The corresponding SitterState.
     */
    @TypeConverter
    public static BabyEntry.SitterState fromInteger(int value) {
        // Check if the passed integer is valid
        if (value < 0 || value > (BabyEntry.SitterState.values().length - 1)) {
            throw new IllegalArgumentException();
        } else {
            return BabyEntry.SitterState.values()[value];
        }
    }

    /**
     * Convert a SitterState to an integer representation.
     *
     * @param state The SitterState to be converted.
     * @return An integer representation of that SitterState.
     */
    @TypeConverter
    public static int BabyStateToInt(BabyEntry.SitterState state) {
        return state.ordinal();
    }

}
