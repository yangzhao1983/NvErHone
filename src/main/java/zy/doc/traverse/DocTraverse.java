package zy.doc.traverse;

import java.util.List;

import org.apache.poi.xwpf.usermodel.XWPFParagraph;

/**
 * Use this class to traverse the doc, and for every run do the Traversal
 * command.
 * 
 * @author yangzhao
 * 
 */
public abstract class DocTraverse implements ITraverseDocCommand {

	protected boolean skipDoCommand() {
		return command.canSkip();
	}

	protected boolean breakDoCommand(){
		return command.canBreak();
	}
	
	protected abstract void executeCommand(XWPFParagraph paragraph);
	
	@Override
	public void doCommand() {
		if (command == null) {
			return;
		}

		if (paragraphList != null && paragraphList.size() > 0) {
		
			for (XWPFParagraph paragraph : paragraphList) {
				if(skipDoCommand()){
					continue;
				}		
				executeCommand(paragraph);
				if(breakDoCommand()){
					break;
				}
			}
		}
	}

	private List<XWPFParagraph> paragraphList;

	protected ITraverseParagraphCommand command;

	public DocTraverse(List<XWPFParagraph> paragraphList,
			ITraverseParagraphCommand command) {
		this.paragraphList = paragraphList;
		this.command = command;
	}
}
