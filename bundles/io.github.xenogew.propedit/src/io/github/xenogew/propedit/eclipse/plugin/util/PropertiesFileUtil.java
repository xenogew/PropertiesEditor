package io.github.xenogew.propedit.eclipse.plugin.util;

import io.github.xenogew.propedit.eclipse.plugin.PropEditorXPlugin;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;

public class PropertiesFileUtil {

  public static IFile[] findFileExt(IContainer container, IPath excludePath, String extension) {
    IResource[] list = null;
    try {
      list = container.members();
    } catch (CoreException e) {
      IStatus status =
          new Status(IStatus.ERROR, PropEditorXPlugin.PLUGIN_ID, IStatus.OK, e.getMessage(), e);
      ILog log = PropEditorXPlugin.getDefault().getLog();
      log.log(status);
      return new IFile[0];
    }
    if (list == null) {
      return new IFile[0];
    }
    List<IFile> fileList = new ArrayList<>();
    for (IResource resource : list) {
      if (resource instanceof IFile) {
        if (extension.equals(resource.getFileExtension())) {
          fileList.add((IFile) resource);
        }
      } else if (resource instanceof IContainer) {
        if (excludePath != null && excludePath
            .matchingFirstSegments(resource.getFullPath()) == excludePath.segmentCount()) {
          continue;
        }
        IFile[] files = findFileExt((IContainer) resource, excludePath, extension);
        for (IFile file : files) {
          fileList.add(file);
        }
      }
    }

    return fileList.toArray(new IFile[0]);
  }

  /**
   * Identifies the property key at the given offset in a properties file.
   *
   * @param document the properties file document
   * @param offset the cursor offset
   * @return the property key, or null if not on a key
   */
  public static String getPropertyKeyAtOffset(IDocument document, int offset) {
    try {
      IRegion lineRegion = document.getLineInformationOfOffset(offset);
      String line = document.get(lineRegion.getOffset(), lineRegion.getLength());
      String trimmedLine = line.trim();

      // Skip comments
      if (trimmedLine.isEmpty() || trimmedLine.startsWith("#") || trimmedLine.startsWith("!")) {
        return null;
      }

      // Find leading whitespace length
      int leadingWS = 0;
      while (leadingWS < line.length() && Character.isWhitespace(line.charAt(leadingWS))) {
        leadingWS++;
      }

      // Find the first separator (=, :, or unescaped space)
      int separatorIdx = -1;
      for (int i = leadingWS; i < line.length(); i++) {
        char c = line.charAt(i);
        if (c == '=' || c == ':') {
          separatorIdx = i;
          break;
        }
        if (Character.isWhitespace(c)) {
          // Check if it's an unescaped space
          int backslashes = 0;
          for (int j = i - 1; j >= leadingWS && line.charAt(j) == '\\'; j--) {
            backslashes++;
          }
          if (backslashes % 2 == 0) {
            separatorIdx = i;
            break;
          }
        }
      }

      int keyEnd = (separatorIdx == -1) ? line.length() : separatorIdx;
      String key = line.substring(leadingWS, keyEnd).trim();

      // Check if cursor is within the key region
      int relativeOffset = offset - lineRegion.getOffset();
      if (relativeOffset >= leadingWS && relativeOffset <= keyEnd) {
        return key;
      }

    } catch (BadLocationException e) {
      // Ignore
    }
    return null;
  }
}
