package io.github.xenogew.propedit.eclipse.plugin.action;

import io.github.xenogew.propedit.eclipse.plugin.PropEditorXPlugin;
import io.github.xenogew.propedit.eclipse.plugin.util.PropertiesFileUtil;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.search.ui.ISearchQuery;
import org.eclipse.search.ui.NewSearchUI;
import org.eclipse.search.ui.text.FileTextSearchScope;
import org.eclipse.search.ui.text.TextSearchQueryProvider;
import org.eclipse.search.ui.text.TextSearchQueryProvider.TextSearchInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.texteditor.ITextEditor;

/**
 * Handler for standard "References in Workspace" (Ctrl+Shift+G) in PropEditorX.
 */
public class FindUsagesAction extends AbstractHandler {

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {
    IEditorPart editor = HandlerUtil.getActiveEditor(event);
    if (!(editor instanceof ITextEditor)) {
      return null;
    }

    ITextEditor textEditor = (ITextEditor) editor;
    ISelection selection = textEditor.getSelectionProvider().getSelection();
    if (!(selection instanceof ITextSelection)) {
      return null;
    }

    ITextSelection textSelection = (ITextSelection) selection;
    String key = PropertiesFileUtil.getPropertyKeyAtOffset(
        textEditor.getDocumentProvider().getDocument(textEditor.getEditorInput()),
        textSelection.getOffset());

    if (key == null || key.isEmpty()) {
      return null;
    }

    // Use Virtual Thread for search initiation to keep UI responsive
    Thread.ofVirtual().start(() -> {
      try {
        TextSearchQueryProvider provider = TextSearchQueryProvider.getPreferred();
        final FileTextSearchScope scope =
            FileTextSearchScope.newWorkspaceScope(new String[] {"*.java"}, false);

        TextSearchInput input = new TextSearchInput() {
          @Override
          public String getSearchText() {
            return key;
          }

          @Override
          public boolean isCaseSensitiveSearch() {
            return true;
          }

          @Override
          public boolean isRegExSearch() {
            return false;
          }

          @Override
          public FileTextSearchScope getScope() {
            return scope;
          }
        };

        ISearchQuery query = provider.createQuery(input);

        // Run the query in the background using the standard Search UI
        NewSearchUI.runQueryInBackground(query);
      } catch (CoreException e) {
        IStatus status = new Status(IStatus.ERROR, PropEditorXPlugin.PLUGIN_ID, IStatus.OK,
            "Failed to create find usages query", e);
        PropEditorXPlugin.log().log(status);
      }
    });

    return null;
  }
}
