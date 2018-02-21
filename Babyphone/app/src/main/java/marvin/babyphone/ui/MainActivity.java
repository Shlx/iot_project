package marvin.babyphone.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

import marvin.babyphone.BabyAdapter;
import marvin.babyphone.Babyphone;
import marvin.babyphone.R;
import marvin.babyphone.ResponseHandler;
import marvin.babyphone.SharedPrefs;
import marvin.babyphone.db.BabyDatabase;
import marvin.babyphone.model.BabyEntry;
import marvin.babyphone.network.DatabaseReader;

import butterknife.BindView;
import butterknife.ButterKnife;
import marvin.babyphone.network.Urls;
import marvin.babyphone.util.DateFormatter;
import timber.log.Timber;

/**
 * In this activity, the user can view existing entries in a list and request new entries.
 *
 * @author Marvin Suhr
 */
public class MainActivity extends AppCompatActivity implements DatabaseReader.OnPhpResponse {

    /////////////////
    // UI ELEMENTS //
    /////////////////

    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;
    @BindView(R.id.swipe_container) SwipeRefreshLayout mSwipeRefreshLayout;

    /**
     * Adapter to display the entries in a recycler view.
     */
    private BabyAdapter mAdapter;

    ///////////////////////
    // ANDROID LIFECYCLE //
    ///////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize recycler view
        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new BabyAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                mLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestNewEntries();
            }
        });

        checkForExistingUser();
    }

    @Override
    public void onResume() {
        super.onResume();
        Timber.i("Reloading all entries from database");
        List<BabyEntry> entries = BabyDatabase.getInstance(this).getAll();
        mAdapter.deleteData();

        if (entries.size() > 0) {
            mAdapter.addData(entries);
        }
    }

    ////////////////////////
    // OVERRIDDEN METHODS //
    ////////////////////////

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu to add items to the action bar
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_settings:
                // Switch to the 'Settings' activity
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_statistics:
                // Switch to the 'Statistics' activity
                // TODO: Implement this.
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onResponse(String response) {
        mSwipeRefreshLayout.setRefreshing(false);

        if (response == null) {
            Timber.i("Response was null - no new entries");
            toast(R.string.toast_no_new_entries);

        } else if ("".equals(response)) {
            Timber.e("Received empty response");
            toast(R.string.error_database);

        } else if (Babyphone.READ_ERROR.equals(response)) {
            Timber.e("Received READ_ERROR response");
            toast(R.string.error_database);

        } else if (Babyphone.KEY_ERROR.equals(response)) {
            Timber.e("Received KEY_ERROR response");
            toast(R.string.error_database);

        } else if (Babyphone.UNKNOWN_MODE_ERROR.equals(response)) {
            Timber.e("Received UNKNOWN_MODE_ERROR response");
            toast(R.string.error_database);

        } else if (Babyphone.MISSING_MODE_ERROR.equals(response)) {
            Timber.e("Received MISSING_MODE_ERROR response");
            toast(R.string.error_database);

        } else {
            Timber.i("Received response [" + response.length() + "]");
            updateEntries(response);
        }
    }

    @Override
    public void onError(Exception e) {
        mSwipeRefreshLayout.setRefreshing(false);
        toast(R.string.error_database);
    }

    /**
     * Check if there is an existing username / password combination in the
     * SharedPreferences already. Show a dialog to login if there is none,
     * otherwise request the newest data from the server.
     */
    private void checkForExistingUser() {
        String username = SharedPrefs.getUsername(this);
        String password = SharedPrefs.getPassword(this);

        if ("".equals(username) || "".equals(password)) {
            Timber.i("No existing user found. Showing login dialog");
            showLoginDialog();

        } else {
            Timber.i("Existing user found. Requesting new data");
            long lastUpdate = SharedPrefs.getLastUpdate(this);

            // Send request
            String url = Urls.DB_READ(username, password, lastUpdate);
            new DatabaseReader(this).execute(url);
        }
    }

    /**
     * Request new entries from the server asynchronously.
     */
    private void requestNewEntries() {
        String username = SharedPrefs.getUsername(this);
        String password = SharedPrefs.getPassword(this);
        long lastUpdate = SharedPrefs.getLastUpdate(this);

        String date = new DateFormatter(this).getDateString(lastUpdate);
        Timber.i("Requesting entries after " + date);

        String url = Urls.DB_READ(username, password, lastUpdate);
        new DatabaseReader(this).execute(url);
    }

    /**
     * Add any new entries to the recycler view.
     *
     * @param response The server's response to the request.
     */
    private void updateEntries(String response) {
        ResponseHandler responseHandler = new ResponseHandler(this);
        List<BabyEntry> entries = responseHandler.handleResponse(response);

        // Add new entries and scroll to the top
        mAdapter.addData(entries);
        mRecyclerView.scrollToPosition(0);
        BabyDatabase.getInstance(this).addAll(entries);
    }

    /**
     * Show a dialog requesting a username and password.
     */
    private void showLoginDialog() {
        Dialog loginDialog = new LoginDialog(this, false);
        loginDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                requestNewEntries();
            }
        });
        loginDialog.show();
    }

    /**
     * Show a toast.
     *
     * @param resId ID of the string resource to be displayed in the toast.
     */
    public void toast(int resId) {
        Toast.makeText(this, getString(resId), Toast.LENGTH_SHORT).show();
    }

}
