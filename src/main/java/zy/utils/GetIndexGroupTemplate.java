package zy.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import zy.dso.ImageArrayList;
import zy.dso.ImageComment;

public abstract class GetIndexGroupTemplate implements IGetIndexGroup {

	protected String content;

	protected List<String> indexGroups = new ArrayList<String>();

	abstract protected List<String> getIndexs(String str);

	abstract protected Integer getImageIndex(ImageComment image);

	public GetIndexGroupTemplate(String content) {
		this.content = content;
		getIndexGroup();
	}

	protected abstract void initializeContent();

	protected abstract List<String> dealWithContent();

	@Override
	public List<ImageArrayList<ImageComment>> getSortedImageArrayList(
			ImageArrayList<ImageComment> pics) {

		// 1. get indexes of the (index-n,index-n+1...index-n+m), maps is pairs
		// of 1->index(n,n+1,n+2,..n+m), 2->(n+m+1, n+m+2,..n+m+k)
		List<String> indexes;
		Map<String, List<String>> maps = new HashMap<String, List<String>>();
		Integer intIndex = 0;
		for (String str : indexGroups) {
			indexes = getIndexs(str);
			if (indexes.size() > 0) {
				maps.put(intIndex.toString(), indexes);
			}
			intIndex++;
		}

		// 2. Map the indexes to pictures
		Map<Integer, ImageArrayList<ImageComment>> mapIndex2Pics = new HashMap<Integer, ImageArrayList<ImageComment>>();
		ImageArrayList<ImageComment> listImage;
		Set<String> keys = maps.keySet();

		for (ImageComment image : pics) {

			Integer imageIndex = getImageIndex(image);
			// Integer imageIndex = Integer.parseInt(image
			// .getNumbers4Comment("(?:’’∆¨\\s*)(\\d)"));

			for (String key : keys) {
				if (maps.get(key).contains(imageIndex.toString())) {
					if (mapIndex2Pics.get(Integer.parseInt(key)) == null) {
						listImage = new ImageArrayList<ImageComment>();
						listImage.add(image);
						listImage.setHeight(pics.getHeight());
						listImage.setWidth(pics.getWidth());
						listImage.setOrientation(pics.getOrientation());
						mapIndex2Pics.put(Integer.parseInt(key), listImage);
					} else {
						mapIndex2Pics.get(Integer.parseInt(key)).add(image);
					}
					break;
				}
			}
		}

		for (String key : keys) {
			if (mapIndex2Pics.get(Integer.parseInt(key)) == null) {
				listImage = new ImageArrayList<ImageComment>();
				listImage.setHeight(pics.getHeight());
				listImage.setWidth(pics.getWidth());
				listImage.setOrientation(pics.getOrientation());
				mapIndex2Pics.put(Integer.parseInt(key), listImage);
			}
		}

		// Sort the lists of pictures
		List<ImageArrayList<ImageComment>> lists = new ArrayList<ImageArrayList<ImageComment>>();
		if (mapIndex2Pics != null) {
			List<Map.Entry<Integer, ImageArrayList<ImageComment>>> mappingList = new ArrayList<Map.Entry<Integer, ImageArrayList<ImageComment>>>(
					mapIndex2Pics.entrySet());

			Collections
					.sort(mappingList,
							new Comparator<Map.Entry<Integer, ImageArrayList<ImageComment>>>() {
								public int compare(
										Map.Entry<Integer, ImageArrayList<ImageComment>> mapping1,
										Map.Entry<Integer, ImageArrayList<ImageComment>> mapping2) {
									return mapping1.getKey().compareTo(
											mapping2.getKey());
								}
							});
			for (Map.Entry<Integer, ImageArrayList<ImageComment>> item : mappingList) {
				lists.add(item.getValue());
			}
		}
		return lists;
	}

	private void getIndexGroup() {

		if (!UIUtils.isEmptyString(content)) {
			initializeContent();
			indexGroups = dealWithContent();
		}
	}

	@Override
	public String getContentWithMark() {

		StringBuilder sb = new StringBuilder();
		int offset = 0;
		int start = 0;
		for (String str : indexGroups) {
			start = this.content.indexOf(str, offset);
			if (start > 0) {
				sb.append(content.substring(offset, start));
				sb.append(UIConsts.PICTURE_MARK);
				offset = start + str.length();
			}
		}
		sb.append(this.content.substring(offset));
		
		// added "\n" before "**"
		String s1 = sb.toString();	
		String s2 = s1.replaceAll("\\*\\*", "\n\\*\\*");
		if(s2.substring(0,1).equals("\n")){
			s2 = s2.substring(1);
		}

		return s2;
	}

}
