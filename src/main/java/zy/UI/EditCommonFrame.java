package zy.UI;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.openxml4j.opc.OPCPackage;

import zy.doc.CustomXWPFDocument;
import zy.dso.DocObject;
import zy.utils.ContentPanelFactory;
import zy.utils.UIConsts;
import zy.utils.UIUtils;

public class EditCommonFrame extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private DocObject object;
	private int cpIndex;

	private ContentPanel cp;

	// Parent frame.
	private ImportWord parent;
	
	private JButton btnNextStep;
	private JButton btnUnSet;
	private JButton btnReturn;
	
	private static final Logger logger = LogManager.getLogger(EditCommonFrame.class.getName());
	
	public EditCommonFrame(ImportWord frame, DocObject object){
		this.parent = frame;
		this.object = object;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// display the next frame and dispose itself.
		if(e.getSource().equals(this.btnNextStep)){
			logger.info("Start to Save the content");
			cp.saveContent(object);
			String path = object.getPath();
			
			if (this.cpIndex + 1 < UIConsts.NAME_CONTENT_PANEL.length) {

				this.setContentPanel(ContentPanelFactory.getNextIndex(cpIndex));
			}else{
				
				logger.info("Start to write the content to the doc file, and exit");
				// write the content to the doc file, and exit.
				
				logger.info("Start to create a new word by copying the original word");
				// 1. create a new word by copying the original word
				String tmp = UIUtils.getTmpPath(object.getPath());
				if (tmp != null && !tmp.trim().equals("")) {
					UIUtils.copyFile(path, tmp);
				}

				logger.info("Start to delete the content");
				// 2. delete the content
				removeContent();
				
				String target = UIUtils.getTmpPath(object.getPath(),"target");
				if (target != null && !target.trim().equals("")) {
					UIUtils.copyFile(tmp, target);
				}

				// 3. add the generated pictures and comments to the new word
				logger.info("Start to add the generated pictures and comments to the new word");
				writeObject2Word(path);
			}
			repaint();
			setVisible(true);
			logger.info("End to Save the content");
		}else if(e.getSource().equals(this.btnReturn)){
			
			logger.info("do not save the content");
			// do not save the content
			if(this.cpIndex>0){
				this.setContentPanel(ContentPanelFactory.getBeforeIndex(cpIndex));
				repaint();
				setVisible(true);
			}else{
				this.setVisible(false);
				parent.setVisible(true);
			}
		}else if(e.getSource().equals(this.btnUnSet)){
			this.cp.clearContent();
		}
	}
	
	/**
	 * Write pictures 2 doc.
	 * 
	 * @param startMark
	 * @param endMark
	 * @param path
	 * @param images
	 * @throws LogicException
	 */
	private void writeObject2Word(String path) {
		
		logger.info("Start to write Object to Word");
		
		CustomXWPFDocument doc = null;
		FileOutputStream fopts = null;
		try {
			
			OPCPackage pack = POIXMLDocument.openPackage(UIUtils.getTmpPath(path,
					"tmp"));
			doc = new CustomXWPFDocument(pack);

			doc.writeObject2Doc(this.object);

			fopts = new FileOutputStream(UIUtils.getTmpPath(path, "target"));
			doc.write(fopts);
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage());
			logger.error("Can not write Object to Word, FileNotFound Exception.");
			e.printStackTrace();
		} catch (IOException e) {
			logger.error(e.getMessage());
			logger.error("Can not write Object to Word, IO Exception.");
			e.printStackTrace();
		}

		logger.info("End to write Object to Word");
	}
	
	/**
	 * Remove the selected content, which has been marked.
	 * 
	 * @param path
	 * @param startMark
	 * @param endMark
	 */
	private void removeContent()  {
		
		logger.info("Start to removeContent");
		
		CustomXWPFDocument doc = null;
		FileOutputStream fopts = null;
		
		try {
			OPCPackage pack = POIXMLDocument
					.openPackage(object.getPath());
			doc = new CustomXWPFDocument(pack,false);
			doc.removeContent();
			
			fopts = new FileOutputStream(
					UIUtils.getTmpPath(object.getPath()));
			doc.write(fopts);
			fopts.close();
			
		} catch (IOException e) {
			logger.error(e.getMessage());
			logger.error("Can not Remove content, IO Exception.");
			e.printStackTrace();
		}finally{
			try {
				fopts.close();
			} catch (IOException e) {
				
				logger.error(e.getMessage());
				logger.error("Can not close doc, IO Exception.");
				
				e.printStackTrace();
			}
		}
		
		logger.info("End to removeContent");
	}

	private void setContentPanel(int index){
		if (cp!=null) {
			this.getContentPane().remove(cp);
		}
		this.cp = ContentPanelFactory.getContentPanel(index, this);
		this.cpIndex = index;
		// add content panel in the center
		this.cp.setContent(object);
		this.getContentPane().add(this.cp, BorderLayout.CENTER);
	}
	
//	/**
//	 * @param args
//	 */
//	public static void main(String[] args) {
//
//		/* Use an appropriate Look and Feel */
//		try {
//			// UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
//			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
//		} catch (UnsupportedLookAndFeelException ex) {
//			ex.printStackTrace();
//		} catch (IllegalAccessException ex) {
//			ex.printStackTrace();
//		} catch (InstantiationException ex) {
//			ex.printStackTrace();
//		} catch (ClassNotFoundException ex) {
//			ex.printStackTrace();
//		}
//		/* Turn off metal's use bold fonts */
//		UIManager.put("swing.boldMetal", Boolean.FALSE);
//
//		// Schedule a job for the event dispatch thread:
//		// creating and showing this application's GUI.
//		javax.swing.SwingUtilities.invokeLater(new Runnable() {
//			public void run() {
//
//				createAndShowGUI();
//			}
//		});
//	}

	/**
	 * Create the GUI and show it. For thread safety, this method should be
	 * invoked from the event-dispatching thread.
	 */
	@SuppressWarnings("unused")
	private static void createAndShowGUI() {

		// Create and set up the window.
		// EditCommonFrame frame = new EditCommonFrame(new ContentPanel());
		// EditCommonFrame frame = new EditCommonFrame(new
		// AbstractContentPanel());
		// EditCommonFrame frame = new EditCommonFrame(new
		// SummaryContentPanel());
		// EditCommonFrame frame = new EditCommonFrame(new
		// SurveyNoteContentPanel());
		// EditCommonFrame frame = new EditCommonFrame(new
		// AnalytisisItemsPanel());
		// EditCommonFrame frame = new EditCommonFrame(new
		// EvidenceItemsPanel());
		// EditCommonFrame frame = new EditCommonFrame(new
		// IdentifyContentPanel());
		EditCommonFrame frame = new EditCommonFrame(null, null);

		frame.init();

		// Display the window.
		frame.pack();
		// set frame in the center of the window
		frame.setLocationRelativeTo(null);

		frame.setVisible(true);
	}

	/**
	 * Display the frame.
	 * 
	 */
	public void display(){
		this.init();

		// Display the window.
		this.pack();
		// set frame in the center of the window
		this.setLocationRelativeTo(null);

		this.setVisible(true);		
	}
	
	public void addComponentsToPane(Container pane) {

		if (!(pane.getLayout() instanceof BorderLayout)) {
			pane.add(new JLabel("Container doesn't use BorderLayout!"));
			return;
		}

		// Make the center component big, since that's the
		// typical usage of BorderLayout.
		Border paneEdge = BorderFactory.createEmptyBorder(50, 10, 50, 10);

		this.btnNextStep = new JButton(UIConsts.BTN_EDIT_NEXT);
		btnNextStep.setPreferredSize(new Dimension(100, 30));
		btnNextStep.addActionListener(this);

		this.btnUnSet = new JButton(UIConsts.BTN_EDIT_UNSET);
		btnUnSet.setPreferredSize(new Dimension(100, 30));
		btnUnSet.addActionListener(this);
		
		this.btnReturn = new JButton(UIConsts.BTN_EDIT_RETURN);
		btnReturn.setPreferredSize(new Dimension(100, 30));
		btnReturn.addActionListener(this);

		JPanel p1 = new JPanel();
		JPanel p2 = new JPanel();
		JPanel p3 = new JPanel();

		p1.add(btnNextStep);
		p2.add(btnUnSet);
		p3.add(btnReturn);

		JPanel panel = new JPanel();

		GridLayout experimentLayout = new GridLayout(0, 3);

		panel.setLayout(experimentLayout);
		panel.setBorder(paneEdge);

		panel.add(p1);
		panel.add(p2);
		panel.add(p3);

		setContentPanel(UIConsts.NAME_CONTENT_PANEL_BASIC);
		pane.add(panel, BorderLayout.SOUTH);
	}

	public void init() {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setPreferredSize(new Dimension(700, 800));

		// Create and set up the window.
		// this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Set up the content pane.
		addComponentsToPane(this.getContentPane());
	}
}
