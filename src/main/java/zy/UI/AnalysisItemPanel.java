package zy.UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import zy.dso.AnalysisItem;
import zy.dso.ImageComment;
import zy.utils.UIConsts;
import zy.utils.UIUtils;

public class AnalysisItemPanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JTextArea jtaItem;
	
	private JButton btnDel;
	
	private JButton btnAddAfter;
	
	private JButton btnAddBefore;
	
	private JButton editPics;
	
	private AnalytisisItemsPanel parent;
	
	private JFrame frame;
	
	private AnalysisItem<ImageComment> pics;
	
	private static final Logger logger = LogManager.getLogger(AnalysisItemPanel.class.getName());
	
	public AnalysisItemPanel(AnalytisisItemsPanel parent, JFrame frame, AnalysisItem<ImageComment> pics){
		this.parent = parent;
		this.frame = frame;
		this.pics = pics;
		init();
	}
	
	/**
	 * Get the analysis.
	 * 
	 * @return
	 */
	public String getAnalysis(){
		return UIUtils.trimString(jtaItem.getText());
	}
	
	public AnalysisItem<ImageComment> getPics(){
		return pics;
	}
	
	public void init(){
    	
        this.setPreferredSize(new Dimension(450, 200));
        
        this.setMaximumSize(new Dimension(450, 200));
        
        this.setMinimumSize(new Dimension(450, 200));
        
        this.setLayout(new BorderLayout());
        
        this.setBorder(BorderFactory.createLineBorder(Color.black));
        
        //Set up the content pane.
        addComponentsToPane();
	}
	
    
	private void operatePic(boolean isAdd, int pos) {

		if (isAdd) {
			// 1. add pic
			logger.info("Add picture.");
			parent.addItemPanel(pos, null);
		} else {
			// 2. delete pic
			logger.info("Delete picture.");
			parent.removeItem(pos);
		}
	}
	
    public void addComponentsToPane() {
        
        jtaItem = new JTextArea();
        
        jtaItem.setWrapStyleWord(true);
        jtaItem.setLineWrap(true);
		
        jtaItem.setPreferredSize(new Dimension(500, 150));
        jtaItem.setText(pics.getAnalysisItemStr());

        btnDel = new JButton(UIConsts.BTN_EDIT_ADD_PIC_DELETE);
        btnDel.setPreferredSize(new Dimension(150, 30));
        btnDel.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				int pos = parent.getItemPanels().indexOf(AnalysisItemPanel.this);
				logger.info("Delete Pos is" + Integer.toString(pos));
				operatePic(false, pos);
			}
        	
        });
        
        btnAddAfter = new JButton(UIConsts.BTN_EDIT_ADD_PIC_AFTER);
        btnAddAfter.setPreferredSize(new Dimension(150, 30));
        btnAddAfter.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				int pos = parent.getItemPanels().indexOf(AnalysisItemPanel.this);
				logger.info("Add after Pos is" + Integer.toString(pos));
				operatePic(true, pos + 1);
			}
        	
        });
        
        btnAddBefore = new JButton(UIConsts.BTN_EDIT_ADD_PIC_BEFORE);
        btnAddBefore.setPreferredSize(new Dimension(150, 30));
        btnAddBefore.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				int pos = parent.getItemPanels().indexOf(AnalysisItemPanel.this);
				logger.info("Add before Pos is" + Integer.toString(pos));
				operatePic(true, pos);
			}
        	
        });

        editPics = new JButton(UIConsts.BTN_EDIT_PICS);
        editPics.setPreferredSize(new Dimension(150, 30));
        editPics.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
		    	PicturesDialog p = new PicturesDialog(pics, frame);//ʹMyJDialog����ɼ�
				logger.info("Edit Pos");
		    	p.init();
			}	
        });
        
        GridLayout btnLayout = new GridLayout(0,4);
        JPanel p1 = new JPanel();
        p1.setLayout(btnLayout);
        
        p1.add(btnDel);
        p1.add(btnAddAfter);
        p1.add(btnAddBefore);
        p1.add(editPics);
        
        this.add(jtaItem, BorderLayout.CENTER);    
        this.add(p1, BorderLayout.SOUTH);   
    }
}
