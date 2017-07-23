package zy.UI;

import javax.swing.JFrame;

import zy.dso.DocObject;
import zy.dso.ImageArrayList;
import zy.dso.ImageComment;
import zy.utils.UIConsts;

/**
 * ¿±Ñé¹ý³Ì
 * 
 * @author yangzhao
 *
 */
public class SurveyNoteContentPanel extends ContentPicPanel {

	@Override
	public void saveContent(DocObject obj) {

		obj.setSurveyNote(jtaContent.getText());
		obj.setSurveyPics(getIamgeList());
		obj.setSurveyPicsList(this.getImageListList());
		obj.setSurveyOrientation(this.getRadioButton());
	}

	@Override
	public void clearContent() {
		jtaContent.setText("");
		setIamgeList(new ImageArrayList<ImageComment>());
	}

	@Override
	public void setContent(DocObject obj) {
		jtaContent.setText(obj.getSurveyNote());
		setIamgeList(obj.getSurveyPics());
		this.setImageListList(obj.getSurveyPicsList());
		if (obj.isSurveyOrientation() == UIConsts.ORIENTATION_LANDSCAPE) {
			this.setRadioButton(UIConsts.INDEX_ORIENTATION_LANDSCAPE);
		} else {
			this.setRadioButton(UIConsts.INDEX_ORIENTATION_PORTRAIT);
		}
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SurveyNoteContentPanel( JFrame frame ) {

		super(UIConsts.LABEL_SURVEY, frame);
	}
}
