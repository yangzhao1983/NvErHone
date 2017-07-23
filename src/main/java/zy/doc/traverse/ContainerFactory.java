package zy.doc.traverse;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import zy.doc.ReplaceContent4TwoP;
import zy.dso.ImageArrayList;
import zy.dso.ImageComment;

public class ContainerFactory {
	
	private static final Logger logger = LogManager.getLogger(ContainerFactory.class.getName());

	public static IPictureContainer generateContainer(ReplaceContent4TwoP rc4Two) {
		logger.info("return rc for two Container");
		return new Rc4TwoContainer(rc4Two);
	}

	public static IPictureContainer generateContainer(
			ImageArrayList<ImageComment> images) {
		logger.info("return rc for image Container");
		return new ImageContainer(images);
	}
}
