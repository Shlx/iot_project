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

class LoginDialog extends Dialog implements android.view.View.OnClickListener, DatabaseReader.OnPhpResponse {

    @BindView(R.id.text_login) TextView mTextViewLogin;
    @BindView(R.id.edit_text_username) EditText mEditTextUsername;
    @BindView(R.id.edit_text_password) EditText mEditTextPassword;
    @BindView(R.id.button_login) Button mButtonLogin;
    @BindView(R.id.button_register) Button mButtonRegister;
    @BindView(R.id.text_login_error) TextView mTextViewError;
    @BindView(R.id.progress_request) ProgressBar mProgressRequest;

    private boolean mRequestInProgress;
    private boolean mSwitchAccount;

    LoginDialog(Context context, boolean switchAccount) {
        super(context);

        // Make the dialog non-cancelable if the user isn't logged in yet
        setCancelable(switchAccount);
        setCanceledOnTouchOutside(switchAccount);

        mSwitchAccount = switchAccount;
        mRequestInProgress = false;
    }

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
        setCancelable(mSwitchAccount);
        setCanceledOnTouchOutside(mSwitchAccount);
        mRequestInProgress = false;
        mProgressRequest.setVisibility(View.GONE);

        if (!Babyphone.KEY_ERROR.equals(response)) {
            Timber.i("Username / password combination was valid");

            // Clear local database if the account was switched
            if (mSwitchAccount) {
                BabyDatabase.getInstance(getContext()).deleteAll();
            }

            // User was valid. Save to shared preferences and dismiss dialog
            SharedPrefs.setUsername(getContext(), mEditTextUsername.getText().toString());
            SharedPrefs.setPassword(getContext(), mEditTextPassword.getText().toString());
            SharedPrefs.setLastUpdate(getContext(), 0);
            dismiss();

        } else {
            Timber.i("Username / password combination was not valid");

            // Show error message
            setError(R.string.error_invalid_user);
        }
    }

    @Override
    public void onError(Exception e) {
        this.mRequestInProgress = false;
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
            setCancelable(false);
            setCanceledOnTouchOutside(false);
            mProgressRequest.setVisibility(View.VISIBLE);
            mRequestInProgress = true;

            clearError();
            String url = Urls.DB_CHECK_ACCOUNT(username, password);
            new DatabaseReader(this).execute(url);
        }
    }

    private void onClickRegister() {
        // TODO
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