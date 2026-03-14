package io.github.xenogew.propedit;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * SWT-based About Box.
 */
public class PropertiesEditorAboutBoxSWT extends BaseDialogSWT {

  /**
   * Constructs a new About box.
   *
   * @param parent the parent shell
   */
  public PropertiesEditorAboutBoxSWT(Shell parent) {
    super(parent, PropertiesEditor.getI18nProperty("versionMenuItem_Text"));
  }

  @Override
  public Object open() {
    shell = new Shell(getParent(), getStyle());
    shell.setText(getText());
    shell.setLayout(new GridLayout(2, false));

    // Icon
    Label imageLabel = new Label(shell, SWT.NONE);
    Image image = new Image(shell.getDisplay(),
        PropertiesEditorFrame.class.getResourceAsStream("resource/editor.png"));
    imageLabel.setImage(image);
    imageLabel.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, false, false));
    shell.addDisposeListener(e -> image.dispose());

    // Content area
    Composite content = new Composite(shell, SWT.NONE);
    content.setLayout(new GridLayout(1, false));
    content.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

    Label titleLabel = new Label(content, SWT.NONE);
    titleLabel.setFont(new Font(shell.getDisplay(), "Dialog", 14, SWT.BOLD));
    titleLabel.setText(PropertiesEditor.getI18nProperty("label2_Text") + PropertiesEditor.VERSION);
    titleLabel.addDisposeListener(e -> titleLabel.getFont().dispose());

    Label copyrightLabel = new Label(content, SWT.NONE);
    copyrightLabel.setFont(new Font(shell.getDisplay(), "Dialog", 12, SWT.BOLD));
    copyrightLabel.setText(PropertiesEditor.COPYRIGHT);
    copyrightLabel.addDisposeListener(e -> copyrightLabel.getFont().dispose());

    Label licenseLabel = new Label(content, SWT.NONE);
    licenseLabel.setText(PropertiesEditor.getI18nProperty("license_Text"));

    Text infoText = new Text(content, SWT.READ_ONLY | SWT.BORDER);
    infoText.setText(PropertiesEditor.getI18nProperty("jTextField1_Text"));
    infoText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

    // OK Button
    Button okButton = new Button(shell, SWT.PUSH);
    okButton.setText("OK");
    GridData gd = new GridData(SWT.CENTER, SWT.BOTTOM, false, false, 2, 1);
    gd.widthHint = 80;
    okButton.setLayoutData(gd);
    okButton.addListener(SWT.Selection, e -> shell.close());

    centerOnParent();
    shell.open();
    dispatchEvents();
    return null;
  }
}
