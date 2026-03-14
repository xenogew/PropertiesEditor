package io.github.xenogew.propedit;

import io.github.xenogew.propedit.util.EncodeChanger;
import io.github.xenogew.propedit.util.EncodeManager;
import io.github.xenogew.propedit.util.FileOpener;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.printing.PrintDialog;
import org.eclipse.swt.printing.Printer;
import org.eclipse.swt.printing.PrinterData;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.FontDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

public class PropertiesEditorFrameSWT {

  private static final Logger LOG = Logger.getLogger(PropertiesEditorFrameSWT.class.getName());

  private Shell shell;
  private StyledText editTextArea;
  private StyledText lineNumberTextArea;
  private ToolBar toolBar;

  private FileOpener opFp;
  private boolean isModified = false;
  private String currentSearchText = "";

  public PropertiesEditorFrameSWT(Display display) {
    shell = new Shell(display);
    shell.setText("PropEditorX");
    shell.setImage(new Image(display,
        PropertiesEditorFrameSWT.class.getResourceAsStream("resource/pe_32.png")));

    GridLayout layout = new GridLayout(1, false);
    layout.marginHeight = 0;
    layout.marginWidth = 0;
    layout.verticalSpacing = 0;
    shell.setLayout(layout);

    createMenu();
    createToolBar();
    createEditorArea();

    shell.setSize(800, 600);
    shell.addListener(SWT.Close, e -> {
      e.doit = checkSave();
    });
  }

  public void open() {
    shell.open();
  }

  public Shell getShell() {
    return shell;
  }

  private void createMenu() {
    Menu menuBar = new Menu(shell, SWT.BAR);
    shell.setMenuBar(menuBar);

    // File Menu
    MenuItem fileMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
    fileMenuHeader.setText(PropertiesEditor.getI18nProperty("fileMenu_Text"));
    Menu fileMenu = new Menu(shell, SWT.DROP_DOWN);
    fileMenuHeader.setMenu(fileMenu);

    MenuItem openItem = new MenuItem(fileMenu, SWT.PUSH);
    openItem.setText(PropertiesEditor.getI18nProperty("openMenuItem_Text") + "\tCtrl+O");
    openItem.setAccelerator(SWT.MOD1 | 'O');
    openItem.addListener(SWT.Selection, e -> selectOpenFile());

    MenuItem saveItem = new MenuItem(fileMenu, SWT.PUSH);
    saveItem.setText(PropertiesEditor.getI18nProperty("unicodeSaveMenuItem_Text") + "\tCtrl+S");
    saveItem.setAccelerator(SWT.MOD1 | 'S');
    saveItem.addListener(SWT.Selection, e -> saveFile());

    MenuItem printItem = new MenuItem(fileMenu, SWT.PUSH);
    printItem.setText(PropertiesEditor.getI18nProperty("printMenuItem_Text") + "\tCtrl+P");
    printItem.setAccelerator(SWT.MOD1 | 'P');
    printItem.addListener(SWT.Selection, e -> printFile());

    new MenuItem(fileMenu, SWT.SEPARATOR);

    MenuItem closeItem = new MenuItem(fileMenu, SWT.PUSH);
    closeItem.setText(PropertiesEditor.getI18nProperty("closeMenuItem_Text"));
    closeItem.addListener(SWT.Selection, e -> {
      if (checkSave()) {
        shell.close();
      }
    });

    // Edit Menu
    MenuItem editMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
    editMenuHeader.setText(PropertiesEditor.getI18nProperty("editMenu_Text"));
    Menu editMenu = new Menu(shell, SWT.DROP_DOWN);
    editMenuHeader.setMenu(editMenu);

    MenuItem findItem = new MenuItem(editMenu, SWT.PUSH);
    findItem.setText(PropertiesEditor.getI18nProperty("findMenuItem_Text") + "\tCtrl+F");
    findItem.setAccelerator(SWT.MOD1 | 'F');
    findItem.addListener(SWT.Selection, e -> openFindDialog());

    MenuItem replaceItem = new MenuItem(editMenu, SWT.PUSH);
    replaceItem.setText(PropertiesEditor.getI18nProperty("replaceMenuItem_Text") + "\tCtrl+R");
    replaceItem.setAccelerator(SWT.MOD1 | 'R');
    replaceItem.addListener(SWT.Selection, e -> openReplaceDialog());

    // View Menu
    MenuItem viewMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
    viewMenuHeader.setText(PropertiesEditor.getI18nProperty("dispMenu_Text"));
    Menu viewMenu = new Menu(shell, SWT.DROP_DOWN);
    viewMenuHeader.setMenu(viewMenu);

    MenuItem fontItem = new MenuItem(viewMenu, SWT.PUSH);
    fontItem.setText(PropertiesEditor.getI18nProperty("fontSelectMenuItem_Text"));
    fontItem.addListener(SWT.Selection, e -> selectFont());

    // Help Menu
    MenuItem helpMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
    helpMenuHeader.setText(PropertiesEditor.getI18nProperty("helpMenu_Text"));
    Menu helpMenu = new Menu(shell, SWT.DROP_DOWN);
    helpMenuHeader.setMenu(helpMenu);

    MenuItem aboutItem = new MenuItem(helpMenu, SWT.PUSH);
    aboutItem.setText(PropertiesEditor.getI18nProperty("versionMenuItem_Text"));
    aboutItem.addListener(SWT.Selection, e -> new PropertiesEditorAboutBoxSWT(shell).open());
  }

  private void createToolBar() {
    toolBar = new ToolBar(shell, SWT.FLAT | SWT.HORIZONTAL);
    toolBar.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

    ToolItem openBtn = new ToolItem(toolBar, SWT.PUSH);
    openBtn.setImage(new Image(shell.getDisplay(),
        PropertiesEditorFrameSWT.class.getResourceAsStream("resource/Open16.png")));
    openBtn.setToolTipText(PropertiesEditor.getI18nProperty("openMenuItem_Text"));
    openBtn.addListener(SWT.Selection, e -> selectOpenFile());

    ToolItem saveBtn = new ToolItem(toolBar, SWT.PUSH);
    saveBtn.setImage(new Image(shell.getDisplay(),
        PropertiesEditorFrameSWT.class.getResourceAsStream("resource/Save16.png")));
    saveBtn.setToolTipText(PropertiesEditor.getI18nProperty("unicodeSaveMenuItem_Text"));
    saveBtn.addListener(SWT.Selection, e -> saveFile());

    new ToolItem(toolBar, SWT.SEPARATOR);

    ToolItem findBtn = new ToolItem(toolBar, SWT.PUSH);
    findBtn.setImage(new Image(shell.getDisplay(),
        PropertiesEditorFrameSWT.class.getResourceAsStream("resource/Find16.png")));
    findBtn.setToolTipText(PropertiesEditor.getI18nProperty("findButton_ToolTipText"));
    findBtn.addListener(SWT.Selection, e -> openFindDialog());

    Label separator = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL);
    separator.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
  }

  private void createEditorArea() {
    Composite editorComposite = new Composite(shell, SWT.NONE);
    GridLayout grid = new GridLayout(2, false);
    grid.marginWidth = 0;
    grid.marginHeight = 0;
    grid.horizontalSpacing = 2;
    editorComposite.setLayout(grid);
    editorComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

    lineNumberTextArea = new StyledText(editorComposite, SWT.BORDER | SWT.READ_ONLY | SWT.RIGHT);
    GridData lnGd = new GridData(SWT.LEFT, SWT.FILL, false, true);
    lnGd.widthHint = 40;
    lineNumberTextArea.setLayoutData(lnGd);
    lineNumberTextArea
        .setBackground(shell.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
    lineNumberTextArea.setText("1\n");

    editTextArea =
        new StyledText(editorComposite, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
    editTextArea.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

    editTextArea.addModifyListener(e -> {
      isModified = true;
      updateLineNumbers();
    });

    editTextArea.getVerticalBar().addListener(SWT.Selection, e -> {
      lineNumberTextArea.setTopPixel(editTextArea.getVerticalBar().getSelection());
    });

    // Drag and Drop
    DropTarget dt = new DropTarget(editTextArea, DND.DROP_DEFAULT | DND.DROP_COPY);
    dt.setTransfer(new Transfer[] {FileTransfer.getInstance()});
    dt.addDropListener(new DropTargetAdapter() {
      @Override
      public void drop(DropTargetEvent event) {
        if (FileTransfer.getInstance().isSupportedType(event.currentDataType)) {
          String[] files = (String[]) event.data;
          if (files != null && files.length > 0) {
            if (checkSave()) {
              openFile(new File(files[0]), EncodeManager.AUTO);
            }
          }
        }
      }
    });
  }

  private void updateLineNumbers() {
    int lines = editTextArea.getLineCount();
    StringBuilder sb = new StringBuilder();
    for (int i = 1; i <= lines; i++) {
      sb.append(i).append("\n");
    }
    lineNumberTextArea.setText(sb.toString());
  }

  private void selectOpenFile() {
    if (!checkSave())
      return;

    FileDialog fd = new FileDialog(shell, SWT.OPEN);
    fd.setFilterExtensions(new String[] {"*.properties", "*.*"});
    String path = fd.open();
    if (path != null) {
      openFile(new File(path), EncodeManager.AUTO);
    }
  }

  private void openFile(File file, String encode) {
    Thread.startVirtualThread(() -> {
      try {
        opFp = new FileOpener(file);
        // Fallback to UTF-8 if AUTO is passed as we don't have the old UI encoding detector fully
        // migrated yet.
        String enc = (encode.equals(EncodeManager.AUTO)) ? "UTF-8" : encode;
        opFp.read(enc);
        String decoded = EncodeChanger.unicodeEsc2Unicode(opFp.getText());

        shell.getDisplay().asyncExec(() -> {
          editTextArea.setText(decoded);
          isModified = false;
          shell.setText("PropEditorX - " + file.getName());
        });
      } catch (Exception ex) {
        LOG.log(Level.SEVERE, "Failed to read file", ex);
      }
    });
  }

  private void saveFile() {
    if (opFp == null) {
      FileDialog fd = new FileDialog(shell, SWT.SAVE);
      fd.setFilterExtensions(new String[] {"*.properties"});
      String path = fd.open();
      if (path == null)
        return;
      opFp = new FileOpener(new File(path));
    }

    String content = editTextArea.getText();
    Thread.startVirtualThread(() -> {
      try {
        String encoded = EncodeChanger.unicode2UnicodeEsc(content);
        opFp.setText(encoded);
        opFp.write("UTF-8"); // Force UTF-8 writing for now
        shell.getDisplay().asyncExec(() -> {
          isModified = false;
          MessageBox box = new MessageBox(shell, SWT.ICON_INFORMATION | SWT.OK);
          box.setText("Saved");
          box.setMessage("File saved successfully.");
          box.open();
        });
      } catch (Exception ex) {
        LOG.log(Level.SEVERE, "Failed to save file", ex);
      }
    });
  }

  private void printFile() {
    PrintDialog pd = new PrintDialog(shell);
    PrinterData pdata = pd.open();
    if (pdata != null) {
      Printer printer = new Printer(pdata);
      Thread.startVirtualThread(() -> {
        shell.getDisplay().asyncExec(() -> {
          Runnable printJob = editTextArea.print(printer);
          printJob.run();
          printer.dispose();
        });
      });
    }
  }

  private boolean checkSave() {
    if (isModified) {
      MessageBox box = new MessageBox(shell, SWT.ICON_QUESTION | SWT.YES | SWT.NO | SWT.CANCEL);
      box.setText(PropertiesEditor.getI18nProperty("KEY7"));
      box.setMessage(PropertiesEditor.getI18nProperty("KEY6"));
      int choice = box.open();
      if (choice == SWT.YES) {
        saveFile();
        return true;
      } else if (choice == SWT.CANCEL) {
        return false;
      }
    }
    return true;
  }

  private void openFindDialog() {
    SearchTextDialogSWT dialog = new SearchTextDialogSWT(shell);
    String searchFor = dialog.open();
    if (searchFor != null && !searchFor.isEmpty()) {
      currentSearchText = searchFor;
      doSearch();
    }
  }

  private void doSearch() {
    if (currentSearchText.isEmpty())
      return;
    String text = editTextArea.getText();
    int startIdx = editTextArea.getCaretOffset();
    int idx = text.indexOf(currentSearchText, startIdx);
    if (idx == -1) {
      idx = text.indexOf(currentSearchText, 0);
    }
    if (idx != -1) {
      editTextArea.setSelection(idx, idx + currentSearchText.length());
    } else {
      MessageBox mb = new MessageBox(shell, SWT.ICON_INFORMATION | SWT.OK);
      mb.setText("Find");
      mb.setMessage("Text not found: " + currentSearchText);
      mb.open();
    }
  }

  private void openReplaceDialog() {
    ReplaceTextDialogSWT dialog = new ReplaceTextDialogSWT(shell);
    ReplaceTextDialogSWT.ReplaceResult result = dialog.open();
    if (result != null && !result.sourceText.isEmpty()) {
      String text = editTextArea.getText();
      String newText = text.replace(result.sourceText, result.exchangeText);
      editTextArea.setText(newText);
      MessageBox mb = new MessageBox(shell, SWT.ICON_INFORMATION | SWT.OK);
      mb.setText("Replace");
      mb.setMessage("Replacement complete.");
      mb.open();
    }
  }

  private void selectFont() {
    FontDialog fd = new FontDialog(shell);
    Font currentFont = editTextArea.getFont();
    if (currentFont != null) {
      fd.setFontList(currentFont.getFontData());
    }
    FontData fontData = fd.open();
    if (fontData != null) {
      Font newFont = new Font(shell.getDisplay(), fontData);
      editTextArea.setFont(newFont);
      lineNumberTextArea.setFont(newFont);

      ColorDialog cd = new ColorDialog(shell);
      cd.setRGB(editTextArea.getForeground().getRGB());
      org.eclipse.swt.graphics.RGB rgb = cd.open();
      if (rgb != null) {
        editTextArea.setForeground(new org.eclipse.swt.graphics.Color(shell.getDisplay(), rgb));
      }
    }
  }
}
