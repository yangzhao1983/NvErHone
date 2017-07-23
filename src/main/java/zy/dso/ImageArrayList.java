package zy.dso;

import java.util.ArrayList;

public class ImageArrayList<T> extends ArrayList<ImageComment> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long width;
	private long height;
	
	private int orientation; 
	
	public int getOrientation() {
		return orientation;
	}

	public void setOrientation(int orientation) {
		this.orientation = orientation;
	}

	public long getWidth() {
		return width;
	}

	public void setWidth(long width) {
		this.width = width;
	}

	public long getHeight() {
		return height;
	}

	public void setHeight(long height) {
		this.height = height;
	}

}
