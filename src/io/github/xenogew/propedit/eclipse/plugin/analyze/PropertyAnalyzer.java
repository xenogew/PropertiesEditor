package io.github.xenogew.propedit.eclipse.plugin.analyze;

import org.eclipse.jface.text.IDocument;

public class PropertyAnalyzer {

  public static PropertyElement[] analyze(IDocument document) throws Exception {
    return null;
  }

}


class ElementListCreator {

  void addElement(int keyStartPos, int keyLength, String key, int valueStartPos, int valueLength,
      String value, int elementStartLine, int elementLineCount) {
    var element = PropertyElement.builder().keyStartPos(keyStartPos).keyLength(keyLength).key(key)
        .valueStartPos(valueStartPos).valueLength(valueLength).value(value)
        .elementStartLine(elementStartLine).elementLineCount(elementLineCount).build();
  }
}
