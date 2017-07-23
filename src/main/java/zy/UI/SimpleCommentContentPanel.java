package zy.UI;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JTextArea;

public class SimpleCommentContentPanel extends ContentPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected JLabel jlTitle;
	protected JTextArea jtaComment;
	
	public SimpleCommentContentPanel(){
		this("");
	}
	
	public SimpleCommentContentPanel(String title){
		
		super();
		
		jlTitle = new NameLabel(title);
		jtaComment = new JTextArea();
		jtaComment.setWrapStyleWord(true);
		jtaComment.setLineWrap(true);
		
		this.setLayout(new BorderLayout());
		this.add(jlTitle, BorderLayout.NORTH);
		
		this.add(jtaComment, BorderLayout.CENTER);
		
		jtaComment.setPreferredSize(new Dimension(600, 500));
		jtaComment.setMinimumSize(new Dimension(600, 500));
		jtaComment.setPreferredSize(new Dimension(600, 500));
	}
}
