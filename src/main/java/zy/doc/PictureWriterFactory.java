package zy.doc;

import zy.utils.UIConsts;

public class PictureWriterFactory {

	public static IPictureWriter generatePictureWriter(boolean format) {
		if (format == UIConsts.ORIENTATION_LANDSCAPE) {
			return new LandScapePictureWriter();
		}else{
			return new PortraitPictureWriter();
		}
	}
}
