package io.github.xenogew.propedit.eclipse.plugin.listener;

import io.github.xenogew.propedit.eclipse.plugin.PropEditorXPlugin;
import io.github.xenogew.propedit.eclipse.plugin.checker.CheckAndMarkDuplicateKey;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.IFileEditorInput;

public class DuplicationKeyCheckListener implements IPropertiesDocumentListener {

  /**
   * @see io.github.xenogew.propedit.eclipse.plugin.listener.IPropertiesDocumentListener#afterConvertAtLoadingDocument(org.eclipse.jface.text.IDocument,
   *      java.lang.Object)
   */
  @Override
  public void afterConvertAtLoadingDocument(IDocument document, Object element) {
    if (element instanceof IFileEditorInput) {
      CheckAndMarkDuplicateKey camd = new CheckAndMarkDuplicateKey();
      try {
        camd.checkAndMarkDuplicateKeyInString(document.get(), (IFileEditorInput) element);
      } catch (CoreException e) {
        IStatus status = new Status(IStatus.ERROR, PropEditorXPlugin.PLUGIN_ID, e.getMessage(), e);
        PropEditorXPlugin.getDefault().getLog().log(status);
      }
    }
  }

  /**
   * @see io.github.xenogew.propedit.eclipse.plugin.listener.IPropertiesDocumentListener#afterUnicodeConvertAtSavingDocument(org.eclipse.core.runtime.IProgressMonitor,
   *      java.lang.Object, org.eclipse.jface.text.IDocument, boolean)
   */
  @Override
  public void afterUnicodeConvertAtSavingDocument(IProgressMonitor monitor, Object element,
      IDocument document, boolean overwrite) {}

  /**
   * @see io.github.xenogew.propedit.eclipse.plugin.listener.IPropertiesDocumentListener#beforeConvertAtLoadingDocument(org.eclipse.jface.text.IDocument,
   *      java.lang.Object)
   */
  @Override
  public void beforeConvertAtLoadingDocument(IDocument document, Object element) {}

  /**
   * @see io.github.xenogew.propedit.eclipse.plugin.listener.IPropertiesDocumentListener#beforeUnicodeConvertAtSavingDocument(org.eclipse.core.runtime.IProgressMonitor,
   *      java.lang.Object, org.eclipse.jface.text.IDocument, boolean)
   */
  @Override
  public void beforeUnicodeConvertAtSavingDocument(IProgressMonitor monitor, Object element,
      IDocument document, boolean overwrite) {
    if (element instanceof IFileEditorInput) {
      CheckAndMarkDuplicateKey camd = new CheckAndMarkDuplicateKey();
      try {
        camd.checkAndMarkDuplicateKeyInString(document.get(), (IFileEditorInput) element);
      } catch (CoreException e) {
        IStatus status = new Status(IStatus.ERROR, PropEditorXPlugin.PLUGIN_ID, e.getMessage(), e);
        PropEditorXPlugin.getDefault().getLog().log(status);
      }
    }
  }

}
