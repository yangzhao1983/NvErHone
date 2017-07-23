package zy.doc.traverse;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

/**
 * Generate traverses according to the number of the rows.
 * 
 * @author yangzhao
 * 
 */
public class CellTraverseFactory {
	
	private static final Logger logger = LogManager.getLogger(CellTraverseFactory.class.getName());
	
	static public ITraverseRows getTraverseRows(IPictureContainer ipc, List<XWPFTableRow> rows) {
		logger.info("Start CellTraverseFactory.");
		if (rows.get(0).getTableCells().size() == 1) {
			logger.info("return single cell traverse");
			return new SingleCellTraverse(ipc, rows);
		} else {
			logger.info("return bi cell traverse");
			return new BiCellTraverse(ipc, rows);
		}
	}
}
