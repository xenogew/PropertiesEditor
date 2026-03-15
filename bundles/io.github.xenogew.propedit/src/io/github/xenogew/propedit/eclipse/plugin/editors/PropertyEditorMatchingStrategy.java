package io.github.xenogew.propedit.eclipse.plugin.editors;

import io.github.xenogew.propedit.eclipse.plugin.PropEditorXPlugin;
import io.github.xenogew.propedit.eclipse.plugin.util.PropertiesFileUtil;
import org.eclipse.core.resources.IFile;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorMatchingStrategy;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;

/**
 * Matching strategy to ensure all files in a property bundle share the same editor instance.
 */
public class PropertyEditorMatchingStrategy implements IEditorMatchingStrategy {

  @Override
  public boolean matches(IEditorReference editorRef, IEditorInput input) {
    if (!(input instanceof IFileEditorInput)) {
      return false;
    }

    IFile newFile = ((IFileEditorInput) input).getFile();
    String newBaseName = PropertiesFileUtil.extractBaseName(newFile.getName());

    try {
      IEditorInput existingInput = editorRef.getEditorInput();
      if (!(existingInput instanceof IFileEditorInput)) {
        return false;
      }

      IFile existingFile = ((IFileEditorInput) existingInput).getFile();

      // 1. Must be in same directory
      if (!existingFile.getParent().equals(newFile.getParent())) {
        return false;
      }

      // 2. Must share the same base name (same bundle)
      String existingBaseName = PropertiesFileUtil.extractBaseName(existingFile.getName());
      return existingBaseName.equals(newBaseName);

    } catch (PartInitException e) {
      PropEditorXPlugin.getDefault().error("Error matching editor input", e);
      return false;
    }
  }
}
