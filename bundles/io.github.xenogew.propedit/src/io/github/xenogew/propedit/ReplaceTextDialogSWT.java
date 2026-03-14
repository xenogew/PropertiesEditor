package io.github.xenogew.propedit;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class ReplaceTextDialogSWT extends BaseDialogSWT {

  public static class ReplaceResult {
    public final String sourceText;
    public final String exchangeText;

    public ReplaceResult(String sourceText, String exchangeText) {
      this.sourceText = sourceText;
      this.exchangeText = exchangeText;
    }
  }

  private ReplaceResult result;

  public ReplaceTextDialogSWT(Shell parent) {
    super(parent, PropEditorX.getI18nProperty("replaceMenuItem_Text"));
  }

  @Override
  public ReplaceResult open() {
    shell = new Shell(getParent(), getStyle());
    shell.setText(getText());
    shell.setLayout(new GridLayout(1, false));

    Composite inputComposite = new Composite(shell, SWT.NONE);
    inputComposite.setLayout(new GridLayout(2, false));
    inputComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

    Label sourceLabel = new Label(inputComposite, SWT.NONE);
    sourceLabel.setText(PropEditorX.getI18nProperty("jLabel1_Text1"));

    Text sourceTextField = new Text(inputComposite, SWT.BORDER);
    GridData textGd1 = new GridData(SWT.FILL, SWT.CENTER, true, false);
    textGd1.widthHint = 200;
    sourceTextField.setLayoutData(textGd1);

    Label exchangeLabel = new Label(inputComposite, SWT.NONE);
    exchangeLabel.setText(PropEditorX.getI18nProperty("jLabel2_Text"));

    Text exchangeTextField = new Text(inputComposite, SWT.BORDER);
    GridData textGd2 = new GridData(SWT.FILL, SWT.CENTER, true, false);
    textGd2.widthHint = 200;
    exchangeTextField.setLayoutData(textGd2);

    Composite buttonComposite = new Composite(shell, SWT.NONE);
    buttonComposite.setLayout(new GridLayout(2, true));
    buttonComposite.setLayoutData(new GridData(SWT.RIGHT, SWT.BOTTOM, false, false));

    Button replaceButton = new Button(buttonComposite, SWT.PUSH);
    replaceButton.setText(PropEditorX.getI18nProperty("replaceMenuItem_Text"));
    replaceButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
    replaceButton.addListener(SWT.Selection, e -> {
      result = new ReplaceResult(sourceTextField.getText(), exchangeTextField.getText());
      shell.close();
    });

    Button cancelButton = new Button(buttonComposite, SWT.PUSH);
    cancelButton.setText(PropEditorX.getI18nProperty("KEY17"));
    cancelButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
    cancelButton.addListener(SWT.Selection, e -> {
      result = null;
      shell.close();
    });

    shell.setDefaultButton(replaceButton);

    centerOnParent();
    shell.open();
    dispatchEvents();

    return result;
  }
}
