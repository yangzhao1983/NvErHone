package zy.UI;

import zy.dso.DocObject;
import zy.utils.UIConsts;

/**
 * Panel °¸ÇéÕªÒª
 * 
 * @author yangzhao
 * 
 */
public class AbstractContentPanel extends SimpleCommentContentPanel {

	@Override
	public void saveContent(DocObject obj) {
		obj.setCaseAbstract(jtaComment.getText());
	}

	@Override
	public void clearContent() {
		this.jtaComment.setText("");
	}

	@Override
	public void setContent(DocObject obj) {
		this.jtaComment.setText(obj.getCaseAbstract());
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AbstractContentPanel() {

		super(UIConsts.LABEL_ABSTRACT);
	}
}
