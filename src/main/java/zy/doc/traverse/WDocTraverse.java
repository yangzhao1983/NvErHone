package zy.doc.traverse;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import zy.doc.ReplaceContent;

/**
 * This class get the "clear" doc as input, and then "replace" the content of target paragraph.
 * The "clear" doc means that doc is a intermediate product, whose paragraphs between the START and END paragraph have been cleared with only one empty paragraph left. 
 * 
 * If the content is a one line content, then just change the target paragraph.
 * Else if the content is mult-line content, then change the empty paragraph under the START paragraph. 
 * 
 * @author yangzhao
 *
 */
public class WDocTraverse implements ITraverseDocCommand{

	private List<ReplaceContent> replaceContents;
	private List<XWPFParagraph> paragraphList;
	
	private static final Logger logger = LogManager.getLogger(WDocTraverse.class.getName());
	
	public WDocTraverse(List<XWPFParagraph> paragraphList,List<ReplaceContent> replaceContent){
		this.paragraphList = paragraphList;
		this.replaceContents = replaceContent;
	}

	@Override
	public void doCommand() {

		logger.info("Start to do command");
		
		if (paragraphList != null && paragraphList.size() > 0) {
			int contentIndex = 0;
			for (XWPFParagraph paragraph : paragraphList) {
				if(contentIndex >= replaceContents.size()){
					return;
				}else{
					if(paragraph == replaceContents.get(contentIndex).getPosStart().getParagraph()){
						replaceContents.get(contentIndex).replace();
						contentIndex++;
					}			
				}
			}
		}
		
		logger.info("End to do command");
	}
}
