package jp.gr.java_conf.ussiy.app.propedit;

import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;

/**
 * 
 * @author Sou Miyazaki
 *  
 */
class UndoHandler implements UndoableEditListener {

	private final PropertiesEditorFrame frame;

	UndoHandler(PropertiesEditorFrame frame) {
		this.frame = frame;
	}

	/**
	 * 
	 * @param e
	 * @since 1.0.0
	 */
	@Override
	public void undoableEditHappened(UndoableEditEvent e) {

		frame.undo.addEdit(e.getEdit());
		frame.undoAction.update();
		frame.redoAction.update();
	}
}
