package zy.UI;

import zy.dso.DocObject;
import zy.utils.UIConsts;
import zy.utils.UIUtils;

/**
 * ¼ø¶¨Òâ¼û
 * 
 * @author yangzhao
 */
public class IdentifyContentPanel extends SimpleCommentContentPanel implements
IContentPanelControl{
		
		@Override
	public void saveContent(DocObject obj) {
		
		obj.setComments(UIUtils.trimString(jtaComment.getText()));
	}

	@Override
	public void clearContent() {
		this.jtaComment.setText("");
		
	}

	@Override
	public void setContent(DocObject obj) {
		this.jtaComment.setText(obj.getComments());
	}

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		public IdentifyContentPanel(){
			
			super(UIConsts.LABEL_IDENTIFY);
		}
	}
