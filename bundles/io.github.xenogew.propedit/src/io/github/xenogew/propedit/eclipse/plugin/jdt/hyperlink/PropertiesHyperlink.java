package io.github.xenogew.propedit.eclipse.plugin.jdt.hyperlink;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;

/**
 * Hyperlink to a property key in a properties file.
 */
public class PropertiesHyperlink implements IHyperlink {

  private final IFile file;
  private final String key;
  private final IRegion region;

  public PropertiesHyperlink(IFile file, String key, IRegion region) {
    this.file = file;
    this.key = key;
    this.region = region;
  }

  @Override
  public IRegion getHyperlinkRegion() {
    return region;
  }

  @Override
  public String getTypeLabel() {
    return "PropEditorX Hyperlink";
  }

  @Override
  public String getHyperlinkText() {
    return "Open '" + key + "' in " + file.getName();
  }

  @Override
  public void open() {
    IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
    try {
      IEditorPart editor =
          IDE.openEditor(page, file, "io.github.xenogew.propedit.editors.PropEditorX");
      if (editor instanceof ITextEditor) {
        ITextEditor textEditor = (ITextEditor) editor;
        IDocumentProvider provider = textEditor.getDocumentProvider();
        IDocument document = provider.getDocument(textEditor.getEditorInput());

        String content = document.get();
        int keyIndex = content.indexOf(key + "=");
        if (keyIndex == -1) {
          keyIndex = content.indexOf(key + ":");
        }
        if (keyIndex == -1) {
          keyIndex = content.indexOf(key + " ");
        }

        if (keyIndex != -1) {
          textEditor.selectAndReveal(keyIndex, key.length());
        }
      }
    } catch (Exception e) {
      // Ignore or log
    }
  }
}
