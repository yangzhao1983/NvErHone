package zy.doc.traverse;

import zy.dso.ImageArrayList;
import zy.dso.ImageComment;

public class ImageContainer implements IPictureContainer{

	private ImageArrayList<ImageComment> images;
	
	public ImageContainer(ImageArrayList<ImageComment> images){
		this.images = images;
	}
	
	@Override
	public void addPicture(ImageComment picture) {
		this.images.add(picture);
	}

	@Override
	public int getSize() {
		return this.images.size();
	}

	@Override
	public ImageComment getItem(int index) {
		return images.get(index);
	}

	@Override
	public void setHeight(long height) {
		images.setHeight(height);
	}

	@Override
	public void setWidth(long width) {

		this.images.setWidth(width);
	}
	
}
