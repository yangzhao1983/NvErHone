package zy.UI;

import javax.swing.JFrame;

import zy.dso.DocObject;
import zy.dso.ImageArrayList;
import zy.dso.ImageComment;
import zy.utils.UIConsts;
import zy.utils.UIUtils;

/**
 * Panel ¹¤³Ì¸Å¿ö
 * 
 * @author yangzhao
 *
 */
public class SummaryContentPanel extends ContentPicPanel {

	@Override
	public void saveContent(DocObject obj) {

		obj.setProjectSummary(UIUtils.catMultiline(jtaContent.getText()));
		obj.setSummaryPics(getIamgeList());
		obj.setSummaryPicsList(this.getImageListList());
		obj.setProjectSummaryOrientation(this.getRadioButton());
	}

	@Override
	public void clearContent() {
		jtaContent.setText("");
		setIamgeList(new ImageArrayList<ImageComment>());
	}

	@Override
	public void setContent(DocObject obj) {
		jtaContent.setText(obj.getProjectSummary());
		setIamgeList(obj.getSummaryPics());
		this.setImageListList(obj.getSummaryPicsList());
		if(obj.isProjectSummaryOrientation() == UIConsts.ORIENTATION_LANDSCAPE){
			this.setRadioButton(UIConsts.INDEX_ORIENTATION_LANDSCAPE);
		}else{
			this.setRadioButton(UIConsts.INDEX_ORIENTATION_PORTRAIT);
		}
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SummaryContentPanel(JFrame frame) {

		super(UIConsts.LABEL_SUMMARY, frame);
	}
}
