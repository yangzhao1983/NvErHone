package zy.UI;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import zy.UI.Logic.ImportWordLogic;
import zy.dso.DocObject;
import zy.utils.UIConsts;
import zy.utils.UIUtils;

public class ImportWord extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String wordPath;
	private DocObject object;
	
	public DocObject getObject() {
		return object;
	}

	public void setObject(DocObject object) {
		this.object = object;
	}

	public String getWordPath() {
		return wordPath;
	}

	public JButton getBtnNext() {
		return btnNext;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		ImportWordLogic logic = new ImportWordLogic(this);
		
		if(e.getSource() == btnReadTmplate){
			wordPath = getPath();
			logic.setPath(wordPath);
			if (!UIUtils.isEmptyString(wordPath)) {
				btnNext.setEnabled(false);
				logic.setUIInfo();
				btnNext.setEnabled(true);
				btnNext.revalidate();
			}
		}else if(e.getSource() == this.btnNext){
			logic.navigate2Next();
		}else if(e.getSource() == this.btnCancel){
			System.exit(0);
		}
	}

	// read the word file
	private JButton btnReadTmplate;

	// go to next frame
	private JButton btnNext;

	// exit Capricorn
	private JButton btnCancel;

	private JFileChooser fc;
	
	private static final Logger logger = LogManager.getLogger(ImportWord.class.getName());
	 
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		/* Use an appropriate Look and Feel */
		try {
			// UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		} catch (UnsupportedLookAndFeelException ex) {
			ex.printStackTrace();
			logger.error(ex.getMessage());
		} catch (IllegalAccessException ex) {
			ex.printStackTrace();
			logger.error(ex.getMessage());
		} catch (InstantiationException ex) {
			ex.printStackTrace();
			logger.error(ex.getMessage());
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
			logger.error(ex.getMessage());
		}
		/* Turn off metal's use bold fonts */
		UIManager.put("swing.boldMetal", Boolean.FALSE);

		
		logger.error("Log4j can work!!");
		
		// Schedule a job for the event dispatch thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {

				createAndShowGUI();
			}
		});
	}

	/**
	 * Create the GUI and show it. For thread safety, this method should be
	 * invoked from the event-dispatching thread.
	 */
	private static void createAndShowGUI() {

		// Create and set up the window.
		ImportWord frame = new ImportWord();
		
		frame.init();
		
		// Display the window.
		frame.pack();
		// set frame in the center of the window
		frame.setLocationRelativeTo(null);
		
		frame.setVisible(true);
	}

	/**
	 * Get the path of selected image.
	 *  
	 * @return
	 */
	private String getPath() {
		int returnVal = fc.showOpenDialog(ImportWord.this);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			return file.getAbsolutePath();

			// This is where a real application would open the file.
		} else {
			return "";
		}
	}

	public void addComponentsToPane(Container pane) {
		
		if (!(pane.getLayout() instanceof BorderLayout)) {
			pane.add(new JLabel("Container doesn't use BorderLayout!"));
			return;
		}

		// Make the center component big, since that's the
		// typical usage of BorderLayout.
		Border paneEdge = BorderFactory.createEmptyBorder(50, 10, 50, 10);

		btnReadTmplate = new JButton(UIConsts.BTN_IMPORT_WORD_TITLE);
		btnReadTmplate.setPreferredSize(new Dimension(200, 100));
		btnReadTmplate.addActionListener(this);
		
		// Create a file chooser
		fc = new JFileChooser();

		btnNext = new JButton(UIConsts.BTN_NEXT_TITLE);
		btnNext.setPreferredSize(new Dimension(200, 100));
		btnNext.addActionListener(this);
		btnNext.setEnabled(false);

		btnCancel = new JButton(UIConsts.BTN_CANCEL_TITLE);
		btnCancel.setPreferredSize(new Dimension(200, 100));
		btnCancel.addActionListener(this);

		JPanel p1 = new JPanel();
		JPanel p2 = new JPanel();
		JPanel p3 = new JPanel();

		p1.add(btnReadTmplate);
		p2.add(btnNext);
		p3.add(btnCancel);

		JPanel panel = new JPanel();

		GridLayout experimentLayout = new GridLayout(0, 1);

		panel.setLayout(experimentLayout);
		panel.setBorder(paneEdge);

		panel.add(p1);
		panel.add(p2);
		panel.add(p3);

		pane.add(panel, BorderLayout.CENTER);
	}
	
	public void init() {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		
		// Create and set up the window.
		// this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Set up the content pane.
		addComponentsToPane(this.getContentPane());
	}
}
