package marvin.babyphone.ui;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import marvin.babyphone.R;
import marvin.babyphone.SharedPrefs;
import marvin.babyphone.db.BabyDatabase;
import marvin.babyphone.network.DatabaseReader;
import marvin.babyphone.network.Urls;

public class SettingsActivity extends AppCompatActivity implements android.view.View.OnClickListener, DatabaseReader.OnPhpResponse {

    @BindView(R.id.button_switch_account) Button mButtonSwitchAccount;
    @BindView(R.id.button_delete_entries) Button mButtonDeleteEntries;
    @BindView(R.id.button_delete_account) Button mButtonDeleteAccount;
    // TODO: REMOVE
    @BindView(R.id.delete_everything) Button mButtonDeleteEverything;

    private boolean mRequestInProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        mButtonSwitchAccount.setOnClickListener(this);
        mButtonDeleteEntries.setOnClickListener(this);
        mButtonDeleteAccount.setOnClickListener(this);
        // TODO: REMOVE
        mButtonDeleteEverything.setOnClickListener(this);

        mRequestInProgress = false;

        // TODO: Back arrow
    }

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

            // TODO: REMOVE
            // Debug: Clear SharedPreferences and database
            case R.id.delete_everything:
                SharedPrefs.setUsername(getApplicationContext(), null);
                SharedPrefs.setPassword(getApplicationContext(), null);
                SharedPrefs.setLastUpdate(getApplicationContext(), 0);
                BabyDatabase.getInstance(this).deleteAll();
                SystemClock.sleep(1000);
                System.exit(0);
                break;

            default:
                break;
        }
    }

    private void onClickSwitchAccount() {
        new LoginDialog(this, true).show();
    }

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

    private AlertDialog.Builder getBuilder() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            return new AlertDialog.Builder(this);
        }
    }

    @Override
    public void onResponse(String response) {
        mRequestInProgress = false;
    }

    @Override
    public void onError(Exception e) {
        // TODO
    }
}
