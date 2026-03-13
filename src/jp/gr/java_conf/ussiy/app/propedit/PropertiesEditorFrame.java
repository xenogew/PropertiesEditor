/*****************************************************
 * It is the main frame of application.
 *
 *   @author  Sou Miyazaki
 *
 ****************************************************/
package jp.gr.java_conf.ussiy.app.propedit;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.dnd.DropTarget;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.UndoManager;

import jp.gr.java_conf.ussiy.app.propedit.bean.AppSetting;
import jp.gr.java_conf.ussiy.app.propedit.util.AlreadyFileLockException;
import jp.gr.java_conf.ussiy.app.propedit.util.EncodeChanger;
import jp.gr.java_conf.ussiy.app.propedit.util.EncodeManager;
import jp.gr.java_conf.ussiy.app.propedit.util.FileOpener;
import jp.gr.java_conf.ussiy.swing.JFontChooserDialog;
import jp.gr.java_conf.ussiy.swing.filechooser.PropertiesFileFilter;

/**
 * It is the main frame of application.
 * 
 * @author Sou Miyazaki
 *  
 */
public class PropertiesEditorFrame extends JFrame implements ActionListener {

	private static final long serialVersionUID = 3204064610764930004L;

	private static final Logger LOG = Logger.getLogger(PropertiesEditorFrame.class.getName());

	UndoAction undoAction;

	/**
	 */
	RedoAction redoAction;

	/**
	 */
	protected UndoManager undo = new UndoManager();

	/**
	 */
	protected UndoableEditListener undoHandler;

	/**
	 */
	FileOpener opFp = null;

	/**
	 */
	private String key = null;

	/**
	 */
	private ImageIcon openImage16;

	/**
	 */
	private ImageIcon closeImage16;

	/**
	 */
	private ImageIcon infoImage16;

	/**
	 */
	private ImageIcon saveImage16;

	/**
	 */
	private ImageIcon searchImage16;

	/**
	 */
	private ImageIcon searchNextImage16;

	/**
	 */
	private ImageIcon newImage16;

	/**
	 */
	private ImageIcon undoImage16;

	/**
	 */
	private ImageIcon pasteImage16;

	/**
	 */
	private ImageIcon replaceImage16;

	/**
	 */
	private ImageIcon deleteImage16;

	/**
	 */
	private ImageIcon cutImage16;

	/**
	 */
	private ImageIcon copyImage16;

	/**
	 */
	private ImageIcon lineNumImage16;

	/**
	 */
	private Image imgIcon;

	/**
	 */
	private JPanel contentPane;

	/**
	 */
	private JMenuBar jMenuBar1 = new JMenuBar();

	/**
	 */
	private JMenu fileMenu = new JMenu();

	/**
	 */
	private JMenuItem closeMenuItem = new JMenuItem();

	/**
	 */
	private JMenu helpMenu = new JMenu();

	/**
	 */
	private JMenuItem versionMenuItem = new JMenuItem();

	/**
	 */
	private JToolBar toolBar = new JToolBar();

	/**
	 */
	private JButton newButton = new JButton();

	/**
	 */
	private JButton openButton = new JButton();

	/**
	 */
	private JButton closeButton = new JButton();

	/**
	 */
	private BorderLayout borderLayout1 = new BorderLayout();

	/**
	 */
	private JMenuItem newMenuItem = new JMenuItem();

	/**
	 */
	private JMenuItem openMenuItem = new JMenuItem();

	/**
	 */
	private JMenuItem unicodeSaveMenuItem = new JMenuItem();

	/**
	 */
	private JMenu editMenu = new JMenu();

	/**
	 */
	private JMenu dispMenu = new JMenu();

	/**
	 */
	private JMenuItem undoMenuItem = new JMenuItem();

	/**
	 */
	private JMenuItem redoMenuItem = new JMenuItem();

	/**
	 */
	private JMenuItem cutMenuItem = new JMenuItem();

	/**
	 */
	private JMenuItem copyMenuItem = new JMenuItem();

	/**
	 */
	private JMenuItem pasteMenuItem = new JMenuItem();

	/**
	 */
	private JMenuItem deleteMenuItem = new JMenuItem();

	/**
	 */
	private JMenuItem findMenuItem = new JMenuItem();

	/**
	 */
	private JMenuItem nextFindMenuItem = new JMenuItem();

	/**
	 */
	private JMenuItem replaceMenuItem = new JMenuItem();

	/**
	 */
	private JMenuItem selectAllMenuItem = new JMenuItem();

	/**
	 */
	private JCheckBoxMenuItem wordWrapCheckBoxMenuItem = new JCheckBoxMenuItem();

	/**
	 */
	private JCheckBoxMenuItem lineNumberCheckBoxMenuItem = new JCheckBoxMenuItem();

	/**
	 */
	private JCheckBoxMenuItem toolbarCheckBoxMenuItem = new JCheckBoxMenuItem();

	/**
	 */
	private JButton cutButton = new JButton();

	/**
	 */
	private JButton copyButton = new JButton();

	/**
	 */
	private JButton pasteButton = new JButton();

	/**
	 */
	private JButton undoButton = new JButton();

	/**
	 */
	private JButton findButton = new JButton();

	/**
	 */
	private JButton nextFindButton = new JButton();

	/**
	 */
	private JButton replaceButton = new JButton();

	/**
	 */
	private JToggleButton showLineNumberButton = new JToggleButton();

	/**
	 */
	private JButton infomationButton = new JButton();

	/**
	 */
	private JScrollPane editScrollPane = new JScrollPane();

	/**
	 */
	JTextArea editTextArea = new JTextArea();

	/**
	 */
	private JTextArea lineNumberTextArea = new JTextArea();

	/**
	 */
	private JButton saveUnicodeButton = new JButton();

	/**
	 */
	private JMenuItem showUnicodeMenuItem = new JMenuItem();

	private JMenuItem fontSelectMenuItem = new JMenuItem();

	private JMenuItem printMenuItem = new JMenuItem();

	private JMenuItem printLayoutMenuItem = new JMenuItem();

	PageFormat pageFormat;

	JMenu jMenu1 = new JMenu();

	JRadioButtonMenuItem[] lfRadioMenuItem;

	ButtonGroup lnfButtonGroup = new ButtonGroup();

	private JMenuItem unicodeSaveWithNameMenuItem = new JMenuItem();

	/**
	 * 
	 * @since 1.0.0
	 */
	public PropertiesEditorFrame(String path) {

		this();
		File file = new File(path);
		if (file.exists() && file.isFile()) {
			this.openFile(file, EncodeManager.AUTO);
		}
	}

	public PropertiesEditorFrame() {

		undoAction = new UndoAction(this);
		redoAction = new RedoAction(this);
		undoHandler = new UndoHandler(this);
		enableEvents(AWTEvent.WINDOW_EVENT_MASK);
		try {
			jbInit();
			addShutdownHook();
		} catch (Exception e) {
			LOG.log(Level.SEVERE, e.getMessage(), e);
		}
	}

	private void addShutdownHook() {

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			if (!checkSave()) {
				return;
			}
		}));
	}

	/**
	 * 
	 * @since 1.0.0
	 */
	private void initIcon() {

		openImage16 = new ImageIcon(PropertiesEditor.class.getResource("resource/Open16.gif")); //$NON-NLS-1$
		closeImage16 = new ImageIcon(PropertiesEditor.class.getResource("resource/Stop16.gif")); //$NON-NLS-1$
		infoImage16 = new ImageIcon(PropertiesEditor.class.getResource("resource/Information16.gif")); //$NON-NLS-1$
		searchImage16 = new ImageIcon(PropertiesEditor.class.getResource("resource/Find16.gif")); //$NON-NLS-1$
		searchNextImage16 = new ImageIcon(PropertiesEditor.class.getResource("resource/FindAgain16.gif")); //$NON-NLS-1$
		saveImage16 = new ImageIcon(PropertiesEditor.class.getResource("resource/Save16.gif")); //$NON-NLS-1$
		newImage16 = new ImageIcon(PropertiesEditor.class.getResource("resource/New16.gif")); //$NON-NLS-1$
		undoImage16 = new ImageIcon(PropertiesEditor.class.getResource("resource/Undo16.gif")); //$NON-NLS-1$
		pasteImage16 = new ImageIcon(PropertiesEditor.class.getResource("resource/Paste16.gif")); //$NON-NLS-1$
		replaceImage16 = new ImageIcon(PropertiesEditor.class.getResource("resource/Replace16.gif")); //$NON-NLS-1$
		deleteImage16 = new ImageIcon(PropertiesEditor.class.getResource("resource/Delete16.gif")); //$NON-NLS-1$
		cutImage16 = new ImageIcon(PropertiesEditor.class.getResource("resource/Cut16.gif")); //$NON-NLS-1$
		copyImage16 = new ImageIcon(PropertiesEditor.class.getResource("resource/Copy16.gif")); //$NON-NLS-1$
		lineNumImage16 = new ImageIcon(PropertiesEditor.class.getResource("resource/ColumnInsertBefore16.gif")); //$NON-NLS-1$
		imgIcon = Toolkit.getDefaultToolkit().createImage(PropertiesEditor.class.getResource("resource/pe_16.gif")); //$NON-NLS-1$
	}

	/**
	 * 
	 * @since 1.0.0
	 */
	private void jbInit() throws Exception {

		initIcon();
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.setIconImage(imgIcon);
		new DropTarget(this, new DropHandler(this));
		new DropTarget(lineNumberTextArea, new DropHandler(this));
		new DropTarget(editTextArea, new DropHandler(this));
		contentPane = (JPanel) this.getContentPane();
		contentPane.setLayout(borderLayout1);
		this.setSize(new Dimension(640, 480));
		this.setTitle(PropertiesEditor.getI18nProperty("Title")); //$NON-NLS-1$
		var lfInfo = UIManager.getInstalledLookAndFeels();
		lfRadioMenuItem = new JRadioButtonMenuItem[lfInfo.length];
		for (int cnt = 0; cnt < lfInfo.length; cnt++) {
			lfRadioMenuItem[cnt] = new JRadioButtonMenuItem(lfInfo[cnt].getName());
			lfRadioMenuItem[cnt].addActionListener(this);
			lnfButtonGroup.add(lfRadioMenuItem[cnt]);
		}
		lineNumberTextArea.setFont(new java.awt.Font("dialog", 0, 14)); //$NON-NLS-1$
		lineNumberTextArea.setBackground(new Color(229, 247, 255));
		lineNumberTextArea.setForeground(new Color(184, 158, 210));
		lineNumberTextArea.setEditable(false);
		//    lineNumberTextArea.setDoubleBuffered(true);
		//    lineNumberTextArea.setPreferredSize(new Dimension(0, 19));
		fileMenu.setText(PropertiesEditor.getI18nProperty("fileMenu_Text")); //$NON-NLS-1$
		closeMenuItem.setIcon(closeImage16);
		closeMenuItem.setText(PropertiesEditor.getI18nProperty("closeMenuItem_Text")); //$NON-NLS-1$
		closeMenuItem.addActionListener(e -> close_actionPerformed(e));
		helpMenu.setText(PropertiesEditor.getI18nProperty("helpMenu_Text")); //$NON-NLS-1$
		versionMenuItem.setIcon(infoImage16);
		versionMenuItem.setText(PropertiesEditor.getI18nProperty("versionMenuItem_Text")); //$NON-NLS-1$
		versionMenuItem.addActionListener(e -> version_actionPerformed(e));
		newMenuItem.setIcon(newImage16);
		newMenuItem.setText(PropertiesEditor.getI18nProperty("newMenuItem_Text")); //$NON-NLS-1$
		newMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke('N', java.awt.event.KeyEvent.CTRL_DOWN_MASK, false));
		newMenuItem.addActionListener(e -> new_actionPerformed(e));
		openMenuItem.setIcon(openImage16);
		openMenuItem.setText(PropertiesEditor.getI18nProperty("openMenuItem_Text")); //$NON-NLS-1$
		openMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke('O', java.awt.event.KeyEvent.CTRL_DOWN_MASK, false));
		openMenuItem.addActionListener(e -> open_actionPerformed(e));
		unicodeSaveMenuItem.setIcon(saveImage16);
		unicodeSaveMenuItem.setText(PropertiesEditor.getI18nProperty("unicodeSaveMenuItem_Text")); //$NON-NLS-1$
		unicodeSaveMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke('S', java.awt.event.KeyEvent.CTRL_DOWN_MASK, false));
		unicodeSaveMenuItem.addActionListener(e -> saveUnicode_actionPerformed(e));
		editMenu.setText(PropertiesEditor.getI18nProperty("editMenu_Text")); //$NON-NLS-1$
		dispMenu.setText(PropertiesEditor.getI18nProperty("dispMenu_Text")); //$NON-NLS-1$
		undoMenuItem.setIcon(undoImage16);
		undoMenuItem.setText(PropertiesEditor.getI18nProperty("undoMenuItem_Text")); //$NON-NLS-1$
		undoMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke('Z', java.awt.event.KeyEvent.CTRL_DOWN_MASK, false));
		undoMenuItem.addActionListener(e -> undo_actionPerformed(e));
		redoMenuItem.setText(PropertiesEditor.getI18nProperty("redoMenuItem_Text")); //$NON-NLS-1$
		redoMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke('Y', java.awt.event.KeyEvent.CTRL_DOWN_MASK, false));
		redoMenuItem.addActionListener(e -> redo_actionPerformed(e));
		cutMenuItem.setIcon(cutImage16);
		cutMenuItem.setText(PropertiesEditor.getI18nProperty("cutMenuItem_Text")); //$NON-NLS-1$
		cutMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke('X', java.awt.event.KeyEvent.CTRL_DOWN_MASK, false));
		cutMenuItem.addActionListener(e -> cut_actionPerformed(e));
		copyMenuItem.setIcon(copyImage16);
		copyMenuItem.setText(PropertiesEditor.getI18nProperty("copyMenuItem_Text")); //$NON-NLS-1$
		copyMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke('C', java.awt.event.KeyEvent.CTRL_DOWN_MASK, false));
		copyMenuItem.addActionListener(e -> copy_actionPerformed(e));
		pasteMenuItem.setIcon(pasteImage16);
		pasteMenuItem.setText(PropertiesEditor.getI18nProperty("pasteMenuItem_Text")); //$NON-NLS-1$
		pasteMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke('V', java.awt.event.KeyEvent.CTRL_DOWN_MASK, false));
		pasteMenuItem.addActionListener(e -> paste_actionPerformed(e));
		deleteMenuItem.setIcon(deleteImage16);
		deleteMenuItem.setText(PropertiesEditor.getI18nProperty("deleteMenuItem_Text")); //$NON-NLS-1$
		deleteMenuItem.addActionListener(e -> delete_actionPerformed(e));
		findMenuItem.setIcon(searchImage16);
		findMenuItem.setText(PropertiesEditor.getI18nProperty("findMenuItem_Text")); //$NON-NLS-1$
		findMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke('F', java.awt.event.KeyEvent.CTRL_DOWN_MASK, false));
		findMenuItem.addActionListener(e -> find_actionPerformed(e));
		nextFindMenuItem.setIcon(searchNextImage16);
		nextFindMenuItem.setText(PropertiesEditor.getI18nProperty("nextFindMenuItem_Text")); //$NON-NLS-1$
		nextFindMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0, false));
		nextFindMenuItem.addActionListener(e -> nextFind_actionPerformed(e));
		replaceMenuItem.setIcon(replaceImage16);
		replaceMenuItem.setText(PropertiesEditor.getI18nProperty("replaceMenuItem_Text")); //$NON-NLS-1$
		replaceMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke('R', java.awt.event.KeyEvent.CTRL_DOWN_MASK, false));
		replaceMenuItem.addActionListener(e -> replace_actionPerformed(e));
		selectAllMenuItem.setText(PropertiesEditor.getI18nProperty("selectAllMenuItem_Text")); //$NON-NLS-1$
		selectAllMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke('A', java.awt.event.KeyEvent.CTRL_DOWN_MASK, false));
		selectAllMenuItem.addActionListener(e -> selectAllMenuItem_actionPerformed(e));
		toolBar.setFloatable(false);
		wordWrapCheckBoxMenuItem.setEnabled(false);
		wordWrapCheckBoxMenuItem.setText(PropertiesEditor.getI18nProperty("wordWrapCheckBoxMenuItem_Text")); //$NON-NLS-1$
		lineNumberCheckBoxMenuItem.setIcon(lineNumImage16);
		lineNumberCheckBoxMenuItem.setText(PropertiesEditor.getI18nProperty("lineNumberCheckBoxMenuItem_Text")); //$NON-NLS-1$
		lineNumberCheckBoxMenuItem.addActionListener(e -> lineNumberCheckBoxMenuItem_actionPerformed(e));
		toolbarCheckBoxMenuItem.setText(PropertiesEditor.getI18nProperty("toolbarCheckBoxMenuItem_Text")); //$NON-NLS-1$
		toolbarCheckBoxMenuItem.addActionListener(e -> toolbarCheckBoxMenuItem_actionPerformed(e));
		newButton.setToolTipText(PropertiesEditor.getI18nProperty("newMenuItem_Text")); //$NON-NLS-1$
		newButton.setFocusPainted(true);
		newButton.setIcon(newImage16);
		newButton.addActionListener(e -> new_actionPerformed(e));
		openButton.setToolTipText(PropertiesEditor.getI18nProperty("openMenuItem_Text")); //$NON-NLS-1$
		openButton.setIcon(openImage16);
		openButton.addActionListener(e -> open_actionPerformed(e));
		closeButton.setToolTipText(PropertiesEditor.getI18nProperty("closeMenuItem_Text")); //$NON-NLS-1$
		closeButton.setIcon(closeImage16);
		closeButton.addActionListener(e -> close_actionPerformed(e));
		cutButton.setToolTipText(PropertiesEditor.getI18nProperty("cutMenuItem_Text")); //$NON-NLS-1$
		cutButton.setIcon(cutImage16);
		cutButton.setText(""); //$NON-NLS-1$
		cutButton.addActionListener(e -> cut_actionPerformed(e));
		pasteButton.setToolTipText(PropertiesEditor.getI18nProperty("pasteMenuItem_Text")); //$NON-NLS-1$
		pasteButton.setIcon(pasteImage16);
		pasteButton.setText(""); //$NON-NLS-1$
		pasteButton.addActionListener(e -> paste_actionPerformed(e));
		copyButton.setToolTipText(PropertiesEditor.getI18nProperty("copyMenuItem_Text")); //$NON-NLS-1$
		copyButton.setIcon(copyImage16);
		copyButton.setText(""); //$NON-NLS-1$
		copyButton.addActionListener(e -> copy_actionPerformed(e));
		findButton.setToolTipText(PropertiesEditor.getI18nProperty("findButton_ToolTipText")); //$NON-NLS-1$
		findButton.setIcon(searchImage16);
		findButton.setText(""); //$NON-NLS-1$
		findButton.addActionListener(e -> find_actionPerformed(e));
		undoButton.setToolTipText(PropertiesEditor.getI18nProperty("undoMenuItem_Text")); //$NON-NLS-1$
		undoButton.setIcon(undoImage16);
		undoButton.setText(""); //$NON-NLS-1$
		undoButton.addActionListener(e -> undo_actionPerformed(e));
		nextFindButton.setToolTipText(PropertiesEditor.getI18nProperty("nextFindMenuItem_Text")); //$NON-NLS-1$
		nextFindButton.setIcon(searchNextImage16);
		nextFindButton.setText(""); //$NON-NLS-1$
		nextFindButton.addActionListener(e -> nextFind_actionPerformed(e));
		replaceButton.setToolTipText(PropertiesEditor.getI18nProperty("replaceMenuItem_Text")); //$NON-NLS-1$
		replaceButton.setIcon(replaceImage16);
		replaceButton.setText(""); //$NON-NLS-1$
		replaceButton.addActionListener(e -> replace_actionPerformed(e));
		infomationButton.setToolTipText(PropertiesEditor.getI18nProperty("versionMenuItem_Text")); //$NON-NLS-1$
		infomationButton.setIcon(infoImage16);
		infomationButton.addActionListener(e -> version_actionPerformed(e));
		showLineNumberButton.setToolTipText(PropertiesEditor.getI18nProperty("showLineNumberButton_ToolTipText")); //$NON-NLS-1$
		showLineNumberButton.setIcon(lineNumImage16);
		showLineNumberButton.setText(""); //$NON-NLS-1$
		showLineNumberButton.addActionListener(e -> showLineNumberButton_actionPerformed(e));
		editTextArea.setFont(new java.awt.Font("Dialog", 0, 14)); //$NON-NLS-1$
		editTextArea.setText(""); //$NON-NLS-1$
		editTextArea.addKeyListener(new java.awt.event.KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent e) {

				editTextArea_keyReleased(e);
			}
		});
		saveUnicodeButton.setToolTipText(PropertiesEditor.getI18nProperty("saveUnicodeButton_ToolTipText")); //$NON-NLS-1$
		saveUnicodeButton.setIcon(saveImage16);
		saveUnicodeButton.setText(""); //$NON-NLS-1$
		saveUnicodeButton.addActionListener(e -> saveUnicode_actionPerformed(e));
		showUnicodeMenuItem.setText(PropertiesEditor.getI18nProperty("showUnicodeMenuItem_Text")); //$NON-NLS-1$
		showUnicodeMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke('U', java.awt.event.KeyEvent.CTRL_DOWN_MASK, false));
		showUnicodeMenuItem.addActionListener(e -> showUnicodeMenuItem_actionPerformed(e));
		fontSelectMenuItem.addActionListener(e -> fontSelectMenuItem_actionPerformed(e));
		fontSelectMenuItem.setText(PropertiesEditor.getI18nProperty("fontSelectMenuItem_Text")); //$NON-NLS-1$
		printMenuItem.setRequestFocusEnabled(true);
		printMenuItem.setText(PropertiesEditor.getI18nProperty("printMenuItem_Text")); //$NON-NLS-1$
		printMenuItem.addActionListener(e -> printMenuItem_actionPerformed(e));
		printLayoutMenuItem.setText(PropertiesEditor.getI18nProperty("printLayoutMenuItem_Text")); //$NON-NLS-1$
		printLayoutMenuItem.addActionListener(e -> printLayoutMenuItem_actionPerformed(e));
		jMenu1.setText(PropertiesEditor.getI18nProperty("jMenu1_Text")); //$NON-NLS-1$
		unicodeSaveWithNameMenuItem.setIcon(saveImage16);
		unicodeSaveWithNameMenuItem.setText(PropertiesEditor.getI18nProperty("unicodeSaveWithNameMenuItem_Text")); //$NON-NLS-1$
		unicodeSaveWithNameMenuItem.addActionListener(e -> unicodeSaveWithNameMenuItem_actionPerformed(e));
		toolBar.add(newButton);
		toolBar.add(openButton);
		toolBar.add(saveUnicodeButton, null);
		toolBar.add(closeButton);
		toolBar.addSeparator();
		toolBar.add(cutButton, null);
		toolBar.add(copyButton, null);
		toolBar.add(pasteButton, null);
		toolBar.add(undoButton, null);
		toolBar.addSeparator();
		toolBar.add(findButton, null);
		toolBar.add(nextFindButton, null);
		toolBar.add(replaceButton, null);
		toolBar.addSeparator();
		toolBar.add(showLineNumberButton, null);
		toolBar.addSeparator();
		toolBar.add(infomationButton, null);
		fileMenu.add(newMenuItem);
		fileMenu.add(openMenuItem);
		fileMenu.addSeparator();
		fileMenu.add(unicodeSaveMenuItem);
		fileMenu.add(unicodeSaveWithNameMenuItem);
		fileMenu.addSeparator();
		fileMenu.add(printLayoutMenuItem);
		fileMenu.add(printMenuItem);
		fileMenu.addSeparator();
		fileMenu.add(closeMenuItem);
		helpMenu.add(versionMenuItem);
		jMenuBar1.add(fileMenu);
		jMenuBar1.add(editMenu);
		jMenuBar1.add(dispMenu);
		jMenuBar1.add(helpMenu);
		this.setJMenuBar(jMenuBar1);
		contentPane.add(toolBar, BorderLayout.NORTH);
		contentPane.add(editScrollPane, BorderLayout.CENTER);
		editScrollPane.getViewport().add(editTextArea, null);
		editTextArea.getDocument().addUndoableEditListener(undoHandler);
		editMenu.add(undoMenuItem);
		editMenu.add(redoMenuItem);
		editMenu.addSeparator();
		editMenu.add(cutMenuItem);
		editMenu.add(copyMenuItem);
		editMenu.add(pasteMenuItem);
		editMenu.add(deleteMenuItem);
		editMenu.addSeparator();
		editMenu.add(findMenuItem);
		editMenu.add(nextFindMenuItem);
		editMenu.add(replaceMenuItem);
		editMenu.addSeparator();
		editMenu.add(selectAllMenuItem);
		dispMenu.add(jMenu1);
		dispMenu.add(showUnicodeMenuItem);
		dispMenu.add(wordWrapCheckBoxMenuItem);
		dispMenu.add(lineNumberCheckBoxMenuItem);
		dispMenu.add(toolbarCheckBoxMenuItem);
		dispMenu.add(fontSelectMenuItem);
		for (var item : lfRadioMenuItem) {
			jMenu1.add(item);
		}

		// Initialization of application
		// A setting file is read and each function is set up.
		initialize();
	}

	/**
	 * 
	 * @since 1.0.0
	 */
	private void initialize() {

		// Initialization processing
		AppSetting setting = AppSetting.getInstance();
		try {
			// Reading of a setting file
			setting.loadSetting();
		} catch (Exception e) {
			// In reading failure, it starts by default setup.
		}
		// A setup of each option
		toolBar.setVisible(setting.isShowToolBarFlag());
		toolbarCheckBoxMenuItem.setSelected(setting.isShowToolBarFlag());
		if (setting.isWordWrapFlag()) {
			wordWrapCheckBoxMenuItem.setSelected(true);
		} else {
			wordWrapCheckBoxMenuItem.setSelected(false);
		}
		if (setting.isShowLineNumberFlag()) {
			showLineNumber();
			lineNumberCheckBoxMenuItem.setSelected(true);
			showLineNumberButton.setSelected(true);
		} else {
			hideLineNumber();
			lineNumberCheckBoxMenuItem.setSelected(false);
			showLineNumberButton.setSelected(false);
		}
		if (setting.getFramesize() != null) {
			this.setSize(setting.getFramesize());
		}
		if (setting.getFont() != null) {
			editTextArea.setFont(setting.getFont());
			lineNumberTextArea.setFont(setting.getFont());
		}
		if (setting.getForegroundColor() != null) {
			editTextArea.setForeground(setting.getForegroundColor());
		}
		if (setting.getBackgroundColor() != null) {
			editTextArea.setBackground(setting.getBackgroundColor());
		}
		if (setting.getLookAndFeelClass() == null) {
			setting.setLookAndFeelClass(UIManager.getSystemLookAndFeelClassName());
			try {
				UIManager.setLookAndFeel(setting.getLookAndFeelClass());
				SwingUtilities.updateComponentTreeUI(this);
			} catch (Exception ex) {
			}
		} else {
			boolean lfFlag = false;
			var lfInfo = UIManager.getInstalledLookAndFeels();
			OUT : for (var lf : lfInfo) {
				if (setting.getLookAndFeelClass().equals(lf.getClassName())) {
					for (var menuItem : lfRadioMenuItem) {
						if (lf.getName().equals(menuItem.getText())) {
							menuItem.setSelected(true);
							lfFlag = true;
							break OUT;
						}
					}
				}
			}
			if (!lfFlag) {
				OUT : for (var lf : lfInfo) {
					if (lf.getClassName().equals(UIManager.getSystemLookAndFeelClassName())) {
						for (var menuItem : lfRadioMenuItem) {
							if (lf.getName().equals(menuItem.getText())) {
								menuItem.setSelected(true);
								setting.setLookAndFeelClass(lf.getClassName());
								break OUT;
							}
						}
					}
				}
			}
		}
	}

	/**
	 * 
	 * @param e
	 * @since 1.0.0
	 */
	public void close_actionPerformed(ActionEvent e) {

		// It is confirmed whether the data displayed now is saved.
		// When not saved, preservation is urged and preservation is performed.
		// In no cases of cancellation, it carries out.
		if (!checkSave()) {
			return;
		}
		// Each setup of application is stored in a preservation instance.
		AppSetting setting = AppSetting.getInstance();
		setting.setShowLineNumberFlag(lineNumberCheckBoxMenuItem.isSelected());
		setting.setWordWrapFlag(wordWrapCheckBoxMenuItem.isSelected());
		setting.setShowToolBarFlag(toolbarCheckBoxMenuItem.isSelected());
		setting.setFramesize(new Dimension(this.getSize()));
		setting.setFont(editTextArea.getFont());
		setting.setForegroundColor(editTextArea.getForeground());
		setting.setBackgroundColor(editTextArea.getBackground());
		try {
			// Preservation processing of a setup
			setting.saveSetting();
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, PropertiesEditor.getI18nProperty("KEY2")); //$NON-NLS-1$
		}
		this.dispose();
		//    System.exit(0);
	}

	/**
	 * 
	 * @param e
	 * @since 1.0.0
	 */
	protected void processWindowEvent(WindowEvent e) {

		super.processWindowEvent(e);
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			// Processing when the "x" button of a window is pushed
			close_actionPerformed(null);
		}
	}

	/**
	 * 
	 * @param e
	 * @since 1.0.0
	 */
	void new_actionPerformed(ActionEvent e) {

		var thread = new Thread(() -> {
			var frame = new PropertiesEditorFrame();
			frame.validate();
			var parentLocation = PropertiesEditorFrame.this.getLocation();
			frame.setLocation((int) parentLocation.getX() + 20, (int) parentLocation.getY() + 20);
			frame.setVisible(true);
		});
		thread.start();
	}

	/**
	 * 
	 * @since 1.0.0
	 */
	private void selectOpenFile() {

		// A file is made to choose.
		var jFc = new JSelectCodeFileChooser();
		// A file filter is added.
		jFc.setFileFilter(new PropertiesFileFilter());

		if (opFp != null) {
			jFc.setSelectedFile(opFp);
		}
		if (JFileChooser.APPROVE_OPTION == jFc.showOpenDialog(this)) {
			// Preservation processing is performed when a preservation button
			// is pushed.
			openFile(jFc.getSelectedFile(), jFc.getReadCode());
		}
	}

	/**
	 * 
	 * @param file
	 * @param code
	 * @since 1.0.0
	 */
	void openFile(File file, String code) {

		// Open file information is evacuated.
		var opFpBuk = opFp;
		try {
			opFp = new FileOpener(file);
			// Reading of a file
			opFp.read(code);
			// The read data of a file is displayed.
			// The Unicode reference character is changed at this time.
			editTextArea.setText(EncodeChanger.unicodeEsc2Unicode(opFp.getText()));

			// A line number is united.
			ajustLineNumber(editTextArea.getLineCount());

			opFpBuk = null;

			// Reset of a history
			resetUndoHistory();

		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, PropertiesEditor.getI18nProperty("KEY3"), PropertiesEditor.getI18nProperty("KEY4"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
			LOG.log(Level.SEVERE, e.getMessage(), e);
			opFp = opFpBuk;
			return;
		}
		setTitle(PropertiesEditor.getI18nProperty("KEY5") + opFp.getAbsolutePath()); //$NON-NLS-1$
		AppSetting.getInstance().setOpenFileHistory(file);
		return;
	}

	/**
	 * 
	 * @since 1.0.0
	 */
	boolean checkSave() {

		// It confirms whether to be data under edit.
		if (undo.canUndo()) {
			// In the case of under edit
			// The dialog display which asks whether to save
			int ret = JOptionPane.showConfirmDialog(this, PropertiesEditor.getI18nProperty("KEY6"), PropertiesEditor.getI18nProperty("KEY7"), JOptionPane.YES_NO_CANCEL_OPTION); //$NON-NLS-1$ //$NON-NLS-2$
			if (ret == JOptionPane.YES_OPTION) {
				// When saving
				// It changes and saves to the Unicode reference character.
				// A preservation result is returned.
				return unicodeSave();
			} else if (ret != JOptionPane.CANCEL_OPTION) {
				// In cancellation, true is returned, without saving.
				return true;
			} else {
				// In the case of others, false is returned, without saving.
				return false;
			}
		}
		// When it is not it under edit
		return true;
	}

	/**
	 * 
	 * @param e
	 * @since 1.0.0
	 */
	void version_actionPerformed(ActionEvent e) {

		// A version information dialog is displayed.
		var dlg = new PropertiesEditorFrame_AboutBox(this);
		var dlgSize = dlg.getPreferredSize();
		var frmSize = getSize();
		var loc = getLocation();
		dlg.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
		dlg.setModal(true);
		dlg.pack();
		dlg.setVisible(true);
	}

	/**
	 * 
	 * @param e
	 * @since 1.0.0
	 */
	void open_actionPerformed(ActionEvent e) {

		// It is confirmed whether the data displayed now is saved.
		// When not saved, preservation is urged and preservation is performed.
		// In no cases of cancellation, it carries out.
		if (!checkSave()) {
			return;
		}
		selectOpenFile();
	}

	/**
	 * 
	 * @param e
	 * @since 1.0.0
	 */
	void cut_actionPerformed(ActionEvent e) {

		// Cutoff processing
		editTextArea.cut();
	}

	/**
	 * 
	 * @param e
	 * @since 1.0.0
	 */
	void copy_actionPerformed(ActionEvent e) {

		// Copy processing
		editTextArea.copy();
	}

	/**
	 * 
	 * @param e
	 * @since 1.0.0
	 */
	void paste_actionPerformed(ActionEvent e) {

		// Paste processing
		editTextArea.paste();
	}

	/**
	 * 
	 * @param e
	 * @since 1.0.0
	 */
	void delete_actionPerformed(ActionEvent e) {

		// Delete processing
		editTextArea.replaceSelection(""); //$NON-NLS-1$
	}

	/**
	 * 
	 * @param e
	 * @since 1.0.0
	 */
	void undo_actionPerformed(ActionEvent e) {

		// Undo processing
		if (undo.canUndo()) {
			undoAction.actionPerformed(e);
		}
	}

	/**
	 * 
	 * @param e
	 * @since 1.0.0
	 */
	void redo_actionPerformed(ActionEvent e) {

		// Redo processing
		if (undo.canRedo()) {
			redoAction.actionPerformed(e);
		}
	}

	/**
	 * 
	 * @since 1.0.0
	 */
	protected void resetUndoHistory() {

		// Reset of an operation history
		undo.discardAllEdits();
		undoAction.update();
		redoAction.update();
	}

	/**
	 * 
	 * @since 1.0.0
	 */
	private void textSearch() {

		// Character sequence reference processing
		// The dialog which asks a reference character sequence is displayed.
		SearchTextDialog dlg = new SearchTextDialog(this, true);
		dlg.setVisible(true);

		// Nothing is carried out, when null the reference character sequence or
		// it is not inputted.
		if (dlg.getInputString() == null || dlg.getInputString().equals("")) { //$NON-NLS-1$
			return;
		}
		// Acquisition of the inputted reference character sequence
		key = dlg.getInputString();

		// Reference start
		nextTextSearch(true);
	}

	/**
	 * 
	 * @since 1.0.0
	 */
	private void nextTextSearch() {

		// The next is reference-processed.
		nextTextSearch(false);
	}

	/**
	 * 
	 * @param newFlag
	 * @since 1.0.0
	 */
	private void nextTextSearch(boolean newFlag) {

		// The next is reference-processed.
		// Nothing is carried out when a reference character sequence is null.
		if (key == null || key.equals("")) { //$NON-NLS-1$
			return;
		}
		int searchStartPossition = 0;
		if (!newFlag) {
			// If it is not new reference, the present cursor position will be
			// set as a reference start position.
			searchStartPossition = editTextArea.getCaretPosition();
		}
		// The text for reference is acquired.
		String text = editTextArea.getText();
		if (editTextArea.getText().length() < searchStartPossition) {
			// If the reference start position is larger than the length of the
			// character sequence
			// for reference, a reference start position will be set as the
			// length of the
			// character sequence for reference.
			searchStartPossition = editTextArea.getText().length();
		}
		// Reference
		int targetIndex = text.indexOf(key, searchStartPossition);
		if (targetIndex == -1) {
			if (searchStartPossition == 0) {
				JOptionPane.showConfirmDialog(this, PropertiesEditor.getI18nProperty("KEY8"), "", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
				return;
			} else {
				int ret = JOptionPane.showConfirmDialog(this, PropertiesEditor.getI18nProperty("KEY8"), "", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
				if (ret == JOptionPane.OK_OPTION) {
					this.nextTextSearch(true);
				}
			}
			return;
		}
		// A focus is returned to an input domain.
		editTextArea.requestFocus();
		// A setup of a selection start position
		editTextArea.setSelectionStart(targetIndex);
		// A setup of a selection end position
		editTextArea.setSelectionEnd(targetIndex + key.length());
		// A reference start position is updated for the next reference.
		searchStartPossition = targetIndex + key.length();
	}

	/**
	 * 
	 * @param e
	 * @since 1.0.0
	 */
	void find_actionPerformed(ActionEvent e) {

		// Reference processing
		textSearch();
	}

	/**
	 * 
	 * @param e
	 * @since 1.0.0
	 */
	void nextFind_actionPerformed(ActionEvent e) {

		// The next is reference-processed.
		nextTextSearch();
	}

	/**
	 * 
	 * @since 1.0.0
	 */
	private void replace() {

		// Substitution processing
		// The dialog which acquires the character sequence before substitution
		// and
		// the character sequence after substitution is displayed.
		ReplaceTextDialog dlg = new ReplaceTextDialog(this, true);
		dlg.setVisible(true);

		// Nothing is carried out when the character sequence before
		// substitution is
		// null or an empty character sequence.
		if (dlg.getSourceText() == null || dlg.getSourceText().equals("")) { //$NON-NLS-1$
			return;
		}
		// Acquisition of the character sequence before substitution, and the
		// character sequence after substitution
		String source = dlg.getSourceText();
		String exchange = dlg.getExchangeText();

		int start = 0;
		int tmp = 0;
		int cnt = 0;
		// Repetition substitution processing
		String str = editTextArea.getText();
		while (true) {
			tmp = str.indexOf(source, start);
			if (tmp != -1) {
				cnt++;
				start = tmp + source.length();
			} else {
				break;
			}
		}

		// The data which performed substitution processing is displayed on text
		// area.
		editTextArea.setText(editTextArea.getText().replaceAll(source, exchange));

		// A substitution result is displayed on a status bar.
		JOptionPane.showMessageDialog(this, cnt + PropertiesEditor.getI18nProperty("KEY9"), PropertiesEditor.getI18nProperty("KEY10"), JOptionPane.INFORMATION_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * 
	 * @param e
	 * @since 1.0.0
	 */
	void replace_actionPerformed(ActionEvent e) {

		// Substitution processing
		replace();
	}

	/**
	 * 
	 * @param e
	 * @since 1.0.0
	 */
	void selectAllMenuItem_actionPerformed(ActionEvent e) {

		// All selection processings
		editTextArea.setSelectionStart(0);
		editTextArea.setSelectionEnd(editTextArea.getText().length());
	}

	/**
	 * 
	 * @param e
	 * @since 1.0.0
	 */
	void toolbarCheckBoxMenuItem_actionPerformed(ActionEvent e) {

		// A display and non-displaying setting processing of a tool bar
		if (toolbarCheckBoxMenuItem.isSelected()) {
			toolBar.setVisible(true);
		} else {
			toolBar.setVisible(false);
		}
	}

	/**
	 * 
	 * @param e
	 * @since 1.0.0
	 */
	void lineNumberCheckBoxMenuItem_actionPerformed(ActionEvent e) {

		// A display and processing in which it does not display of a line
		// number
		if (lineNumberCheckBoxMenuItem.isSelected()) {
			showLineNumberButton.setSelected(true);
			showLineNumber();
		} else {
			showLineNumberButton.setSelected(false);
			hideLineNumber();
		}
	}

	/**
	 * 
	 * @param e
	 * @since 1.0.0
	 */
	void showLineNumberButton_actionPerformed(ActionEvent e) {

		// A display and processing in which it does not display of a line
		// number
		if (showLineNumberButton.isSelected()) {
			lineNumberCheckBoxMenuItem.setSelected(true);
			showLineNumber();
		} else {
			lineNumberCheckBoxMenuItem.setSelected(false);
			hideLineNumber();
		}
	}

	/**
	 * 
	 * @param currentLineCount
	 * @since 1.0.0
	 */
	private void ajustLineNumber(int currentLineCount) {

		// A line number is adjusted to the data displayed now.
		var lineNumberBuf = new StringBuilder(currentLineCount * 2);
		for (int i = 1; i <= currentLineCount; i++) {
			lineNumberBuf.append(i + "\n"); //$NON-NLS-1$
		}
		if (lineNumberBuf.length() != 0) {
			lineNumberBuf.deleteCharAt(lineNumberBuf.length() - 1);
		}
		JTextArea lineNumberArea = lineNumberTextArea;
		int lineCountDigit = Integer.toString(currentLineCount).length();
		if (lineCountDigit > 3) {
			lineNumberArea.setColumns(lineCountDigit);
		} else {
			lineNumberArea.setColumns(3);
		}
		lineNumberArea.setText(lineNumberBuf.toString());
	}

	/**
	 * 
	 * @since 1.0.0
	 */
	private void showLineNumber() {

		// Processing which displays a line number
		ajustLineNumber(editTextArea.getLineCount());
		editScrollPane.setRowHeaderView(lineNumberTextArea);
		editScrollPane.repaint();
	}

	/**
	 * 
	 * @since 1.0.0
	 */
	private void hideLineNumber() {

		// Processing which makes a line number un-displaying
		editScrollPane.setRowHeader(null);
		editScrollPane.repaint();
	}

	/**
	 * 
	 * @param e
	 * @since 1.0.0
	 */
	void editTextArea_keyReleased(KeyEvent e) {

		// It is called whenever a key is pushed.
		int currentLineCount = editTextArea.getLineCount();
		int currentRowHeaderLineCount = lineNumberTextArea.getLineCount();
		if (currentLineCount == currentRowHeaderLineCount) {
			return;
		}
		ajustLineNumber(currentLineCount);
	}

	/**
	 * 
	 * @param type
	 * @since 1.0.0
	 */
	public boolean writeFile() {

		JSelectCodeFileChooser jFc = new JSelectCodeFileChooser();
		jFc.setFileFilter(new PropertiesFileFilter());

		if (opFp != null) {
			jFc.setSelectedFile(opFp);
		}
		int ret = jFc.showSaveDialog(this);
		if (JFileChooser.APPROVE_OPTION == ret) {
			String filepath = jFc.getSelectedFile().getAbsolutePath();
			if (!filepath.endsWith(".properties")) { //$NON-NLS-1$
				filepath += ".properties"; //$NON-NLS-1$
			}
			File file = new FileOpener(filepath);
			if (file.exists() && file.isFile()) {
				ret = JOptionPane.showConfirmDialog(this, PropertiesEditor.getI18nProperty("KEY11"), PropertiesEditor.getI18nProperty("KEY12"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
				if (ret != 0) {
					return false;
				}
			}
			return writeFile(filepath);
		} else if (ret == JFileChooser.CANCEL_OPTION || ret == JFileChooser.ERROR_OPTION) {
			return false;
		}
		return false;
	}

	public boolean writeFile(File file) {

		return writeFile(file.getAbsolutePath());
	}

	public boolean writeFile(String filepath) {

		try {
			if (!filepath.endsWith(".properties")) { //$NON-NLS-1$
				filepath += ".properties"; //$NON-NLS-1$
			}
			opFp = new FileOpener(filepath);
			opFp.setText(EncodeChanger.unicode2UnicodeEsc(this.editTextArea.getText()));
			opFp.write(EncodeManager.ASCII);
			return true;
		} catch (AlreadyFileLockException e) {
			JOptionPane.showMessageDialog(this, PropertiesEditor.getI18nProperty("KEY13"), PropertiesEditor.getI18nProperty("KEY4"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
			return false;
		} catch (Exception e) {
			LOG.log(Level.SEVERE, e.getMessage(), e);
			JOptionPane.showMessageDialog(this, PropertiesEditor.getI18nProperty("KEY14"), PropertiesEditor.getI18nProperty("KEY4"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
			return false;
		}
	}

	/**
	 * 
	 * @param e
	 * @since 1.0.0
	 */
	void saveUnicode_actionPerformed(ActionEvent e) {

		boolean result = false;
		if (opFp != null) {
			result = writeFile(opFp);
		} else {
			result = writeFile();
		}
		if (result) {
			resetUndoHistory();
			setTitle(PropertiesEditor.getI18nProperty("KEY5") + opFp.getAbsolutePath()); //$NON-NLS-1$
		}
	}

	/**
	 * 
	 * @since 1.0.0
	 */
	private boolean unicodeSave() {

		boolean ret = writeFile();
		if (ret) {
			resetUndoHistory();
			setTitle(PropertiesEditor.getI18nProperty("KEY5") + opFp.getAbsolutePath()); //$NON-NLS-1$
		}
		return ret;
	}

	/**
	 * 
	 * @param e
	 * @since 1.0.0
	 */
	void showUnicodeMenuItem_actionPerformed(ActionEvent e) {

		String unicodeText = EncodeChanger.unicode2UnicodeEsc(editTextArea.getText());
		var dlg = new UnicodeDialog(this, PropertiesEditor.getI18nProperty("Unicode_"), false); //$NON-NLS-1$
		dlg.setMessage(unicodeText);
		dlg.setSize(new Dimension(680, 550));
		dlg.setVisible(true);
	}

	void fontSelectMenuItem_actionPerformed(ActionEvent e) {

		var font = editTextArea.getFont();
		var back = editTextArea.getBackground();
		var fore = editTextArea.getForeground();
		var fontChooser = new JFontChooserDialog(this, font, fore, back, true);
		fontChooser.setVisible(true);
		if (fontChooser.getReturnValue() != JFontChooserDialog.APPROVE_OPTION) {
			return;
		}
		editTextArea.setFont(fontChooser.getSelectedFont());
		editTextArea.setForeground(fontChooser.getForgroundColor());
		editTextArea.setBackground(fontChooser.getBackgroundColor());
		lineNumberTextArea.setFont(fontChooser.getSelectedFont());
	}

	void printLayoutMenuItem_actionPerformed(ActionEvent e) {

		var pj = PrinterJob.getPrinterJob();
		if (pageFormat == null) {
			pageFormat = pj.defaultPage();
		}
		pageFormat = pj.pageDialog(pageFormat);
	}

	void printMenuItem_actionPerformed(ActionEvent e) {

		if (pageFormat == null) {
			pageFormat = PrinterJob.getPrinterJob().defaultPage();
		}
		Thread t = new Thread(() -> {
			EditorPrinter printer = new EditorPrinter(this);
			PrintMonitor pm = new PrintMonitor(this, printer);
			try {
				pm.performPrint();
			} catch (PrinterException pe) {
				JOptionPane.showMessageDialog(PropertiesEditorFrame.this, PropertiesEditor.getI18nProperty("KEY16") + pe.getMessage()); //$NON-NLS-1$
			}
		});
		t.start();
	}

	/**
	 * actionPerformed
	 * 
	 * @param actionEvent ActionEvent
	 */
	@Override
	public void actionPerformed(ActionEvent e) {

		var lfInfo = UIManager.getInstalledLookAndFeels();
		for (var lf : lfInfo) {
			if (e.getActionCommand().equals(lf.getName())) {
				try {
					UIManager.setLookAndFeel(lf.getClassName());
					SwingUtilities.updateComponentTreeUI(this);
					AppSetting.getInstance().setLookAndFeelClass(lf.getClassName());
				} catch (UnsupportedLookAndFeelException ex) {
					JOptionPane.showMessageDialog(this, PropertiesEditor.getI18nProperty("_LookAndFeel_"), PropertiesEditor.getI18nProperty("KEY4"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
					LOG.log(Level.SEVERE, ex.getMessage(), ex);
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(this, PropertiesEditor.getI18nProperty("LookAndFeel_"), PropertiesEditor.getI18nProperty("KEY4"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
					LOG.log(Level.SEVERE, ex.getMessage(), ex);
				}
			}
		}
	}

	void unicodeSaveWithNameMenuItem_actionPerformed(ActionEvent e) {

		if (writeFile()) {
			resetUndoHistory();
			setTitle(PropertiesEditor.getI18nProperty("KEY5") + opFp.getAbsolutePath()); //$NON-NLS-1$
		}
	}
}
