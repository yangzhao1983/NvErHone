package zy.UI;

import zy.dso.DocObject;
import zy.utils.UIConsts;
import zy.utils.UIUtils;

/**
 * ¼ø¶¨ÒÀ¾Ý
 * 
 * @author yangzhao
 */
public class EvidenceContentPanel extends SimpleCommentContentPanel implements
		IContentPanelControl {

	@Override
	public void saveContent(DocObject obj) {

		obj.setStrGist(UIUtils.trimString(jtaComment.getText()));
	}

	@Override
	public void clearContent() {
		this.jtaComment.setText("");

	}

	@Override
	public void setContent(DocObject obj) {
		this.jtaComment.setText(obj.getStrGist());
	}

	/**
		 * 
		 */
	private static final long serialVersionUID = 1L;

	public EvidenceContentPanel() {

		super(UIConsts.LABEL_EVIDENCE);
	}
}
