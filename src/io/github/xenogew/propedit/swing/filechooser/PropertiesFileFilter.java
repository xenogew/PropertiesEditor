package io.github.xenogew.propedit.swing.filechooser;

import java.util.ResourceBundle;

public class PropertiesFileFilter extends AbstractFileFilter {

  static ResourceBundle res = ResourceBundle.getBundle("io.github.xenogew.propedit.swing.lang"); //$NON-NLS-1$

  public PropertiesFileFilter() {

    super.setExtension(".properties"); //$NON-NLS-1$
    super.setDescription(res.getString("file_description_text")); //$NON-NLS-1$
  }
}
