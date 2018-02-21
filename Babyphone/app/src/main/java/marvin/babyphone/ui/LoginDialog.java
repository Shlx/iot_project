package marvin.babyphone.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import marvin.babyphone.Babyphone;
import marvin.babyphone.R;
import marvin.babyphone.SharedPrefs;
import marvin.babyphone.db.BabyDatabase;
import marvin.babyphone.network.DatabaseReader;
import marvin.babyphone.network.Urls;
import timber.log.Timber;

/**
 * Login dialog that requests the user to enter their username and password
 *
 * @author Marvin Suhr
 */
class LoginDialog extends Dialog implements android.view.View.OnClickListener, DatabaseReader.OnPhpResponse {

    /////////////////
    // UI ELEMENTS //
    /////////////////

    @BindView(R.id.text_login) TextView mTextViewLogin;
    @BindView(R.id.edit_text_username) EditText mEditTextUsername;
    @BindView(R.id.edit_text_password) EditText mEditTextPassword;
    @BindView(R.id.button_login) Button mButtonLogin;
    @BindView(R.id.button_register) Button mButtonRegister;
    @BindView(R.id.text_login_error) TextView mTextViewError;
    @BindView(R.id.progress_request) ProgressBar mProgressRequest;

    /**
     * Whether or not the dialog was opened by pressing the 'switch account' button.
     * In other words, when this is set to 'false', the user is not logged in yet.
     */
    private boolean mSwitchAccount;
    private boolean mRequestInProgress;

    LoginDialog(Context context, boolean switchAccount) {
        super(context);

        // Make the dialog non-cancelable if the user isn't logged in yet
        setCancelable(switchAccount);
        setCanceledOnTouchOutside(switchAccount);

        mSwitchAccount = switchAccount;
        mRequestInProgress = false;
    }

    ///////////////////////
    // ANDROID LIFECYCLE //
    ///////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.dialog_login);
        ButterKnife.bind(this);

        mButtonLogin.setOnClickListener(this);
        mButtonRegister.setOnClickListener(this);

        if (mSwitchAccount) {
            mTextViewLogin.setVisibility(View.GONE);
        }

    }

    ////////////////////////
    // OVERRIDDEN METHODS //
    ////////////////////////

    @Override
    public void onClick(View view) {
        if (!mRequestInProgress) {
            switch (view.getId()) {

                // Check if the username and password combination exists
                case R.id.button_login:
                    onClickLogin();
                    break;

                // Open 'Register' page
                case R.id.button_register:
                    onClickRegister();
                    break;

                default:
                    break;
            }
        } else {
            Context context = getContext();
            String requestInProgress = context.getString(R.string.request_in_progress);
            Toast.makeText(context, requestInProgress, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResponse(String response) {

        // Make the dialog cancellable again
        setCancelable(mSwitchAccount);
        setCanceledOnTouchOutside(mSwitchAccount);
        mRequestInProgress = false;
        mProgressRequest.setVisibility(View.GONE);

        // If the username / password combination was valid
        if (!Babyphone.KEY_ERROR.equals(response)) {

            String username = mEditTextUsername.getText().toString();
            String password = mEditTextPassword.getText().toString();
            String oldUsername = SharedPrefs.getUsername(getContext());

            // Check if the user logged in with a different account
            if (!username.equals(oldUsername)) {

                Timber.i("User successfully logged in with a new account");

                // Clear local database
                BabyDatabase.getInstance(getContext()).deleteAll();

                // Save user to shared preferences
                SharedPrefs.setUsername(getContext(), username);
                SharedPrefs.setPassword(getContext(), password);
                SharedPrefs.setLastUpdate(getContext(), 0);

            } else {
                Timber.i("User logged in with the same account");
            }

            dismiss();

        } else {
            Timber.i("Username / password combination was not valid");
            setError(R.string.error_invalid_user);
        }
    }

    @Override
    public void onError(Exception e) {
        setCancelable(mSwitchAccount);
        setCanceledOnTouchOutside(mSwitchAccount);
        mRequestInProgress = false;
        mProgressRequest.setVisibility(View.GONE);

        setError(R.string.error_database);
    }

    /**
     * Check if the user entered values into the text fields. If that is the
     * case, send a request to the server to check if the user exists.
     */
    private void onClickLogin() {
        String username = mEditTextUsername.getText().toString();
        String password = mEditTextPassword.getText().toString();

        // Show error if a field is empty
        if (username.equals("") || password.equals("")) {
            setError(R.string.error_empty_input);

        // Check with the server if the user exists
        } else {
            // Make the dialog uncancellable while the request is in progress
            setCancelable(false);
            setCanceledOnTouchOutside(false);
            mProgressRequest.setVisibility(View.VISIBLE);
            mRequestInProgress = true;

            clearError();

            // Start the request
            String url = Urls.DB_CHECK_ACCOUNT(username, password);
            new DatabaseReader(this).execute(url);
        }
    }

    /**
     * Open a WebView with the register page.
     */
    private void onClickRegister() {
        // TODO: Implement this.
    }

    /**
     * Set the error to be shown in the text view.
     *
     * @param resId Resource ID of the error string
     */
    private void setError(int resId) {
        String error = getContext().getString(resId);
        mTextViewError.setText(error);
        mTextViewError.setVisibility(View.VISIBLE);
    }

    /**
     * Clear the error message.
     */
    private void clearError() {
        mTextViewError.setText("");
        mTextViewError.setVisibility(View.GONE);
    }

}
