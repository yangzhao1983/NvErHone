package zy.doc;

import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import zy.dso.ImageArrayList;
import zy.dso.ImageComment;

public interface IPictureWriter {

	public void writePicture2Doc(ImageArrayList<ImageComment> pics,
			XWPFParagraph paragraph, CustomXWPFDocument doc);
}
