package io.github.xenogew.propedit.eclipse.plugin.editors;

import io.github.xenogew.propedit.eclipse.plugin.PropEditorXPlugin;
import java.util.HashMap;
import java.util.Map;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.jface.text.source.projection.ProjectionAnnotationModel;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
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
  public void init(IEditorSite site, IEditorInput input) throws PartInitException {
    super.init(site, input);
    handleNewInput(input);
  }

  @Override
  protected void setInput(IEditorInput input) {
    super.setInput(input);
    handleNewInput(input);
  }

  private void handleNewInput(IEditorInput input) {
    if (bundleModel != null && input instanceof IFileEditorInput fileInput) {
      IFile file = fileInput.getFile();
      String locale = extractLocale(file.getName());

      if (!sourceEditors.containsKey(locale)) {
        // Ensure UI updates happen on the UI thread
        getSite().getShell().getDisplay().asyncExec(() -> {
          if (sourceEditors.containsKey(locale))
            return;

          bundleModel.refresh();
          try {
            addSourcePage(locale, file);
            if (gridPage != null) {
              gridPage.refresh();
            }
            // Force tab folder layout refresh
            if (getContainer() != null && !getContainer().isDisposed()) {
              getContainer().layout(true, true);
            }
          } catch (PartInitException e) {
            PropEditorXPlugin.getDefault().error("Error adding source page for locale: " + locale,
                e);
          }
        });
      }
    }
  }

  private String extractLocale(String fileName) {
    String base = bundleModel.getBaseName();
    String nameNoExt = fileName.substring(0, fileName.lastIndexOf('.'));
    if (nameNoExt.equals(base)) {
      return PropertyBundleModel.DEFAULT_LOCALE;
    }
    if (nameNoExt.startsWith(base + "_")) {
      return nameNoExt.substring(base.length() + 1);
    }
    return nameNoExt;
  }

  @Override
  protected void addPages() {
    IFileEditorInput input = (IFileEditorInput) getEditorInput();
    bundleModel = new PropertyBundleModel(input.getFile());

    try {
      // 1. Grid Page
      gridPage = new PropertiesGridPage(this, "grid", "Editor::{✘}");
      addPage(gridPage);

      // 2. Source Pages (One per discovered locale file)
      for (Map.Entry<String, IFile> entry : bundleModel.getLocaleFiles().entrySet()) {
        addSourcePage(entry.getKey(), entry.getValue());
      }

    } catch (PartInitException e) {
      PropEditorXPlugin.getDefault().error("Error adding pages to editor", e);
    }
  }

  /**
   * Dynamically adds a source tab for a locale file if it doesn't already exist.
   */
  public void addSourcePage(String locale, IFile file) throws PartInitException {
    if (sourceEditors.containsKey(locale)) {
      return;
    }
    PropertiesSourceEditor editor = new PropertiesSourceEditor();
    int index = addPage(editor, new FileEditorInput(file));
    setPageText(index, bundleModel.getDisplayName(locale));
    sourceEditors.put(locale, editor);
  }

  public PropertyBundleModel getBundleModel() {
    return bundleModel;
  }

  @Override
  public void doSave(IProgressMonitor monitor) {
    SubMonitor subMonitor = SubMonitor.convert(monitor, "Saving bundle", 100);
    try {
      // 1. Save all source editors (Commit current UI to disk)
      int workPerEditor = sourceEditors.isEmpty() ? 0 : 40 / sourceEditors.size();
      for (PropertiesSourceEditor editor : sourceEditors.values()) {
        editor.doSave(subMonitor.split(workPerEditor));
      }

      // 2. Refresh Model from disk (Sync with Source tab edits)
      bundleModel.refresh();
      subMonitor.worked(10);

      // 3. Save model state ONLY if Grid was dirty (modified via Grid UI)
      if (isDirty) {
        bundleModel.saveAll(subMonitor.split(40));
      } else {
        subMonitor.worked(40);
      }

      // 4. Update Grid UI to reflect potential changes from Source tabs
      if (gridPage != null) {
        gridPage.refresh();
      }
      subMonitor.worked(10);

      isDirty = false;
      editorDirtyStateChanged();
    } catch (Exception e) {
      PropEditorXPlugin.getDefault().error("Error saving bundle", e);
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
