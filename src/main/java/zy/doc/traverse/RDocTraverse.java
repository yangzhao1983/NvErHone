package zy.doc.traverse;

import java.util.List;

import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

public class RDocTraverse extends DocTraverse{

	@Override
	protected void executeCommand(XWPFParagraph paragraph) {
		List<XWPFRun> runs = paragraph.getRuns();
		command.setRuns(runs);
		command.doCommand();
	}

	public RDocTraverse(List<XWPFParagraph> paragraphList,
			ITraverseParagraphCommand command){
		super(paragraphList, command);
	}
}
