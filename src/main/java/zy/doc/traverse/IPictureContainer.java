package zy.doc.traverse;

import zy.dso.ImageComment;

public interface IPictureContainer {

	public void addPicture(ImageComment picture);
	
	public int getSize();
	
	public ImageComment getItem(int index);
	
	public void setHeight(long height);
	
	public void setWidth(long width);	
}
