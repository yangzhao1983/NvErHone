package zy.date;

import java.util.Date;

import zy.UI.DateTextFiled;
import zy.utils.UIUtils;

public class DateChooserChinese extends DateChooser {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DateChooserChinese(DateTextFiled jftf) {
		super(jftf);
	}

	@Override
	public void setDate(Date date) {
		jFormattedTextField.setText(UIUtils
				.convertFormatedDate2ChineseCharacter(getDefaultDateFormat()
						.format(date), "-"));
	}

}
