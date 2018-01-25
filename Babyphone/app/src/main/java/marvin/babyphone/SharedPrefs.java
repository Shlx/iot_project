package marvin.babyphone;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Class for accessing and modifying shared preferences. This is used to save
 * some values and settings.
 */
public class SharedPrefs {

    private static final String BABYPHONE_SETTINGS  = "BABYPHONE_SETTINGS";

    private static final String PREF_USERNAME       = "PREF_USERNAME";
    private static final String PREF_PASSWORD       = "PREF_PASSWORD";
    private static final String PREF_LAST_UPDATE    = "PREF_LAST_UPDATE";


    private SharedPrefs() {}

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(BABYPHONE_SETTINGS, Context.MODE_PRIVATE);
    }

    public static String getUsername(Context context) {
        return getSharedPreferences(context).getString(PREF_USERNAME, null);
    }

    public static void setUsername(Context context, String value) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(PREF_USERNAME, value);
        editor.apply();
    }

    public static String getPassword(Context context) {
        return getSharedPreferences(context).getString(PREF_PASSWORD, null);
    }

    public static void setPassword(Context context, String value) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(PREF_PASSWORD, value);
        editor.apply();
    }

    public static long getLastUpdate(Context context) {
        return getSharedPreferences(context).getLong(PREF_LAST_UPDATE, 0);
    }

    public static void setLastUpdate(Context context, long value) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putLong(PREF_LAST_UPDATE, value);
        editor.apply();
    }

}
