package zy.UI;

import java.awt.Dimension;

import javax.swing.JTextField;

public class ValueTextField extends JTextField{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ValueTextField(){
		
        this.setPreferredSize(new Dimension(200, 30));
        
        this.setMaximumSize(new Dimension(200, 30));
        
        this.setMinimumSize(new Dimension(200, 30));
	}
}
