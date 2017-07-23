package zy.doc;

import java.util.List;

import org.apache.poi.xwpf.usermodel.XWPFRun;

/**
 * Interface describing the behaviors about replace.
 * 
 * @author yangzhao
 *
 */
public interface IReplace {

	/**
	 * Replace the content.
	 */
	public void replace();

	/**
	 * If the runs will be replaced, then return true; else return false.
	 * 
	 * @param runs
	 * @return
	 */
	public boolean willReplace(List<XWPFRun> runs);
}
