package jp.gr.java_conf.ussiy.app.propedit;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * Prototype for the SWT-based PropertiesEditor standalone application.
 */
public class PropertiesEditorSWT {

  public static void main(String[] args) {
    Display display = new Display();
    Shell shell = new Shell(display);
    shell.setText("PropertiesEditor (SWT Prototype)");
    shell.setLayout(new FillLayout());

    Button aboutButton = new Button(shell, SWT.PUSH);
    aboutButton.setText("Open About Box");
    aboutButton.addListener(SWT.Selection, e -> {
      PropertiesEditorAboutBoxSWT aboutBox = new PropertiesEditorAboutBoxSWT(shell);
      aboutBox.open();
    });

    Button unicodeButton = new Button(shell, SWT.PUSH);
    unicodeButton.setText("Open Unicode Dialog");
    unicodeButton.addListener(SWT.Selection, e -> {
      UnicodeDialogSWT unicodeDialog = new UnicodeDialogSWT(shell, "Unicode Preview");
      unicodeDialog.setMessage("This is a test message with Unicode: \\u3042");
      unicodeDialog.open();
    });

    shell.setSize(400, 300);
    shell.open();

    while (!shell.isDisposed()) {
      if (!display.readAndDispatch()) {
        display.sleep();
      }
    }
    display.dispose();
  }
}
