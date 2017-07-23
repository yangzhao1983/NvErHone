package zy.doc.traverse;

import zy.doc.ReplaceContent4TwoP;
import zy.dso.ImageComment;

public class Rc4TwoContainer implements IPictureContainer{
	
	private ReplaceContent4TwoP rc;
	
	public Rc4TwoContainer(ReplaceContent4TwoP rc){
		this.rc = rc;
	}

	@Override
	public void addPicture(ImageComment picture) {
		rc.getImgComments().add(picture);
	}

	@Override
	public int getSize() {
		return rc.getImgComments().size();
	}

	@Override
	public ImageComment getItem(int index) {
		return rc.getImgComments().get(index);
	}

	@Override
	public void setHeight(long height) {
		rc.getImgComments().setHeight(height);
	}

	@Override
	public void setWidth(long width) {
		rc.getImgComments().setWidth(width);
	}

}
