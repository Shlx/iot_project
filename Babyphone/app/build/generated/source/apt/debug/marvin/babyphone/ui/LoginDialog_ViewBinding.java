// Generated code from Butter Knife. Do not modify!
package marvin.babyphone.ui;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;
import marvin.babyphone.R;

public class LoginDialog_ViewBinding implements Unbinder {
  private LoginDialog target;

  @UiThread
  public LoginDialog_ViewBinding(LoginDialog target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public LoginDialog_ViewBinding(LoginDialog target, View source) {
    this.target = target;

    target.mTextViewLogin = Utils.findRequiredViewAsType(source, R.id.text_login, "field 'mTextViewLogin'", TextView.class);
    target.mEditTextUsername = Utils.findRequiredViewAsType(source, R.id.edit_text_username, "field 'mEditTextUsername'", EditText.class);
    target.mEditTextPassword = Utils.findRequiredViewAsType(source, R.id.edit_text_password, "field 'mEditTextPassword'", EditText.class);
    target.mButtonLogin = Utils.findRequiredViewAsType(source, R.id.button_login, "field 'mButtonLogin'", Button.class);
    target.mButtonRegister = Utils.findRequiredViewAsType(source, R.id.button_register, "field 'mButtonRegister'", Button.class);
    target.mTextViewError = Utils.findRequiredViewAsType(source, R.id.text_login_error, "field 'mTextViewError'", TextView.class);
    target.mProgressRequest = Utils.findRequiredViewAsType(source, R.id.progress_request, "field 'mProgressRequest'", ProgressBar.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    LoginDialog target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mTextViewLogin = null;
    target.mEditTextUsername = null;
    target.mEditTextPassword = null;
    target.mButtonLogin = null;
    target.mButtonRegister = null;
    target.mTextViewError = null;
    target.mProgressRequest = null;
  }
}
