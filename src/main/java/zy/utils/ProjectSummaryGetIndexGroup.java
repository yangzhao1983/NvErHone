package zy.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import zy.dso.ImageComment;

public class ProjectSummaryGetIndexGroup extends GetIndexGroupTemplate {

	@Override
	protected List<String> getIndexs(String str) {
		Pattern pattern1 = Pattern.compile(SUB_PATTERN_PROJECT_SUMMARY);
		Matcher m = pattern1.matcher(str);
		List<String> indexes = new ArrayList<String>();
		String index = null;
		while (m.find()) {
			index = m.group(1);
			indexes.add(index);
		}
		return indexes;
	}

	@Override
	protected Integer getImageIndex(ImageComment image) {
		Integer imageIndex = Integer.parseInt(image
				.getNumbers4Comment("’’∆¨\\d-(\\d)"));
		return imageIndex;
	}

	private static String PATTERN_PROJECT_SUMMARY = "\\d-\\d";
	private static String SUB_PATTERN_PROJECT_SUMMARY = "\\d-(\\d)";
	
	public ProjectSummaryGetIndexGroup(String content){
		super(content);
	}
	
	@Override
	protected void initializeContent() {
	}

	@Override
	protected List<String> dealWithContent() {

		Pattern pattern = Pattern.compile(PATTERN_PROJECT_SUMMARY);
		Matcher matcher = pattern.matcher(content);

		String strInBracket;
		while (matcher.find()) {
			strInBracket = matcher.group();
			indexGroups.add(strInBracket);
		}

		return indexGroups;
	}
}
