package zy.doc.traverse;

import java.util.List;

import org.apache.poi.xwpf.usermodel.XWPFRun;

import zy.doc.ReplaceContent;

/**
 * For every run, get the positions of start Mark and end Mark.
 * 
 * @author yangzhao
 * 
 */
public class WriteContentTraverseParagraphCommand implements
		ITraverseParagraphCommand {

	private List<XWPFRun> runs;

	private int pIndex = 0;
	private boolean canSkip;
	private boolean canBreak;

	@Override
	public boolean canSkip() {
		return canSkip;
	}

	@Override
	public boolean canBreak() {
		return canBreak;
	}

	private List<ReplaceContent> replaceContents;

	public WriteContentTraverseParagraphCommand(List<ReplaceContent> replaceContents) {
		this.replaceContents = replaceContents;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void doCommand() {
		if (runs == null || runs.size() == 0) {
			return;
		}
		
		if(runs.get(0).getParagraph() == replaceContents.get(pIndex).getPosStart().getParagraph()){
			replaceContents.get(pIndex).replace();
			pIndex++;
		}
	}

	@Override
	public void setRuns(List<XWPFRun> runs) {
		this.runs = runs;
	}

}
