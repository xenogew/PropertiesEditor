package jp.gr.java_conf.ussiy.app.propedit;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;

/**
 * 
 * @author Sou Miyazaki
 * 
 */
class RedoAction extends AbstractAction {

  private final PropertiesEditorFrame frame;

  /**
   * 
   * @since 1.0.0
   */
  RedoAction(PropertiesEditorFrame frame) {

    super("Redo"); //$NON-NLS-1$
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

    frame.undo.redo();
    update();
    frame.undoAction.update();
  }

  /**
   * 
   * @since 1.0.0
   */
  protected void update() {

    if (frame.undo.canRedo()) {
      setEnabled(true);
      putValue(Action.NAME, frame.undo.getRedoPresentationName());
    } else {
      setEnabled(false);
      putValue(Action.NAME, "Redo"); //$NON-NLS-1$
    }
  }
}
