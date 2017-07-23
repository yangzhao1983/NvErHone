package zy.dso;

import java.awt.Image;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import zy.utils.UIUtils;

/**
 * Add a comment to image.
 * 
 * 
 * @author yangzhao
 *
 */
public class ImageComment{
	
	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	private String comment;
	
	private Image image;
	
	private int type;
	
	private int width;
	
	private int height;
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public String getComment() {
		return comment;
	}

	public static void main(String ...strings){
		ImageComment im = new ImageComment();
		im.setComment("’’∆¨1µÿœ¬“ª≤„”È¿÷ “Àƒ÷‹«ΩÃÂ ‹≥±°¢∑¢√π");
		System.out.println(im.getNumbers4Comment("(?:’’∆¨\\s*)(\\d)"));
	}
	
	/**
	 * When display the comments, the number should be ’’∆¨\s*\d+(-\d)? or ’’∆¨\s*\d
	 * 
	 * @return
	 */
	public String getNumbers4Comment(String pattern){
		if(UIUtils.isEmptyString(this.comment)){
			return "";
		}
		
		Pattern p = Pattern.compile(pattern);
		Matcher matcher = p.matcher(comment.trim());
		if (matcher.find()) {
			return matcher.group(1);
		}else{
			return "";
		}
	}
	
	/**
	 * Remove ’’∆¨ {number}
	 * 
	 * @return
	 */
	public String getCommentWithoutNumber(){
		
		Pattern p = Pattern.compile("^\\s*’’∆¨\\s*\\d(?:-\\d)?\\s*(.*)$");
		if (comment == null){
			comment = "";
		}
		Matcher matcher = p.matcher(comment.trim());
		if(matcher.find()){
			return matcher.group(1);
		}
		
		return comment;
	}
	
	public void setComment(String comment) {
		this.comment = comment;
	}
}
