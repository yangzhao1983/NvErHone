package zy.doc.traverse;

import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import zy.doc.ContentPosition;
import zy.doc.IReplace;
import zy.doc.ReplaceContent4TwoP;

/**
 * This class create a CLEAR doc as input.
 * The "clear" doc means that doc is a intermediate product, whose paragraphs between the START and END paragraph have been cleared with only one empty paragraph left. 
 * 
 * @author yangzhao
 *
 */
public class DDocTraverse implements ITraverseDocCommand{

	private Queue<IReplace> replaceContents;
	private List<XWPFParagraph> paragraphList;
	
	private static final Logger logger = LogManager.getLogger(DDocTraverse.class.getName());
	
	public DDocTraverse(List<XWPFParagraph> paragraphList,Queue<IReplace> rcs){
		this.paragraphList = paragraphList;
		this.replaceContents = rcs;
	}

	@Override
	public void doCommand() {
		
		logger.info("Start to do command");
		
		Stack<Integer> indexes4Remove = new Stack<Integer>();
		
		if (paragraphList != null && paragraphList.size() > 0) {
			XWPFDocument doc = paragraphList.get(0).getDocument();
			
			Iterator<IReplace> iter = replaceContents.iterator();
			ReplaceContent4TwoP rc = (ReplaceContent4TwoP)iter.next();;
			boolean toRemove = false;
			ContentPosition curStart = rc.getPosStart();
			ContentPosition curEnd = rc.getPosEnd();					
			int curStartInt = doc.getPosOfParagraph(curStart.getParagraph());;
			int curEndInt =doc.getPosOfParagraph(curEnd.getParagraph());
			
			for (XWPFParagraph paragraph : paragraphList) {	
				
				int cur = doc.getPosOfParagraph(paragraph);
				
				if(toRemove){
					
					if(curStartInt < cur && curEndInt>cur){
						indexes4Remove.push(cur);
					}else if(curEndInt == cur){
						toRemove = false;
						if(!iter.hasNext()){
							break;
						}
						rc= (ReplaceContent4TwoP)iter.next();
						curStart = rc.getPosStart();
						curEnd = rc.getPosEnd();
						curStartInt = doc.getPosOfParagraph(curStart.getParagraph());
						curEndInt = doc.getPosOfParagraph(curEnd.getParagraph());
						if(curEndInt == curStartInt + 1){
							indexes4Remove.push(curEndInt);
						}
						if(curStartInt == cur){
							toRemove = true;
						}
					}
				}else{
					if(cur == curStartInt){
						toRemove = true;
					}
				}
			}
			
			while(!indexes4Remove.empty()){
				Integer index = indexes4Remove.pop();
				doc.removeBodyElement(index);
			}
		}
		
		logger.info("End to do command");
	}
}
