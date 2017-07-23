package zy.doc;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlToken;
import org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps;
import org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D;
import org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTInline;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFonts;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTInd;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STLineSpacingRule;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STOnOff;

import zy.doc.traverse.BeforeDoTraverseDocCommand;
import zy.doc.traverse.CellTraverseFactory;
import zy.doc.traverse.ContainerFactory;
import zy.doc.traverse.DDocTraverse;
import zy.doc.traverse.GetContentPosTraverseParagraphCommand;
import zy.doc.traverse.GetPicsFromPByIndexTraverseParagrahCommand;
import zy.doc.traverse.GetPicsFromPTraverseParagrahCommand;
import zy.doc.traverse.IPictureContainer;
import zy.doc.traverse.ITraverseDocCommand;
import zy.doc.traverse.ITraverseRows;
import zy.doc.traverse.InitialTraverseParagraphCommand;
import zy.doc.traverse.RDocTraverse;
import zy.doc.traverse.WDocTraverse;
import zy.dso.AnalysisItem;
import zy.dso.DocObject;
import zy.dso.ImageArrayList;
import zy.dso.ImageComment;
import zy.utils.UIUtils;

public class CustomXWPFDocument extends XWPFDocument {

	private Queue<IReplace> replaces = new LinkedList<IReplace>();
	private DocObject docObject;

	private static final String PARAGRAPH_PREFIX = "**";
	private static final String PARAGRAPH_PREFIX_2 = "*";

	private static final String PARAGRAPH_PREFIX_4_REGR = "\\*\\*";
	private static final String PARAGRAPH_PREFIX_2_4_REGR = "\\*";

	private static final String PARAGRAPH_SUFFIX = "\\n";
	
	private static final Logger logger = LogManager.getLogger(CustomXWPFDocument.class.getName());

	public CustomXWPFDocument(InputStream in) throws IOException {
		super(in);
	}

	public CustomXWPFDocument() {
		super();
	}

	public CustomXWPFDocument(OPCPackage pkg) throws IOException {
		this(pkg, true);
	}

	public CustomXWPFDocument(OPCPackage pkg, boolean initializeObj)
			throws IOException {
		super(pkg);

		// initialize replaces to get the info about content positions.
		initReplaces();

		// get the content from doc to generate doc object.
		if (initializeObj) {
			initializeObj();
		}
	}

	/**
	 * 
	 * 
	 */
	public void writeObject2Doc(DocObject docObject) {

		Queue<IReplace> tmpRps = new LinkedList<IReplace>();
		tmpRps.addAll(replaces);
		XWPFParagraph p1;
		XWPFParagraph p2;
		IReplace rp;

		// ��������Ժ˾������[2012] �����ֵ�152��
		rp = tmpRps.poll();
		p1 = ((ReplaceContent4OneP) rp).getPosStart().getParagraph();
		String content = "��������Ժ˾������[" + docObject.getYear() + "] �����ֵ�"
				+ docObject.getSeqNo() + "��";
		UIUtils.setContent2Paragraph(p1, content);

		logger.info("write for ��������Ժ˾������[2012] �����ֵ�152�� is OK!");
		
		// ί �� ��
		rp = tmpRps.poll();
		p1 = ((ReplaceContent4OneP) rp).getPosStart().getParagraph();
		content = "ί �� �ˣ�" + docObject.getEntruster();
		UIUtils.setContent2Paragraph(p1, content);
		logger.info("write for ί �� ��  is OK!");

		// ί������
		rp = tmpRps.poll();
		p1 = ((ReplaceContent4OneP) rp).getPosStart().getParagraph();
		content = "ί�����ڣ� " + docObject.getReqYear() + "��"
				+ docObject.getReqMonth() + "�� " + docObject.getReqDay() + "��";
		UIUtils.setContent2Paragraph(p1, content);
		logger.info("write for ί �� ��  is OK!");

		// ί�����
		rp = tmpRps.poll();
		p1 = ((ReplaceContent4OneP) rp).getPosStart().getParagraph();
		content = "ί�����" + docObject.getEntrustItem();
		UIUtils.setContent2Paragraph(p1, content);
		logger.info("write for ί������  is OK!");
		
		// 7. ��������һʽ
		rp = tmpRps.poll();
		p1 = ((ReplaceContent4OneP) rp).getPosStart().getParagraph();
		content = "7. ��������һʽ" + docObject.getAllNumber()
				+ "�ݣ�����ͬ�ȷ���Ч������������һʽ���ݣ���ί����һ�ݣ�����һʽ" + docObject.getCopyNumber()
				+ "�ݣ���ί����" + docObject.getConsignorNumber() + "�ݣ������Ĵ浵��������һ�ݡ�";
		UIUtils.setContent2Paragraph(p1, content);
		logger.info("write for ��������һʽ  is OK!");
		
		// �����н��������о�Ժ���蹤������˾����������,��������Ժ˾������
		rp = tmpRps.poll();
		p2 = ((ReplaceContent4TwoP) rp).getPosEnd().getParagraph();

		Map<String, Object> docStyles = new HashMap<String, Object>();
		BigInteger line = new BigInteger("360");
		docStyles.put("spacing:line", line);

		STLineSpacingRule.Enum lineRule = STLineSpacingRule.Enum.forInt(0);
		docStyles.put("spacing:line-rule", lineRule);

		CTJc jc = CTJc.Factory.newInstance();
		jc.setVal(STJc.Enum.forString("center"));
		docStyles.put("jc", jc);

		CTFonts fonts = CTFonts.Factory.newInstance();
		fonts.setAscii("����");
		fonts.setEastAsia("����");
		fonts.setHAnsi("����");
		docStyles.put("rFonts", fonts);

		CTHpsMeasure sz = CTHpsMeasure.Factory.newInstance();
		sz.setVal(new BigInteger("36"));
		docStyles.put("sz", sz);

		CTHpsMeasure szcs = CTHpsMeasure.Factory.newInstance();
		szcs.setVal(new BigInteger("36"));
		docStyles.put("szcs", szcs);

		content = docObject.getEntrustItem();
		UIUtils.setContent2Paragraph(p2, content, docStyles);

		p2 = ((ReplaceContent4TwoP) rp).getPosEnd().getParagraph();
		logger.info("write between �����н��������о�Ժ���蹤������˾���������� and ��������Ժ˾������  is OK!");
		
		// ��������Ժ˾������
		rp = tmpRps.poll();
		p1 = ((ReplaceContent4OneP) rp).getPosStart().getParagraph();
		content = "��������Ժ˾������[" + docObject.getYear() + "]�����ֵ�"
				+ docObject.getSeqNo() + "��";
		UIUtils.setContent2Paragraph(p1, content);
		logger.info("write for ��������Ժ˾������[X]�����ֵ�X�� is OK!");
		
		// ί���ˣ�
		rp = tmpRps.poll();
		p1 = ((ReplaceContent4OneP) rp).getPosStart().getParagraph();
		content = docObject.getEntruster();
		UIUtils.setContent2Paragraph(p1, content, "��");
		logger.info("write for ί����2 is OK!");
		
		// ί�м������
		rp = tmpRps.poll();
		p2 = ((ReplaceContent4TwoP) rp).getPosEnd().getParagraph();
		docStyles = new HashMap<String, Object>();
		line = new BigInteger("360");
		docStyles.put("spacing:line", line);

		lineRule = STLineSpacingRule.Enum.forInt(0);
		docStyles.put("spacing:line-rule", lineRule);

		CTInd ind = CTInd.Factory.newInstance();
		ind.setFirstLineChars(new BigInteger("200"));

		ind.setFirstLine(new BigInteger("600"));
		docStyles.put("ind", ind);

		fonts = CTFonts.Factory.newInstance();
		fonts.setAscii("����_GB2312");
		fonts.setEastAsia("����_GB2312");
		fonts.setHAnsi("����_GB2312");
		docStyles.put("rFonts", fonts);

		CTFonts pprfonts = CTFonts.Factory.newInstance();
		pprfonts.setAscii("����_GB2312");
		pprfonts.setEastAsia("����_GB2312");
		pprfonts.setHAnsi("����_GB2312");

		docStyles.put("ppr_rFonts", pprfonts);

		sz = CTHpsMeasure.Factory.newInstance();
		sz.setVal(new BigInteger("28"));
		docStyles.put("sz", sz);

		szcs = CTHpsMeasure.Factory.newInstance();
		szcs.setVal(new BigInteger("28"));
		docStyles.put("szcs", szcs);

		content = docObject.getEntrustItem();
		UIUtils.setContent2Paragraph(p2, content, docStyles);
		
		logger.info("write for ί�м������� is OK!");

		// ��������
		rp = tmpRps.poll();
		p1 = ((ReplaceContent4OneP) rp).getPosStart().getParagraph();
		content = docObject.getAcptYear() + "��" + docObject.getAcptMonth()
				+ "��" + docObject.getAcptDay() + "��";
		UIUtils.setContent2Paragraph(p1, content, "��");
		logger.info("write for ��������  is OK!");

		// ��������
		rp = tmpRps.poll();
		p1 = ((ReplaceContent4OneP) rp).getPosStart().getParagraph();
		content = docObject.getAuthnYear1() + "��" + docObject.getAuthnMonth1()
				+ "��" + docObject.getAuthnDay1() + "�ա�"
				+ docObject.getAuthnYear2() + "��" + docObject.getAuthnMonth2()
				+ "��" + docObject.getAuthnDay2() + "��";
		UIUtils.setContent2Paragraph(p1, content, "��");
		logger.info("write for ��������  is OK!");

		// �����ص㣺
		rp = tmpRps.poll();
		p1 = ((ReplaceContent4OneP) rp).getPosStart().getParagraph();
//		content = "�����ص㣺" + docObject.getAuthnPlace();
		content = docObject.getAuthnPlace();
		UIUtils.setContent2Paragraph(p1, content, "��");
		logger.info("write for �����ص�  is OK!");

		// ���� ����ժҪ
		rp = tmpRps.poll();
		p2 = ((ReplaceContent4TwoP) rp).getPosEnd().getParagraph();
		docStyles = new HashMap<String, Object>();
		line = new BigInteger("360");
		docStyles.put("spacing:line", line);

		lineRule = STLineSpacingRule.Enum.forInt(0);
		docStyles.put("spacing:line-rule", lineRule);

		ind = CTInd.Factory.newInstance();
		ind.setFirstLineChars(new BigInteger("250"));

		ind.setFirstLine(new BigInteger("700"));
		docStyles.put("ind", ind);

		CTDecimalNumber dn = CTDecimalNumber.Factory.newInstance();
		dn.setVal(new BigInteger("0"));
		docStyles.put("outlineLvl", dn);

		fonts = CTFonts.Factory.newInstance();
		fonts.setAscii("����_GB2312");
		fonts.setEastAsia("����_GB2312");
		fonts.setHAnsi("����_GB2312");
		docStyles.put("rFonts", fonts);

		sz = CTHpsMeasure.Factory.newInstance();
		sz.setVal(new BigInteger("28"));
		docStyles.put("sz", sz);

		szcs = CTHpsMeasure.Factory.newInstance();
		szcs.setVal(new BigInteger("28"));
		docStyles.put("szcs", szcs);

		content = docObject.getCaseAbstract();
		String[] lines = UIUtils.trimString(content).split("\n");
		for (String l : lines) {
			UIUtils.setContent2Paragraph(p2, l, docStyles);
		}
		logger.info("write for ����ժҪ  is OK!");

		// ���� ���̸ſ�
		rp = tmpRps.poll();
		p2 = ((ReplaceContent4TwoP) rp).getPosEnd().getParagraph();
		docStyles = new HashMap<String, Object>();
		line = new BigInteger("360");
		docStyles.put("spacing:line", line);

		lineRule = STLineSpacingRule.Enum.forInt(0);
		docStyles.put("spacing:line-rule", lineRule);

		ind = CTInd.Factory.newInstance();
		ind.setFirstLineChars(new BigInteger("250"));

		ind.setFirstLine(new BigInteger("700"));
		docStyles.put("ind", ind);

		dn = CTDecimalNumber.Factory.newInstance();
		dn.setVal(new BigInteger("0"));
		docStyles.put("outlineLvl", dn);

		fonts = CTFonts.Factory.newInstance();
		fonts.setAscii("����_GB2312");
		fonts.setEastAsia("����_GB2312");
		fonts.setHAnsi("����_GB2312");
		docStyles.put("rFonts", fonts);

		sz = CTHpsMeasure.Factory.newInstance();
		sz.setVal(new BigInteger("28"));
		docStyles.put("sz", sz);

		szcs = CTHpsMeasure.Factory.newInstance();
		szcs.setVal(new BigInteger("28"));
		docStyles.put("szcs", szcs);

		logger.info("write for ���̸ſ�  is OK!");
		
		// transfer picture markers to comment indexes
		// If there is no �� before @, transfer it to ��Ƭ3-X,
		// else if here is a comma before @ transfer it to 3-X.
		content = UIUtils.transferMark2Index(docObject.getProjectSummary());
		ImageArrayList<ImageComment> list = UIUtils.transferListList2List(docObject.getSummaryPicsList());
		UIUtils.generateComments4Images(list, "��Ƭ3-");

		lines = UIUtils.trimString(content).split("\n");
		for (String l : lines) {
			UIUtils.setContent2Paragraph(p2, l, docStyles);
		}

		IPictureWriter pw = PictureWriterFactory
				.generatePictureWriter(docObject.isProjectSummaryOrientation());

		pw.writePicture2Doc(list, p2, this);
		logger.info("write for ���̸ſ�pic  is OK!");
		
		//UIUtils.addPics2Word(docObject.getSummaryPics(), p2, this);
		// �ġ� �������
		rp = tmpRps.poll();
		p1 = ((ReplaceContent4OneP) rp).getPosStart().getParagraph();
		content = "�ֳ���������¼��" + docObject.getAuthnYear1() + "��"
				+ docObject.getAuthnMonth1() + "�� " + docObject.getAuthnDay1()
				+ "�ա�" + docObject.getAuthnYear2() + "��"
				+ docObject.getAuthnMonth1() + "��" + docObject.getAuthnDay2()
				+ "�գ�";
		UIUtils.setContent2Paragraph(p1, content);
		
		logger.info("write for �������  is OK!");
		
		// �ֳ���������¼,4.2�ֳ�ʵ����Ƭ
		rp = tmpRps.poll();
		p2 = ((ReplaceContent4TwoP) rp).getPosEnd().getParagraph();

		// 1. get the survey notes separated by **, and remove the suffix \n.
		// 2. loop for adding the format and outputing each.
		content = docObject.getSurveyNote();
		List<String> surveyNotes = UIUtils.splitComments(content,
				PARAGRAPH_PREFIX_4_REGR);
		
		Queue<ImageArrayList<ImageComment>> qImages = new LinkedList<ImageArrayList<ImageComment>>();
		qImages.addAll(docObject.getSurveyPicsList());
		
		// transform @ to (��Ƭ n,��Ƭn+1)
		List<String> contents = UIUtils.transferMark2PicIndex(surveyNotes, qImages, PARAGRAPH_PREFIX_4_REGR);
		
		for (String str : contents) {
			
			// output the content
			docStyles = new HashMap<String, Object>();

			CTString ctsPStyle = CTString.Factory.newInstance();
			ctsPStyle.setVal("1");
			docStyles.put("pStyle", ctsPStyle);

			CTDecimalNumber numIlfo = CTDecimalNumber.Factory.newInstance();
			numIlfo.setVal(new BigInteger("4"));
			docStyles.put("ilfo", numIlfo);

			CTDecimalNumber numIlvl = CTDecimalNumber.Factory.newInstance();
			numIlvl.setVal(new BigInteger("0"));
			docStyles.put("ilvl", numIlvl);

			ind = CTInd.Factory.newInstance();
			ind.setLeft(new BigInteger("0"));
			ind.setFirstLine(new BigInteger("567"));
			docStyles.put("ind", ind);

			CTJc ctJc = CTJc.Factory.newInstance();
			ctJc.setVal(STJc.LEFT);
			docStyles.put("jc", ctJc);

			fonts = CTFonts.Factory.newInstance();
			fonts.setAscii("����_GB2312");
			fonts.setEastAsia("����_GB2312");
			fonts.setHAnsi("����_GB2312");
			docStyles.put("rFonts", fonts);

			UIUtils.setContent2Paragraph(p2, str, docStyles);
		}
		logger.info("write from �ֳ���������¼ to 4.2�ֳ�ʵ����Ƭ  is OK!");
		
		// 4.2�ֳ�ʵ����Ƭ,����˵��.
		rp = tmpRps.poll();
		p2 = ((ReplaceContent4TwoP) rp).getPosEnd().getParagraph();
//		UIUtils.addPics2Word(docObject.getSurveyPics(), p2, this);
		pw = PictureWriterFactory.generatePictureWriter(docObject.isSurveyOrientation());
		
		// pw.writePicture2Doc(docObject.getSurveyPics(), p2, this);
		list = UIUtils.transferListList2List(docObject.getSurveyPicsList());
		UIUtils.generateComments4Images(list, "��Ƭ ");
		pw.writePicture2Doc(list, p2, this);

		logger.info("write from 4.2�ֳ�ʵ����Ƭ to ����˵��  is OK!");
		
		// 5.1 �����ֳ����������������ؼ�����׼�淶���������·�����
		rp = tmpRps.poll();
		p2 = ((ReplaceContent4TwoP) rp).getPosEnd().getParagraph();
		List<AnalysisItem<ImageComment>> analysisItems = docObject
				.getAnalysisPicsList();
		for (AnalysisItem<ImageComment> analysisItem : analysisItems) {
			// output the content
			docStyles = new HashMap<String, Object>();

			CTString ctsPStyle = CTString.Factory.newInstance();
			ctsPStyle.setVal("1");
			docStyles.put("pStyle", ctsPStyle);

			CTDecimalNumber numIlfo = CTDecimalNumber.Factory.newInstance();
			numIlfo.setVal(new BigInteger("5"));
			docStyles.put("ilfo", numIlfo);

			CTDecimalNumber numIlvl = CTDecimalNumber.Factory.newInstance();
			numIlvl.setVal(new BigInteger("0"));
			docStyles.put("ilvl", numIlvl);

			CTOnOff wordWrap = CTOnOff.Factory.newInstance();
			wordWrap.setVal(STOnOff.OFF);
			docStyles.put("wordwrap", wordWrap);

			ind = CTInd.Factory.newInstance();
			ind.setLeft(new BigInteger("0"));
			ind.setFirstLine(new BigInteger("567"));
			docStyles.put("ind", ind);

			CTJc ctJc = CTJc.Factory.newInstance();
			ctJc.setVal(STJc.LEFT);
			docStyles.put("jc", ctJc);

			fonts = CTFonts.Factory.newInstance();
			fonts.setAscii("����_GB2312");
			fonts.setEastAsia("����_GB2312");
			fonts.setHAnsi("����_GB2312");
			docStyles.put("rFonts", fonts);

			UIUtils.setContent2Paragraph(p2, analysisItem.getAnalysisItemStr(),
					docStyles);
//			UIUtils.addVerticalPics2Word(analysisItem, p2, this);
			pw = PictureWriterFactory.generatePictureWriter(docObject.isAnalysisOrientation());
			pw.writePicture2Doc(analysisItem, p2, this);
		}
		logger.info("write for 5.1 �����ֳ����������������ؼ�����׼�淶���������·���  is OK!");

		// 5.2 ��������
		rp = tmpRps.poll();
		p2 = ((ReplaceContent4TwoP) rp).getPosEnd().getParagraph();
		content = docObject.getStrGist();
		String[] gistList = UIUtils.splitStr(content, PARAGRAPH_SUFFIX);
		for (String str : gistList) {

			str = UIUtils.trimString(str);

			// output the content
			docStyles = new HashMap<String, Object>();

			if (str.startsWith(PARAGRAPH_PREFIX)) {
				str = UIUtils.removePrefix(str, PARAGRAPH_PREFIX_4_REGR);
				str = UIUtils.trimString(str);
				CTString ctsPStyle = CTString.Factory.newInstance();
				ctsPStyle.setVal("ListParagraph");
				docStyles.put("pStyle", ctsPStyle);

				CTDecimalNumber numIlfo = CTDecimalNumber.Factory.newInstance();
				numIlfo.setVal(new BigInteger("3"));
				docStyles.put("ilfo", numIlfo);

				CTDecimalNumber numIlvl = CTDecimalNumber.Factory.newInstance();
				numIlvl.setVal(new BigInteger("0"));
				docStyles.put("ilvl", numIlvl);

				ind = CTInd.Factory.newInstance();
				ind.setLeft(new BigInteger("0"));
				ind.setFirstLine(new BigInteger("567"));
				docStyles.put("ind", ind);
			} else if (str.startsWith(PARAGRAPH_PREFIX_2)) {
				// remove prefix **
				str = UIUtils.removePrefix(str, PARAGRAPH_PREFIX_2_4_REGR);
				str = UIUtils.trimString(str);
				CTString ctsPStyle = CTString.Factory.newInstance();
				ctsPStyle.setVal("ListParagraph");
				docStyles.put("pStyle", ctsPStyle);

				CTDecimalNumber numIlfo = CTDecimalNumber.Factory.newInstance();
				numIlfo.setVal(new BigInteger("0"));
				docStyles.put("ilfo", numIlfo);

				CTDecimalNumber numIlvl = CTDecimalNumber.Factory.newInstance();
				numIlvl.setVal(new BigInteger("0"));
				docStyles.put("ilvl", numIlvl);

				ind = CTInd.Factory.newInstance();
				ind.setFirstLine(new BigInteger("567"));
				docStyles.put("ind", ind);
			}

			CTJc ctJc = CTJc.Factory.newInstance();
			ctJc.setVal(STJc.LEFT);
			docStyles.put("jc", ctJc);

			fonts = CTFonts.Factory.newInstance();
			fonts.setAscii("����_GB2312");
			fonts.setEastAsia("����_GB2312");
			fonts.setHAnsi("����_GB2312");
			docStyles.put("rFonts", fonts);

			UIUtils.setContent2Paragraph(p2, str, docStyles);
		}
		logger.info("write for 5.2 ��������  is OK!");

		// ���� �������
		rp = tmpRps.poll();
		p1 = ((ReplaceContent4TwoP) rp).getPosStart().getParagraph();
		p1.getDocument().createParagraph();

		XmlCursor cursor = p1.getCTP().newCursor();
		cursor.toNextSibling();
		p2 = p1.getDocument().insertNewParagraph(cursor);
		content = docObject.getComments();
		String[] commentsList = UIUtils.splitStr(content, PARAGRAPH_SUFFIX);
		for (String str : commentsList) {

			str = UIUtils.trimString(str);

			// output the content
			docStyles = new HashMap<String, Object>();

			if (str.startsWith(PARAGRAPH_PREFIX)) {
				str = UIUtils.removePrefix(str, PARAGRAPH_PREFIX_4_REGR);
				str = UIUtils.trimString(str);
				CTString ctsPStyle = CTString.Factory.newInstance();
				ctsPStyle.setVal("1");
				docStyles.put("pStyle", ctsPStyle);

				CTDecimalNumber numIlfo = CTDecimalNumber.Factory.newInstance();
				numIlfo.setVal(new BigInteger("6"));
				docStyles.put("ilfo", numIlfo);

				CTDecimalNumber numIlvl = CTDecimalNumber.Factory.newInstance();
				numIlvl.setVal(new BigInteger("0"));
				docStyles.put("ilvl", numIlvl);

				ind = CTInd.Factory.newInstance();
				ind.setLeft(new BigInteger("0"));
				ind.setFirstLine(new BigInteger("567"));
				docStyles.put("ind", ind);

				CTJc ctJc = CTJc.Factory.newInstance();
				ctJc.setVal(STJc.LEFT);
				docStyles.put("jc", ctJc);

			} else {
				str = UIUtils.trimString(str);

				line = new BigInteger("360");
				docStyles.put("spacing:line", line);

				lineRule = STLineSpacingRule.Enum.forString("auto");
				docStyles.put("spacing:line-rule", lineRule);

				ind = CTInd.Factory.newInstance();
				ind.setFirstLine(new BigInteger("602"));
				ind.setFirstLineChars(new BigInteger("215"));
				docStyles.put("ind", ind);

				sz = CTHpsMeasure.Factory.newInstance();
				sz.setVal(new BigInteger("28"));
				docStyles.put("sz", sz);

				szcs = CTHpsMeasure.Factory.newInstance();
				szcs.setVal(new BigInteger("28"));
				docStyles.put("szcs", szcs);
			}

			fonts = CTFonts.Factory.newInstance();
			fonts.setAscii("����_GB2312");
			fonts.setEastAsia("����_GB2312");
			fonts.setHAnsi("����_GB2312");
			docStyles.put("rFonts", fonts);

			UIUtils.setContent2Paragraph(p2, str, docStyles);
		}
		logger.info("write for ���� �������  is OK!");

		// ��˾��������ִҵ֤��1
		rp = tmpRps.poll();
		p1 = ((ReplaceContent4OneP) rp).getPosStart().getParagraph();
		content = "��˾��������ִҵ֤��֤�ţ�" + docObject.getAuthner1();
		UIUtils.setContent2Paragraph(p1, content);
		logger.info("write for ��˾��������ִҵ֤��1  is OK!");

		// ��˾��������ִҵ֤��2
		rp = tmpRps.poll();
		p1 = ((ReplaceContent4OneP) rp).getPosStart().getParagraph();
		content = "��˾��������ִҵ֤��֤�ţ�" + docObject.getAuthner2();
		UIUtils.setContent2Paragraph(p1, content);

		logger.info("write for ��˾��������ִҵ֤��2  is OK!");
		
		// �����н��������о�Ժ���蹤������˾����������
		rp = tmpRps.poll();
		p1 = ((ReplaceContent4TwoP) rp).getPosEnd().getParagraph();
		content = docObject.getDocDate();
		UIUtils.setContent2Paragraph(p1, content);
		// content = "��˾��������ִҵ֤��֤�ţ�" + docObject.getAuthner2();
		// UIUtils.setContent2Paragraph(p1, content);
		logger.info("write for �����н��������о�Ժ���蹤������˾����������  is OK!");
		
		rp = tmpRps.poll();
	}

	/**
	 * Remove the contents
	 * 
	 */
	public void removeContent() {
		
		logger.info("begin to remove content!");
		Queue<IReplace> twoPReplaces = UIUtils.getReplaces4Remove(replaces);
		List<XWPFParagraph> paragraphList = this.getParagraphs();

		ITraverseDocCommand docCommand = new DDocTraverse(paragraphList,
				twoPReplaces);
		docCommand.doCommand();
		logger.info("end to remove content!");
	}

	/**
	 * initialize replaces to get the info about content positions.
	 */
	private void initReplaces() {

		logger.info("begin to initialize replaces!");
		
		Queue<IReplace> coloneReplaces = new LinkedList<IReplace>();

		replaces.addAll(UIUtils.getTmpReplaces());
		coloneReplaces.addAll(replaces);
		List<XWPFParagraph> paragraphList = this.getParagraphs();
		ITraverseDocCommand docCommand = new RDocTraverse(paragraphList,
				new InitialTraverseParagraphCommand(coloneReplaces));

		docCommand.doCommand();
		
		logger.info("end to initialize replaces!");
	}

	/**
	 * Initialize the doc.
	 */
	private void initializeObj() {
		
		logger.info("begin to initialize doc object!");
		
		docObject = new DocObject(this, replaces);
		// for (IReplace r : replaces) {
		//
		// System.out.println("========================"
		// + ((ReplaceContent) r).getPosStart().getContent());
		// if (r instanceof ReplaceContent4OneP) {
		// System.out.println(this
		// .getPosOfParagraph(((ReplaceContent4OneP) r)
		// .getPosStart().getParagraph()));
		// } else {
		// System.out.println(this
		// .getPosOfParagraph(((ReplaceContent4TwoP) r)
		// .getPosStart().getParagraph()));
		// if ((((ReplaceContent4TwoP) r).getPosEnd().getParagraph()) != null) {
		// System.out.println(this
		// .getPosOfParagraph(((ReplaceContent4TwoP) r)
		// .getPosEnd().getParagraph()));
		// }
		//
		// }
		// }
	}

	public DocObject getDocObject() {
		logger.info("begin to get doc object!");
		return docObject;
	}

	public static void main(String... strings) {
		// test_getContentPositions();
		try {
			test_initialize();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void test_initialize() {
		try {
			OPCPackage pack = POIXMLDocument
					.openPackage("D:\\useful files\\private\\project\\git\\NvErHong\\tmplate_1.docx");
			CustomXWPFDocument doc = new CustomXWPFDocument(pack);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void test_getContentPositions() {
		try {
			OPCPackage pack = POIXMLDocument
					.openPackage("D:\\useful files\\private\\project\\git\\NvErHong\\tmplate_1.docx");
			CustomXWPFDocument doc = new CustomXWPFDocument(pack);
			ReplaceContent4OneP one1 = new ReplaceContent4OneP("��������Ժ˾������");
			ReplaceContent4OneP one2 = new ReplaceContent4OneP("ί �� �ˣ�");
			ReplaceContent4OneP one3 = new ReplaceContent4OneP("ί�����ڣ�");
			ReplaceContent4TwoP two1 = new ReplaceContent4TwoP("����	����ժҪ",
					"����	���̸ſ�");

			System.out.println(doc.getPosOfParagraph(one1.getPosStart()
					.getParagraph()));
			System.out.println(doc.getPosOfParagraph(one2.getPosStart()
					.getParagraph()));
			System.out.println(doc.getPosOfParagraph(one3.getPosStart()
					.getParagraph()));

			System.out.println(doc.getPosOfParagraph(two1.getPosStart()
					.getParagraph()));
			System.out.println(doc.getPosOfParagraph(two1.getPosEnd()
					.getParagraph()));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void test_getContentPosition() {
		try {
			OPCPackage pack = POIXMLDocument
					.openPackage("D:\\useful files\\private\\project\\git\\NvErHong\\tmplate_1.docx");
			CustomXWPFDocument doc = new CustomXWPFDocument(pack);
			ContentPosition pos = doc.getContentPosition("ί �� �ˣ�");
			System.out.println(pos.getPos());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Write the content to word.
	 * TODO: deprecated. 
	 */
	public void writeContent2Word(List<ReplaceContent> rcs) {

		logger.info("begin to write content to word!");
		
		if (rcs == null || rcs.size() == 0) {
			return;
		}

		// deal with paragraph
		List<XWPFParagraph> paragraphList = this.getParagraphs();

		ITraverseDocCommand docCommand = new WDocTraverse(paragraphList, rcs);

		docCommand.doCommand();
	}

	/**
	 * Get the positions of the contents.
	 * TODO: deprecated.
	 * 
	 * @param contents
	 * @return
	 */
	public List<ContentPosition> getContentPositions(List<String> contents) {
		List<ContentPosition> poss = new ArrayList<ContentPosition>();

		if (contents != null && contents.size() != 0) {
			for (String content : contents) {
				poss.add(getContentPosition(content));
			}
		}

		return poss;
	}

	/**
	 * Get the position of the content.
	 * TODO: deprecated.
	 * 
	 * @param content
	 */
	public ContentPosition getContentPosition(String content) {

		ContentPosition pos = new ContentPosition(content);

		if (content == null || content.trim().equals("")) {
			return pos;
		}

		// deal with paragraph
		List<XWPFParagraph> paragraphList = this.getParagraphs();

		ITraverseDocCommand docCommand = new RDocTraverse(paragraphList,
				new GetContentPosTraverseParagraphCommand(pos));

		docCommand.doCommand();

		return pos;
	}

	/**
	 * Process table for getting pictures.
	 * 
	 * @param table
	 * @param rc4Two
	 */
	private void processTable(XWPFTable table, IPictureContainer ipc) {
		logger.info("begin to process Table!");
		
		List<XWPFTableRow> rows = table.getRows();

		getPicturesFromCells(rows, ipc);
		logger.info("end to process Table!");
	}

	/**
	 * Get pictures from cell
	 * 
	 * @param paragraphList
	 * @param toSetSize
	 * @throws UtilsException
	 */
	private void getPicturesFromCells(List<XWPFTableRow> rows,
			IPictureContainer ipc) {
		logger.info("begin to get pictures from cells!");
		ITraverseRows traverse = CellTraverseFactory.getTraverseRows(ipc, rows);
		traverse.traverseCells();
		logger.info("end to get pictures from cells!");
	}

	/**
	 * 
	 * @param rc4Two
	 */
	public void getPicsFromParagraphs(ReplaceContent4TwoP rc4Two) {

		logger.info("begin to get pictures from paragrahs!");
		
		List<XWPFParagraph> paragraphList = this.getParagraphs();

		ITraverseDocCommand docCommand = new BeforeDoTraverseDocCommand(
				paragraphList, new GetPicsFromPTraverseParagrahCommand(this,
						rc4Two));

		docCommand.doCommand();

		// get pictures from table
		if (rc4Two.getImgComments() == null
				|| rc4Two.getImgComments().size() == 0) {
			getPicsFromTable(rc4Two);
		}
		
		logger.info("end to get pictures from paragrahs!");
	}

	/**
	 * Get the pictures from table.
	 * 
	 * @param rc4Two
	 */
	private void getPicsFromTable(ReplaceContent4TwoP rc4Two) {
		
		logger.info("begin to get pictures from Table!");
		
		// 1. get the tables from the doc.
		Iterator<XWPFTable> it = this.getTablesIterator();

		// 2. traverse the tables, if the position of one is before end and
		// after start, then try to get pictures and comments from it
		while (it.hasNext()) {

			XWPFTable table = it.next();

			// If the table is between the start mark and end mark, then get
			// pictures from it.
			// Here, it is suspected that only one table resides between them.
			int pos = this.getPosOfTable(table);

			if (pos > rc4Two.getPosStart().getParagraphPos()
					&& pos < rc4Two.getPosEnd().getParagraphPos()) {
				processTable(table, ContainerFactory.generateContainer(rc4Two));
			}
		}
		
		logger.info("end to get pictures from Table!");
	}

	/**
	 * Get the pictures from table.
	 * 
	 * @param rc4Two
	 */
	private void getPicsFromTable(int start, int end,
			ImageArrayList<ImageComment> images) {

		logger.info("start to get pictures(start, end, images) from Table!");
		
		// 1. get the tables from the doc.
		Iterator<XWPFTable> it = this.getTablesIterator();

		// 2. traverse the tables, if the position of one is before end and
		// after start, then try to get pictures and comments from it
		while (it.hasNext()) {

			XWPFTable table = it.next();

			// If the table is between the start mark and end mark, then get
			// pictures from it.
			// Here, it is suspected that only one table resides between them.
			int pos = this.getPosOfTable(table);

			if (pos >= start && pos < end) {
				processTable(table, ContainerFactory.generateContainer(images));
			}
		}
		
		logger.info("end to get pictures(start, end, images) from Table!");
	}

	/**
	 * 
	 * @param rc4Two
	 */
	public ImageArrayList<ImageComment> getPicsFromParagraphs(int start, int end) {

		logger.info("start to get pictures(start, end) from paragraphs!");
		
		List<XWPFParagraph> paragraphList = this.getParagraphs();

		ImageArrayList<ImageComment> images = new ImageArrayList<ImageComment>();

		ITraverseDocCommand docCommand = new BeforeDoTraverseDocCommand(
				paragraphList, new GetPicsFromPByIndexTraverseParagrahCommand(
						this, start, end, images));

		docCommand.doCommand();

		// get pictures from table
		if (images == null || images.size() == 0) {
			getPicsFromTable(start, end, images);
		}

		logger.info("end to get pictures(start, end) from paragraphs!");
		
		return images;
	}

	/**
	 * @param id
	 * @param width
	 *            ��
	 * @param height
	 *            ��
	 * @param paragraph
	 *            ����
	 */
	public XWPFRun createPicture(String blipId, int id, long width,
			long height, XWPFParagraph paragraph, int pos) {

		logger.info("start to create pictures!");
		
		XWPFRun run = paragraph.insertNewRun(pos);

		CTInline inline = run.getCTR().addNewDrawing().addNewInline();

		String picXml = ""
				+ "<a:graphic xmlns:a=\"http://schemas.openxmlformats.org/drawingml/2006/main\">"
				+ "   <a:graphicData uri=\"http://schemas.openxmlformats.org/drawingml/2006/picture\">"
				+ "      <pic:pic xmlns:pic=\"http://schemas.openxmlformats.org/drawingml/2006/picture\">"
				+ "         <pic:nvPicPr>" + "            <pic:cNvPr id=\""
				+ id
				+ "\" name=\"Generated\"/>"
				+ "            <pic:cNvPicPr/>"
				+ "         </pic:nvPicPr>"
				+ "         <pic:blipFill>"
				+ "            <a:blip r:embed=\""
				+ blipId
				+ "\" xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\"/>"
				+ "            <a:stretch>"
				+ "               <a:fillRect/>"
				+ "            </a:stretch>"
				+ "         </pic:blipFill>"
				+ "         <pic:spPr>"
				+ "            <a:xfrm>"
				+ "               <a:off x=\"0\" y=\"0\"/>"
				+ "               <a:ext cx=\""
				+ width
				+ "\" cy=\""
				+ height
				+ "\"/>"
				+ "            </a:xfrm>"
				+ "            <a:prstGeom prst=\"rect\">"
				+ "               <a:avLst/>"
				+ "            </a:prstGeom>"
				+ "         </pic:spPr>"
				+ "      </pic:pic>"
				+ "   </a:graphicData>" + "</a:graphic>";

		inline.addNewGraphic().addNewGraphicData();
		XmlToken xmlToken = null;
		try {
			xmlToken = XmlToken.Factory.parse(picXml);
		} catch (XmlException xe) {
			xe.printStackTrace();
		}

		inline.set(xmlToken);

		inline.setDistT(0);
		inline.setDistB(0);
		inline.setDistL(0);
		inline.setDistR(0);

		CTPositiveSize2D extent = inline.addNewExtent();
		extent.setCx(width);
		extent.setCy(height);

		CTNonVisualDrawingProps docPr = inline.addNewDocPr();
		docPr.setId(id);
		docPr.setName("ͼƬ" + id);
		docPr.setDescr("����");

		logger.info("end to create pictures!");
		
		return run;
	}
}
