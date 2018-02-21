package marvin.babyphone;

import android.app.Application;

import marvin.babyphone.db.BabyDatabase;
import marvin.babyphone.util.DateFormatter;
import timber.log.Timber;

/**
 * Application main class.
 *
 * @author Marvin Suhr
 */
public class Babyphone extends Application {

    public static final String KEY_ERROR            = "KEY_ERROR";
    public static final String READ_ERROR           = "READ_ERROR";
    public static final String UNKNOWN_MODE_ERROR   = "UNKNOWN_MODE_ERROR";
    public static final String MISSING_MODE_ERROR   = "MISSING_MODE_ERROR";

    @Override
    public void onCreate() {
        super.onCreate();

        // Initiate logger
        Timber.plant(new Timber.DebugTree());

        int numEntries = BabyDatabase.getInstance(this).getAll().size();
        String username = SharedPrefs.getUsername(this);
        String password = SharedPrefs.getPassword(this);
        long timestamp = SharedPrefs.getLastUpdate(this);
        String lastUpdate = new DateFormatter(this).getDateString(timestamp);

        Timber.i("Starting application. " + numEntries + " entries in database."
                + "\nUsername: " + username
                + "\nPassword: " + password
                + "\nlastUpdate: " + timestamp + " (" + lastUpdate + ")");
    }

}
