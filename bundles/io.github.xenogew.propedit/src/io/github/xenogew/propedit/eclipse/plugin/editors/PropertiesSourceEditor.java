package io.github.xenogew.propedit.eclipse.plugin.editors;

import io.github.xenogew.propedit.eclipse.plugin.PropEditorXPlugin;
import io.github.xenogew.propedit.eclipse.plugin.editors.view.outline.PropertiesContentOutlinePage;
import io.github.xenogew.propedit.eclipse.plugin.preference.PropEditorXPreference;
import java.util.ArrayList;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.text.source.projection.ProjectionAnnotation;
import org.eclipse.jface.text.source.projection.ProjectionAnnotationModel;
import org.eclipse.jface.text.source.projection.ProjectionSupport;
import org.eclipse.jface.text.source.projection.ProjectionViewer;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

/**
 * The source (text) editor part of PropEditorX.
 */
public class PropertiesSourceEditor extends TextEditor {

  private ColorManager colorManager;
  public Boolean initialCollapseOption;
  private PropertiesContentOutlinePage fOutlinePage;
  private ProjectionAnnotationModel annotationModel;

  public PropertiesSourceEditor() {
    super();
    colorManager = new ColorManager();
    setSourceViewerConfiguration(new PropertiesConfiguration(colorManager, this));
    setDocumentProvider(new PropertiesDocumentProvider());
  }

  @Override
  public void dispose() {
    if (fOutlinePage != null) {
      fOutlinePage.setInput(null);
    }
    fOutlinePage = null;
    colorManager.dispose();
    super.dispose();
  }

  @Override
  protected void initializeEditor() {
    super.initializeEditor();
    setEditorContextMenuId("#PropEditorXContext"); //$NON-NLS-1$
  }

  public void setFont(Font font) {
    getSourceViewer().getTextWidget().setFont(font);
    if (fLineNumberRulerColumn != null) {
      fLineNumberRulerColumn.setFont(font);
    }
  }

  public void setBackground(Color color) {
    getSourceViewer().getTextWidget().setBackground(color);
  }

  public void setForeground(Color color) {
    getSourceViewer().getTextWidget().setForeground(color);
  }

  public Font getFont() {
    return getSourceViewer().getTextWidget().getFont();
  }

  public Color getBackground() {
    return getSourceViewer().getTextWidget().getBackground();
  }

  public Color getForeground() {
    return getSourceViewer().getTextWidget().getForeground();
  }

  @Override
  public void createPartControl(Composite parent) {
    super.createPartControl(parent);
    if (!PropEditorXPlugin.getDefault().getPreferenceStore()
        .getBoolean(PropEditorXPreference.P_COLLAPSE)) {
      return;
    }
    ProjectionViewer viewer = (ProjectionViewer) getSourceViewer();

    ProjectionSupport projectionSupport =
        new ProjectionSupport(viewer, getAnnotationAccess(), getSharedColors());
    projectionSupport.install();

    // turn projection mode on
    viewer.doOperation(ProjectionViewer.TOGGLE);

    annotationModel = viewer.getProjectionAnnotationModel();
  }

  public ProjectionAnnotationModel getAnnotationModel() {
    return annotationModel;
  }

  @Override
  protected ISourceViewer createSourceViewer(Composite parent, IVerticalRuler ruler, int styles) {
    if (!PropEditorXPlugin.getDefault().getPreferenceStore()
        .getBoolean(PropEditorXPreference.P_COLLAPSE)) {
      return super.createSourceViewer(parent, ruler, styles);
    }
    ISourceViewer viewer =
        new ProjectionViewer(parent, ruler, getOverviewRuler(), isOverviewRulerVisible(), styles);

    // ensure decoration support has been created and configured.
    getSourceViewerDecorationSupport(viewer);

    return viewer;
  }

  public void updateFoldingStructure(ArrayList<Position> positions) {
    if (!PropEditorXPlugin.getDefault().getPreferenceStore()
        .getBoolean(PropEditorXPreference.P_COLLAPSE)) {
      return;
    }

    var ite = annotationModel.getAnnotationIterator();
    while (ite.hasNext()) {
      ProjectionAnnotation annotation = (ProjectionAnnotation) ite.next();
      Position oldPos = annotationModel.getPosition(annotation);
      boolean removeFlg = true;
      for (var newPos : positions) {
        if (newPos.getOffset() == oldPos.getOffset() && newPos.getLength() == oldPos.getLength()) {
          removeFlg = false;
          positions.remove(newPos);
          break;
        }
      }
      if (removeFlg) {
        annotationModel.removeAnnotation(annotation);
      }
    }
    if (initialCollapseOption == null) {
      // get default collapse option
      var store = PropEditorXPlugin.getDefault().getPreferenceStore();
      initialCollapseOption = store.getBoolean(PropEditorXPreference.P_INIT_COLLAPSE);
      for (Position pos : positions) {
        annotationModel
            .addAnnotation(new ProjectionAnnotation(initialCollapseOption.booleanValue()), pos);
      }
    } else {
      for (Position pos : positions) {
        annotationModel.addAnnotation(new ProjectionAnnotation(), pos);
      }
    }
  }

  @Override
  protected void initializeKeyBindingScopes() {
    setKeyBindingScopes(new String[] {"io.github.xenogew.propedit.PropEditorXScope"}); //$NON-NLS-1$
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> T getAdapter(Class<T> adapter) {
    if (IContentOutlinePage.class.equals(adapter)) {
      if (fOutlinePage == null) {
        fOutlinePage = new PropertiesContentOutlinePage(getDocumentProvider(), this);
        if (getEditorInput() != null)
          fOutlinePage.setInput(getEditorInput());
      }
      return (T) fOutlinePage;
    }
    return super.getAdapter(adapter);
  }

  @Override
  public void doRevertToSaved() {
    super.doRevertToSaved();
    if (fOutlinePage != null) {
      fOutlinePage.update();
    }
  }

  @Override
  public void doSave(IProgressMonitor progressMonitor) {
    super.doSave(progressMonitor);
    if (fOutlinePage != null) {
      fOutlinePage.update();
    }
  }

  @Override
  public void doSaveAs() {
    super.doSaveAs();
    if (fOutlinePage != null) {
      fOutlinePage.update();
    }
  }

  @Override
  protected void doSetInput(IEditorInput input) throws CoreException {
    super.doSetInput(input);
    if (fOutlinePage != null) {
      fOutlinePage.update();
    }
  }
}
