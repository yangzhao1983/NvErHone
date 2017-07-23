package zy.UI;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

import javax.swing.JTextArea;

import zy.dso.ImageArrayList;
import zy.dso.ImageComment;
import zy.utils.UIConsts;
import zy.utils.UIUtils;

/**
 * Listen to the change event of the textarea
 * 
 * @author yangzhao
 *
 */
public class KeyEventHandler implements KeyListener{

	private List<ImageArrayList<ImageComment>> mapIndex2Pics;
	
	private JTextArea jta;
	
	private boolean toRemovePics = false;
	
	public KeyEventHandler(JTextArea jta){
		this.jta = jta;
	}
	
	private void addPictures(){
		int cursorPos = jta.getSelectionStart();
		int index = getRelPosOfPicMark(cursorPos);
		mapIndex2Pics.add(index, new ImageArrayList<ImageComment>());
	}
	
	private void deletePictures(){	
		// remove pictures
		int cursorPos = jta.getSelectionStart();
		int index = getRelPosOfPicMark(cursorPos);
		mapIndex2Pics.remove(index);
		
		this.toRemovePics = false;
	}
	
	public void setMapIndex2Pics(List<ImageArrayList<ImageComment>> mapIndex2Pics) {
		this.mapIndex2Pics = mapIndex2Pics;
	}

	/**
	 * There are some picture marks in the content.
	 * From the position of the inputed picture mark, calculate the index of the newly inputed mark in the sequence of the existing marks.
	 * 
	 * @param posOfPicMark
	 * @return
	 */
	private int getRelPosOfPicMark(int posOfPicMark){
		String content = jta.getText();
		
		return UIUtils.getCharPosRel2PicMark(posOfPicMark, content);
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		// input @
		String typedString = String.valueOf(e.getKeyChar());
		if(UIConsts.PICTURE_MARK.equals(typedString)){
			// to add pictures
			addPictures();
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
		// remove @
		int cursorPos = jta.getSelectionStart();
		String removedStr = "";
		
		// keycode is delete or backspace
		int keyCode = e.getKeyCode();
		if(keyCode == KeyEvent.VK_DELETE){
			// delete character after cursor
			removedStr = jta.getText().substring(cursorPos,cursorPos+1);
		}else if (keyCode == KeyEvent.VK_BACK_SPACE){
			// delete character before cursor
			removedStr = jta.getText().substring(cursorPos-1,cursorPos);
		}	
		
		if(UIConsts.PICTURE_MARK.equals(removedStr)){
			this.toRemovePics = true;
		}else{
			this.toRemovePics = false;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
		if(toRemovePics){
			// remove pictures
			deletePictures();
		}
	}

}
