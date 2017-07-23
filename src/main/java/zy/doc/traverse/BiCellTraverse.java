package zy.doc.traverse;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFPicture;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import zy.dso.ImageComment;
import zy.utils.ImageUtil;
import zy.utils.UIUtils;

public class BiCellTraverse implements ITraverseRows {

	private IPictureContainer ipc;

	private List<XWPFTableRow> rows;

	private static final Logger logger = LogManager.getLogger(BiCellTraverse.class.getName());
	
	/**
	 * @param rc4Two
	 */
	public BiCellTraverse(IPictureContainer ipc, List<XWPFTableRow> rows) {
		this.ipc = ipc;
		this.rows = rows;
	}

	@Override
	public void traverseCells() {

		logger.info("Start to traverseCells");
		
		ImageComment imgC;

		try {
			int index = 1;
			List<XWPFPicture> list;

			for (XWPFTableRow row : rows) {

				List<XWPFTableCell> cells = row.getTableCells();

				for (XWPFTableCell cell : cells) {

					List<XWPFParagraph> paragraphList = cell.getParagraphs();
					list = UIUtils.getPicFromParagraphs(paragraphList);

					if (list != null && list.size() > 0) {
						imgC = new ImageComment();
						imgC.setImage(ImageUtil.convertPicData2Image(list
								.get(0).getPictureData()));
						ipc.addPicture(imgC);

						if (ipc.getSize() == 1) {

							ipc.setHeight(list.get(0).getCTPicture().getSpPr()
									.getXfrm().getExt().getCy());
							ipc.setWidth(list.get(0).getCTPicture().getSpPr()
									.getXfrm().getExt().getCx());
						}

					} else {

						int picNum = ipc.getSize();
						if (picNum > 1) {
							String comment = UIUtils
									.getStringFromParagraphs(paragraphList);

							if (index % 4 == 3) {
								ipc.getItem(picNum - 2).setComment(comment);
							} else if (index % 4 == 0) {
								ipc.getItem(picNum - 1).setComment(comment);
							}
						}
					}
					index++;
				}

			}
			
			logger.info("End to traverseCells");
		} catch (IOException e) {
			
			logger.error(e.getMessage());
			logger.error("Can not traverseCells, IO Exception.");
			e.printStackTrace();
		}
	}
}
