package jp.gr.java_conf.ussiy.app.propedit;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

/**
 * 
 * @author Sou Miyazaki
 *  
 */
class UndoAction extends AbstractAction {

	private final PropertiesEditorFrame frame;

	/**
	 * 
	 * @since 1.0.0
	 */
	UndoAction(PropertiesEditorFrame frame) {

		super("Undo"); //$NON-NLS-1$
		this.frame = frame;
		setEnabled(false);
	}

	/**
	 * 
	 * @param e
	 * @since 1.0.0
	 */
	@Override
	public void actionPerformed(ActionEvent e) {

		frame.undo.undo();
		update();
		frame.redoAction.update();
	}

	/**
	 * 
	 * @since 1.0.0
	 */
	protected void update() {

		if (frame.undo.canUndo()) {
			setEnabled(true);
			putValue(Action.NAME, frame.undo.getUndoPresentationName());
		} else {
			setEnabled(false);
			putValue(Action.NAME, "Undo"); //$NON-NLS-1$
		}
	}
}
