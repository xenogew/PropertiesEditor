package io.github.xenogew.propedit.eclipse.plugin.editors;

import io.github.xenogew.propedit.eclipse.plugin.preference.PropEditorXPreference;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;

public class PropertiesScanner extends RuleBasedScanner {

  public PropertiesScanner(ColorManager manager, IPreferenceStore pStore) {

    IToken propertiesDefault = new Token(new TextAttribute(
        manager.getColor(PreferenceConverter.getColor(pStore, PropEditorXPreference.P_KEY_COLOR))));

    setDefaultReturnToken(propertiesDefault);
  }
}
