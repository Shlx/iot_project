package marvin.babyphone.network;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import timber.log.Timber;

/**
 * Class to access .php scripts on the server asynchronously.
 *
 * @author Marvin Suhr
 */
public class DatabaseReader extends AsyncTask<String, Void, String> {

    /**
     * This interface allows the calling class to handle the response.
     */
    public interface OnPhpResponse {
        void onResponse(String response);
        void onError(Exception e);
    }

    private OnPhpResponse onPhpResponse;
    private Exception mException;

    public DatabaseReader(OnPhpResponse onPhpResponse) {
        this.onPhpResponse = onPhpResponse;
    }

    /**
     * Send a request to the server and read the response.
     *
     * @param urls The URL to be requested.
     * @return The server's response to the request.
     */
    protected String doInBackground(String... urls) {

        if (urls.length != 1) {
            throw new IllegalArgumentException("Please request single URLs only.");
        }

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

    /**
     * Use the interface to let the calling class handle the response or error.
     *
     * @param response The response returned by the server.
     */
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
