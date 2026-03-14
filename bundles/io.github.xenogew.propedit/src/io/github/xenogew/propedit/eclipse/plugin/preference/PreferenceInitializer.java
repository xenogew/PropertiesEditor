package io.github.xenogew.propedit.eclipse.plugin.preference;

import io.github.xenogew.propedit.eclipse.plugin.PropEditorXPlugin;
import io.github.xenogew.propedit.eclipse.plugin.resources.Messages;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

public class PreferenceInitializer extends AbstractPreferenceInitializer {

  /**
   * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
   */
  @Override
  public void initializeDefaultPreferences() {

    IPreferenceStore pStore = PropEditorXPlugin.getDefault().getPreferenceStore();
    pStore.setDefault(PropEditorXDuplicationCheckerPreference.P_CHECK_KEY, false);
    pStore.setDefault(PropertiesPreference.P_COMMENT_CHARACTER, "#"); //$NON-NLS-1$
    pStore.setDefault(PropertiesPreference.P_CONVERT_CHAR_CASE,
        Messages.getString("eclipse.propertieseditor.preference.convert.char.lowercase")); //$NON-NLS-1$
    pStore.setDefault(PropEditorXPreference.P_COLLAPSE, false);
    pStore.setDefault(PropEditorXPreference.P_INIT_COLLAPSE, false);
    pStore.setDefault(PropertiesPreference.P_NOT_CONVERT_COMMENT, false);
    pStore.setDefault(PropertiesPreference.P_NOT_ALL_CONVERT, false);
    pStore.setDefault(PropEditorXPreference.P_COMMENT_COLOR, "0,180,0"); //$NON-NLS-1$
    pStore.setDefault(PropEditorXPreference.P_SEPARATOR_COLOR, "0,128,0"); //$NON-NLS-1$
    pStore.setDefault(PropEditorXPreference.P_KEY_COLOR, "0,0,128"); //$NON-NLS-1$
    pStore.setDefault(PropEditorXPreference.P_VALUE_COLOR, "128,0,0"); //$NON-NLS-1$
    pStore.setDefault(PropEditorXPreference.P_BACKGROUND_COLOR, "255,255,255"); //$NON-NLS-1$
  }
}
