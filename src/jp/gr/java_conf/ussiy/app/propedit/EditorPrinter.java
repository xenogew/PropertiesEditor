package jp.gr.java_conf.ussiy.app.propedit;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.print.PageFormat;
import java.awt.print.Printable;

class EditorPrinter implements Printable {

	private final PropertiesEditorFrame frame;

	EditorPrinter(PropertiesEditorFrame frame) {
		this.frame = frame;
	}

	@Override
	public int print(Graphics g, PageFormat pf, int index) {

		if (index == 0) {

			g.translate((int) (pf.getImageableX()), (int) (pf.getImageableY()));

			//        Graphics2D g2d = (Graphics2D)g;
			//        double pageWidth = pf.getImageableWidth();
			//        double panelWidth = editTextArea.getWidth();
			//        double scaleX = pageWidth / panelWidth;
			//        g2d.scale(scaleX,scaleX);

			g.setColor(Color.BLUE);
			g.fillRect(0, 0, 100, 100);

			frame.editTextArea.setDoubleBuffered(false);
			frame.editTextArea.paintAll(g);
			frame.editTextArea.setDoubleBuffered(true);

			return Printable.PAGE_EXISTS;
		}
		return Printable.NO_SUCH_PAGE;
	}
}
