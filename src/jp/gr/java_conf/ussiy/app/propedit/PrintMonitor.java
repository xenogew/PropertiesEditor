package jp.gr.java_conf.ussiy.app.propedit;

import java.awt.Graphics;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

class PrintMonitor implements Printable {

  private final PropertiesEditorFrame frame;

  protected PrinterJob printerJob;

  protected Printable printable;

  protected JOptionPane optionPane;

  protected JDialog statusDialog;

  PrintMonitor(PropertiesEditorFrame frame, Printable p) {

    this.frame = frame;
    printable = p;
    printerJob = PrinterJob.getPrinterJob();
    String[] options = {PropertiesEditor.getI18nProperty("KEY17")}; //$NON-NLS-1$
    optionPane = new JOptionPane("", JOptionPane.INFORMATION_MESSAGE, JOptionPane.CANCEL_OPTION, //$NON-NLS-1$
        null, options);
    statusDialog = optionPane.createDialog(null, PropertiesEditor.getI18nProperty("KEY18")); //$NON-NLS-1$
  }

  public void performPrint() throws PrinterException {

    frame.pageFormat = printerJob.validatePage(frame.pageFormat);
    printerJob.setPrintable(this, frame.pageFormat);

    optionPane.setMessage(PropertiesEditor.getI18nProperty("optionPane_Message")); //$NON-NLS-1$
    Thread t = new Thread(() -> {
      statusDialog.setVisible(true);
      if (optionPane.getValue() != JOptionPane.UNINITIALIZED_VALUE) {
        printerJob.cancel();
      }
    });
    t.start();
    printerJob.print();
    statusDialog.setVisible(false);
  }

  @Override
  public int print(Graphics g, PageFormat pf, int index) throws PrinterException {

    return printable.print(g, pf, index);
  }
}
