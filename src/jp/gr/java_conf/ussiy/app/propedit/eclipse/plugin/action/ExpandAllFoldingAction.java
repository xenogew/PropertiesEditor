package jp.gr.java_conf.ussiy.app.propedit.eclipse.plugin.action;

import java.util.Iterator;

import jp.gr.java_conf.ussiy.app.propedit.eclipse.plugin.editors.PropertiesEditor;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.projection.ProjectionAnnotationModel;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.handlers.HandlerUtil;

public class ExpandAllFoldingAction extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IEditorPart part = HandlerUtil.getActiveEditor(event);
		if (!(part instanceof PropertiesEditor editor)) {
			return null;
		}

		ProjectionAnnotationModel model = editor.getAnnotationModel();
		Iterator<Annotation> annotationIterator = model.getAnnotationIterator();
		while (annotationIterator.hasNext()) {
			model.expand(annotationIterator.next());
		}
		return null;
	}

}