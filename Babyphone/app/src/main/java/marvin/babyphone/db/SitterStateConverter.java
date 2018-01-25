package marvin.babyphone.db;

import android.arch.persistence.room.TypeConverter;

import marvin.babyphone.model.BabyEntry;


public class SitterStateConverter {

    @TypeConverter
    public static BabyEntry.SitterState fromInteger(int value) {
        return BabyEntry.SitterState.values()[value];
    }

    @TypeConverter
    public static int BabyStateToInt(BabyEntry.SitterState state) {
        return state.ordinal();
    }
}
