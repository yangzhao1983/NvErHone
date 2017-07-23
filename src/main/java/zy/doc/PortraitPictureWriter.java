package zy.doc;

import java.io.ByteArrayInputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.apache.xmlbeans.XmlCursor;

import zy.dso.ImageArrayList;
import zy.dso.ImageComment;
import zy.utils.UIConsts;
import zy.utils.UIUtils;

/**
 * Portrait picture writer, one row one picture.
 * 
 * 
 * @author yangzhao
 * 
 */
public class PortraitPictureWriter implements IPictureWriter {

	private static final Logger logger = LogManager.getLogger(PortraitPictureWriter.class.getName());
	
	@Override
	public void writePicture2Doc(ImageArrayList<ImageComment> pics,
			XWPFParagraph paragraph, CustomXWPFDocument doc) {
		
		logger.info("start to write pictures to doc Portrait.");
		
		if (pics == null || pics.size() == 0) {
			return;
		}

		try {
			// delete the first empty space in a paragraph
			// standardizeParagraph(paragraph);

			XmlCursor cursor = paragraph.getCTP().newCursor();

			// XWPFParagraph cP = doc.insertNewParagraph(cursor);
			XWPFTable table = paragraph.getDocument().insertNewTbl(cursor);

			table.getCTTbl().getTblPr().getTblBorders().unsetTop();
			table.getCTTbl().getTblPr().getTblBorders().unsetBottom();
			table.getCTTbl().getTblPr().getTblBorders().unsetInsideH();
			table.getCTTbl().getTblPr().getTblBorders().unsetInsideV();
			table.getCTTbl().getTblPr().getTblBorders().unsetLeft();
			table.getCTTbl().getTblPr().getTblBorders().unsetRight();

			XWPFTableRow tableOneRowOne;

			tableOneRowOne = table.getRow(0);
			XWPFTableCell cell = tableOneRowOne.getCell(0);
			UIUtils.setFormat4SingleCell(cell);
			int numOfItem = pics.size() * 2;

			for (int i = 0; i < numOfItem; i++) {

				String picId = "";

				// add picture
				if (i % 2 == 0) {
					
					logger.info("start to deal with picture line Portrait.");
					byte[] picContent = UIUtils.getByteContentFromIamge(pics
							.get(i / 2));
					ByteArrayInputStream byteInputStream = new ByteArrayInputStream(
							picContent);
					picId = doc.addPictureData(byteInputStream,
							CustomXWPFDocument.PICTURE_TYPE_JPEG);

					// add pic and add a new row
					XWPFParagraph p = cell.getParagraphs().get(0);
					UIUtils.setContent2Center4P(p);

					doc.createPicture(
							picId,
							doc.getNextPicNameNumber(XWPFDocument.PICTURE_TYPE_PNG),
							pics.getWidth(), pics.getHeight(), p, 0);

					// add comment
					logger.info("End to deal with picture line Portrait.");
				} else {
					logger.info("start to deal with comment line Portrait.");
					int num = i / 2;
					String comment = pics.get(num).getComment();
					if (!UIUtils.isEmptyString(comment)) {
						XWPFParagraph p = cell.getParagraphs().get(0);

						UIUtils.setContent(p, UIUtils.generateIndexedComment(
								UIConsts.COMMENT_PREFIX_1, num+1, pics.get(num).getCommentWithoutNumber()), UIUtils
								.getStyles2CellP());
					}

					logger.info("end to deal with comment line Portrait.");
				}

				if (i < numOfItem - 1) {
					
					logger.info("start to create row Portrait.");
					tableOneRowOne = table.createRow();
					cell = tableOneRowOne.getCell(0);
					UIUtils.setFormat4SingleCell(cell);
					logger.info("end to create row Portrait.");
				}
			}

		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
			logger.error("Failed to write pictures to doc Portrait.");
			e.printStackTrace();
		}
		
		logger.info("end to write pictures to doc Portrait.");
	}

}
