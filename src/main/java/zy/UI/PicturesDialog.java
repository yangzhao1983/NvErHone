package zy.UI;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import zy.dso.ImageArrayList;
import zy.dso.ImageComment;
import zy.utils.UIConsts;

public class PicturesDialog extends JDialog implements ActionListener{
	
	private int width = 0;
	private int height = 0;
	
	public static final int STANDARD_WIDTH = 3058520;
	public static final int STANDARD_HEIGHT = 2376000;	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// read the word file
	private JButton btnAdd;
	
	private JScrollPane scrb;
	
	private GridBagConstraints c;
	
	private static final Logger logger = LogManager.getLogger(PicturesDialog.class.getName());
	
	public JScrollPane getScrb() {
		return scrb;
	}
	
	private List<PicturePanel> picPanels;
	
	/** pictures to display*/
	private ImageArrayList<ImageComment> pictures;
	
	public ImageArrayList<ImageComment> getPictures() {
		return pictures;
	}

	public void setPictures(ImageArrayList<ImageComment> pictures) {
		this.pictures = pictures;
	}

	private JPanel pPics;

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == btnComplete) {

			savePictures();
			this.dispose();
		} else if (e.getSource() == btnCancel) {
			this.dispose();
		} else if (e.getSource() == btnAdd) {
			addPicturePanel(getPicPanels().size(), null);
		}
	}

	// go to next frame
	private JButton btnComplete;
	
	// exit Capricorn
	private JButton btnCancel;
    
    /**
     * Save the pictures.
     * 
     */
    private void savePictures(){
    	
    	this.pictures.clear();
    	for(PicturePanel p :picPanels){
    		if (p.getPic()!=null){
    			if(p.getPic().getHeight()==0){
    				p.getPic().setHeight(STANDARD_HEIGHT);
    			}
    			if(p.getPic().getWidth()==0){
    				p.getPic().setWidth(STANDARD_WIDTH);
    			}
    			pictures.add(p.getPic());
    		}
    	}
    	if(pictures.size() > 0){
    		pictures.setWidth(pictures.get(0).getWidth());
    		pictures.setHeight(pictures.get(0).getHeight());
    	}
    }
    
    /**
     * Constructor, will take the pictures got from former frame. 
     * 
     * @param pictures
     */
    public PicturesDialog(ImageArrayList<ImageComment> pictures, JFrame frame){
    	super(frame, "�޸�ͼƬ");
    	this.pictures = pictures;
    	if(this.pictures == null || this.pictures.size() == 0){
    		this.width = STANDARD_WIDTH;
    		this.height = STANDARD_HEIGHT;
    	}else{
    		this.width = this.pictures.get(0).getWidth();
    		this.height = this.pictures.get(0).getHeight();
    	}
    	
    	if(this.width == 0){
    		this.width = STANDARD_WIDTH;
    	}
    	
    	if(this.height == 0){
    		this.height = STANDARD_HEIGHT;
    	}  	
    	
    }

    public void init(){
    	
    	picPanels = new ArrayList<PicturePanel>();
        this.setSize(new Dimension(700, 800));
    	setLocationRelativeTo(null);
    	
//        this.setPreferredSize(new Dimension(700, 800));
        
        this.setResizable(false);
        
        //Set up the content pane.
        addComponentsToPane(this.getContentPane());
        
    	setVisible(true);
    }
    
    public void addComponentsToPane(Container pane) {
        
        //Make the center component big, since that's the
        //typical usage of BorderLayout.
      
        pPics = new JPanel(new GridBagLayout());
        
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        
        // initialize picture panels
        initPicPanel();
        
        scrb = new javax.swing.JScrollPane(pPics);
        
        JPanel pBtns = new JPanel();
        
        btnAdd = new JButton(UIConsts.BTN_EDIT_ADD_PIC_PANEL);        
        btnComplete = new JButton(UIConsts.BTN_EDIT_FINISH_PIC_PANEL);
        btnCancel= new JButton(UIConsts.BTN_EDIT_CANCLE_PIC_PANEL); 
        
        btnAdd.addActionListener(this);  
        btnComplete.addActionListener(this);  
        btnCancel.addActionListener(this);  
        
        pBtns.setMaximumSize(new Dimension(700, 60));
        GridLayout btnsLayout = new GridLayout(1,4);  
        pBtns.setLayout(btnsLayout);
        pBtns.add(btnAdd);
        pBtns.add(btnComplete);
        pBtns.add(btnCancel);

        pane.setLayout(new BoxLayout(pane, BoxLayout.PAGE_AXIS));
        
        pane.add(scrb);
        pane.add(pBtns);       
    }
    
    /**
     * Initialize picture panels 
     * 
     */
    private void initPicPanel(){
    	
        // add picture panels
        if (pictures== null || pictures.size()==0){
        	logger.info("picture # is 0");
            addPicturePanel(0, null);
        }else{
        	logger.info("picture # is " + pictures.size());
        	this.addMultiPicturePanel(pictures);
        }
    }
    
    /**
     * Add pictures
     * 
     * @param pos
     */
    public void addPicturePanel(int pos, ImageComment pic){
    	
    	doAddPicPanel(pos, pic);	
		reDisplayPictures();
    }
    
    /**
	 * Do the operation for adding pic panels.
	 * 
	 * @param pos
	 * @param pic
	 */
	private void doAddPicPanel(int pos, ImageComment pic) {

		logger.info("Start to do add picture");
		PicturePanel p = new PicturePanel(this, pic);

		this.getPicPanels().add(pos, p);
		logger.info("End to do add picture");
	}
    
    /**
     * Add multi-pictures
     * 
     * @param pics
     */
    public void addMultiPicturePanel(List<ImageComment> pics){
    	int i = 0;
    	
		logger.info("Start to do add multi pictures " + pics.size());
		
    	for(ImageComment pic : pics){
        	doAddPicPanel(i, pic);
    		i++;
    	}
		logger.info("reDisplayPictures");
		reDisplayPictures();
		logger.info("End to do add multi pictures");
    }
    
    public void removePicture(int pos){
		logger.info("Start to remove picture pos " + pos);
    	if (pos>=0 && this.getPicPanels().size()>pos) {
			this.getPicPanels().remove(pos);
			this.reDisplayPictures();
		}
		logger.info("End to remove picture pos " + pos);
    }
    
    private void reDisplayPictures(){
    	int i = 0;
    	
		logger.info("remove all");
		getpPics().removeAll();
		
		getpPics().setVisible(false);
		
    	for( JPanel p : this.getPicPanels()){

    		c.gridx = (i%2==0)?0:1;
    		c.gridy = i/2;
    		i++;
    		getpPics().add(p,c);
    	}
    	
		getpPics().setVisible(true);
    	
  		getpPics().revalidate();
    }

	public JPanel getpPics() {
		return pPics;
	}


	public List<PicturePanel> getPicPanels() {
		return picPanels;
	}


	public void setPicPanels(List<PicturePanel> picPanels) {
		this.picPanels = picPanels;
	}
}
