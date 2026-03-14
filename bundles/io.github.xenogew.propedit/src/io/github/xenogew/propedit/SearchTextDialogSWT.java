package io.github.xenogew.propedit;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class SearchTextDialogSWT extends BaseDialogSWT {

  private String inputString;

  public SearchTextDialogSWT(Shell parent) {
    super(parent, PropEditorX.getI18nProperty("findMenuItem_Text"));
  }

  @Override
  public String open() {
    shell = new Shell(getParent(), getStyle());
    shell.setText(getText());
    shell.setLayout(new GridLayout(1, false));

    Composite inputComposite = new Composite(shell, SWT.NONE);
    inputComposite.setLayout(new GridLayout(2, false));
    inputComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

    Label label = new Label(inputComposite, SWT.NONE);
    label.setText(PropEditorX.getI18nProperty("jLabel1_Text2"));

    Text inputTextField = new Text(inputComposite, SWT.BORDER);
    GridData textGd = new GridData(SWT.FILL, SWT.CENTER, true, false);
    textGd.widthHint = 200;
    inputTextField.setLayoutData(textGd);

    Composite buttonComposite = new Composite(shell, SWT.NONE);
    buttonComposite.setLayout(new GridLayout(2, true));
    buttonComposite.setLayoutData(new GridData(SWT.RIGHT, SWT.BOTTOM, false, false));

    Button searchButton = new Button(buttonComposite, SWT.PUSH);
    searchButton.setText(PropEditorX.getI18nProperty("findMenuItem_Text"));
    searchButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
    searchButton.addListener(SWT.Selection, e -> {
      inputString = inputTextField.getText();
      shell.close();
    });

    Button cancelButton = new Button(buttonComposite, SWT.PUSH);
    cancelButton.setText(PropEditorX.getI18nProperty("KEY17"));
    cancelButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
    cancelButton.addListener(SWT.Selection, e -> {
      inputString = null;
      shell.close();
    });

    inputTextField.addListener(SWT.DefaultSelection, e -> {
      inputString = inputTextField.getText();
      shell.close();
    });

    shell.setDefaultButton(searchButton);

    centerOnParent();
    shell.open();
    dispatchEvents();

    return inputString;
  }
}
