package io.github.xenogew.propedit;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

/**
 * SWT-based Unicode Dialog.
 */
public class UnicodeDialogSWT extends BaseDialogSWT {

  private StyledText styledText;
  private String message = "";

  /**
   * Constructs a new Unicode dialog.
   *
   * @param parent the parent shell
   * @param title the dialog title
   */
  public UnicodeDialogSWT(Shell parent, String title) {
    super(parent, title);
  }

  /**
   * Sets the message to display.
   *
   * @param message the message
   */
  public void setMessage(String message) {
    this.message = message;
    if (styledText != null && !styledText.isDisposed()) {
      styledText.setText(message);
    }
  }

  @Override
  public Object open() {
    shell = new Shell(getParent(), getStyle());
    shell.setText(getText());
    shell.setLayout(new FillLayout());
    shell.setSize(600, 400);

    // Menu Bar
    Menu menuBar = new Menu(shell, SWT.BAR);
    shell.setMenuBar(menuBar);

    MenuItem fileMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
    fileMenuHeader.setText(PropEditorX.getI18nProperty("fileMenu_Text"));
    Menu fileMenu = new Menu(shell, SWT.DROP_DOWN);
    fileMenuHeader.setMenu(fileMenu);

    MenuItem closeItem = new MenuItem(fileMenu, SWT.PUSH);
    closeItem.setText(PropEditorX.getI18nProperty("closeMenuItem_Text") + "\tCtrl+U");
    closeItem.setAccelerator(SWT.MOD1 + 'U');
    closeItem.addListener(SWT.Selection, e -> shell.close());

    // Editor Area
    styledText =
        new StyledText(shell, SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL | SWT.READ_ONLY | SWT.BORDER);
    Color bgColor = new Color(shell.getDisplay(), 230, 230, 255);
    styledText.setBackground(bgColor);
    shell.addDisposeListener(e -> bgColor.dispose());

    if (message != null) {
      styledText.setText(message);
    }

    centerOnParent();
    shell.open();
    dispatchEvents();
    return null;
  }
}
