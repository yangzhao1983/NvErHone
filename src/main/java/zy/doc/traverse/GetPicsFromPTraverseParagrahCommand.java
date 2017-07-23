package zy.doc.traverse;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xwpf.usermodel.XWPFPicture;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import zy.doc.CustomXWPFDocument;
import zy.doc.ReplaceContent4TwoP;
import zy.dso.ImageArrayList;
import zy.dso.ImageComment;
import zy.utils.ImageUtil;

/**
 * If found a picture, then start to generate the comments.
 * If generated the comments, and a new paragraph contains no picture, stop.
 * 
 * @author yangzhao
 *
 */
public class GetPicsFromPTraverseParagrahCommand implements
		ITraverseParagraphCommand {

	private boolean foundPic = false;
	private boolean generatedComments = false;
	private List<XWPFRun> runs;
	private ReplaceContent4TwoP rc;
	private boolean canBreak = false;
	private boolean hasSize = false;
	private CustomXWPFDocument doc;

	private static final Logger logger = LogManager.getLogger(GetPicsFromPTraverseParagrahCommand.class.getName());
//	private static final String COMMENT_PREFIX_1 = "��Ƭ";
//	private static final String COMMENT_PREFIX_2 = "ͼ";
	
	@Override
	public boolean canSkip() {
		return false;
	}

	@Override
	public boolean canBreak() {
		return canBreak;
	}

	public GetPicsFromPTraverseParagrahCommand(CustomXWPFDocument doc, ReplaceContent4TwoP rc) {
		this.doc = doc;
		this.rc = rc;
	}

	@Override
	public void doCommand() {

		logger.info("Start to do command");
		
		StringBuilder sb = new StringBuilder();

		if (runs == null || runs.size() == 0) {
			return;
		}
		ImageComment imgC;

		@SuppressWarnings("deprecation")
		int indexCurrent = this.doc.getPosOfParagraph(runs.get(0)
				.getParagraph());
		int indexStart = this.doc.getPosOfParagraph(rc.getPosStart()
				.getParagraph());
		int indexEnd = this.doc
				.getPosOfParagraph(rc.getPosEnd().getParagraph());
		if (indexCurrent < indexStart) {
			return;
		}

		if (indexCurrent >= indexEnd) {
			this.canBreak = true;
			return;
		}

		for (XWPFRun run : runs) {

			if (run.getEmbeddedPictures().size() > 0) {
				this.foundPic = true;
				for (XWPFPicture pic : run.getEmbeddedPictures()) {
					if (!hasSize) {

						rc.getImgComments().setHeight(
								pic.getCTPicture().getSpPr().getXfrm().getExt()
										.getCy());
						rc.getImgComments().setWidth(
								pic.getCTPicture().getSpPr().getXfrm().getExt()
										.getCx());
						hasSize = true;

					}

					imgC = new ImageComment();
					try {
						imgC.setImage(ImageUtil.convertPicData2Image(pic
								.getPictureData()));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					rc.getImgComments().add(imgC);
				}
				generatedComments = false;
			} else if(generatedComments){
				return;
			} else if (foundPic) {
				sb.append(run.getText(0));
			}
		}

		List<String> comments = ImageUtil.splitComment(sb.toString());

		if (comments != null && comments.size() > 0 && !generatedComments) {
			setComments4Image(rc.getImgComments(), comments);
			generatedComments = true;
		}
		logger.info("End to do command");
	}

	private void setComments4Image(ImageArrayList<ImageComment> images, List<String> list){

		logger.info("Start set comments to image");
		
		if(list == null || list.size() == 0){
			return;
		}
		
		int numComments = list.size();
		int numImage = images.size();
		
		int numSet = numComments > numImage ? numImage : numComments;
		
		for(int i = 0; i< numSet; i++){
			rc.getImgComments().get(numImage-numSet + i).setComment(list.get(numComments-numSet + i));
		}
		
		logger.info("End set comments to image");
	}

	@Override
	public void setRuns(List<XWPFRun> runs) {
		this.runs = runs;
	}

//	/**
//	 * Separate string to lists
//	 * 
//	 * @param comments
//	 * @param prefix
//	 * @return
//	 */
//	private List<String> separateString2List(String comments, String prefix){
//		List<String> listComments = UIUtils.splitComments(comments, prefix);
//		return listComments;
//	}
	
	
//	/**
//	 * Split the comments, and attach them to the corresponding pics. Note that
//	 * more than one comments may exist in the same line, so it is necessary to
//	 * split them. TODO It is not clear what is the separator, now just use "��Ƭ"
//	 * 
//	 * @param images
//	 * @param comments
//	 */
//	private void setCommets4Image(List<ImageComment> images, String comments) {
//		List<String> listComments = separateString2List(comments, COMMENT_PREFIX_1);
//		if(listComments == null || listComments.size()==0){
//			listComments = separateString2List(comments, COMMENT_PREFIX_2);
//		}
//
//		if (images.size() >= listComments.size()) {
//			for (int i = 0; i < listComments.size(); i++) {
//				images.get(images.size() - listComments.size() + i).setComment(
//						listComments.get(i));
//			}
//		}
//	}
}
