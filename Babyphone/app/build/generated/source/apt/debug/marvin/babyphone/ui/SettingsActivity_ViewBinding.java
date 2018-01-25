// Generated code from Butter Knife. Do not modify!
package marvin.babyphone.ui;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;
import marvin.babyphone.R;

public class SettingsActivity_ViewBinding implements Unbinder {
  private SettingsActivity target;

  @UiThread
  public SettingsActivity_ViewBinding(SettingsActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public SettingsActivity_ViewBinding(SettingsActivity target, View source) {
    this.target = target;

    target.mButtonSwitchAccount = Utils.findRequiredViewAsType(source, R.id.button_switch_account, "field 'mButtonSwitchAccount'", Button.class);
    target.mButtonDeleteEntries = Utils.findRequiredViewAsType(source, R.id.button_delete_entries, "field 'mButtonDeleteEntries'", Button.class);
    target.mButtonDeleteAccount = Utils.findRequiredViewAsType(source, R.id.button_delete_account, "field 'mButtonDeleteAccount'", Button.class);
    target.mButtonDeleteEverything = Utils.findRequiredViewAsType(source, R.id.delete_everything, "field 'mButtonDeleteEverything'", Button.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    SettingsActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mButtonSwitchAccount = null;
    target.mButtonDeleteEntries = null;
    target.mButtonDeleteAccount = null;
    target.mButtonDeleteEverything = null;
  }
}
