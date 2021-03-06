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

public class SingleCellTraverse implements ITraverseRows {

	private IPictureContainer ipc;

	private List<XWPFTableRow> rows;
	
	private static final Logger logger = LogManager.getLogger(SingleCellTraverse.class.getName());

	/**
	 * @param rc4Two
	 */
	public SingleCellTraverse(IPictureContainer ipc, List<XWPFTableRow> rows) {
		this.ipc = ipc;
		this.rows = rows;
	}

	@Override
	public void traverseCells() {

		logger.info("Start to traverse cells");
		
		ImageComment imgC;

		try {
			for (XWPFTableRow row : rows) {

				XWPFTableCell cell = row.getTableCells().get(0);

				List<XWPFPicture> list;
				List<XWPFParagraph> paragraphList = cell.getParagraphs();

				list = UIUtils.getPicFromParagraphs(paragraphList);

				if (list != null && list.size() > 0) {
					imgC = new ImageComment();
					imgC.setImage(ImageUtil.convertPicData2Image(list.get(0)
							.getPictureData()));
					ipc.addPicture(imgC);

					if (ipc.getSize() == 1) {

						ipc.setHeight(list.get(0).getCTPicture().getSpPr()
								.getXfrm().getExt().getCy());
						ipc.setWidth(list.get(0).getCTPicture().getSpPr()
								.getXfrm().getExt().getCx());
					}

				} else {

					int picNum = ipc.getSize();
					if (picNum > 0) {

						StringBuilder sb = new StringBuilder();
						String tmp = ipc.getItem(picNum - 1).getComment();
						String oldComment = UIUtils.trimString(tmp);

						String newComment = UIUtils
								.getStringFromParagraphs(paragraphList);
						sb.append(oldComment);
						sb.append(newComment);
						ipc.getItem(picNum - 1).setComment(sb.toString());
					}
				}
			}
		} catch (IOException e) {
			
			logger.error(e.getMessage());
			logger.error("Can not traverseCells, IO Exception.");
			e.printStackTrace();
		}
		
		logger.info("End to traverse cells");
	}
}
