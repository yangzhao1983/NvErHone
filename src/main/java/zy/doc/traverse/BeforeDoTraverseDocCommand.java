package zy.doc.traverse;

import java.util.List;

import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

public class BeforeDoTraverseDocCommand extends DocTraverse {
	
	@Override
	protected boolean skipDoCommand() {

		return super.skipDoCommand();
	}

	@Override
	protected void executeCommand(XWPFParagraph paragraph) {
		
		List<XWPFRun> runs = paragraph.getRuns();
		command.setRuns(runs);
		command.doCommand();
	}



	public BeforeDoTraverseDocCommand(List<XWPFParagraph> paragraphList,
			ITraverseParagraphCommand command) {
		super(paragraphList, command);
	}
	
	public boolean skipDoCommand(XWPFParagraph paragraph) {
		return false;
	}
}
