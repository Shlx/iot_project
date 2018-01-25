package marvin.babyphone.db;

import android.arch.persistence.room.TypeConverter;

import marvin.babyphone.model.BabyEntry;


public class BabyStateConverter {
    @TypeConverter

    public static BabyEntry.BabyState fromInteger(int value) {
        return BabyEntry.BabyState.values()[value];
    }

    @TypeConverter
    public static int BabyStateToInt(BabyEntry.BabyState state) {
        return state.ordinal();
    }
}
