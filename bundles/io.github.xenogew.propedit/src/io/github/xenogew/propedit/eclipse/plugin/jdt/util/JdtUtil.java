package io.github.xenogew.propedit.eclipse.plugin.jdt.util;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Region;

/**
 * Utility class for JDT integration.
 */
public class JdtUtil {

  /**
   * Find a string literal region at the given offset.
   *
   * @param document the document to search
   * @param offset the offset within the document
   * @return the region of the string literal (excluding quotes), or null if not found
   * @throws BadLocationException if the offset is invalid
   */
  public static IRegion getStringLiteralRegion(IDocument document, int offset)
      throws BadLocationException {
    int lineNum = document.getLineOfOffset(offset);
    int lineOffset = document.getLineOffset(lineNum);
    int lineLength = document.getLineLength(lineNum);
    String source = document.get(lineOffset, lineLength);
    int relativeOffset = offset - lineOffset;

    int startIdx = -1;
    int tmp = relativeOffset - 1;
    while (tmp >= 0) {
      tmp = source.lastIndexOf("\"", tmp); //$NON-NLS-1$
      if (tmp < 0) {
        startIdx = -1;
        break;
      }
      if (tmp > 0) {
        if (source.charAt(tmp - 1) == '\\') {
          tmp--;
          continue;
        } else {
          startIdx = tmp + 1;
          break;
        }
      } else if (tmp == 0) {
        startIdx = 0 + 1;
        break;
      } else {
        startIdx = -1;
        break;
      }
    }

    if (startIdx == -1) {
      return null;
    }

    tmp = relativeOffset;
    int endIdx = -1;
    while (tmp < lineLength) {
      tmp = source.indexOf("\"", tmp); //$NON-NLS-1$
      if (tmp == -1) {
        endIdx = -1;
        break;
      }
      if (tmp > 0) {
        if (source.charAt(tmp - 1) == '\\') {
          tmp++;
          continue;
        } else {
          endIdx = tmp;
          break;
        }
      } else if (tmp == 0) {
        endIdx = 0;
        break;
      } else {
        endIdx = -1;
        break;
      }
    }

    if (endIdx == -1 || startIdx == endIdx) {
      return null;
    }

    return new Region(lineOffset + startIdx, endIdx - startIdx);
  }

  /**
   * Extract the string literal key at the given offset.
   *
   * @param document the document to search
   * @param offset the offset within the document
   * @return the string literal key, or null if not found
   * @throws BadLocationException if the offset is invalid
   */
  public static String extractKey(IDocument document, int offset) throws BadLocationException {
    IRegion region = getStringLiteralRegion(document, offset);
    if (region == null) {
      return null;
    }
    return document.get(region.getOffset(), region.getLength());
  }
}
