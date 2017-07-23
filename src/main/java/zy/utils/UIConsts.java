package zy.utils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import zy.UI.SignerContentPanel;

public class UIConsts {

	public static final String PICTURE_MARK = "@";
	
	public static final String DATE_YEAR = "��";
	public static final String DATE_MONTH = "��";
	public static final String DATE_DAY = "��";

	public static final List<String> signers = new ArrayList<String>();
	
	private static final Logger logger = LogManager.getLogger(UIConsts.class.getName());
	
	public static final Map<Integer, String> mapArab2Chinese = new HashMap<Integer, String>();
	static {
		mapArab2Chinese.put(1, "һ");
		mapArab2Chinese.put(2, "��");
		mapArab2Chinese.put(3, "��");
		mapArab2Chinese.put(4, "��");
		mapArab2Chinese.put(5, "��");
		mapArab2Chinese.put(6, "��");
		mapArab2Chinese.put(7, "��");
		mapArab2Chinese.put(8, "��");
		mapArab2Chinese.put(9, "��");
		mapArab2Chinese.put(0, "��");
	}

	// Error Message
	public static final String WARN_MESSAGE_FAILED_READ_PROP_LOCAL_1 = "Failed to read signer property file!";
	public static final String WARN_MESSAGE_FAILED_READ_PROP_LOCAL_2 = "Get it from default!";
	public static final String ERROR_MESSAGE_FAILED_READ_PROP_DEFAULT = "Failed to read signer property file from default!";
	
	static {
		Properties prop = new Properties();
		
			
			try {
				// get singer property file from pwd
				String pwd = System.getProperty("user.dir");
				String path = pwd+"\\signer.prop";
				logger.info(path);
				InputStream in =new FileInputStream(path);
				
				logger.info("after File f = new File(path)");	
				
				Reader reader = new InputStreamReader(in, "UTF-8");
				
				logger.info("after Reader reader = new InputStreamReader");	
				prop.load(reader);
			} catch (Exception e) {
				logger.error(e.getMessage());
				logger.error("There is no property file under pwd");
				logger.warn(WARN_MESSAGE_FAILED_READ_PROP_LOCAL_1);
				logger.info(WARN_MESSAGE_FAILED_READ_PROP_LOCAL_2);
				try {
					InputStream in = SignerContentPanel.class
							.getResourceAsStream("/signer.prop");
					Reader reader = new InputStreamReader(in, "UTF-8");
					prop.load(reader);
				} catch (Exception e1) {
					logger.error(ERROR_MESSAGE_FAILED_READ_PROP_DEFAULT);
					logger.error(e.getMessage());
				}

			}

		Set<Object> keyValue = prop.keySet();
		for (Iterator<Object> it = keyValue.iterator(); it.hasNext();) {
			String key = (String) it.next();
			signers.add(key + ":" + prop.get(key));
		}
	}
	
	public static final String CH_NUMBER_TEN = "ʮ";
	public static final String CH_NUMBER_ZERO = "��";

	public static final int MULTI_LINE_LINE_LENGTH = 70;
	public static final boolean ORIENTATION_PORTRAIT = true;
	public static final boolean ORIENTATION_LANDSCAPE = false;

	public static final int INDEX_ORIENTATION_PORTRAIT = 1;
	public static final int INDEX_ORIENTATION_LANDSCAPE = 0;

	public static final String BTN_IMPORT_WORD_TITLE = "����Word";
	public static final String BTN_NEXT_TITLE = "�༭Word";
	public static final String BTN_CANCEL_TITLE = "�˳�";

	public static final String BTN_EDIT_NEXT = "��һ��	";
	public static final String BTN_EDIT_UNSET = "�ÿ�";
	public static final String BTN_EDIT_RETURN = "����";

	public static final String LABEL_BASIC_IFNO_ENTRUSTER = "ί����";
	public static final String LABEL_BASIC_IFNO_ENTRUST_DATE = "ί������";
	public static final String LABEL_BASIC_IFNO_ENTRUST_ITEM = "ί������";
	public static final String LABEL_BASIC_IFNO_TITLE1 = "��������Ժ˾������";
	public static final String LABEL_BASIC_IFNO_TITLE2 = "�����ֵ�";
	public static final String LABEL_BASIC_IFNO_TITLE3 = "��";

	public static final String LABEL_BASIC_INFO_PAPERNO11 = "��������һʽ";
	public static final String LABEL_BASIC_INFO_PAPERNO21 = "����һʽ";
	public static final String LABEL_BASIC_INFO_PAPERNO31 = "��ί����";
	public static final String LABEL_BASIC_INFO_PAPERNON2 = "��; ";
	public static final String LABEL_BASIC_INFO_PAPERNO_END = "�ݡ�";

	public static final String LABEL_BASIC_ACCEPT_DATE = "��������";
	public static final String LABEL_BASIC_AUTHN_DATE = "��������";
	public static final String LABEL_BASIC_AUTHN_PLACE = "�����ص�";

	public static final String LABEL_BASIC_INFO_COMPLETED_DATE = "�������";

	public static final String LABEL_ABSTRACT = "����ժҪ";

	public static final String LABEL_SUMMARY = "���̸ſ�";
	public static final String LABEL_SURVEY = "�������";
	public static final String LABEL_IDENTIFY = "�������";
	public static final String LABEL_EVIDENCE = "��������";
	public static final String RDB_TITILE = "��Ƭ��ʽ";
	public static final String[] RDB_ITEMS = { "���", "����" };

	public static final String BTN_EDIT_PICS = "�༭ͼƬ";

	public static final String BTN_EDIT_ADD_PIC_BEFORE = "������ǰ";
	public static final String BTN_EDIT_ADD_PIC_AFTER = "�����ں�";
	public static final String BTN_EDIT_ADD_PIC_DELETE = "ɾ��";

	public static final String BTN_EDIT_ADD_PIC_PANEL = "����";
	public static final String BTN_EDIT_FINISH_PIC_PANEL = "���";
	public static final String BTN_EDIT_CANCLE_PIC_PANEL = "ȡ��";

	public static final String LABEL_ANALYSIS_TITLE = "����˵��";
	public static final String LABEL_EVIDENCE_TITLE = "��������";
	public static final String LABEL_SIGNER_TITLE = "˾��������ǩ��";

	public static final String COMMENT_PREFIX_1 = "��Ƭ";
	public static final String COMMENT_PREFIX_2 = "ͼ";
	
	// TODO: read from configure file?
	public static final int NAME_CONTENT_PANEL_BASIC = 0;
	public static final int NAME_CONTENT_PANEL_ABSTRACT = 1;
	public static final int NAME_CONTENT_PANEL_SUMMARY = 2;
	public static final int NAME_CONTENT_PANEL_SURVEY = 3;
	public static final int NAME_CONTENT_PANEL_ANALYSIS = 4;
	// ��������
	public static final int NAME_CONTENT_PANEL_EVIDENCE = 5;
	// �������
	public static final int NAME_CONTENT_PANEL_IDENTIFY = 6;
	public static final int NAME_CONTENT_PANEL_SIGNER = 7;
	public static final int[] NAME_CONTENT_PANEL = { NAME_CONTENT_PANEL_BASIC,
			NAME_CONTENT_PANEL_ABSTRACT, NAME_CONTENT_PANEL_SUMMARY,
			NAME_CONTENT_PANEL_SURVEY, NAME_CONTENT_PANEL_ANALYSIS,
			NAME_CONTENT_PANEL_EVIDENCE, NAME_CONTENT_PANEL_IDENTIFY,
			NAME_CONTENT_PANEL_SIGNER };

	public static final String STRING_YEAR = "��";
	public static final String STRING_MONTH = "��";
	public static final String STRING_DAY = "��";

	public static final String IDENTITY_1 = "";
}
