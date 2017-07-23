package zy.utils;

import java.awt.Image;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xwpf.usermodel.XWPFPictureData;

import zy.doc.CustomXWPFDocument;
import zy.dso.ImageComment;

public class ImageUtil {

	private static final String COMMENT_PREFIX_2 = "ͼƬ";
	
	private static final Logger logger = LogManager.getLogger(ImageUtil.class.getName());
	
	/**
	 * Convert XWPFPictureData to Image, which can be displayed in UI.
	 * 
	 * @param pic
	 * @return
	 */
	public static Image convertPicData2Image(XWPFPictureData pic)
			throws IOException {

		Image image = null;
		ByteArrayInputStream bis = new ByteArrayInputStream(pic.getData());
		Iterator<?> readers = ImageIO.getImageReadersByFormatName(ImageUtil
				.getPictureType(pic.getPictureType()));
		// ImageIO is a class containing static convenience methods
		// for locating ImageReaders
		// and ImageWriters, and performing simple encoding and
		// decoding.

		if (readers.hasNext()) {
			ImageReader reader = (ImageReader) readers.next();
			Object source = bis; // File or InputStream, it seems file
									// is OK

			try {
				ImageInputStream iis = ImageIO.createImageInputStream(source);
				// Returns an ImageInputStream that will take its input from
				// the given Object

				reader.setInput(iis, true);
				ImageReadParam param = reader.getDefaultReadParam();

				image = reader.read(0, param);
			} catch (IOException e) {
				logger.error("can not convert PicData to Image");
				logger.error(e.getMessage());
				e.printStackTrace();
				throw e;
			}
		}
		return image;

	}

	/**
	 * 
	 * @param image
	 * @return
	 */
	public static byte[] getByteContentFromIamge(ImageComment image)
			throws IOException {

		byte[] content = null;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			ImageIO.write((RenderedImage) image.getImage(),
					getPictureType(image.getType()), bos);
			content = bos.toByteArray();
		} catch (IOException e) {
			
			logger.error("can not get Byte Content From Iamge");
			logger.error(e.getMessage());
			
			e.printStackTrace();
			throw e;
		}
		return content;
	}
	
	/**
	 * Split comments.
	 * 
	 * @param strComment
	 * @return
	 */
	public static List<String> splitComment(String strComment){
		List<String> comments = separateString2List(strComment, UIConsts.COMMENT_PREFIX_1);
		if(comments == null || comments.size()==0){
			comments = separateString2List(strComment, COMMENT_PREFIX_2);
		}
		
		return comments;
	}

	/**
	 * Get the corresponding type code of the picture, according to the type of
	 * the picture.
	 * 
	 * @param picType
	 * @return String
	 */
	public static String getPictureType(int picType) {
		String res = "jpg";
		if (picType == CustomXWPFDocument.PICTURE_TYPE_PNG) {
			res = "png";
		} else if (picType == CustomXWPFDocument.PICTURE_TYPE_DIB) {
			res = "dib";
		} else if (picType == CustomXWPFDocument.PICTURE_TYPE_EMF) {
			res = "emf";
		} else if (picType == CustomXWPFDocument.PICTURE_TYPE_JPEG) {
			res = "jpeg";
		} else if (picType == CustomXWPFDocument.PICTURE_TYPE_WMF) {
			res = "wmf";
		}
		return res;
	}
	
	/**
	 * Split the comments, and attach them to the corresponding pics. Note that
	 * more than one comments may exist in the same line, so it is necessary to
	 * split them. TODO It is not clear what is the separator, now just use "��Ƭ"
	 * 
	 * @param images
	 * @param comments
	 */
	public static void setCommets4Image(List<ImageComment> images, String comments,
			String sep) {

		List<String> listComments = UIUtils.splitComments(comments, sep);
		if (images.size() >= listComments.size()) {
			for (int i = 0; i < listComments.size(); i++) {
				images.get(images.size() - listComments.size() + i).setComment(
						listComments.get(i));
			}
		}
	}
	
	/**
	 * Split the comments, and attach them to the corresponding pics. Note that
	 * more than one comments may exist in the same line, so it is necessary to
	 * split them. TODO It is not clear what is the separator, now just use "��Ƭ"
	 * 
	 * @param images
	 * @param comments
	 */
	public static void setCommets4Image(List<ImageComment> images, String comments) {
		
		List<String> listComments = splitComment(comments);

		if (images.size() >= listComments.size()) {
			for (int i = 0; i < listComments.size(); i++) {
				images.get(images.size() - listComments.size() + i).setComment(
						listComments.get(i));
			}
		}
	}
	

	/**
	 * Separate string to lists
	 * 
	 * @param comments
	 * @param prefix
	 * @return
	 */
	public static List<String> separateString2List(String comments, String prefix){
		List<String> listComments = UIUtils.splitComments(comments, prefix);
		return listComments;
	}
}
