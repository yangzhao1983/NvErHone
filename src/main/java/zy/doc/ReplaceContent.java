package zy.doc;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xwpf.usermodel.XWPFRun;

/**
 * The subclass of twoP and oneP.
 * 
 * @author yangzhao
 *
 */
public abstract class ReplaceContent implements IReplace{

	private static final Logger logger = LogManager.getLogger(ReplaceContent.class.getName());
	
	//	/**
//	 * The type number of the type:
//	 * 1: Paragraph identified by the starting strings, the remaining content will be replaced.
//	 * 2: Paragraph a identified by the starting strings, paragraph b identified by the starting strings. The content between these paragraphs will be replaced.
//	 */
//	private int intTypeNo;
	public ReplaceContent(String strContentStart){
		this.posStart = new ContentPosition(strContentStart);
	}
	
	@Override
	public void replace() {
		
	}
	
	public ContentPosition getPosStart() {
		return posStart;
	}

	/**
	 * The first position
	 */
	private ContentPosition posStart;

	@Override
	public boolean willReplace(List<XWPFRun> runs) {
		return doWillReplace(runs, posStart);
	}
	
	@SuppressWarnings("deprecation")
	protected boolean doWillReplace(List<XWPFRun> runs, ContentPosition cp){
		
		logger.info("start to doWillReplace.");
		
		String content = cp.getContent();
		boolean rel = false;
		int length = content.length();
		StringBuilder sb = new StringBuilder();
		int num = 0;
		for (XWPFRun run : runs) {
			String text = run.getText(0);

			if (text == null || text.trim().equals("")) {
				continue;
			}
			
			sb.append(text.trim());
			num = sb.toString().length();
			if (num >= length && sb.toString().startsWith(content)) {
				
				cp.setParagraph(runs.get(0).getParagraph());
				rel = true;
				break;
			}

			if (num > length) {
				break;
			}
		}
		logger.info("end to doWillReplace.");
		return rel;
	}
}
