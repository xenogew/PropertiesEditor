package io.github.xenogew.propedit.eclipse.plugin.jdt.hyperlink;

import io.github.xenogew.propedit.eclipse.plugin.jdt.util.JdtUtil;
import io.github.xenogew.propedit.eclipse.plugin.util.ProjectProperties;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.hyperlink.AbstractHyperlinkDetector;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PlatformUI;

/**
 * Hyperlink detector for properties keys in Java string literals.
 */
public class PropertiesHyperlinkDetector extends AbstractHyperlinkDetector {

  @Override
  public IHyperlink[] detectHyperlinks(ITextViewer textViewer, IRegion region,
      boolean canShowMultipleHyperlinks) {
    IDocument document = textViewer.getDocument();
    int offset = region.getOffset();

    try {
      IRegion keyRegion = JdtUtil.getStringLiteralRegion(document, offset);
      if (keyRegion == null) {
        return null;
      }

      String key = document.get(keyRegion.getOffset(), keyRegion.getLength());
      if (key == null || key.isEmpty()) {
        return null;
      }

      IProject project = getProject();
      if (project == null) {
        return null;
      }

      Map<IFile, Properties> properties = ProjectProperties.getInstance().getProperty(project, key);
      if (properties == null || properties.isEmpty()) {
        return null;
      }

      List<IHyperlink> hyperlinks = new ArrayList<>();
      for (IFile file : properties.keySet()) {
        hyperlinks.add(new PropertiesHyperlink(file, key, keyRegion));
      }

      return hyperlinks.isEmpty() ? null : hyperlinks.toArray(new IHyperlink[0]);

    } catch (BadLocationException e) {
      return null;
    }
  }

  private IProject getProject() {
    IEditorPart activeEditor =
        PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
    if (activeEditor != null) {
      IEditorInput input = activeEditor.getEditorInput();
      if (input instanceof IFileEditorInput) {
        return ((IFileEditorInput) input).getFile().getProject();
      }
    }
    return null;
  }
}
