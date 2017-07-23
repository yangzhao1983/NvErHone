package zy.UI;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;

import zy.dso.DocObject;

public class ContentPanel extends JPanel implements IContentPanelControl{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private DocObject object;
	
	//TODO: will add the doc object to the panel
	public ContentPanel(){
		
		Border paneEdge = BorderFactory.createEmptyBorder(10, 100, 10, 100);
		
		this.setBorder(paneEdge);
		
        this.setPreferredSize(new Dimension(700, 700));
        
        this.setMaximumSize(new Dimension(700, 700));
        this.setMinimumSize(new Dimension(700, 700));
	}

	@Override
	public void saveContent(DocObject obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clearContent() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setContent(DocObject obj) {
		this.object = obj;
	}
}
