package io.github.xenogew.propedit.eclipse.plugin.preference;

import io.github.xenogew.propedit.eclipse.plugin.PropertiesEditorPlugin;
import io.github.xenogew.propedit.eclipse.plugin.resources.Messages;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class PropertiesPreference extends FieldEditorPreferencePage
    implements IWorkbenchPreferencePage {

  public static final String P_NOT_CONVERT_COMMENT = "notConvertComment"; //$NON-NLS-1$

  public static final String P_NOT_ALL_CONVERT = "notConvert"; //$NON-NLS-1$

  public static final String P_COMMENT_CHARACTER = "commentCharacter"; //$NON-NLS-1$

  public static final String P_CONVERT_CHAR_CASE = "convertCharCase"; //$NON-NLS-1$

  private BooleanFieldEditor notAllConvertEditor;

  private BooleanFieldEditor notConvertCommentField;

  private StringFieldEditor commentCharacterField;

  private RadioGroupFieldEditor convertCharCaseField;

  private String[][] charCaseItems = new String[][] {
      {Messages.getString("eclipse.propertieseditor.preference.convert.char.uppercase"), //$NON-NLS-1$
          Messages.getString("eclipse.propertieseditor.preference.convert.char.uppercase")}, //$NON-NLS-1$
      {Messages.getString("eclipse.propertieseditor.preference.convert.char.lowercase"), //$NON-NLS-1$
          Messages.getString("eclipse.propertieseditor.preference.convert.char.lowercase")}}; //$NON-NLS-1$

  public PropertiesPreference() {

    super(GRID);
    setPreferenceStore(PropertiesEditorPlugin.getDefault().getPreferenceStore());
    setDescription(Messages.getString("eclipse.propertieseditor.preference.page.title")); //$NON-NLS-1$
  }

  @Override
  public void createFieldEditors() {

    Composite parent = getFieldEditorParent();

    commentCharacterField = new StringFieldEditor(P_COMMENT_CHARACTER,
        Messages.getString("eclipse.propertieseditor.preference.comment.character"), 1, parent); //$NON-NLS-1$
    commentCharacterField.setTextLimit(1);
    convertCharCaseField = new RadioGroupFieldEditor(P_CONVERT_CHAR_CASE,
        Messages.getString("eclipse.propertieseditor.preference.convert.char.case"), 2, //$NON-NLS-1$
        charCaseItems, parent, true);

    final Group convGroup = new Group(parent, SWT.NONE);
    GridData convGd = new GridData(GridData.FILL_HORIZONTAL);
    convGd.horizontalSpan = 2;
    convGroup.setLayoutData(convGd);
    convGroup.setLayout(new GridLayout(1, true));
    convGroup
        .setText(Messages.getString("eclipse.propertieseditor.preference.convert.option.group")); //$NON-NLS-1$
    notAllConvertEditor = new BooleanFieldEditor(P_NOT_ALL_CONVERT,
        Messages.getString("eclipse.propertieseditor.preference.convert"), convGroup) { //$NON-NLS-1$

      /**
       * @see org.eclipse.jface.preference.BooleanFieldEditor#valueChanged(boolean, boolean)
       */
      public void valueChanged(boolean oldValue, boolean newValue) {

        notConvertCommentField.setEnabled(!newValue, convGroup);
        super.valueChanged(oldValue, newValue);
      }
    };
    notConvertCommentField = new BooleanFieldEditor(P_NOT_CONVERT_COMMENT,
        Messages.getString("eclipse.propertieseditor.preference.convert.comment"), convGroup); //$NON-NLS-1$
    notConvertCommentField.setEnabled(!PropertiesEditorPlugin.getDefault().getPreferenceStore()
        .getBoolean(PropertiesPreference.P_NOT_ALL_CONVERT), convGroup);

    addField(commentCharacterField);
    addField(convertCharCaseField);
    addField(notAllConvertEditor);
    addField(notConvertCommentField);
  }

  @Override
  public void init(IWorkbench workbench) {

  }

  /**
   * @see org.eclipse.jface.preference.PreferencePage#performApply()
   */
  @Override
  protected void performApply() {
    super.performApply();
  }

}
