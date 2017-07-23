package zy.utils;

import java.util.List;

import zy.dso.ImageArrayList;
import zy.dso.ImageComment;

public interface IGetIndexGroup {

	public String getContentWithMark();

	public List<ImageArrayList<ImageComment>> getSortedImageArrayList(
			ImageArrayList<ImageComment> pics);
}
