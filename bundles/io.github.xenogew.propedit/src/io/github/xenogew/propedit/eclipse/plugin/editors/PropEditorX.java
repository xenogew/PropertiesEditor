package io.github.xenogew.propedit.eclipse.plugin.editors;

import java.util.HashMap;
import java.util.Map;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.jface.text.source.projection.ProjectionAnnotationModel;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.IDocumentProvider;

/**
 * Multi-page editor for PropEditorX. Supports a Grid view and multiple Source tabs (one per
 * locale).
 */
public class PropEditorX extends FormEditor {
  private PropertiesGridPage gridPage;
  private PropertyBundleModel bundleModel;
  private final Map<String, PropertiesSourceEditor> sourceEditors = new HashMap<>();
  private boolean isDirty = false;
  private final ColorManager colorManager = new ColorManager();

  @Override
  protected void addPages() {
    IFileEditorInput input = (IFileEditorInput) getEditorInput();
    bundleModel = new PropertyBundleModel(input.getFile());

    try {
      // 1. Grid Page
      gridPage = new PropertiesGridPage(this, "grid", "Grid");
      addPage(gridPage);

      // 2. Source Pages (One per discovered locale file)
      for (Map.Entry<String, IFile> entry : bundleModel.getLocaleFiles().entrySet()) {
        String locale = entry.getKey();
        IFile file = entry.getValue();

        PropertiesSourceEditor editor = new PropertiesSourceEditor();
        int index = addPage(editor, new FileEditorInput(file));
        setPageText(index, bundleModel.getDisplayName(locale));
        sourceEditors.put(locale, editor);
      }

    } catch (PartInitException e) {
      // Log error
    }
  }

  public PropertyBundleModel getBundleModel() {
    return bundleModel;
  }

  @Override
  public void doSave(IProgressMonitor monitor) {
    SubMonitor subMonitor = SubMonitor.convert(monitor, "Saving bundle", 100);
    try {
      // 1. Save all source editors (50% of work)
      int workPerEditor = sourceEditors.isEmpty() ? 0 : 50 / sourceEditors.size();
      for (PropertiesSourceEditor editor : sourceEditors.values()) {
        editor.doSave(subMonitor.split(workPerEditor));
      }
      // 2. Save model state if modified via Grid (50% of work)
      bundleModel.saveAll(subMonitor.split(50));

      isDirty = false;
      editorDirtyStateChanged();
    } catch (Exception e) {
      // Log error
    } finally {
      subMonitor.done();
    }
  }

  @Override
  public void doSaveAs() {
    // SaveAs usually targets the active editor
    if (getActiveEditor() != null) {
      getActiveEditor().doSaveAs();
    }
  }

  @Override
  public boolean isSaveAsAllowed() {
    return true;
  }

  @Override
  public boolean isDirty() {
    if (isDirty)
      return true;
    for (PropertiesSourceEditor editor : sourceEditors.values()) {
      if (editor.isDirty())
        return true;
    }
    return false;
  }

  public void markDirty() {
    isDirty = true;
    editorDirtyStateChanged();
  }

  @Override
  public void dispose() {
    colorManager.dispose();
    super.dispose();
  }

  public ColorManager getColorManager() {
    return colorManager;
  }

  // --- Delegate methods for legacy TextEditor integrations ---

  public ProjectionAnnotationModel getAnnotationModel() {
    PropertiesSourceEditor active = getActiveSourceEditor();
    return active != null ? active.getAnnotationModel() : null;
  }

  public ISelectionProvider getSelectionProvider() {
    return getSite().getSelectionProvider();
  }

  public Color getBackground() {
    PropertiesSourceEditor active = getActiveSourceEditor();
    return active != null ? active.getBackground() : null;
  }

  public Color getForeground() {
    PropertiesSourceEditor active = getActiveSourceEditor();
    return active != null ? active.getForeground() : null;
  }

  public Font getFont() {
    PropertiesSourceEditor active = getActiveSourceEditor();
    return active != null ? active.getFont() : null;
  }

  public void setBackground(Color color) {
    for (PropertiesSourceEditor editor : sourceEditors.values()) {
      editor.setBackground(color);
    }
  }

  public void setForeground(Color color) {
    for (PropertiesSourceEditor editor : sourceEditors.values()) {
      editor.setForeground(color);
    }
  }

  public void setFont(Font font) {
    for (PropertiesSourceEditor editor : sourceEditors.values()) {
      editor.setFont(font);
    }
  }

  public IDocumentProvider getDocumentProvider() {
    PropertiesSourceEditor active = getActiveSourceEditor();
    return active != null ? active.getDocumentProvider() : null;
  }

  private PropertiesSourceEditor getActiveSourceEditor() {
    int index = getActivePage();
    if (index > 0) {
      Object page = getEditor(index);
      if (page instanceof PropertiesSourceEditor) {
        return (PropertiesSourceEditor) page;
      }
    }
    // Fallback to primary if available
    return sourceEditors.get(PropertyBundleModel.DEFAULT_LOCALE);
  }
}
