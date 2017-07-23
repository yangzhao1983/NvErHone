package zy.doc.traverse;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import zy.doc.ContentPosition;

/**
 * For every run, get the positions of start Mark and end Mark.
 * 
 * @author yangzhao
 * 
 */
public class GetContentPosTraverseParagraphCommand implements
		ITraverseParagraphCommand {

	private List<XWPFRun> runs;
	
	private boolean canBreak;

	private static final Logger logger = LogManager.getLogger(GetContentPosTraverseParagraphCommand.class.getName());
	
	@Override
	public boolean canSkip() {
		return false;
	}

	@Override
	public boolean canBreak() {
		return canBreak;
	}

	private ContentPosition pos;

	public GetContentPosTraverseParagraphCommand(ContentPosition pos) {
		this.pos = pos;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void doCommand() {
		
		logger.info("Start to do command");
		
		if (runs == null || runs.size() == 0) {
			return;
		}

		StringBuilder sb = new StringBuilder();
		int num = 0;
		int length = pos.getContent().length();

		for (XWPFRun run : runs) {
			String text = run.getText(0);

			if (text == null || text.trim().equals("")) {
				continue;
			}
			sb.append(text);
			num = sb.toString().length();
			if (num >= length && sb.toString().startsWith(pos.getContent())) {
				pos.setParagraph(run.getParagraph());
				pos.setPos(runs.indexOf(run));
				pos.setInitialized(true);
				this.canBreak = true;
				break;
			}

			if (num > length) {
				break;
			}
		}
		
		logger.info("End to do command");
	}

	@Override
	public void setRuns(List<XWPFRun> runs) {
		this.runs = runs;
	}
}
