package zy.doc.traverse;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xwpf.usermodel.XWPFPicture;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import zy.doc.CustomXWPFDocument;
import zy.dso.ImageArrayList;
import zy.dso.ImageComment;
import zy.utils.ImageUtil;
import zy.utils.UIConsts;
import zy.utils.UIUtils;

public class GetPicsFromPByIndexTraverseParagrahCommand implements
		ITraverseParagraphCommand {

	private List<XWPFRun> runs;
	private int start;
	private int end;
	private boolean canBreak = false;
	private boolean hasSize = false;
	private CustomXWPFDocument doc;
	private boolean appendComments = false;
	private boolean firstPic = false;
	private StringBuilder sb = new StringBuilder();
	private ImageArrayList<ImageComment> images;
	
	private static final Logger logger = LogManager.getLogger(GetPicsFromPByIndexTraverseParagrahCommand.class.getName());
	
	@Override
	public boolean canSkip() {
		return false;
	}

	@Override
	public boolean canBreak() {
		return canBreak;
	}

	public GetPicsFromPByIndexTraverseParagrahCommand(CustomXWPFDocument doc, int start, int end, ImageArrayList<ImageComment> images) {
		this.doc = doc;
		this.start = start;
		this.end = end;
		this.images = images;
	}

	@Override
	public void doCommand() {
		
		logger.info("Start to do commnad");
		
		if (runs == null || runs.size() == 0) {
			return;
		}
		ImageComment imgC;

		@SuppressWarnings("deprecation")
		int indexCurrent = this.doc.getPosOfParagraph(runs.get(0).getParagraph());
		
		if(indexCurrent < start){
			return;
		}
		
		if(indexCurrent >= end){
			this.canBreak = true;
			return;
		}
		
		for (XWPFRun run : runs) {

			if (run.getEmbeddedPictures().size() > 0) {
				for (XWPFPicture pic : run.getEmbeddedPictures()) {
					if (!hasSize) {
						images.setHeight(
								pic.getCTPicture().getSpPr().getXfrm().getExt()
										.getCy());
						images.setWidth(
								pic.getCTPicture().getSpPr().getXfrm().getExt()
										.getCx());
						hasSize = true;

					}
					appendComments = true;
					if (!firstPic) {
						firstPic = true;
					} else {
						if (!sb.toString().trim().equals("")) {
							ImageUtil.setCommets4Image(
									images, sb.toString(),
									"��Ƭ");
							sb.delete(0, sb.length());
							firstPic = false;
						}
					}
					imgC = new ImageComment();
					try {
						imgC.setImage(ImageUtil.convertPicData2Image(pic
								.getPictureData()));
					} catch (IOException e) {
						
						logger.error(e.getMessage());
						logger.error("Can not set image date, IO Exception.");
						e.printStackTrace();
					}
					images.add(imgC);
				}
			} else {
				if (appendComments) {
					sb.append(run.getText(0));
				}
			}
		}
		
		ImageUtil.setCommets4Image(images, sb.toString());
		
		logger.info("End to do commnad");
	}

	@Override
	public void setRuns(List<XWPFRun> runs) {
		this.runs = runs;
	}

	/**
	 * Separate string to lists
	 * 
	 * @param comments
	 * @param prefix
	 * @return
	 */
	private List<String> separateString2List(String comments, String prefix){
		List<String> listComments = UIUtils.splitComments(comments, prefix);
		return listComments;
	}
	
	
	/**
	 * Split the comments, and attach them to the corresponding pics. Note that
	 * more than one comments may exist in the same line, so it is necessary to
	 * split them. TODO It is not clear what is the separator, now just use "��Ƭ"
	 * 
	 * @param images
	 * @param comments
	 */
	@SuppressWarnings("unused")
	private void setCommets4Image(List<ImageComment> images, String comments) {
		List<String> listComments = separateString2List(comments, UIConsts.COMMENT_PREFIX_1);
		if(listComments == null || listComments.size()==0){
			listComments = separateString2List(comments, UIConsts.COMMENT_PREFIX_2);
		}

		if (images.size() >= listComments.size()) {
			for (int i = 0; i < listComments.size(); i++) {
				images.get(images.size() - listComments.size() + i).setComment(
						listComments.get(i));
			}
		}
	}
}
