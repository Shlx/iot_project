package marvin.babyphone;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import marvin.babyphone.db.BabyDatabase;
import marvin.babyphone.model.BabyEntry;
import marvin.babyphone.security.AesCrypt;
import marvin.babyphone.util.DateFormatter;
import timber.log.Timber;

/**
 * This class handles incoming responses and converts them to regular java
 * objects.
 */
public class ResponseHandler {

    private Context context;

    public ResponseHandler(Context context) {
        this.context = context;
    }

    /**
     * Split the response String at any '#' characters. This will split the
     * response into single entries from the database. Return a list of objects
     * after converting each entry.
     *
     * @param response Response from the .php script.
     */
    public List<BabyEntry> handleResponse(String response) {
        String[] entries = response.split("#");
        List<BabyEntry> entriesList = new ArrayList<>();

        // Convert every entry
        for (String entry : entries) {
            entriesList.add(convert(entry));
        }

        return entriesList;
    }

    /**
     * Convert an entry to a BabyState object.
     *
     * @param entry A single entry that represents a BabyState object.
     */
    private BabyEntry convert(String entry) {
        String[] values = entry.split(";");

        // Get the timestamp, save the timestamp of the newest entry
        long timestamp = Long.parseLong(values[0]) * 1000;

        long lastUpdate = SharedPrefs.getLastUpdate(context);
        if (timestamp > lastUpdate) {
            // TODO: Last update = angefragter timestamp
            SharedPrefs.setLastUpdate(context, timestamp);
        }

        // Get the state
        String state = values[1];

        try {
            // Decrypt the state
            AesCrypt crypt = new AesCrypt();
            state = crypt.decrypt(state);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        String date = new DateFormatter(context).getDateString(timestamp);
        Timber.i("Date: " + date + " Decoded: " + state);

        // Get the values and build the object
        String[] x = state.split(";");
        String y = x[0].split("\\.")[0];
        long dataTimestamp = Long.parseLong(y) * 1000;
        int volume = Integer.parseInt(x[1]);
        int movement = Integer.parseInt(x[2]);

        return new BabyEntry(dataTimestamp, volume, movement);

    }

}
