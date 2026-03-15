package io.github.xenogew.propedit.eclipse.plugin.editors;

import io.github.xenogew.propedit.eclipse.plugin.PropEditorXPlugin;
import io.github.xenogew.propedit.eclipse.plugin.listener.IPropertiesDocumentListener;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.ui.editors.text.FileDocumentProvider;

public class PropertiesDocumentProvider extends FileDocumentProvider {
  private static final String EXTENSION_POINT = "io.github.xenogew.propedit.listeners"; //$NON-NLS-1$

  protected List<IPropertiesDocumentListener> computePropertiesDocumentListeners() {
    IExtensionRegistry registry = Platform.getExtensionRegistry();
    IExtensionPoint extensionPoint = registry.getExtensionPoint(EXTENSION_POINT);
    IExtension[] extensions = extensionPoint.getExtensions();
    ArrayList<IPropertiesDocumentListener> results = new ArrayList<>();
    for (IExtension extension : extensions) {
      IConfigurationElement[] elements = extension.getConfigurationElements();
      for (IConfigurationElement element : elements) {
        try {
          Object listener = element.createExecutableExtension("class"); //$NON-NLS-1$
          if (listener instanceof IPropertiesDocumentListener) {
            results.add((IPropertiesDocumentListener) listener);
          }
        } catch (CoreException e) {
          IStatus status =
              new Status(IStatus.ERROR, PropEditorXPlugin.PLUGIN_ID, e.getMessage(), e);
          PropEditorXPlugin.getDefault().getLog().log(status);
        }
      }
    }

    return results;
  }

  @Override
  protected IDocument createDocument(Object element) throws CoreException {

    IDocument document = super.createDocument(element);

    List<IPropertiesDocumentListener> listeners = computePropertiesDocumentListeners();
    for (IPropertiesDocumentListener listener : listeners) {
      try {
        listener.beforeConvertAtLoadingDocument(document, element);
      } catch (Exception e) {
        IStatus status = new Status(IStatus.ERROR, PropEditorXPlugin.PLUGIN_ID, e.getMessage(), e);
        PropEditorXPlugin.getDefault().getLog().log(status);
      }
    }

    if (document != null) {
      // DISABLED: Legacy \\uXXXX conversion removed to maintain human-readable UTF-8.
      /*
       * try { document.set(EncodeChanger.unicodeEsc2Unicode(document.get())); } catch (Exception e)
       * { IStatus status = new Status(IStatus.ERROR, PropEditorXPlugin.PLUGIN_ID, IStatus.OK,
       * e.getMessage(), e); ILog log = PropEditorXPlugin.getDefault().getLog(); log.log(status);
       * ErrorDialog.openError(null, Messages.getString("eclipse.propertieseditor.convert.error"),
       * //$NON-NLS-1$ Messages.getString("eclipse.propertieseditor.property.get.settings.error"),
       * status); //$NON-NLS-1$ }
       */

      for (IPropertiesDocumentListener listener : listeners) {
        try {
          listener.afterConvertAtLoadingDocument(document, element);
        } catch (Exception e) {
          IStatus status =
              new Status(IStatus.ERROR, PropEditorXPlugin.PLUGIN_ID, e.getMessage(), e);
          PropEditorXPlugin.getDefault().getLog().log(status);
        }
      }

      IDocumentPartitioner partitioner = new FastPartitioner(new PropertiesPartitionScanner(),
          new String[] {IDocument.DEFAULT_CONTENT_TYPE,
              PropertiesPartitionScanner.PROPERTIES_COMMENT,
              PropertiesPartitionScanner.PROPERTIES_SEPARATOR,
              PropertiesPartitionScanner.PROPERTIES_VALUE});
      partitioner.connect(document);
      document.setDocumentPartitioner(partitioner);
    }
    return document;
  }

  @Override
  protected void doSaveDocument(IProgressMonitor monitor, Object element, IDocument document,
      boolean overwrite) throws CoreException {

    // DISABLED: Legacy \\uXXXX conversion removed.
    // Saving now directly writes the human-readable UTF-8 document content to disk.

    super.doSaveDocument(monitor, element, document, overwrite);
  }
}
