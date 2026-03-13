package jp.gr.java_conf.ussiy.app.propedit.eclipse.plugin.action;

import jp.gr.java_conf.ussiy.app.propedit.eclipse.plugin.PropertiesEditorPlugin;
import jp.gr.java_conf.ussiy.app.propedit.eclipse.plugin.editors.PropertiesEditor;
import jp.gr.java_conf.ussiy.app.propedit.eclipse.plugin.preference.PropertiesPreference;
import jp.gr.java_conf.ussiy.app.propedit.eclipse.plugin.property.PropertyUtil;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.handlers.HandlerUtil;

public class ToggleCommentAction extends AbstractHandler {

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {
    IEditorPart part = HandlerUtil.getActiveEditor(event);
    if (!(part instanceof PropertiesEditor textEditor)) {
      return null;
    }

    IPreferenceStore pStore = PropertiesEditorPlugin.getDefault().getPreferenceStore();

    IProject project = ((IFileEditorInput) textEditor.getEditorInput()).getFile().getProject();
    String commentString = PropertyUtil.getCommentChar(project,
        pStore.getString(PropertiesPreference.P_COMMENT_CHARACTER));

    ISelectionProvider selection = textEditor.getSelectionProvider();
    if (selection == null) {
      return null;
    }
    ITextSelection ts = (ITextSelection) textEditor.getSelectionProvider().getSelection();
    int startLineNum = ts.getStartLine();
    int endLineNum = ts.getEndLine();
    IDocument document = textEditor.getDocumentProvider().getDocument(textEditor.getEditorInput());

    try {
      int startPos = document.getLineOffset(startLineNum);
      StringBuilder strBuf = new StringBuilder();
      int length = 0;
      int comCnt = 0;
      int firstComCnt = 0;
      for (int lineNum = startLineNum; lineNum <= endLineNum; lineNum++) {
        int lineLen = document.getLineLength(lineNum);
        length += lineLen;
        String line =
            document.get(document.getLineOffset(lineNum), document.getLineLength(lineNum));
        if (line != null && line.startsWith(commentString)) {
          line = line.substring(1, line.length());
          if (lineNum == startLineNum) {
            firstComCnt--;
          } else {
            comCnt--;
          }
        } else if (line != null) {
          line = commentString + line;
          if (lineNum == startLineNum) {
            firstComCnt++;
          } else {
            comCnt++;
          }
        }
        strBuf.append(line);
      }
      document.replace(startPos, length, strBuf.toString());

      int selectionStartPos = ts.getOffset() + firstComCnt;
      int selectionLength = ts.getLength() + comCnt;
      textEditor.getSelectionProvider()
          .setSelection(new TextSelection(selectionStartPos, selectionLength));
    } catch (BadLocationException e) {
      IStatus status =
          new Status(IStatus.ERROR, PropertiesEditorPlugin.PLUGIN_ID, e.getMessage(), e);
      PropertiesEditorPlugin.getDefault().getLog().log(status);
    }
    return null;
  }

}
