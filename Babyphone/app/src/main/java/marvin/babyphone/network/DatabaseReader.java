package marvin.babyphone.network;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import timber.log.Timber;

/**
 * Class to access .php scripts on the server asynchronously.
 */
public class DatabaseReader extends AsyncTask<String, Void, String> {

    // TODO: Rename
    public interface OnPhpResponse {
        void onResponse(String response);
        void onError(Exception e);
    }

    private OnPhpResponse onPhpResponse;
    private Exception mException;

    public DatabaseReader(OnPhpResponse onPhpResponse) {
        this.onPhpResponse = onPhpResponse;
    }

    protected String doInBackground(String... urls) {
        mException = null;
        String response = "";

        Timber.i("Requesting " + urls[0]);

        try {
            // Open the connection
            URL url = new URL(urls[0]);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            // Read incoming data
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            response = in.readLine();

            // Close the reader and the connection
            in.close();
            con.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
            mException = e;
        }

        return response;
    }

    protected void onPostExecute(String response) {
        if (mException == null) {
            // Handle response
            onPhpResponse.onResponse(response);
        } else {
            // Handle error
            onPhpResponse.onError(mException);
        }
    }
}
