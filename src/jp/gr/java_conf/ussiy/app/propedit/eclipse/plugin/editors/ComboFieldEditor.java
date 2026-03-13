package jp.gr.java_conf.ussiy.app.propedit.eclipse.plugin.editors;

import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;

public class ComboFieldEditor extends FieldEditor {

  private Combo combo;

  private String[] items;

  /**
   * (これを消してコンストラクターの説明を記述)
   * 
   * @param name
   * @param labelText
   * @param parent
   */
  public ComboFieldEditor(String name, String labelText, String[] items, Composite parent) {

    super(name, labelText, parent);
    this.items = items;
    setComboItems();
  }

  /*
   * (非 Javadoc)
   * 
   * @see org.eclipse.jface.preference.FieldEditor#adjustForNumColumns(int)
   */
  @Override
  protected void adjustForNumColumns(int numColumns) {

  }

  /*
   * (非 Javadoc)
   * 
   * @see org.eclipse.jface.preference.FieldEditor#doFillIntoGrid(org.eclipse.swt.widgets.Composite,
   * int)
   */
  @Override
  protected void doFillIntoGrid(Composite parent, int numColumns) {

    getLabelControl(parent);

    combo = new Combo(parent, SWT.NONE);
  }

  protected void setComboItems() {

    combo.setItems(items);
  }

  /*
   * (非 Javadoc)
   * 
   * @see org.eclipse.jface.preference.FieldEditor#doLoad()
   */
  @Override
  protected void doLoad() {

    if (combo != null) {
      String value = getPreferenceStore().getString(getPreferenceName());
      combo.setText(value);
    }
  }

  /*
   * (非 Javadoc)
   * 
   * @see org.eclipse.jface.preference.FieldEditor#doLoadDefault()
   */
  @Override
  protected void doLoadDefault() {

    if (combo != null) {
      String value = getPreferenceStore().getDefaultString(getPreferenceName());
      combo.setText(value);
    }
  }

  /*
   * (非 Javadoc)
   * 
   * @see org.eclipse.jface.preference.FieldEditor#doStore()
   */
  @Override
  protected void doStore() {

    getPreferenceStore().setValue(getPreferenceName(), combo.getText());
  }

  /*
   * (非 Javadoc)
   * 
   * @see org.eclipse.jface.preference.FieldEditor#getNumberOfControls()
   */
  @Override
  public int getNumberOfControls() {

    return 2;
  }
}
