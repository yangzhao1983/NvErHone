package zy.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import zy.dso.ImageComment;

public class SurveyNoteGetIndexGroup extends GetIndexGroupTemplate {

	private static String PATTERN_SURVEY_NOTE = "£®.*?£©";
	private static String SUB_PATTERN_SURVEY_NOTE = "\\d+";

	public SurveyNoteGetIndexGroup(String content) {
		super(content);
	}

	@Override
	protected void initializeContent() {

		content = content.replaceAll("\n", "");
	}

	@Override
	protected List<String> dealWithContent() {
		String str = content.replaceAll("\n", "");
		Pattern pattern = Pattern.compile(PATTERN_SURVEY_NOTE);
		Matcher matcher = pattern.matcher(str);

		String strInBracket;
		while (matcher.find()) {
			strInBracket = matcher.group();
			indexGroups.add(strInBracket);
		}
		return indexGroups;
	}

	@Override
	protected List<String> getIndexs(String str) {

		Pattern pattern1 = Pattern.compile(SUB_PATTERN_SURVEY_NOTE);
		Matcher m = pattern1.matcher(str);
		List<String> indexes = new ArrayList<String>();
		String index = null;
		while (m.find()) {
			index = m.group();
			indexes.add(index);
		}
		return indexes;
	}

	@Override
	protected Integer getImageIndex(ImageComment image) {
		Integer imageIndex = Integer.parseInt(image
				.getNumbers4Comment("(?:’’∆¨\\s*)(\\d)"));
		return imageIndex;
	}

}
