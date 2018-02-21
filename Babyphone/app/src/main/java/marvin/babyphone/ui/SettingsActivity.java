package marvin.babyphone.ui;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import marvin.babyphone.R;
import marvin.babyphone.SharedPrefs;
import marvin.babyphone.db.BabyDatabase;
import marvin.babyphone.network.DatabaseReader;
import marvin.babyphone.network.Urls;

/**
 * In this activity, users can choose to delete their data or switch their account.
 *
 * @author Marvin Suhr
 */
public class SettingsActivity extends AppCompatActivity implements android.view.View.OnClickListener, DatabaseReader.OnPhpResponse {

    /////////////////
    // UI ELEMENTS //
    /////////////////

    @BindView(R.id.button_switch_account) Button mButtonSwitchAccount;
    @BindView(R.id.button_delete_entries) Button mButtonDeleteEntries;
    @BindView(R.id.button_delete_account) Button mButtonDeleteAccount;
    // TODO: Remove this button.
    @BindView(R.id.delete_everything) Button mButtonDeleteEverything;

    private boolean mRequestInProgress;

    ///////////////////////
    // ANDROID LIFECYCLE //
    ///////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        mButtonSwitchAccount.setOnClickListener(this);
        mButtonDeleteEntries.setOnClickListener(this);
        mButtonDeleteAccount.setOnClickListener(this);
        mButtonDeleteEverything.setOnClickListener(this);

        mRequestInProgress = false;
    }

    ////////////////////////
    // OVERRIDDEN METHODS //
    ////////////////////////

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            // Open the login dialog
            case R.id.button_switch_account:
                onClickSwitchAccount();
                break;

            // Delete every entry associated with this account
            case R.id.button_delete_entries:
                onClickDeleteEntries();
                break;

            // Delete the whole account
            case R.id.button_delete_account:
                onClickDeleteAccount();
                break;

            // Debug: Clear SharedPreferences and database
            case R.id.delete_everything:
                deleteEverything();
                break;

            default:
                break;
        }
    }

    @Override
    public void onResponse(String response) {
        mRequestInProgress = false;
    }

    @Override
    public void onError(Exception e) {
        mRequestInProgress = false;
        Toast.makeText(this, getString(R.string.error_database), Toast.LENGTH_SHORT).show();
    }

    /**
     * Show the login dialog.
     */
    private void onClickSwitchAccount() {
        new LoginDialog(this, true).show();
    }

    /**
     * Build and show a dialog warning the user. If he confirms, a request will
     * be sent to the database to delete all entries associated with this account.
     */
    private void onClickDeleteEntries() {
        String title = getString(R.string.delete_entries_title);
        String message = getString(R.string.delete_entries_message);

        AlertDialog.Builder builder = getBuilder();

        builder.setTitle(title)
                .setMessage(message)

                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (!mRequestInProgress) {
                            mRequestInProgress = true;
                            String username = SharedPrefs.getUsername(SettingsActivity.this);
                            String password = SharedPrefs.getPassword(SettingsActivity.this);
                            String url = Urls.DB_DELETE_ENTRIES(username, password);
                            new DatabaseReader(SettingsActivity.this).execute(url);
                        }
                    }
                })

                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    /**
     * Build and show a dialog warning the user. If he confirms, a request
     * will be sent to the database to delete his entire account.
     */
    private void onClickDeleteAccount() {
        String title = getString(R.string.delete_account_title);
        String message = getString(R.string.delete_account_message);

        AlertDialog.Builder builder = getBuilder();

        builder.setTitle(title)
                .setMessage(message)

                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (!mRequestInProgress) {
                            mRequestInProgress = true;
                            String username = SharedPrefs.getUsername(SettingsActivity.this);
                            String password = SharedPrefs.getPassword(SettingsActivity.this);
                            String url = Urls.DB_DELETE_ACCOUNT(username, password);
                            new DatabaseReader(SettingsActivity.this).execute(url);
                        }
                    }
                })

                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    /**
     * Get a dialog builder depending on the current Android version.
     *
     * @return An instance of a DialogBuilder.
     */
    private AlertDialog.Builder getBuilder() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            return new AlertDialog.Builder(this);
        }
    }

    /**
     * Reset the app. All entries will be deleted from
     * the local database and shared preferences.
     */
    private void deleteEverything() {
        SharedPrefs.setUsername(getApplicationContext(), "");
        SharedPrefs.setPassword(getApplicationContext(), "");
        SharedPrefs.setLastUpdate(getApplicationContext(), 0);
        BabyDatabase.getInstance(this).deleteAll();
        SystemClock.sleep(1000);
        System.exit(0);
    }

}
