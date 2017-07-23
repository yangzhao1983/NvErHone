package zy.doc;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import zy.dso.ImageArrayList;
import zy.dso.ImageComment;
import zy.utils.UIUtils;

/**
 * TwoP, here the content will be get from the paragraph which is between
 * startPos and endPos.
 * 
 */
public class ReplaceContent4TwoP extends ReplaceContent {
	
	private static final Logger logger = LogManager.getLogger(ReplaceContent4TwoP.class.getName());
	
	private ImageArrayList<ImageComment> imgComments = new ImageArrayList<ImageComment>();
	
	public boolean isFirstSet() {
		return isFirstSet;
	}

	private boolean isFirstSet = true;

	public ReplaceContent4TwoP(String strContentStart, String strContentEnd) {
		super(strContentStart);
		this.posEnd = new ContentPosition(strContentEnd);
	}

	@Override
	public void replace() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean willReplace(List<XWPFRun> runs) {
		
		logger.info("Start to willReplace");
		if (isFirstSet) {
			boolean rel = super.willReplace(runs);
			if (rel) {
				isFirstSet = false;
			}

			return rel;
		} else {
			return doWillReplace(runs, posEnd);
		}

	}

	/**
	 * The second position
	 */
	private ContentPosition posEnd;

	public ContentPosition getPosEnd() {
		return posEnd;
	}

	public ImageArrayList<ImageComment> getImgComments() {
		return imgComments;
	}
	
	/**
	 * Split the comments, and attach them to the corresponding pics. Note that
	 * more than one comments may exist in the same line, so it is necessary to
	 * split them. TODO It is not clear what is the separator, now just use "��Ƭ"
	 * 
	 * @param images
	 * @param comments
	 */
	public void setCommets4Image(List<ImageComment> images, String comments,
			String sep) {
		logger.info("Start to set comments to image");
		List<String> listComments = UIUtils.splitComments(comments, sep);
		if (images.size() >= listComments.size()) {
			for (int i = 0; i < listComments.size(); i++) {
				images.get(images.size() - listComments.size() + i).setComment(
						listComments.get(i));
			}
		}
		logger.info("End to set comments to image");
	}
	
}
