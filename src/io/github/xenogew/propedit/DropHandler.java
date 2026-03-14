package io.github.xenogew.propedit;

import io.github.xenogew.propedit.util.EncodeManager;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.File;
import java.io.IOException;

/**
 * 
 * @author Sou Miyazaki
 * 
 */
class DropHandler implements DropTargetListener {

  private final PropertiesEditorFrame frame;

  DropHandler(PropertiesEditorFrame frame) {
    this.frame = frame;
  }

  /**
   * 
   * @param e
   * @since 1.0.0
   */
  @Override
  public void dragEnter(DropTargetDragEvent e) {

    e.acceptDrag(DnDConstants.ACTION_COPY_OR_MOVE);
  }

  /**
   * 
   * @param e
   * @since 1.0.0
   */
  @Override
  public void dragExit(DropTargetEvent e) {

  }

  /**
   * 
   * @param e
   * @since 1.0.0
   */
  @Override
  public void drop(DropTargetDropEvent e) {

    try {
      Transferable tr = e.getTransferable();
      if (tr.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
        e.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
        @SuppressWarnings("unchecked")
        java.util.List<File> l =
            (java.util.List<File>) tr.getTransferData(DataFlavor.javaFileListFlavor);
        String filepath = l.get(0).toString();
        if (!frame.checkSave()) {
          return;
        }
        frame.openFile(new File(filepath), EncodeManager.AUTO);
        e.getDropTargetContext().dropComplete(true);
      } else {
        e.rejectDrop();
      }
    } catch (IOException io) {
      e.rejectDrop();
    } catch (UnsupportedFlavorException ufe) {
      e.rejectDrop();
    }
  }

  /**
   * 
   * @param e
   * @since 1.0.0
   */
  @Override
  public void dropActionChanged(DropTargetDragEvent e) {

  }

  /**
   * 
   * @param e
   * @since 1.0.0
   */
  @Override
  public void dragOver(DropTargetDragEvent e) {

  }
}
