package io.github.xenogew.propedit.eclipse.plugin.editors;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.core.resources.IContainer;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

/**
 * Multi-Locale Editor Page. Uses Table for grid lines and resizable columns (30/70 ratio).
 */
public class PropertiesGridPage extends FormPage {

  private PropertyBundleModel model;
  private final List<TableEditor> editors = new ArrayList<>();

  public PropertiesGridPage(FormEditor editor, String id, String title) {
    super(editor, id, title);
  }

  public void refresh() {
    IManagedForm managedForm = getManagedForm();
    if (managedForm == null) {
      return;
    }
    ScrolledForm scrolledForm = managedForm.getForm();
    if (scrolledForm == null || scrolledForm.isDisposed()) {
      return;
    }
    // Remove all existing content from body and dispose editors
    for (TableEditor editor : editors) {
      if (editor.getEditor() != null) {
        editor.getEditor().dispose();
      }
      editor.dispose();
    }
    editors.clear();

    for (org.eclipse.swt.widgets.Control child : scrolledForm.getBody().getChildren()) {
      child.dispose();
    }
    // Re-create content
    createFormContent(managedForm);
    scrolledForm.layout(true, true);
  }

  @Override
  protected void createFormContent(IManagedForm managedForm) {
    ScrolledForm form = managedForm.getForm();
    FormToolkit toolkit = managedForm.getToolkit();
    model = ((PropEditorX) getEditor()).getBundleModel();

    // 1. Title: Parent Folder Name
    IContainer parentFolder = model.getLocaleFiles().values().iterator().next().getParent();
    form.setText(parentFolder.getName());

    Composite body = form.getBody();
    GridLayout layout = new GridLayout(1, false);
    layout.marginWidth = 10;
    layout.marginHeight = 10;
    body.setLayout(layout);

    // 2. Subtitle Area
    Composite header = toolkit.createComposite(body);
    header.setLayout(new GridLayout(1, false));
    header.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

    int numLocales = model.getLocaleFiles().size();
    String localesInfo =
        numLocales + " locales detected: " + String.join(", ", model.getLocaleFiles().keySet());

    Label baseLabel =
        toolkit.createLabel(header, model.getBaseName() + " bundle (" + localesInfo + ")");
    baseLabel.setFont(JFaceResources.getFontRegistry().getBold(JFaceResources.HEADER_FONT));

    Label pathLabel = toolkit.createLabel(header, parentFolder.getFullPath().toString());
    pathLabel.setForeground(toolkit.getColors().getColor(org.eclipse.ui.forms.IFormColors.TITLE));
    pathLabel.setFont(JFaceResources.getFontRegistry().getItalic(JFaceResources.DEFAULT_FONT));

    // 3. The Grid Table
    Table table =
        toolkit.createTable(body, SWT.FULL_SELECTION | SWT.HIDE_SELECTION | SWT.DOUBLE_BUFFERED);
    table.setLinesVisible(true);
    table.setHeaderVisible(true);
    table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

    TableColumn keyCol = new TableColumn(table, SWT.NONE);
    keyCol.setText("Property Key");

    TableColumn valCol = new TableColumn(table, SWT.NONE);
    valCol.setText("Translations (English + Locales)");

    // Optimized row height: margins (20) + text boxes (40 * n) + spacing (10 * (n-1))
    final int rowHeight = (numLocales * 40) + ((numLocales - 1) * 10) + 20;
    table.addListener(SWT.MeasureItem, event -> event.height = rowHeight);

    // Set initial widths before creating rows to ensure TableEditors find their bounds correctly
    table.addControlListener(new ControlAdapter() {
      @Override
      public void controlResized(ControlEvent e) {
        int width = table.getClientArea().width;
        if (width > 100) {
          table.removeControlListener(this);
          keyCol.setWidth((int) (width * 0.3));
          valCol.setWidth((int) (width * 0.7));

          // Populate rows AFTER columns have widths
          for (String key : model.getAllKeys()) {
            createTableRow(toolkit, table, key, rowHeight);
          }
          table.layout(true, true);
        }
      }
    });
  }

  private void createTableRow(FormToolkit toolkit, Table table, String key, int rowHeight) {
    TableItem item = new TableItem(table, SWT.NONE);

    // 1. Key Editor (Left Column)
    TableEditor keyEditor = new TableEditor(table);
    Composite keyContainer = toolkit.createComposite(table, SWT.NONE);
    keyContainer.setBackground(table.getBackground());

    // Debug Border
    keyContainer.addPaintListener(e -> {
      e.gc.setForeground(e.display.getSystemColor(SWT.COLOR_BLACK));
      Point size = keyContainer.getSize();
      e.gc.drawRectangle(0, 0, size.x - 1, size.y - 1);
    });

    GridLayout keyLayout = new GridLayout(1, false);
    keyLayout.marginHeight = 10; // p-2 equivalent
    keyLayout.marginWidth = 10;
    keyContainer.setLayout(keyLayout);

    Label keyLabel = toolkit.createLabel(keyContainer, key);
    keyLabel.setFont(JFaceResources.getFontRegistry().getBold(JFaceResources.DEFAULT_FONT));
    keyLabel.setBackground(keyContainer.getBackground());
    keyLabel.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

    keyEditor.grabHorizontal = true;
    keyEditor.grabVertical = true;
    keyEditor.verticalAlignment = SWT.TOP;
    keyEditor.minimumHeight = rowHeight;
    keyEditor.setEditor(keyContainer, item, 0);
    editors.add(keyEditor);

    // 2. Translations Editor (Right Column)
    TableEditor valEditor = new TableEditor(table);
    Composite valContainer = toolkit.createComposite(table, SWT.NONE);
    valContainer.setBackground(table.getBackground());

    // Debug Border
    valContainer.addPaintListener(e -> {
      e.gc.setForeground(e.display.getSystemColor(SWT.COLOR_BLACK));
      Point size = valContainer.getSize();
      e.gc.drawRectangle(0, 0, size.x - 1, size.y - 1);
    });

    GridLayout valLayout = new GridLayout(1, false);
    valLayout.marginHeight = 10; // p-2 equivalent
    valLayout.marginWidth = 10;
    valLayout.verticalSpacing = 10;
    valContainer.setLayout(valLayout);

    for (String locale : model.getLocaleFiles().keySet()) {
      createLocaleRow(toolkit, valContainer, key, locale);
    }

    valContainer.layout(true, true);

    valEditor.grabHorizontal = true;
    valEditor.grabVertical = true;
    valEditor.verticalAlignment = SWT.TOP;
    valEditor.minimumHeight = rowHeight;
    valEditor.setEditor(valContainer, item, 1);
    editors.add(valEditor);
  }

  private void createLocaleRow(FormToolkit toolkit, Composite parent, String key, String locale) {
    Composite row = toolkit.createComposite(parent);
    row.setBackground(parent.getBackground());
    GridLayout layout = new GridLayout(2, false);
    layout.marginHeight = 0;
    layout.marginWidth = 0;
    layout.horizontalSpacing = 10; // pl-2 equivalent
    row.setLayout(layout);
    row.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

    String value = model.getValue(key, locale);

    // Locale Header Style (Lokalise-inspired)
    Label label = toolkit.createLabel(row, model.getDisplayName(locale) + ":", SWT.LEFT);
    label.setFont(JFaceResources.getFontRegistry().getBold(JFaceResources.DEFAULT_FONT));

    // slate-400 (148, 163, 184) background
    ColorManager cm = ((PropEditorX) getEditor()).getColorManager();
    label.setBackground(cm.getColor(new RGB(148, 163, 184)));
    label.setForeground(row.getDisplay().getSystemColor(SWT.COLOR_BLACK)); // Solid black text

    GridData gdLabel = new GridData(SWT.FILL, SWT.FILL, false, true);
    gdLabel.widthHint = 120;
    label.setLayoutData(gdLabel);

    if (value == null) {
      label.setForeground(parent.getDisplay().getSystemColor(SWT.COLOR_MAGENTA));
    }

    Text text = toolkit.createText(row, value != null ? value : "", SWT.MULTI | SWT.WRAP);
    GridData gdText = new GridData(SWT.FILL, SWT.TOP, true, false);
    gdText.heightHint = 40;
    text.setLayoutData(gdText);

    if (value == null) {
      text.setMessage("EMPTY");
    }

    text.addModifyListener(e -> {
      model.setValue(key, locale, text.getText());
      ((PropEditorX) getEditor()).markDirty();
    });
  }

  @Override
  public void dispose() {
    for (TableEditor editor : editors) {
      if (editor.getEditor() != null)
        editor.getEditor().dispose();
      editor.dispose();
    }
    super.dispose();
  }
}
