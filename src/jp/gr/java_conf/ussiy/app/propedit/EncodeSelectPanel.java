package jp.gr.java_conf.ussiy.app.propedit;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import jp.gr.java_conf.ussiy.app.propedit.bean.Encode;
import jp.gr.java_conf.ussiy.app.propedit.util.EncodeManager;

class EncodeSelectPanel extends JPanel {

	private static final long serialVersionUID = -3161561981802069139L;

	private GridLayout gridLayout1 = new GridLayout();

	private JLabel jLabel1 = new JLabel();

	private ArrayList<JRadioButton> encodeRadioButton = new ArrayList<>();

	private ButtonGroup encodeButtonGroup = new ButtonGroup();

	private List<Encode> encodeList;

	private boolean store = true;

	public EncodeSelectPanel() {

		this(true);
	}

	public EncodeSelectPanel(boolean store) {

		this.store = store;
		try {
			jbInit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	void jbInit() throws Exception {

		jLabel1.setText(PropertiesEditor.getI18nProperty("jLabel1_Text")); //$NON-NLS-1$
		this.setLayout(gridLayout1);
		this.setBorder(BorderFactory.createEtchedBorder());
		if (!store) {
			encodeList = EncodeManager.getReadEncodeList();
		}
		for (Encode enc : encodeList) {
			JRadioButton radioButton = new JRadioButton(enc.name());
			if (enc.no() == 0) {
				radioButton.setSelected(true);
			}
			encodeRadioButton.add(radioButton);
		}
		gridLayout1.setColumns(0);
		gridLayout1.setHgap(5);
		gridLayout1.setRows(12);
		gridLayout1.setVgap(5);
		this.add(jLabel1, null);
		for (JRadioButton rb : encodeRadioButton) {
			encodeButtonGroup.add(rb);
			this.add(rb, null);
		}
	}

	public String getSelectedEncode() {

		String name = null;
		var enu = encodeButtonGroup.getElements();
		while (enu.hasMoreElements()) {
			AbstractButton button = enu.nextElement();
			if (button.isSelected()) {
				name = button.getText();
				break;
			}
		}
		for (Encode enc : encodeList) {
			if (enc.name().equals(name)) {
				return enc.code();
			}
		}
		return null;
	}
}