package zy.doc.traverse;

import java.util.List;

import org.apache.poi.xwpf.usermodel.XWPFRun;


public interface ITraverseParagraphCommand {

	public void doCommand();
	public void setRuns(List<XWPFRun> runs);
	public boolean canSkip();
	public boolean canBreak();
}
