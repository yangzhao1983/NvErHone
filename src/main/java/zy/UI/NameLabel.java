package zy.UI;

import java.awt.Dimension;

import javax.swing.JLabel;


public class NameLabel extends JLabel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NameLabel(){
		this("");
	}
	
	public NameLabel(String name){
		
		super(name);
        this.setPreferredSize(new Dimension(100, 30));
        
        this.setMaximumSize(new Dimension(100, 30));
        
        this.setMinimumSize(new Dimension(100, 30));
        
//        this.setBorder(BorderFactory.createLineBorder(Color.black));
	}
}
