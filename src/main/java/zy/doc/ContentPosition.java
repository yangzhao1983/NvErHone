package zy.doc;

import org.apache.poi.xwpf.usermodel.XWPFParagraph;

public class ContentPosition {
	
	public String getContent() {
		return content;
	}
	
	public ContentPosition(String content){
		this.content = content;
	}
	
	private boolean isInitialized = false;

	public XWPFParagraph getParagraph() {
		return paragraph;
	}

	public void setParagraph(XWPFParagraph paragraph) {
		this.paragraph = paragraph;
	}

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	public boolean isInitialized() {
		return isInitialized;
	}

	public void setInitialized(boolean isInitialized) {
		this.isInitialized = isInitialized;
	}
	
	public int getParagraphPos(){
		return paragraph.getDocument().getPosOfParagraph(paragraph);
	}

	/**
	 * The reference of the paragraph.
	 */
	private XWPFParagraph paragraph; 
	
	/**
	 * The position in the paragraph.
	 */
	private int pos;
	
	/**
	 * The content identifying this paragraph.
	 */
	private String content;
}
