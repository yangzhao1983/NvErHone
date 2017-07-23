package zy.utils;

import java.awt.Image;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFPicture;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.xmlbeans.XmlCursor;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTColor;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFonts;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTInd;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STLineSpacingRule;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth;

import zy.doc.CustomXWPFDocument;
import zy.doc.IReplace;
import zy.doc.ReplaceContent4OneP;
import zy.doc.ReplaceContent4TwoP;
import zy.dso.ImageArrayList;
import zy.dso.ImageComment;

public class UIUtils {
	
	private static final Logger logger = LogManager.getLogger(UIUtils.class.getName());
	
	public static ImageArrayList<ImageComment> transferListList2List(List<ImageArrayList<ImageComment>> ll){
		ImageArrayList<ImageComment> list = new ImageArrayList<ImageComment>();
		if (ll == null){
			return list;
		}
		list.setHeight(ll.get(0).getHeight());
		list.setWidth(ll.get(0).getWidth());
		
		for(ImageArrayList<ImageComment> l : ll){
			list.addAll(l);
		}
		
		return list;
	}
	
	public static void generateComments4Images(ImageArrayList<ImageComment> list, String prefix) {
		if (list != null) {
			StringBuilder sb;
			int index = 1;
			for(ImageComment ic : list){
				sb = new StringBuilder();
				sb.append(prefix);
				sb.append(index);
				sb.append(" ");
				sb.append(ic.getCommentWithoutNumber());
				ic.setComment(sb.toString());
				index++;
			}
		}
	}
	
	/** transfer picture markers to comment indexes
	 * If there is no �� before @, transfer it to ��Ƭ3-X,
	 * else if here is a comma before @ transfer it to 3-X.
	 */
	public static String transferMark2Index(String org){
		
		if(UIUtils.isEmptyString(org)){
			return "";
		}
		StringBuilder sb = new StringBuilder();
		int start = 0;
		int pos = org.indexOf(UIConsts.PICTURE_MARK);
		String prefix = "3-";
		int index = 1;
		while(pos >= 0){
			sb.append(org.substring(start, pos));
			sb.append(prefix);
			sb.append(index);
			index++;
			start = pos + 1;
			pos = org.indexOf(UIConsts.PICTURE_MARK, start);
		}
		
		if (start<org.length()) {
			sb.append(org.substring(start));
		}
		
		return sb.toString();
	}
	/**
	 * Transform @ to (��Ƭ n,��Ƭn+1)
	 * 
	 * @param str
	 * @param list
	 * @param picNum
	 * @return
	 */
	public static List<String> transferMark2PicIndex(List<String> contents, Queue<ImageArrayList<ImageComment>> qImages, String prefix){
		
		List<String> transferedContents = new ArrayList<String>();
		if(contents == null || contents.size() == 0){
			return transferedContents;
		}
		
		String str;
		int picNum = 0;
		StringBuilder sb;
		for(String content : contents){
			// remove prefix **
			str = UIUtils.removePrefix(content, prefix);	
			
			sb = new StringBuilder();
			
			int start = 0;
			int index = str.indexOf(UIConsts.PICTURE_MARK);
			
			while(index >= 0){
				if(index == 0){
					sb.append("");
				}else{
					sb.append(str.subSequence(start, index));
				}
				start = index + 1;
				
				if(start >= str.length()){
					break;
				}
				ImageArrayList<ImageComment> list = qImages.poll();
				
				if(list !=null && list.size() > 0){
					sb.append("������Ƭ");
					if (list.size()>0) {
						for (ImageComment image : list) {
							picNum++;
							sb.append(picNum);
							sb.append("��");
						}
						sb.deleteCharAt(sb.length() - 1);
					}
					sb.append("��");
				}
				index = str.indexOf(UIConsts.PICTURE_MARK, start);
			}
			
			if(start< str.length()){
				sb.append(str.substring(start, str.length()-1));
			}
			
			transferedContents.add(sb.toString());
		}	
		
		return transferedContents;
	}

	/**
	 * Add index  for comment: "{prefix} {num} {comment}"
	 * 
	 * @param prefix
	 * @param num
	 * @param comment
	 * @return
	 */
	public static String generateIndexedComment(String prefix, int num, String comment){
		
		StringBuilder sb = new StringBuilder();
		
		if(UIUtils.isEmptyString(prefix)){
			sb.append("");
		}else{
			sb.append(prefix);
		}
		
		sb.append(" ");
		sb.append(String.valueOf(num));
		sb.append(" ");
		
		if(UIUtils.isEmptyString(comment)){
			sb.append("");
		}else{
			sb.append(comment);
		}
		
		return sb.toString();
	}
	
	/**
	 * Combine multiple image list into one single list.
	 * 
	 * @param height
	 * @param width
	 * @param lists
	 * @return
	 */
//	public static ImageArrayList<ImageComment> combineMultiLists(long height, long width, List<ImageArrayList<ImageComment>> lists){
//		ImageArrayList<ImageComment> temp = new ImageArrayList<ImageComment>();
//		temp.setHeight(height);
//		temp.setWidth(width);
//		
//		for(ImageArrayList<ImageComment> item : lists){
//			temp.addAll(item);
//		}
//		return temp;
//	}

	
	/**
	 * Given the position(charPos) of a char contained by contentWithMark, return the relative index of the char.
	 * For example: content = abcd@ef@hi
	 * index for f is 6, getCharPosRel2PicMark(6, contentWithMark) should return 1.
	 * 
	 * @param charPos
	 * @param contentWithMark
	 * @returno
	 */
	public static int getCharPosRel2PicMark(int charPos, String content){
		if(charPos < 0 || UIUtils.isEmptyString(content)){
			return -1;
		}
		
		if(charPos >= content.length()){
			return 0;
		}
		int index = 0;
		int start = 0;
		int num = 0;
		index = content.indexOf(UIConsts.PICTURE_MARK, start);
	
		while (index>0){
			if(charPos <= index){
				break;
			}
			start = index + 1;
			num ++;
			index = content.indexOf(UIConsts.PICTURE_MARK, start);
		}
		return num;
	}
	
	/**
	 * Sort the values sorted by key.
	 * 
	 * @param mapIndex2Pics
	 * @return
	 */
	public static List<ImageArrayList<ImageComment>> getSortedImageArrayList(
			Map<Integer, ImageArrayList<ImageComment>> mapIndex2Pics) {
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
			for(Map.Entry<Integer, ImageArrayList<ImageComment>> item : mappingList){
				lists.add(item.getValue());
			}
		}
		return lists;
	}
	
	public static String getDateFromYMD(String year, String month, String day) {
		StringBuilder sb = new StringBuilder();
		sb.append(year);
		sb.append(UIConsts.DATE_YEAR);
		sb.append(month);
		sb.append(UIConsts.DATE_MONTH);
		sb.append(day);
		sb.append(UIConsts.DATE_DAY);
		return sb.toString();
	}

	public static boolean addNewPPr4P(XWPFParagraph p) {
		if (p == null) {
			return false;
		} else if (p.getCTP().getPPr() == null) {
			p.getCTP().addNewPPr();
		}
		return true;
	}

	public static boolean addNewRPr4Run(XWPFRun r) {
		if (r == null) {
			return false;
		} else if (r.getCTR().getRPr() == null) {
			r.getCTR().addNewRPr();
		}
		return true;
	}

	public static void addNewSpacing4P(XWPFParagraph p) {
		if (!addNewPPr4P(p)) {
			return;
		}
		if (p.getCTP().getPPr().getSpacing() == null) {
			p.getCTP().getPPr().addNewSpacing();
		}
	}

	public static boolean addNewRPr4P(XWPFParagraph p) {
		if (!addNewPPr4P(p)) {
			return false;
		}

		if (p.getCTP().getPPr().getRPr() == null) {
			p.getCTP().getPPr().addNewRPr();
		}

		return true;
	}

	public static boolean addNewNumPr4P(XWPFParagraph p) {
		if (!addNewPPr4P(p)) {
			return false;
		}

		if (p.getCTP().getPPr().getNumPr() == null) {
			p.getCTP().getPPr().addNewNumPr();
		}

		return true;
	}

	private static XWPFParagraph createPBefore(XWPFParagraph p2) {
		XmlCursor cursor = p2.getCTP().newCursor();
		XWPFParagraph newP = p2.getDocument().insertNewParagraph(cursor);
		return newP;
	}

	/**
	 * Set format for paragraph.
	 * 
	 * @param newP
	 * @param styles
	 */
	private static void setFormat4P(XWPFParagraph newP,
			Map<String, Object> styles) {

		BigInteger line = (BigInteger) styles.get("spacing:line");
		if (line != null) {
			addNewSpacing4P(newP);
			newP.getCTP().getPPr().getSpacing().setLine(line);
		}

		STLineSpacingRule.Enum lineRule = (STLineSpacingRule.Enum) styles
				.get("spacing:line-rule");
		if (lineRule != null) {
			addNewSpacing4P(newP);
			newP.getCTP().getPPr().getSpacing().setLineRule(lineRule);
		}

		// center?
		CTJc ctjc = (CTJc) styles.get("jc");
		if (ctjc != null) {
			addNewPPr4P(newP);
			newP.getCTP().getPPr().setJc(ctjc);
		}

		CTFonts pprfonts = (CTFonts) styles.get("ppr_rFonts");
		if (pprfonts != null) {
			addNewRPr4P(newP);
			newP.getCTP().getPPr().getRPr().setRFonts(pprfonts);
		}

		CTColor ctc = (CTColor) styles.get("color");
		if (ctc != null) {
			addNewRPr4P(newP);
			newP.getCTP().getPPr().getRPr().setColor(ctc);
		}

		// w:ind
		CTInd ind = (CTInd) styles.get("ind");
		if (ind != null) {
			addNewPPr4P(newP);
			newP.getCTP().getPPr().setInd(ind);
		}

		// w:outlineLvl
		CTDecimalNumber outlineLvl = (CTDecimalNumber) styles.get("outlineLvl");
		if (outlineLvl != null) {
			addNewPPr4P(newP);
			newP.getCTP().getPPr().setOutlineLvl(outlineLvl);
		}

		// <w:pPr><w:pStyle w:val="1"/>
		CTString ctsPStyle = (CTString) styles.get("pStyle");
		if (ctsPStyle != null) {
			addNewPPr4P(newP);
			newP.getCTP().getPPr().setPStyle(ctsPStyle);
		}

		// <w:pPr><w:listPr><w:ilvl w:val="0"/>
		CTDecimalNumber numIlfo = (CTDecimalNumber) styles.get("ilfo");
		if (numIlfo != null) {
			addNewNumPr4P(newP);
			// newP.getCTP().getPPr().getNumPr().setNumId(numIlfo);
			newP.getCTP().getPPr().getNumPr().addNewNumId();
			newP.getCTP().getPPr().getNumPr().getNumId()
					.setVal(numIlfo.getVal());
		}

		// <w:pPr><w:listPr><w:ilfo w:val="4"/>
		CTDecimalNumber numIlvl = (CTDecimalNumber) styles.get("ilvl");
		if (numIlvl != null) {
			addNewNumPr4P(newP);
			// newP.getCTP().getPPr().getNumPr().setNumId(numIlvl);
			newP.getCTP().getPPr().getNumPr().addNewIlvl();
			newP.getCTP().getPPr().getNumPr().getIlvl()
					.setVal(numIlvl.getVal());
		}

		// <w:pPr><w:wordWrap w:val="off"/>
		CTOnOff wordWrap = (CTOnOff) styles.get("wordwrap");
		if (wordWrap != null) {
			newP.getCTP().getPPr().addNewWordWrap();
			newP.getCTP().getPPr().getWordWrap().setVal(wordWrap.getVal());
		}
	}

	/**
	 * Set format for run.
	 * 
	 * @param run
	 * @param styles
	 */
	private static void setFormat4R(XWPFRun run, Map<String, Object> styles,
			XWPFParagraph newP) {

		CTFonts ctf = (CTFonts) styles.get("rFonts");
		if (ctf != null) {
			addNewRPr4P(newP);
			addNewRPr4Run(run);
			run.getCTR().getRPr().setRFonts(ctf);
		}

		CTHpsMeasure ctsz = (CTHpsMeasure) styles.get("sz");
		if (ctsz != null) {
			addNewRPr4P(newP);
			newP.getCTP().getPPr().getRPr().setSzCs(ctsz);
			addNewRPr4Run(run);
			run.getCTR().getRPr().setSz(ctsz);
		}

		CTHpsMeasure ctszcs = (CTHpsMeasure) styles.get("szcs");
		if (ctszcs != null) {
			addNewRPr4P(newP);
			newP.getCTP().getPPr().getRPr().setSzCs(ctszcs);
			addNewRPr4Run(run);
			run.getCTR().getRPr().setSzCs(ctszcs);
		}
	}

	/**
	 * Set content with format to paragraph
	 * 
	 * @param newP
	 * @param content
	 * @param styles
	 */
	public static void setContent(XWPFParagraph newP, String content,
			Map<String, Object> styles) {
		XWPFRun run = newP.createRun();

		setFormat4P(newP, styles);
		setFormat4R(run, styles, newP);
		run.setText(content);
	}

	public static void setContent2Paragraph(XWPFParagraph p2, String content,
			Map<String, Object> styles) {

		XWPFParagraph newP = createPBefore(p2);

		setContent(newP, content, styles);
	}

	/**
	 * Set the content to the paragraph.
	 * 
	 * 
	 * @param p
	 * @param content
	 */
	public static void setContent2Paragraph(XWPFParagraph p, String content,
			String start) {
		List<XWPFRun> runs = p.getRuns();
		String s;
		boolean isSet = false;
		boolean isReady = false;
		for (XWPFRun r : runs) {

			if (isSet) {
				r.setText("", 0);
			} else {
				s = r.getText(0);
				if (isReady || (!isEmptyString(s) && isEmptyString(start))) {
					r.setText(content, 0);
					isSet = true;
				} else if (!isEmptyString(s) && !isEmptyString(start)
						&& s.endsWith(start)) {
					isReady = true;
				}
			}
		}
	}

	public static void setContent2Paragraph(XWPFParagraph p, String content) {
		setContent2Paragraph(p, content, "");
	}

	/**
	 * Remove one places.
	 * 
	 * 
	 * @param replaces
	 * @return
	 */
	public static Queue<IReplace> getReplaces4Remove(Queue<IReplace> replaces) {

		Queue<IReplace> tmpReplaces = new LinkedList<IReplace>();

		tmpReplaces.addAll(replaces);
		Iterator<IReplace> iter = tmpReplaces.iterator();

		while (iter.hasNext()) {
			IReplace ir = iter.next();
			if (ir instanceof ReplaceContent4OneP) {
				iter.remove();
			}
		}
		iter.remove();
		return tmpReplaces;
	}

	public static Queue<IReplace> getTmpReplaces() {
		Queue<IReplace> replaces = new LinkedList<IReplace>();

		IReplace one1 = new ReplaceContent4OneP("��������Ժ˾������");
		IReplace one2 = new ReplaceContent4OneP("ί �� �ˣ�");
		IReplace one3 = new ReplaceContent4OneP("ί�����ڣ�");
		IReplace one4 = new ReplaceContent4OneP("ί�����");
		IReplace one5 = new ReplaceContent4OneP("7. ��������һʽ");
		IReplace two1 = new ReplaceContent4TwoP("�����н��������о�Ժ���蹤������˾����������",
				"��������Ժ˾������");
		IReplace one6 = new ReplaceContent4OneP("��������Ժ˾������");
		IReplace one7 = new ReplaceContent4OneP("ί���ˣ�");
		IReplace two2 = new ReplaceContent4TwoP("ί�м������", "�������ڣ�");
		IReplace one8 = new ReplaceContent4OneP("�������ڣ�");
		IReplace one9 = new ReplaceContent4OneP("�������ڣ�");
		IReplace one10 = new ReplaceContent4OneP("�����ص㣺");
		IReplace two3 = new ReplaceContent4TwoP("����ժҪ", "���̸ſ�");
		IReplace two4 = new ReplaceContent4TwoP("���̸ſ�", "�������");
		IReplace one11 = new ReplaceContent4OneP("�ֳ���������¼");
		IReplace two5 = new ReplaceContent4TwoP("�ֳ���������¼", "4.2�ֳ�ʵ����Ƭ");
		IReplace two6 = new ReplaceContent4TwoP("4.2�ֳ�ʵ����Ƭ", "����˵��");
		IReplace two7 = new ReplaceContent4TwoP("�����ֳ��������", "��������");
		IReplace two8 = new ReplaceContent4TwoP("��������", "�������");
		IReplace two9 = new ReplaceContent4TwoP("�������", "��˾��������ִҵ֤��");
		IReplace one12 = new ReplaceContent4OneP("��˾��������ִҵ֤��");
		IReplace one13 = new ReplaceContent4OneP("��˾��������ִҵ֤��");
		IReplace two10 = new ReplaceContent4TwoP("�����н��������о�Ժ���蹤������˾����������", "");
		//
		replaces.add(one1);
		replaces.add(one2);
		replaces.add(one3);
		replaces.add(one4);
		replaces.add(one5);

		replaces.add(two1);
		replaces.add(one6);
		replaces.add(one7);
		replaces.add(two2);
		replaces.add(one8);

		replaces.add(one9);
		replaces.add(one10);
		replaces.add(two3);
		replaces.add(two4);
		replaces.add(one11);

		replaces.add(two5);
		replaces.add(two6);
		replaces.add(two7);
		replaces.add(two8);
		replaces.add(two9);
		replaces.add(one12);
		replaces.add(one13);
		replaces.add(two10);

		return replaces;
	}

	/**
	 * Copy file from old path to new path.
	 * 
	 * @param oldPath
	 * @param newPath
	 */
	public static void copyFile(String oldPath, String newPath) {
		// int bytesum = 0;

		int byteread = 0;
		File oldfile = new File(oldPath);
		try {
			if (oldfile.exists()) {
				InputStream inStream = new FileInputStream(oldPath);
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];

				while ((byteread = inStream.read(buffer)) != -1) {
					// bytesum += byteread;
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
			}
		} catch (FileNotFoundException e) {
			logger.error("Can not copy file FileNotFoundException");
			logger.error(e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("Can not copy file IOException");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Get the temporary path of the file. If no suffix is imported, then set
	 * suffix to "tmp". The output of the file path is xx.suffix.docx
	 * 
	 * @param path
	 * @param suffix
	 * @return
	 */
	public static String getTmpPath(String path) {

		return getTmpPath(path, "tmp");
	}

	/**
	 * Get the temporary path of the file. If no suffix is imported, then set
	 * suffix to "tmp". The output of the file path is xx.suffix.docx
	 * 
	 * @param path
	 * @param suffix
	 * @return
	 */
	public static String getTmpPath(String path, String suffix) {

		if (suffix == null || suffix.trim().equals("")) {
			suffix = "tmp";
		}

		String tmpPath = "";
		if (path != null) {
			int index = path.lastIndexOf(".");
			if (index >= 0) {
				tmpPath = path.substring(0, index) + "." + suffix + "."
						+ "docx";
			} else {
				tmpPath = path + "." + suffix + ".docx";
			}
		}

		return tmpPath;
	}

	/**
	 * Remove "\n" for multi-lines.
	 * 
	 * @param multiLine
	 * @return
	 */
	public static String catMultiline(String multiLine) {
		if (isEmptyString(multiLine)) {
			return "";
		} else {
			String[] lines = multiLine.split("\n");
			StringBuilder sb = new StringBuilder();
			for (String s : lines) {
				sb.append(s);
			}
			return sb.toString();
		}
	}

	/**
	 * Get pictures from file.
	 * 
	 * @param path
	 * @return
	 * @throws UtilsException
	 */
	public static ImageComment getPicture(String path) {
		File imageFile = new File(path);
		ImageComment imgComment = new ImageComment();
		Image image = null;
		try {
			image = ImageIO.read(imageFile);
			imgComment.setImage(image);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return imgComment;
	}

	/**
	 * Multi-comments may exist in the same
	 * 
	 * @param comments
	 * @return
	 */
	public static List<String> splitComments(String comments, String mark) {
		List<String> list = new ArrayList<String>();
		List<Integer> indexList = new ArrayList<Integer>();
		if (comments != null) {
			Pattern p = Pattern.compile(mark);
			Matcher matcher = p.matcher(comments);
			while (matcher.find()) {
				indexList.add(matcher.start());
			}
			for (int i = 0; i < indexList.size(); i++) {
				if (i == indexList.size() - 1) {
					list.add(comments.substring(indexList.get(i)));
				} else {
					list.add(comments.substring(indexList.get(i),
							indexList.get(i + 1) - 1));
				}
			}
		}
		return list;
	}

	public static String[] splitStr(String str, String separator) {
		if (UIUtils.isEmptyString(str)) {
			return new String[0];
		} else {
			return str.split(separator);
		}
	}

	/**
	 * Test whether the strings are equal to each other.
	 * 
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static boolean strEquals(String str1, String str2) {
		return trimString(str1).equals(trimString(str2));
	}

	/**
	 * Remove the prefix from content.
	 * 
	 * @param content
	 * @param prefix
	 * @return
	 */
	public static String removePrefix(String content, String prefix) {
		if (UIUtils.isEmptyString(content) || UIUtils.isEmptyString(prefix)) {
			return "";
		}

		content = content.trim();
		return content.replaceFirst(prefix, "");
	}

	/**
	 * Test whether the first string contains the second one.
	 * 
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static boolean strContains(String str1, String str2) {
		return trimString(str1).contains(trimString(str2));
	}

	/**
	 * If string is null or contains nothing than "", then it is empty string.
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmptyString(String str) {
		if (str == null || str.trim().equals("")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Split the string of Year Month Day.
	 * 
	 * 
	 * @param ymd
	 * @return
	 */
	public static String[] splitYMD(String ymd) {
		String[] strs = new String[3];

		if (ymd == null) {
			strs[0] = "";
			strs[1] = "";
			strs[2] = "";
		}

		int yearIndex = ymd.indexOf(UIConsts.STRING_YEAR);
		strs[0] = trimString(ymd.substring(0, yearIndex));

		int monthIndex = ymd.indexOf(UIConsts.STRING_MONTH);
		strs[1] = trimString(ymd.substring(yearIndex + 1, monthIndex));

		int dayIndex = ymd.indexOf(UIConsts.STRING_DAY);
		strs[2] = trimString(ymd.substring(monthIndex + 1, dayIndex));

		return strs;
	}

	/**
	 * Concatenate the year, month and day to year��month��day��.
	 * 
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	public static String catDate(String year, String month, String day) {
		StringBuilder sb = new StringBuilder();
		sb.append(trimString(year));
		sb.append(UIConsts.STRING_YEAR);
		sb.append(trimString(month));
		sb.append(UIConsts.STRING_MONTH);
		sb.append(trimString(day));
		sb.append(UIConsts.STRING_DAY);
		return sb.toString();
	}

	/**
	 * Trim the string, and if it is null then return a space.
	 * 
	 * @param org
	 * @return
	 */
	public static String trimString(String org) {
		if (org == null) {
			return "";
		} else {
			return org.trim();
		}
	}

	/**
	 * Transfer string to the format of multi-line
	 * 
	 * @param src
	 * @return
	 */
	public static String createdMultiLineString(String src) {
		if (src == null) {
			return "";
		} else if (src.length() <= UIConsts.MULTI_LINE_LINE_LENGTH) {
			return src;
		}

		StringBuilder sb = new StringBuilder();
		sb.append("<html><pre>");
		for (int i = 0; i < src.length(); i++) {
			sb.append(src.charAt(i));
			if (i % UIConsts.MULTI_LINE_LINE_LENGTH == (UIConsts.MULTI_LINE_LINE_LENGTH - 1)) {
				sb.append("\r\n");
			}
		}
		sb.append("</html></pre>");
		// System.out.println(sb.toString());
		return sb.toString();
	}

	/**
	 * Get all the pictures from paragraphs.
	 * 
	 * @param paragraphList
	 * @return
	 */
	public static List<XWPFPicture> getPicFromParagraphs(
			List<XWPFParagraph> paragraphList) {

		List<XWPFPicture> list = new ArrayList<XWPFPicture>();
		if (paragraphList == null || paragraphList.size() == 0) {
			return list;
		}
		for (XWPFParagraph paragraph : paragraphList) {
			List<XWPFRun> runs = paragraph.getRuns();
			for (XWPFRun run : runs) {

				if (run.getEmbeddedPictures().size() > 0) {
					list.addAll(run.getEmbeddedPictures());
				}
			}
		}

		return list;
	}

	/**
	 * Set format for single cell <w:tcW w:w="10137" w:type="dxa"/>
	 * 
	 * @param cell
	 */
	public static void setFormat4SingleCell(XWPFTableCell cell) {
		cell.getCTTc().addNewTcPr().addNewTcW().setW(new BigInteger("10137"));
		cell.getCTTc().getTcPr().getTcW().setType(STTblWidth.DXA);
	}

	// /**
	// * Set format for single table.
	// * <w:tblPr>
	// * <w:tblStyle w:val="TableGrid"/>
	// * <w:tblW w:w="0" w:type="auto"/>
	// * <w:tblLook w:val="04A0"/>
	// * </w:tblPr>
	// * <w:tblGrid>
	// * <w:gridCol w:w="10137"/>
	// * </w:tblGrid>
	// *
	// * @param table
	// */
	// private static void setFormat4SingleTable(XWPFTable table) {
	// table.getCTTbl().getTblPr().getTblW().setW(new BigInteger("0"));
	// table.getCTTbl().getTblPr().getTblW().setType(STTblWidth.AUTO);
	//
	// byte[] bytes = { new Byte("4"), new Byte("-96") };
	// table.getCTTbl().getTblPr().addNewTblLook().setVal(bytes);
	//
	// table.getCTTbl().addNewTblGrid().addNewGridCol()
	// .setW(new BigInteger("10137"));
	// }

	/**
	 * Set content to the center of the cell.
	 * 
	 * @param p
	 */
	public static void setContent2Center4P(XWPFParagraph p) {
		Map<String, Object> docStyles = new HashMap<String, Object>();

		CTJc ctJc = CTJc.Factory.newInstance();
		ctJc.setVal(STJc.CENTER);
		docStyles.put("jc", ctJc);

		setFormat4P(p, docStyles);
	}

	// /**
	// * Add the pictures to word
	// *
	// * @param pics
	// * @throws InvalidFormatException
	// */
	// public static void addVerticalPics2Word(ImageArrayList<ImageComment>
	// pics,
	// XWPFParagraph paragraph, CustomXWPFDocument doc) {
	//
	// if (pics == null || pics.size() == 0) {
	// return;
	// }
	//
	// try {
	// // delete the first empty space in a paragraph
	// // standardizeParagraph(paragraph);
	//
	// XmlCursor cursor = paragraph.getCTP().newCursor();
	//
	// // XWPFParagraph cP = doc.insertNewParagraph(cursor);
	// XWPFTable table = paragraph.getDocument().insertNewTbl(cursor);
	// setFormat4SingleTable(table);
	//
	// table.getCTTbl().getTblPr().getTblBorders().unsetTop();
	// table.getCTTbl().getTblPr().getTblBorders().unsetBottom();
	// table.getCTTbl().getTblPr().getTblBorders().unsetInsideH();
	// table.getCTTbl().getTblPr().getTblBorders().unsetInsideV();
	// table.getCTTbl().getTblPr().getTblBorders().unsetLeft();
	// table.getCTTbl().getTblPr().getTblBorders().unsetRight();
	//
	// XWPFTableRow tableOneRowOne;
	//
	// tableOneRowOne = table.getRow(0);
	// XWPFTableCell cell = tableOneRowOne.getCell(0);
	// setFormat4SingleCell(cell);
	//
	// int numOfItem = 0;
	//
	// numOfItem = pics.size();
	//
	// String picId = "";
	//
	// for (int i = 0; i < numOfItem; i++) {
	// byte[] picContent = getByteContentFromIamge(pics.get(i / 2 + i
	// % 2));
	// ByteArrayInputStream byteInputStream = new ByteArrayInputStream(
	// picContent);
	// picId = doc.addPictureData(byteInputStream,
	// CustomXWPFDocument.PICTURE_TYPE_JPEG);
	//
	// XWPFParagraph p = cell.getParagraphs().get(0);
	// setContent2Center4P(p);
	// doc.createPicture(
	// picId,
	// doc.getNextPicNameNumber(XWPFDocument.PICTURE_TYPE_PNG),
	// pics.getWidth(), pics.getHeight(), p, 0);
	//
	// tableOneRowOne = table.createRow();
	// cell = tableOneRowOne.getCell(0);
	// setFormat4SingleCell(cell);
	// }
	// } catch (InvalidFormatException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }

	// /**
	// * Add the pictures to word
	// *
	// * @param pics
	// * @throws InvalidFormatException
	// */
	// public static void addPics2Word(ImageArrayList<ImageComment> pics,
	// XWPFParagraph paragraph, CustomXWPFDocument doc) {
	//
	// if (pics == null || pics.size() == 0) {
	// return;
	// }
	//
	// try {
	// // delete the first empty space in a paragraph
	// // standardizeParagraph(paragraph);
	//
	// XmlCursor cursor = paragraph.getCTP().newCursor();
	//
	// // XWPFParagraph cP = doc.insertNewParagraph(cursor);
	// XWPFTable table = paragraph.getDocument().insertNewTbl(cursor);
	//
	// table.getCTTbl().getTblPr().getTblBorders().unsetTop();
	// table.getCTTbl().getTblPr().getTblBorders().unsetBottom();
	// table.getCTTbl().getTblPr().getTblBorders().unsetInsideH();
	// table.getCTTbl().getTblPr().getTblBorders().unsetInsideV();
	// table.getCTTbl().getTblPr().getTblBorders().unsetLeft();
	// table.getCTTbl().getTblPr().getTblBorders().unsetRight();
	//
	// XWPFTableRow tableOneRowOne;
	//
	// tableOneRowOne = table.getRow(0);
	// XWPFTableCell cell = tableOneRowOne.getCell(0);
	//
	// int numOfItem = 0;
	//
	// if (pics.size() % 2 == 0) {
	// numOfItem = pics.size() * 2;
	// } else {
	// numOfItem = pics.size() * 2 + 2;
	// }
	//
	// boolean end = false;
	// for (int i = 0; i < numOfItem; i++) {
	//
	// String picId = "";
	// if (i % 4 < 2 && (i / 2 + i % 2) < pics.size()) {
	// byte[] picContent = getByteContentFromIamge(pics.get(i / 2
	// + i % 2));
	// ByteArrayInputStream byteInputStream = new ByteArrayInputStream(
	// picContent);
	// picId = doc.addPictureData(byteInputStream,
	// CustomXWPFDocument.PICTURE_TYPE_JPEG);
	//
	// if (((i / 2 + i % 2) == pics.size() - 1)
	// && (pics.size() % 2 == 1)) {
	// end = true;
	// }
	// }
	//
	// if (i == 0) {
	// // if it is the first row, then add the pic, and create new
	// // cell.
	//
	// XWPFParagraph p = cell.getParagraphs().get(0);
	//
	// doc.createPicture(
	// picId,
	// doc.getNextPicNameNumber(XWPFDocument.PICTURE_TYPE_PNG),
	// pics.getWidth(), pics.getHeight(), p, 0);
	//
	// cell = tableOneRowOne.addNewTableCell();
	// } else if (i % 4 == 1) {
	// if (!end) {
	// // add pic and add a new row
	// XWPFParagraph p = tableOneRowOne.getCell(1)
	// .getParagraphs().get(0);
	// doc.createPicture(
	// picId,
	// doc.getNextPicNameNumber(XWPFDocument.PICTURE_TYPE_PNG),
	// pics.getWidth(), pics.getHeight(), p, 0);
	// }
	// tableOneRowOne = table.createRow();
	//
	// } else if (i % 4 == 0) {
	// // add pic
	// XWPFParagraph p = tableOneRowOne.getCell(0).getParagraphs()
	// .get(0);
	// // System.out.println(tableOneRowOne.getCell(0)
	// // .getParagraphs().size());
	// doc.createPicture(
	// picId,
	// doc.getNextPicNameNumber(XWPFDocument.PICTURE_TYPE_PNG),
	// pics.getWidth(), pics.getHeight(), p, 0);
	// } else if (i % 4 == 2) {
	//
	// // add comments
	// XWPFParagraph p = tableOneRowOne.getCell(0).getParagraphs()
	// .get(0);
	//
	// // p.createRun().setText(
	// // pics.get(i / 2 + i % 2 - 1).getComment());
	//
	// setContent(p, pics.get(i / 2 + i % 2 - 1).getComment(),
	// getStyles2CellP());
	// } else if (i % 4 == 3) {
	// if (!end) {
	// // add comments
	// XWPFParagraph p = tableOneRowOne.getCell(1)
	// .getParagraphs().get(0);
	// // p.createRun().setText(
	// // pics.get(i / 2 + i % 2 - 1).getComment());
	// setContent(p, pics.get(i / 2 + i % 2 - 1).getComment(),
	// getStyles2CellP());
	// // add new row
	// tableOneRowOne = table.createRow();
	// }
	// }
	//
	// }
	// } catch (InvalidFormatException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }

	private static final String getChineseNumber(String str) {
		int num = Integer.parseInt(str);
		String s = "";
		while (num > 0) {
			int i = num % 10;
			s = UIConsts.mapArab2Chinese.get(i) + s;
			num = num / 10;
		}
		return s;
	}

	/**
	 * Add "ʮ" to the end of number.
	 * 
	 * @param str
	 * @return
	 */
	private static final String getChineseNumber4MD(String str) {
		int num = Integer.parseInt(str);
		String s = "";
		int i = 0;
		while (num > 0) {
			int j = num % 10;
			if (i == 1) {
				if(j==1){
					s = UIConsts.CH_NUMBER_TEN
							+ s;
				}else{
					if(s.equals(UIConsts.CH_NUMBER_ZERO)){
						
						s = UIConsts.mapArab2Chinese.get(j) + UIConsts.CH_NUMBER_TEN;
					}else{
						s = UIConsts.mapArab2Chinese.get(j) + UIConsts.CH_NUMBER_TEN
								+ s;	
					}
				}
			} else {
				s = UIConsts.mapArab2Chinese.get(j) + s;
			}
			i++;
			num = num / 10;
		}
		return s;
	}

	public static String convertFormatedDate2ChineseCharacter(String date,
			String separator) {
		if (date == null || separator == null) {
			return "";
		}

		String[] strs = UIUtils.splitStr(date, separator);
		if (strs == null || strs.length != 3) {
			return "";
		}

		return UIUtils.getDateFromYMD(getChineseNumber(strs[0]),
				getChineseNumber4MD(strs[1]), getChineseNumber4MD(strs[2]));
	}

	public static String convertFormatedDate2Chinese(String date,
			String separator) {
		if (date == null || separator == null) {
			return "";
		}

		String[] strs = UIUtils.splitStr(date, separator);
		if (strs == null || strs.length != 3) {
			return "";
		}

		return UIUtils.getDateFromYMD(strs[0], strs[1], strs[2]);
	}

	public static Map<String, Object> getStyles2CellP() {
		Map<String, Object> docStyles = new HashMap<String, Object>();

		BigInteger line = new BigInteger("360");
		docStyles.put("spacing:line", line);

		STLineSpacingRule.Enum lineRule = STLineSpacingRule.Enum
				.forString("auto");
		docStyles.put("spacing:line-rule", lineRule);

		CTJc jc = CTJc.Factory.newInstance();
		jc.setVal(STJc.Enum.forString("center"));
		docStyles.put("jc", jc);

		return docStyles;
	}

	/**
	 * 
	 * @param image
	 * @return
	 */
	public static byte[] getByteContentFromIamge(ImageComment image) {

		byte[] content = null;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			ImageIO.write((RenderedImage) image.getImage(),
					getPictureType(image.getType()), bos);
			content = bos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content;
	}

	/**
	 * Get the corresponding type code of the picture, according to the type of
	 * the picture.
	 * 
	 * @param picType
	 * @return String
	 */
	public static String getPictureType(int picType) {
		String res = "jpg";
		if (picType == CustomXWPFDocument.PICTURE_TYPE_PNG) {
			res = "png";
		} else if (picType == CustomXWPFDocument.PICTURE_TYPE_DIB) {
			res = "dib";
		} else if (picType == CustomXWPFDocument.PICTURE_TYPE_EMF) {
			res = "emf";
		} else if (picType == CustomXWPFDocument.PICTURE_TYPE_JPEG) {
			res = "jpeg";
		} else if (picType == CustomXWPFDocument.PICTURE_TYPE_WMF) {
			res = "wmf";
		}
		return res;
	}

	/**
	 * Get all the strings from paragraphs.
	 * 
	 * @param paragraphList
	 * @return
	 */
	public static String getStringFromParagraphs(
			List<XWPFParagraph> paragraphList) {

		StringBuilder sb = new StringBuilder();
		if (paragraphList == null || paragraphList.size() == 0) {
			return "";
		}
		for (XWPFParagraph paragraph : paragraphList) {
			sb.append(trimString(paragraph.getParagraphText()));
		}

		return sb.toString();
	}
}
