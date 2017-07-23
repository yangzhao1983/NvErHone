package zy.dso;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import zy.doc.CustomXWPFDocument;
import zy.doc.IReplace;
import zy.doc.ReplaceContent4OneP;
import zy.doc.ReplaceContent4TwoP;
import zy.utils.IGetIndexGroup;
import zy.utils.ProjectSummaryGetIndexGroup;
import zy.utils.SurveyNoteGetIndexGroup;
import zy.utils.UIConsts;
import zy.utils.UIUtils;

public class DocObject {

	private static final String COMMENT_PREFIX_2 = "^ͼ(\\d+)-(\\d+)";
//	private static final String PATTERN_PROJECT_SUMMARY = "����Ƭ(\\d-\\d��)*\\d-\\d��";
//	private static final String PATTERN_PROJECT_SUMMARY_CHILDREN = "-(\\d+)";

	// private static final String DATE_YEAR = "��";
	// private static final String DATE_MONTH = "��";
	// private static final String DATE_DAY = "��";

	private CustomXWPFDocument doc;
	private String path;

	private static final Logger logger = LogManager.getLogger(DocObject.class.getName());
	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * Convert string to list.
	 * 
	 * @param str
	 * @return
	 */
	private List<String> convertString2List(String str) {
		List<String> list = new ArrayList<String>();
		String[] strs = str.split("\n");
		for (String s : strs) {
			list.add(s);
		}

		return list;
	}

	/**
	 * Get the Paragraph content with index info.
	 * 
	 * @param p
	 * @return
	 */
	private PWithIndex getParagraphContentWithIndexInfo(XWPFParagraph p) {

		logger.info("Start to getParagraphContentWithIndexInfo");
		PWithIndex pwi = new PWithIndex();

		if (p.getStyle() != null) {
			pwi.setIlfo(p.getNumID());
			pwi.setIlvl(p.getNumIlvl());
		}

		List<XWPFRun> runs = p.getRuns();
		StringBuilder sb = new StringBuilder();

		for (XWPFRun run : runs) {
			String text = run.getText(0);
			if (text != null) {
				sb.append(text);
			}
		}

		pwi.setContent(sb.toString());
		
		logger.info("Enmd to getParagraphContentWithIndexInfo");
		return pwi;
	}

	/**
	 * Get the String value.
	 * 
	 * @param p
	 * @return
	 */
	private String getStringValue(XWPFParagraph p) {
		
		logger.info("Start to get string value");
		List<XWPFRun> runs = p.getRuns();
		StringBuilder sb = new StringBuilder();
		if (p.getCTP().getPPr() != null
				&& p.getCTP().getPPr().getNumPr() != null
				&& p.getCTP().getPPr().getPStyle().getVal().toString()
						.equals("ListParagraph")) {

			// if (p.getCTP().getPPr().getDomNode().g) {
			// System.out.println("++++++++++++++");
			// System.out.println(p.getCTP().getPPr().getDivId().getVal());
			// System.out.println("++++++++++++++");
			// }

			// System.out.println(p.getNumID());

			// System.out.println(p.getNumFmt());
			// System.out.println(p.getNumIlvl());
			// System.out.println(p.getStyle());
			// System.out.println("++++++++++++++");
			// System.out.println(p.getCTP().getDomNode().getNodeName());
			// System.out.println(p.getCTP().getDomNode().getAttributes().item(3)
			// .getNodeName());
			// System.out.println(p.getCTP().getPPr().getDomNode().getNodeName());
			// System.out.println(p.getCTP().getPPr().getDomNode()
			// .getChildNodes().getLength());
			if (p.getCTP().getPPr().getDomNode().getChildNodes().item(1) != null) {
				// DocumentHelper.addNamespaceDeclaration(p.getCTP().getPPr(),
				// "wx","");
				// System.out.println(p.getText());

				HashMap<String, String> xmlMap = new HashMap<String, String>();
				xmlMap.put("tns", "http://www.99bill.com/schema/fo/settlement");
				// System.out.println(p.getCTP().getPPr().getDomNode()
				// .getChildNodes().item(1).getPrefix().toString());
				// System.out
				// .println(org.apache.xmlbeans.XmlBeans
				// .getContextTypeLoader()
				// .isNamespaceDefined(
				// "http://schemas.microsoft.com/office/word/2003/wordml"));
				// XPath
				// xpath=doc.getDocument().getDomNode().createXPath("//tns:status");
				// //Ҫ��ȡ�ĸ��ڵ㣬������Ϳ�����
				// xpath.setNamespaceURIs(xmlMap);
				// return (Element)xpath.selectSingleNode(doc);
			}
		}
		for (XWPFRun run : runs) {
			String text = run.getText(0);
			if (text != null) {
				sb.append(text);
			}
		}
		
		logger.info("End to get string value");
		return sb.toString();
	}

	/**
	 * Get string and pictures from multi-paragrah.
	 * 
	 * @param p1
	 * @param p2
	 * @return
	 */
	private List<AnalysisItem<ImageComment>> getStringAndPicFromMultiP(
			XWPFParagraph p1, XWPFParagraph p2) {

		logger.info("Start to get StringAndPicFromMultiP");
		
		String pContent;
		List<XWPFParagraph> ps = doc.getParagraphs();
		int start = doc.getPosOfParagraph(p1);
		int end = doc.getPosOfParagraph(p2);
		XWPFParagraph curParagraph;
		List<AnalysisItem<ImageComment>> analysisList = new ArrayList<AnalysisItem<ImageComment>>();
		AnalysisItem<ImageComment> analysis;

		// end of the doc
		if (end - start <= 1) {
			end++;
		}

		boolean toGetPics = false;
		int picsStart = 0;
		for (int i = start + 1; i < end; i++) {
			// if there is not a paragraph or table in position i
			if (doc.getParagraphPos(i) < 0 && doc.getTablePos(i) < 0) {
				continue;
			}

			if (doc.getTablePos(i) >= 0) {
				if (!toGetPics) {
					toGetPics = true;
					picsStart = i;
				}
			} else {
				curParagraph = ps.get(doc.getParagraphPos(i));

				pContent = this.getStringValue(curParagraph);

				// If it starts with ��Ƭ, then skip it.
				if (pContent.trim().startsWith(UIConsts.COMMENT_PREFIX_1)) {
					continue;
				}

				if (!pContent.trim().equals("")) {

					// if the paragraph is indexed
					if (curParagraph.getCTP().getPPr().getNumPr() != null) {
						analysis = new AnalysisItem<ImageComment>();
						analysis.setAnalysisItemStr(pContent);
						analysisList.add(analysis);
					} else {
						int anyNum = analysisList.size();
						if (anyNum > 0) {
							String oldAny = analysisList.get(anyNum - 1)
									.getAnalysisItemStr();
							String newAny = oldAny + "\n" + pContent;
							analysisList.get(anyNum - 1).setAnalysisItemStr(
									newAny);
						}
					}

					if (toGetPics) {
						ImageArrayList<ImageComment> tmpList = doc
								.getPicsFromParagraphs(picsStart, i);
						analysisList.get(analysisList.size() - 2).addAll(
								tmpList);
						analysisList.get(analysisList.size() - 2).setHeight(
								tmpList.getHeight());
						analysisList.get(analysisList.size() - 2).setWidth(
								tmpList.getWidth());
						toGetPics = false;
					}
				} else {
					if (!toGetPics) {
						toGetPics = true;
						picsStart = i;
					}
				}

				if ("".equals(pContent.trim())) {
					continue;
				}

				// If it start with ͼ(\\d+)-(\\d+), then skip it.
				Pattern pattern = Pattern.compile(COMMENT_PREFIX_2);
				Matcher matcher = pattern.matcher(pContent.trim());
				if (matcher.find()) {
					continue;
				}
			}
			// curParagraph = ps.get(doc.getParagraphPos(i));
			//
			// pContent = this.getStringValue(curParagraph);
			//
			// // If it starts with ��Ƭ, then skip it.
			// if (pContent.trim().startsWith(COMMENT_PREFIX_1)) {
			// continue;
			// }
			//
			// if (!pContent.trim().equals("")) {
			//
			// // if the paragraph is indexed
			// if (curParagraph.getCTP().getPPr().getNumPr() != null) {
			// analysis = new AnalysisItem<ImageComment>();
			// analysis.setAnalysisItemStr(pContent);
			// analysisList.add(analysis);
			// } else {
			// int anyNum = analysisList.size();
			// if (anyNum > 0) {
			// String oldAny = analysisList.get(anyNum - 1)
			// .getAnalysisItemStr();
			// String newAny = oldAny + "\n" + pContent;
			// analysisList.get(anyNum - 1).setAnalysisItemStr(newAny);
			// }
			// }
			//
			// if (toGetPics) {
			// ImageArrayList<ImageComment> tmpList =
			// doc.getPicsFromParagraphs(picsStart, i);
			// analysisList.get(analysisList.size() - 2).addAll(tmpList);
			// analysisList.get(analysisList.size() -
			// 2).setHeight(tmpList.getHeight());
			// analysisList.get(analysisList.size() -
			// 2).setWidth(tmpList.getWidth());
			// toGetPics = false;
			// }
			// } else {
			// if (!toGetPics) {
			// toGetPics = true;
			// picsStart = i;
			// }
			// }

			// if ("".equals(pContent.trim())) {
			// continue;
			// }
			//
			// // If it start with ͼ(\\d+)-(\\d+), then skip it.
			// Pattern pattern = Pattern.compile(COMMENT_PREFIX_2);
			// Matcher matcher = pattern.matcher(pContent.trim());
			// if (matcher.find()) {
			// continue;
			// }

		}

		// In case the last paragraph is pics.
		if (toGetPics && analysisList.size() > 0) {
			logger.info("In case the last paragraph is pics");
			analysisList.get(analysisList.size() - 1).addAll(
					doc.getPicsFromParagraphs(picsStart,
							analysisList.size() - 1));
			toGetPics = false;
		}

		logger.info("End to get StringAndPicFromMultiP");
		return analysisList;
	}

	/**
	 * ��������ϵ�� lastIlvl curIlvl lastIlfo curIlfo if curIlvl<lastIlvl ������һ��Ŀ¼ if
	 * curIlvl=lastIlvl $$ curIlfo=lastIlfo ͬ��Ŀ¼ if curIlvl=lastIlvl $$
	 * curIlfo<lastIlfo �����ϼ�Ŀ¼ if curIlvl=lastIlvl $$ curIlfo>lastIlfo ������һ��Ŀ¼
	 * if curIlvl>lastIlvl ������һ��Ŀ¼
	 * 
	 * @param pwis
	 * @return
	 */
	private String extractStringFromPargCntWithIndexInfos(List<PWithIndex> pwis) {
		
		logger.info("Start to get extractStringFromPargCntWithIndexInfos");
		
		int lastIlvl = 0;
		int lastIlfo = 0;

		StringBuilder sb = new StringBuilder();
		int level = 0;
		for (PWithIndex pwi : pwis) {
			if (pwi.getIlfo() == null || pwi.getIlvl() == null) {
				sb.append(pwi.getContent());
				sb.append("\n");
				continue;
			}

			int curIlvl = pwi.getIlvl().intValue();
			int curIlfo = pwi.getIlfo().intValue();

			if (curIlvl < lastIlvl) {
				level--;
			} else if (curIlvl == lastIlvl) {
				if (curIlfo < lastIlfo) {
					level--;
				} else if (curIlfo > lastIlfo) {
					level++;
				}
			} else {
				level++;
			}
			for (int i = 0; i < level + 1; i++) {
				sb.append("*");
			}
			sb.append(pwi.getContent());
			sb.append("\n");

			lastIlvl = curIlvl;
			lastIlfo = curIlfo;
		}
		if (sb.length() > 0) {
			sb.deleteCharAt(sb.length() - 1);
		}
		
		logger.info("End to get extractStringFromPargCntWithIndexInfos");
		return sb.toString();
	}

	/**
	 * Get the paragraph content with index info from multi paragraphs.
	 * 
	 * @param p1
	 * @param p2
	 * @return
	 */
	private List<PWithIndex> getPargCntWithIndexInfoFromMultiP(
			XWPFParagraph p1, XWPFParagraph p2) {

		logger.info("Start to get getPargCntWithIndexInfoFromMultiP");
		
		List<PWithIndex> pwis = new ArrayList<PWithIndex>();
		PWithIndex pwi = new PWithIndex();

		XWPFDocument doc = p1.getDocument();

		List<XWPFParagraph> ps = doc.getParagraphs();

		int start = doc.getPosOfParagraph(p1);
		int end = doc.getPosOfParagraph(p2);

		// end of the doc
		if (end - start <= 1) {
			end++;
		}

		for (int i = start + 1; i < end; i++) {
			// if there is not a paragraph in position i
			if (doc.getParagraphPos(i) < 0) {
				continue;
			}
			pwi = this.getParagraphContentWithIndexInfo(ps.get(doc
					.getParagraphPos(i)));

			// If it starts with ��Ƭ, then skip it.
			if (pwi.getContent().trim().startsWith(UIConsts.COMMENT_PREFIX_1)) {
				continue;
			}

			if ("".equals(pwi.getContent().trim())) {
				continue;
			}

			// If it start with ͼ(\\d+)-(\\d+), then skip it.
			Pattern pattern = Pattern.compile(COMMENT_PREFIX_2);
			Matcher matcher = pattern.matcher(pwi.getContent().trim());
			if (matcher.find()) {
				continue;
			}

			pwis.add(pwi);
		}

		logger.info("End to get getPargCntWithIndexInfoFromMultiP");
		return pwis;
	}

	/**
	 * Get the string value from multi paragraphs.
	 * 
	 * @param p1
	 * @param p2
	 * @return
	 */
	private String getStringValueFromMultiP(XWPFParagraph p1, XWPFParagraph p2) {
		
		logger.info("Start to get getStringValueFromMultiP");

		XWPFDocument doc = p1.getDocument();
		StringBuilder sb = new StringBuilder();
		String pContent;
		List<XWPFParagraph> ps = doc.getParagraphs();
		int start = doc.getPosOfParagraph(p1);
		int end = doc.getPosOfParagraph(p2);

		// end of the doc
		if (end - start <= 1) {
			end++;
		}

		for (int i = start + 1; i < end; i++) {
			// if there is not a paragraph in position i
			if (doc.getParagraphPos(i) < 0) {
				continue;
			}
			pContent = this.getStringValue(ps.get(doc.getParagraphPos(i)));

			// If it starts with ��Ƭ, then skip it.
			if (pContent.trim().startsWith(UIConsts.COMMENT_PREFIX_1)) {
				continue;
			}

			if ("".equals(pContent.trim())) {
				continue;
			}

			// If it start with ͼ(\\d+)-(\\d+), then skip it.
			Pattern pattern = Pattern.compile(COMMENT_PREFIX_2);
			Matcher matcher = pattern.matcher(pContent.trim());
			if (matcher.find()) {
				continue;
			}

			sb.append(pContent);
			sb.append("\n");
		}
		
		logger.info("End to get getStringValueFromMultiP");
		return sb.toString();
	}

	public void write2Doc(String path) {

		logger.info("Start to write2Doc");
		
		CustomXWPFDocument doc = null;
		FileOutputStream fopts = null;
		try {
			String tmpPath = UIUtils.getTmpPath(path);
			OPCPackage pack = POIXMLDocument.openPackage(tmpPath);
			doc = new CustomXWPFDocument(pack);

			fopts = new FileOutputStream(UIUtils.getTmpPath(path, "target"));
			doc.write(fopts);

		} catch (IOException e) {
			logger.error(e.getMessage());
			logger.error("Can not write2Doc. IOException");
			e.printStackTrace();
		} finally {
			try {
				fopts.close();
			} catch (IOException e) {
				
				logger.error(e.getMessage());
				logger.error("Can not close file object. IOException");
				e.printStackTrace();
			}
		}
		
		logger.info("End to write2Doc");
	}

	public DocObject(CustomXWPFDocument doc, Queue<IReplace> replaces) {

		logger.info("Start to create DocObject");
		
		this.doc = doc;
		XWPFParagraph p1;
		XWPFParagraph p2;
		IReplace r;
		Iterator<IReplace> iterator = replaces.iterator();

		// ��������Ժ˾������
		logger.info("Create DocObject ��������Ժ˾������");
		p1 = ((ReplaceContent4OneP) iterator.next()).getPosStart()
				.getParagraph();
		Pattern pattern = Pattern.compile("\\d+");
		String docContent = getStringValue(p1);
		Matcher matcher = pattern.matcher(docContent);
		if (matcher.find()) {
			this.setYear(matcher.group());
		}
		if (matcher.find()) {
			this.setSeqNo(matcher.group());
		}

		// ��������Ժ˾������, year
		logger.info("Create DocObject ��������Ժ˾������, year");
		p1 = ((ReplaceContent4OneP) iterator.next()).getPosStart()
				.getParagraph();
		pattern = Pattern.compile("ί �� �ˣ�(.+)");
		docContent = getStringValue(p1);
		matcher = pattern.matcher(docContent);
		if (matcher.find()) {
			this.setEntruster(matcher.group(1));
		}

		// ί�����ڣ�
		logger.info("Create DocObject ί������");
		p1 = ((ReplaceContent4OneP) iterator.next()).getPosStart()
				.getParagraph();
		pattern = Pattern.compile("ί�����ڣ� (\\d+)\\D+(\\d+)\\D+(\\d+)\\D+");
		docContent = getStringValue(p1);
		matcher = pattern.matcher(docContent);
		if (matcher.find()) {
			this.setReqYear(matcher.group(1));
			this.setReqMonth(matcher.group(2));
			this.setReqDay(matcher.group(3));
		}

		// ί�����
		logger.info("Create DocObject ί������");
		p1 = ((ReplaceContent4OneP) iterator.next()).getPosStart()
				.getParagraph();
		pattern = Pattern.compile("ί�����(.+)");
		docContent = getStringValue(p1);
		matcher = pattern.matcher(docContent);
		if (matcher.find()) {
			this.setEntrustItem(matcher.group(1));
		}

		// 7. ��������һʽ
		logger.info("Create DocObject ��������һʽ");
		p1 = ((ReplaceContent4OneP) iterator.next()).getPosStart()
				.getParagraph();
		pattern = Pattern
				.compile("7. ��������һʽ(.+)�ݣ�����ͬ�ȷ���Ч������������һʽ���ݣ���ί����һ�ݣ�����һʽ(.+)�ݣ���ί����(.+)�ݣ������Ĵ浵��������һ�ݡ�");
		docContent = getStringValue(p1);
		matcher = pattern.matcher(docContent);
		if (matcher.find()) {
			this.setAllNumber(matcher.group(1));
			this.setCopyNumber(matcher.group(2));
			this.setConsignorNumber(matcher.group(3));
		}

		// �����н��������о�Ժ���蹤������˾����������,��������Ժ˾������
		logger.info("Create from �����н��������о�Ժ���蹤������˾���������� to ��������Ժ˾������");
		r = iterator.next();
		p1 = ((ReplaceContent4TwoP) r).getPosStart().getParagraph();
		p2 = ((ReplaceContent4TwoP) r).getPosEnd().getParagraph();
		this.setEntrustItem(UIUtils.catMultiline(getStringValueFromMultiP(p1,
				p2)));

		// ��������Ժ˾������ skip
		logger.info("Skip ��������Ժ˾������ ");
		iterator.next();

		// ί���� skip
		logger.info("Skip ί���� ");
		iterator.next();

		// ί�м������,�������ڣ�skip
		logger.info("Skip ί�м������,�������� ");
		r = iterator.next();

		// �������ڣ�
		logger.info("Create �������� ");
		p1 = ((ReplaceContent4OneP) iterator.next()).getPosStart()
				.getParagraph();
		pattern = Pattern.compile("�������ڣ�(\\d+)��(\\d+)��(\\d+)��");
		docContent = getStringValue(p1);
		matcher = pattern.matcher(docContent);
		if (matcher.find()) {
			this.setAcptYear(matcher.group(1));
			this.setAcptMonth(matcher.group(2));
			this.setAcptDay(matcher.group(3));
		}

		// �������ڣ�
		logger.info("Create �������ڣ�");
		p1 = ((ReplaceContent4OneP) iterator.next()).getPosStart()
				.getParagraph();
		pattern = Pattern
				.compile("�������ڣ�(\\d+)��(\\d+)�� (\\d+)�ա�(\\d+)��(\\d)��(\\d)��");
		docContent = getStringValue(p1);
		matcher = pattern.matcher(docContent);
		if (matcher.find()) {
			this.setAuthnYear1(matcher.group(1));
			this.setAuthnMonth1(matcher.group(2));
			this.setAuthnDay1(matcher.group(3));
			this.setAuthnYear2(matcher.group(4));
			this.setAuthnMonth2(matcher.group(5));
			this.setAuthnDay2(matcher.group(6));
		}

		// �����ص㣺
		logger.info("Create �����ص㣺");
		p1 = ((ReplaceContent4OneP) iterator.next()).getPosStart()
				.getParagraph();
		pattern = Pattern.compile("�����ص㣺(\\S+)");
		docContent = getStringValue(p1);
		matcher = pattern.matcher(docContent);
		if (matcher.find()) {
			this.setAuthnPlace(matcher.group(1));
		}

		// ����ժҪ,���̸ſ�
		logger.info("Create from ����ժҪ  to ���̸ſ�");
		r = iterator.next();

		p1 = ((ReplaceContent4TwoP) r).getPosStart().getParagraph();
		p2 = ((ReplaceContent4TwoP) r).getPosEnd().getParagraph();
		this.setCaseAbstract(getStringValueFromMultiP(p1, p2));

		// ���̸ſ�,�������
		logger.info("Create from ���̸ſ�  to �������");
		r = iterator.next();
		p1 = ((ReplaceContent4TwoP) r).getPosStart().getParagraph();
		p2 = ((ReplaceContent4TwoP) r).getPosEnd().getParagraph();
		String pSummary = getStringValueFromMultiP(p1, p2);

		IGetIndexGroup psIndexGroup = new ProjectSummaryGetIndexGroup(pSummary);
		this.setProjectSummary(psIndexGroup.getContentWithMark());
		
		// TODO: change ��Ƭ3-2��3-3 to @, and map the @s to respective pictures.
		// 1. When hit ��ƬN-N, 
		// 2. Find the numbers between them
		// 3. If find the numbers, index this ��Ƭ3-2��3-3.
		
		doc.getPicsFromParagraphs((ReplaceContent4TwoP) r);
		ImageArrayList<ImageComment> iamges = ((ReplaceContent4TwoP) r).getImgComments();
		this.setSummaryPicsList(psIndexGroup.getSortedImageArrayList(iamges));
		this.setSummaryPics(iamges);

		// �ֳ���������¼
		logger.info("Skip �ֳ���������¼");
		iterator.next();

		// �ֳ���������¼,4.2�ֳ�ʵ����Ƭ
		logger.info("Create from �ֳ���������¼ to 4.2�ֳ�ʵ����Ƭ");
		r = iterator.next();
		p1 = ((ReplaceContent4TwoP) r).getPosStart().getParagraph();
		p2 = ((ReplaceContent4TwoP) r).getPosEnd().getParagraph();
		this.setSurveyNote(extractStringFromPargCntWithIndexInfos(getPargCntWithIndexInfoFromMultiP(
				p1, p2)));
		// TODO: change ������Ƭ3��4�� to @, and map the @s to respective pictures.
		// 1. When hit (, try to find next )
		// 2. Find the numbers between them
		// 3. If find the numbers, index this pairs of ().
		IGetIndexGroup snIndexGroup = new SurveyNoteGetIndexGroup(this.getSurveyNote());

		// 4. Map this index to respective pictures.
		// 4.2�ֳ�ʵ����Ƭ,����˵��.
		logger.info("Create from 4.2�ֳ�ʵ����Ƭ  to ����˵��");
		r = iterator.next();
		doc.getPicsFromParagraphs((ReplaceContent4TwoP) r);
		ImageArrayList<ImageComment> temps = ((ReplaceContent4TwoP) r)
				.getImgComments();
		
		this.setSurveyNote(snIndexGroup.getContentWithMark());
		this.setSurveyPicsList(snIndexGroup.getSortedImageArrayList(temps));

		// �����ֳ��������,��������
		logger.info("Create from �����ֳ��������  to ��������");
		r = iterator.next();
		p1 = ((ReplaceContent4TwoP) r).getPosStart().getParagraph();
		p2 = ((ReplaceContent4TwoP) r).getPosEnd().getParagraph();
		String all = getStringValueFromMultiP(p1, p2);
		this.setAnalysis(this.convertString2List(all));
		// TODO:
		this.setAnalysisPicsList(getStringAndPicFromMultiP(p1, p2));

		// ��������,�������
		logger.info("Create from ��������  to �������");
		r = iterator.next();
		p1 = ((ReplaceContent4TwoP) r).getPosStart().getParagraph();
		p2 = ((ReplaceContent4TwoP) r).getPosEnd().getParagraph();
		// all = getStringValueFromMultiP(p1, p2);
		all = extractStringFromPargCntWithIndexInfos(getPargCntWithIndexInfoFromMultiP(
				p1, p2));
		this.setStrGists(convertString2List(all));
		this.setStrGist(all);

		// �������,��˾��������ִҵ֤��
		logger.info("Create from �������  to ��˾��������ִҵ֤��");
		r = iterator.next();
		p1 = ((ReplaceContent4TwoP) r).getPosStart().getParagraph();
		p2 = ((ReplaceContent4TwoP) r).getPosEnd().getParagraph();
		// all = getStringValueFromMultiP(p1, p2);
		all = extractStringFromPargCntWithIndexInfos(getPargCntWithIndexInfoFromMultiP(
				p1, p2));
		
		// �������
		logger.info("Create �������");
		this.setComments(all);

		// ��˾��������ִҵ֤��1
		logger.info("Create ˾��������ִҵ֤��1");
		p1 = ((ReplaceContent4OneP) iterator.next()).getPosStart()
				.getParagraph();
		pattern = Pattern.compile("��˾��������ִҵ֤��֤�ţ�(.+)");
		docContent = getStringValue(p1);
		matcher = pattern.matcher(docContent);
		if (matcher.find()) {
			this.setAuthner1(matcher.group(1));
		}

		// ��˾��������ִҵ֤��2
		logger.info("Create ˾��������ִҵ֤��2");
		p1 = ((ReplaceContent4OneP) iterator.next()).getPosStart()
				.getParagraph();
		pattern = Pattern.compile("��˾��������ִҵ֤��֤�ţ�(.+)");
		docContent = getStringValue(p1);
		matcher = pattern.matcher(docContent);
		if (matcher.find()) {
			this.setAuthner2(matcher.group(1));
		}

		// �����н��������о�Ժ���蹤������˾����������
		logger.info("Create �����н��������о�Ժ���蹤������˾����������");
		r = iterator.next();
		p1 = ((ReplaceContent4TwoP) r).getPosStart().getParagraph();
		p2 = ((ReplaceContent4TwoP) r).getPosEnd().getParagraph();
		all = getStringValueFromMultiP(p1, p2);
		this.setDocDate(all);
		
		logger.info("End to create DocObject");
	}

	// public String getEntrustDay(){
	// return UIUtils.catDate(year, month, day);
	// }

	public List<String> getStrGists() {
		return strGists;
	}

	public void setStrGists(List<String> strGists) {
		this.strGists = strGists;
	}

	private String entruster;

	// ��������Ժ˾������, year
	private String year;
	private String seqNo;

	// ί�����ڣ�
	private String reqYear;
	private String reqMonth;
	private String reqDay;

	// ��������
	private String acptYear;
	private String acptMonth;
	private String acptDay;

	private String entrustItem;
	private String allNumber;
	private String copyNumber;
	private String consignorNumber;

	private String title;

	private String authnYear1;
	private String authnYear2;

	private String authnMonth1;
	private String authnMonth2;

	private String authnDay1;
	private String authnDay2;
	private String authnPlace;

	private String caseAbstract;

	// ���̸ſ�
	private String projectSummary;
	private ImageArrayList<ImageComment> summaryPics;
	private List<ImageArrayList<ImageComment>> summaryPicsList;
	private boolean projectSummaryOrientation = UIConsts.ORIENTATION_LANDSCAPE;

	// �������/�ֳ���������¼
	private String surveyNote;
	private ImageArrayList<ImageComment> surveyPics;
	private boolean surveyOrientation = UIConsts.ORIENTATION_LANDSCAPE;
	private List<ImageArrayList<ImageComment>> surveyPicsList;

	// �塢 ����˵��
	private List<String> analysis;
	private List<AnalysisItem<ImageComment>> analysisPicsList;
	private boolean analysisOrientation = UIConsts.ORIENTATION_LANDSCAPE;

	// �������� TODO: how to compare and extract the items in the word doc?
	private List<Integer> gists = new ArrayList<Integer>();
	private List<String> strGists;
	private String strGist;

	// �������
	private String comments;

	private String authner1;
	private String authner2;

	// �����н��������о�Ժ���蹤������˾����������: date
	private String docDate;

	// ί������ XXXX��X��X��
	public String getEntrustDay() {

		return UIUtils.getDateFromYMD(getReqYear(), getReqMonth(), getReqDay());
	}

	public String getStrGist() {
		return strGist;
	}

	public void setStrGist(String strGist) {
		this.strGist = strGist;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(String seqNo) {
		this.seqNo = seqNo;
	}

	public String getEntrustItem() {
		return entrustItem;
	}

	public void setEntrustItem(String entrustItem) {
		this.entrustItem = entrustItem;
	}

	public String getAllNumber() {
		return allNumber;
	}

	public void setAllNumber(String allNumber) {
		this.allNumber = allNumber;
	}

	public String getCopyNumber() {
		return copyNumber;
	}

	public void setCopyNumber(String copyNumber) {
		this.copyNumber = copyNumber;
	}

	public String getConsignorNumber() {
		return consignorNumber;
	}

	public void setConsignorNumber(String consignorNumber) {
		this.consignorNumber = consignorNumber;
	}

	// ��������
	public String getAcceptDate() {

		return UIUtils.getDateFromYMD(getAcptYear(), getAcptMonth(),
				getAcptDay());
	}

	// ��������
	public String getAuthnDate1() {

		return UIUtils.getDateFromYMD(getAuthnYear1(), this.getAuthnMonth1(),
				this.getAuthnDay1());
	}

	public String getAuthnDate2() {

		return UIUtils.getDateFromYMD(getAuthnYear2(), this.getAuthnMonth2(),
				this.getAuthnDay2());
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthnYear1() {
		return authnYear1;
	}

	public void setAuthnYear1(String authnYear1) {
		this.authnYear1 = authnYear1;
	}

	public String getAuthnYear2() {
		return authnYear2;
	}

	public void setAuthnYear2(String authnYear2) {
		this.authnYear2 = authnYear2;
	}

	public String getAuthnMonth1() {
		return authnMonth1;
	}

	public void setAuthnMonth1(String authnMonth1) {
		this.authnMonth1 = authnMonth1;
	}

	public String getAuthnMonth2() {
		return authnMonth2;
	}

	public void setAuthnMonth2(String authnMonth2) {
		this.authnMonth2 = authnMonth2;
	}

	public String getAuthnDay1() {
		return authnDay1;
	}

	public void setAuthnDay1(String authnDay1) {
		this.authnDay1 = authnDay1;
	}

	public String getAuthnDay2() {
		return authnDay2;
	}

	public void setAuthnDay2(String authnDay2) {
		this.authnDay2 = authnDay2;
	}

	public String getAuthnPlace() {
		return authnPlace;
	}

	public void setAuthnPlace(String authnPlace) {
		this.authnPlace = authnPlace;
	}

	public String getCaseAbstract() {
		return caseAbstract;
	}

	public void setCaseAbstract(String caseAbstract) {
		this.caseAbstract = caseAbstract;
	}

	public String getProjectSummary() {
		return projectSummary;
	}

	public void setProjectSummary(String projectSummary) {
		this.projectSummary = projectSummary;
	}

	public ImageArrayList<ImageComment> getSummaryPics() {
		return summaryPics;
	}

	public void setSummaryPics(ImageArrayList<ImageComment> summaryPics) {
		this.summaryPics = summaryPics;
	}

	public String getSurveyNote() {
		return surveyNote;
	}

	public void setSurveyNote(String surveyNote) {
		this.surveyNote = surveyNote;
	}

	public ImageArrayList<ImageComment> getSurveyPics() {
		return surveyPics;
	}

	public void setSurveyPics(ImageArrayList<ImageComment> surveyPics) {
		this.surveyPics = surveyPics;
	}

	public List<AnalysisItem<ImageComment>> getAnalysisPicsList() {
		return analysisPicsList;
	}

	public void setAnalysisPicsList(
			List<AnalysisItem<ImageComment>> analysisPicsList) {
		this.analysisPicsList = analysisPicsList;
	}

	public List<Integer> getGists() {
		return gists;
	}

	public void setGists(List<Integer> gists) {
		this.gists = gists;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getAuthner1() {
		return authner1;
	}

	public void setAuthner1(String authner1) {
		this.authner1 = authner1;
	}

	public String getAuthner2() {
		return authner2;
	}

	public void setAuthner2(String authner2) {
		this.authner2 = authner2;
	}

	public String getDocDate() {
		return docDate;
	}

	public void setDocDate(String docDate) {
		this.docDate = docDate;
	}

	public String getReqYear() {
		return reqYear;
	}

	public void setReqYear(String reqYear) {
		this.reqYear = reqYear;
	}

	public String getReqMonth() {
		return reqMonth;
	}

	public void setReqMonth(String reqMonth) {
		this.reqMonth = reqMonth;
	}

	public String getReqDay() {
		return reqDay;
	}

	public void setReqDay(String reqDay) {
		this.reqDay = reqDay;
	}

	public String getEntruster() {
		return entruster;
	}

	public void setEntruster(String entruster) {
		this.entruster = entruster;
	}

	public String getAcptYear() {
		return acptYear;
	}

	public void setAcptYear(String acptYear) {
		this.acptYear = acptYear;
	}

	public String getAcptMonth() {
		return acptMonth;
	}

	public void setAcptMonth(String acptMonth) {
		this.acptMonth = acptMonth;
	}

	public String getAcptDay() {
		return acptDay;
	}

	public List<String> getAnalysis() {
		return analysis;
	}

	public void setAnalysis(List<String> analysis) {
		this.analysis = analysis;
	}

	public void setAcptDay(String acptDay) {
		this.acptDay = acptDay;
	}

	public boolean isProjectSummaryOrientation() {
		return projectSummaryOrientation;
	}

	public void setProjectSummaryOrientation(boolean projectSummaryOrientation) {
		this.projectSummaryOrientation = projectSummaryOrientation;
	}

	public boolean isSurveyOrientation() {
		return surveyOrientation;
	}

	public void setSurveyOrientation(boolean surveyOrientation) {
		this.surveyOrientation = surveyOrientation;
	}

	public boolean isAnalysisOrientation() {
		return analysisOrientation;
	}

	public void setAnalysisOrientation(boolean analysisOrientation) {
		this.analysisOrientation = analysisOrientation;
	}

	public List<ImageArrayList<ImageComment>> getSurveyPicsList() {
		return surveyPicsList;
	}

	public void setSurveyPicsList(
			List<ImageArrayList<ImageComment>> surveyPicsList) {
		this.surveyPicsList = surveyPicsList;
	}

	public List<ImageArrayList<ImageComment>> getSummaryPicsList() {
		return summaryPicsList;
	}

	public void setSummaryPicsList(
			List<ImageArrayList<ImageComment>> summaryPicsList) {
		this.summaryPicsList = summaryPicsList;
	}

}
