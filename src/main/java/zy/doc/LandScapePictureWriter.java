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
import zy.utils.UIUtils;

/**
 * LandScape picture wirter, one row/two pictures.
 * 
 * 
 * @author yangzhao
 * 
 */
public class LandScapePictureWriter implements IPictureWriter {

	private static final Logger logger = LogManager.getLogger(LandScapePictureWriter.class.getName());
	
	@Override
	public void writePicture2Doc(ImageArrayList<ImageComment> pics,
			XWPFParagraph paragraph, CustomXWPFDocument doc) {
		logger.info("start to write pictures to doc");
		
		if (pics == null || pics.size() == 0) {
			
			logger.info("No pictures!");
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

			int numOfItem = 0;

			if (pics.size() % 2 == 0) {
				numOfItem = pics.size() * 2;
			} else {
				numOfItem = pics.size() * 2 + 2;
			}

			boolean end = false;
			for (int i = 0; i < numOfItem; i++) {

				String picId = "";
				if (i % 4 < 2 && (i / 2 + i % 2) < pics.size()) {
					
					logger.info("Start for picture line.");
					
					byte[] picContent = UIUtils.getByteContentFromIamge(pics
							.get(i / 2 + i % 2));
					ByteArrayInputStream byteInputStream = new ByteArrayInputStream(
							picContent);
					picId = doc.addPictureData(byteInputStream,
							CustomXWPFDocument.PICTURE_TYPE_JPEG);

					if (((i / 2 + i % 2) == pics.size() - 1)
							&& (pics.size() % 2 == 1)) {
						end = true;
					}
					logger.info("End for picture line.");
				}

				if (i == 0) {
					logger.info("Start for the first line and the first cell.");
					// if it is the first row, then add the pic, and create new
					// cell.

					XWPFParagraph p = cell.getParagraphs().get(0);

					doc.createPicture(
							picId,
							doc.getNextPicNameNumber(XWPFDocument.PICTURE_TYPE_PNG),
							pics.getWidth(), pics.getHeight(), p, 0);

					cell = tableOneRowOne.addNewTableCell();
					logger.info("End for the picture line and the first cell.");
				} else if (i % 4 == 1) {
					logger.info("Start for the picture line and the second cell.");
					
					if (!end) {
						// add pic and add a new row
						XWPFParagraph p = tableOneRowOne.getCell(1)
								.getParagraphs().get(0);
						doc.createPicture(
								picId,
								doc.getNextPicNameNumber(XWPFDocument.PICTURE_TYPE_PNG),
								pics.getWidth(), pics.getHeight(), p, 0);
					}
					tableOneRowOne = table.createRow();
					logger.info("End for the picture line and the second cell.");
				} else if (i % 4 == 0) {
					logger.info("Start for the picture line and the first cell.");
					
					// add pic
					XWPFParagraph p = tableOneRowOne.getCell(0).getParagraphs()
							.get(0);
					// System.out.println(tableOneRowOne.getCell(0)
					// .getParagraphs().size());
					doc.createPicture(
							picId,
							doc.getNextPicNameNumber(XWPFDocument.PICTURE_TYPE_PNG),
							pics.getWidth(), pics.getHeight(), p, 0);
					logger.info("End for the picture line and the first cell.");
				} else if (i % 4 == 2) {
					logger.info("Start for the comment line and the first cell.");
					// p.createRun().setText(
					// pics.get(i / 2 + i % 2 - 1).getComment());

					int num = i / 2 + i % 2 - 1;
					String content = pics.get(num).getComment();
					if (!UIUtils.isEmptyString(content)) {
						// add comments
						XWPFParagraph p = tableOneRowOne.getCell(0)
								.getParagraphs().get(0);
						UIUtils.setContent(p, pics.get(num).getComment(),
								UIUtils.getStyles2CellP());
					}
					logger.info("End for the comment line and the first cell.");
				} else if (i % 4 == 3) {
					logger.info("Start for the comment line and the second cell.");
					if (!end) {
						logger.info("End.");
						int num = i / 2 + i % 2 - 1;
						String content = pics.get(num).getComment();

						if (!UIUtils.isEmptyString(content)) {
							// add comments
							XWPFParagraph p = tableOneRowOne.getCell(1)
									.getParagraphs().get(0);
							// p.createRun().setText(
							// pics.get(i / 2 + i % 2 - 1).getComment());
							UIUtils.setContent(p,

							pics.get(num).getComment(),
									UIUtils.getStyles2CellP());
						}
						// add new row
						tableOneRowOne = table.createRow();
					}
					logger.info("End for the comment line and the second cell.");
				}

			}
		} catch (InvalidFormatException e) {
			logger.error(e.getMessage());
			logger.error("Can not write pictures to doc");
			e.printStackTrace();
		}
		
		logger.info("end to write pictures to doc");
	}
}
