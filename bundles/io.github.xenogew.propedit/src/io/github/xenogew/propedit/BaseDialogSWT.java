package io.github.xenogew.propedit;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * Base class for SWT-based dialogs.
 */
public abstract class BaseDialogSWT extends Dialog {

  protected Shell shell;

  /**
   * Constructs a new dialog.
   *
   * @param parent the parent shell
   * @param title the dialog title
   */
  public BaseDialogSWT(Shell parent, String title) {
    this(parent, title, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
  }

  /**
   * Constructs a new dialog with specific style.
   *
   * @param parent the parent shell
   * @param title the dialog title
   * @param style the shell style
   */
  public BaseDialogSWT(Shell parent, String title, int style) {
    super(parent, style);
    setText(title);
  }

  /**
   * Opens the dialog and returns the result.
   *
   * @return the result
   */
  public abstract Object open();

  /**
   * Dispatches events while the shell is not disposed.
   */
  protected void dispatchEvents() {
    Display display = getParent().getDisplay();
    while (!shell.isDisposed()) {
      if (!display.readAndDispatch()) {
        display.sleep();
      }
    }
  }

  /**
   * Centers the dialog shell on the parent shell.
   */
  protected void centerOnParent() {
    Shell parent = getParent();
    shell.pack();
    int x = parent.getLocation().x + (parent.getSize().x - shell.getSize().x) / 2;
    int y = parent.getLocation().y + (parent.getSize().y - shell.getSize().y) / 2;
    shell.setLocation(x, y);
  }
}
