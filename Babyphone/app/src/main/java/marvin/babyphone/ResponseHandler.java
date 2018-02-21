package marvin.babyphone;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import marvin.babyphone.model.BabyEntry;
import marvin.babyphone.security.AesCrypt;
import marvin.babyphone.util.DateFormatter;
import timber.log.Timber;

/**
 * This class handles incoming responses and converts them to regular java
 * objects.
 *
 * @author Marvin Suhr
 */
public class ResponseHandler {

    private Context context;

    public ResponseHandler(Context context) {
        this.context = context;
    }

    /**
     * Convert the response into a list of BabyState objects.
     *
     * @param response Response from the .php script.
     * @return A List consisting of all the entries contained in the response.
     */
    public List<BabyEntry> handleResponse(String response) {
        // Split the response String at any '#' characters
        // since that character is used to divide entries.
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
        // Split again at any ';' characters since that
        // character is used to divide the values of an entry.
        String[] values = entry.split(";");

        // Get the timestamp, save the timestamp of the newest entry
        long timestamp = Long.parseLong(values[0]);
        long lastUpdate = SharedPrefs.getLastUpdate(context);
        if (timestamp > lastUpdate) {
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

        // Yet again, split at any ';' characters to get the values.
        String[] entryValues = state.split(";");

        // This is disgusting, but the only way to parse
        // the timestamps since they come as decimal numbers.
        String entryTimestamp = entryValues[0].split("\\.")[0];
        long dataTimestamp = Long.parseLong(entryTimestamp);
        int volume = Integer.parseInt(entryValues[1]);
        int movement = Integer.parseInt(entryValues[2]);

        return new BabyEntry(dataTimestamp, volume, movement);
    }

}
