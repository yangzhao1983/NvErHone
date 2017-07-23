package zy.UI;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import zy.dso.DocObject;
import zy.utils.UIConsts;
import zy.utils.UIUtils;

/**
 * 基本信息
 * 
 * @author yangzhao
 * 
 */
public class BasicContentPanel extends ContentPanel implements
		IContentPanelControl {

	@Override
	public void setContent(DocObject obj) {
		super.setContent(obj);

		this.jtfEntruster.setText(obj.getEntruster());
		this.jtfEntrustDay.setText(obj.getEntrustDay());

		this.jtaEntrustItem.setText(obj.getEntrustItem());
		this.jtfTitileNo.setText(obj.getSeqNo());
		this.jtfTitileYear.setText(obj.getYear());

		this.jtfPaperNoTotal.setText(obj.getAllNumber());
		this.jtfPaperNoCopy.setText(obj.getCopyNumber());
		this.jtfPaperNoconsignor.setText(obj.getConsignorNumber());

		this.jtfAcceptDate.setText(obj.getAcceptDate());
		this.jtfAuthnDate1.setText(obj.getAuthnDate1());
		this.jtfAuthnDate2.setText(obj.getAuthnDate2());
		this.jtfAuthnPlace.setText(obj.getAuthnPlace());

		this.jtfCompletedDate.setText(obj.getDocDate());
	}

	@Override
	public void saveContent(DocObject obj) {

		obj.setEntruster(UIUtils.trimString(jtfEntruster.getText()));

		String[] strs = UIUtils.splitYMD(jtfEntrustDay.getText());
		obj.setReqYear(strs[0]);
		obj.setReqMonth(strs[1]);
		obj.setReqDay(strs[2]);

		obj.setEntrustItem(UIUtils.catMultiline(jtaEntrustItem.getText()));
		obj.setSeqNo(UIUtils.trimString(jtfTitileNo.getText()));
		obj.setYear(UIUtils.trimString(jtfTitileYear.getText()));

		obj.setAllNumber(UIUtils.trimString(jtfPaperNoTotal.getText()));
		obj.setCopyNumber(UIUtils.trimString(jtfPaperNoCopy.getText()));
		obj.setConsignorNumber(UIUtils.trimString(jtfPaperNoconsignor.getText()));

		strs = UIUtils.splitYMD(this.jtfAcceptDate.getText());
		obj.setAcptYear(strs[0]);
		obj.setAcptMonth(strs[1]);
		obj.setAcptDay(strs[2]);
		
		strs = UIUtils.splitYMD(this.jtfAuthnDate1.getText());
		obj.setAuthnYear1(strs[0]);
		obj.setAuthnMonth1(strs[1]);
		obj.setAuthnDay1(strs[2]);	
		
		strs = UIUtils.splitYMD(this.jtfAuthnDate2.getText());
		obj.setAuthnYear2(strs[0]);
		obj.setAuthnMonth2(strs[1]);
		obj.setAuthnDay2(strs[2]);
		
		obj.setAuthnPlace(this.jtfAuthnPlace.getText());
		
		obj.setDocDate(UIUtils.trimString(jtfCompletedDate.getText()));
	}

	@Override
	public void clearContent() {

		this.jtfEntruster.setText("");
		this.jtfEntrustDay.setText("");

		this.jtaEntrustItem.setText("");

		this.jtfTitileNo.setText("");
		this.jtfTitileYear.setText("");
		this.jtfPaperNoTotal.setText("");
		this.jtfPaperNoCopy.setText("");
		this.jtfPaperNoconsignor.setText("");
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel jlEntruster;
	private JTextField jtfEntruster;

	private JLabel jlEntrustDay;
	// TODO: date component?
//	private JTextField jtfEntrustDay;
	private JFormattedTextField jtfEntrustDay;

	private JLabel jlEntrustItem;
	private JTextArea jtaEntrustItem;

	private JLabel jlTitle1;
	private JLabel jlTitle2;
	private JLabel jlTitle3;
	private JTextField jtfTitileNo;
	private JTextField jtfTitileYear;

	private JLabel jlPaperNo11;
	private JLabel jlPaperNo12;
	private JLabel jlPaperNo21;
	private JLabel jlPaperNo22;
	private JLabel jlPaperNo31;
	private JLabel jlPaperNo32;

	// 受理日期
	private JLabel jlAcceptDate;
	// 鉴定日期
	private JLabel jlAuthnDate;
	// 鉴定地点
	private JLabel jlAuthnPlace;

	private JLabel jlCompletedDate;

	private JTextField jtfPaperNoTotal;
	private JTextField jtfPaperNoCopy;
	private JTextField jtfPaperNoconsignor;

	// 受理日期
	private JTextField jtfAcceptDate;
	// 鉴定日期
	private JTextField jtfAuthnDate1;
	private JTextField jtfAuthnDate2;
	// 鉴定地点
	private JTextField jtfAuthnPlace;

	private JTextField jtfCompletedDate;

	public BasicContentPanel() {

		super();

		jlEntruster = new NameLabel(UIConsts.LABEL_BASIC_IFNO_ENTRUSTER);
		jtfEntruster = new ValueTextField();

		jlEntrustDay = new NameLabel(UIConsts.LABEL_BASIC_IFNO_ENTRUST_DATE);
		jtfEntrustDay = new DateTextFiled(false,this);
		
		jlEntrustItem = new NameLabel(UIConsts.LABEL_BASIC_IFNO_ENTRUST_ITEM);
		jtaEntrustItem = new JTextArea();
		jtaEntrustItem.setLineWrap(true);
		jtaEntrustItem.setWrapStyleWord(true);

		jlTitle1 = new JLabel(UIConsts.LABEL_BASIC_IFNO_TITLE1);
		jlTitle1.setPreferredSize(new Dimension(150, 30));
		jtfTitileYear = new ShortTextField();
		jlTitle2 = new JLabel(UIConsts.LABEL_BASIC_IFNO_TITLE2);
		jtfTitileNo = new ShortTextField();
		jlTitle3 = new JLabel(UIConsts.LABEL_BASIC_IFNO_TITLE3);

		jlPaperNo11 = new JLabel(UIConsts.LABEL_BASIC_INFO_PAPERNO11);
		jtfPaperNoTotal = new ShortTextField();
		jlPaperNo12 = new JLabel(UIConsts.LABEL_BASIC_INFO_PAPERNON2);

		jlPaperNo21 = new JLabel(UIConsts.LABEL_BASIC_INFO_PAPERNO21);
		jtfPaperNoCopy = new ShortTextField();
		jlPaperNo22 = new JLabel(UIConsts.LABEL_BASIC_INFO_PAPERNON2);

		jlPaperNo31 = new JLabel(UIConsts.LABEL_BASIC_INFO_PAPERNO31);
		jtfPaperNoconsignor = new ShortTextField();
		jlPaperNo32 = new JLabel(UIConsts.LABEL_BASIC_INFO_PAPERNO_END);

		// 受理日期
		jlAcceptDate = new JLabel(UIConsts.LABEL_BASIC_ACCEPT_DATE);
		jtfAcceptDate = new DateTextFiled(false,this);
		// 鉴定日期
		jlAuthnDate = new JLabel(UIConsts.LABEL_BASIC_AUTHN_DATE);
		jtfAuthnDate1 = new DateTextFiled(false,this);
		jtfAuthnDate2 = new DateTextFiled(false,this);

		// 鉴定地点
		jlAuthnPlace = new JLabel(UIConsts.LABEL_BASIC_AUTHN_PLACE);
		jtfAuthnPlace = new ValueTextField();

		jlCompletedDate = new NameLabel(
				UIConsts.LABEL_BASIC_INFO_COMPLETED_DATE);
		jtfCompletedDate = new DateTextFiled(true,this);

		// entrust
		JPanel panel1 = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		panel1.add(jlEntruster, c);
		panel1.add(jtfEntruster, c);

		// entrust day
		JPanel panel2 = new JPanel(new GridBagLayout());
		panel2.add(jlEntrustDay, c);
		panel2.add(jtfEntrustDay, c);

		// entrust item
		JPanel panel3 = new JPanel(new GridBagLayout());
		panel3.add(jlEntrustItem, c);
		jtaEntrustItem.setPreferredSize(new Dimension(400, 300));
		jtaEntrustItem.setMinimumSize(new Dimension(400, 300));
		jtaEntrustItem.setPreferredSize(new Dimension(400, 300));
		panel3.add(jtaEntrustItem, c);

		// entrust title
		JPanel panel4 = new JPanel(new GridBagLayout());
		panel4.add(jlTitle1, c);
		panel4.add(jtfTitileYear, c);
		panel4.add(jlTitle2, c);
		panel4.add(jtfTitileNo, c);
		panel4.add(jlTitle3, c);

		// paper no
		JPanel panel5 = new JPanel(new GridBagLayout());
		panel5.add(jlPaperNo11, c);
		panel5.add(jtfPaperNoTotal, c);
		panel5.add(jlPaperNo12, c);
		panel5.add(jlPaperNo21, c);
		panel5.add(jtfPaperNoCopy, c);
		panel5.add(jlPaperNo22, c);
		panel5.add(jlPaperNo31, c);
		panel5.add(jtfPaperNoconsignor, c);
		panel5.add(jlPaperNo32, c);

		// accept date
		JPanel panel7 = new JPanel(new GridBagLayout());
		panel7.add(jlAcceptDate, c);
		panel7.add(jtfAcceptDate, c);

		// authn date
		JPanel panel8 = new JPanel(new GridBagLayout());
		panel8.add(jlAuthnDate, c);
		panel8.add(jtfAuthnDate1, c);
		panel8.add(jtfAuthnDate2, c);

		// authn place
		JPanel panel9 = new JPanel(new GridBagLayout());
		panel9.add(jlAuthnPlace, c);
		panel9.add(jtfAuthnPlace, c);

		// completed date
		JPanel panel6 = new JPanel(new GridBagLayout());
		panel6.add(jlCompletedDate, c);
		panel6.add(jtfCompletedDate, c);

		this.setLayout(new GridBagLayout());
		c.weightx = 1;
		c.weighty = 1;
		c.gridx = 0;
		c.gridy = 0;
		this.add(panel1, c);

		c.gridy = 1;
		this.add(panel2, c);
		c.gridy = 2;
		c.gridwidth = 3;
		this.add(panel3, c);
		c.gridheight = 1;
		c.gridy = 5;
		this.add(panel4, c);
		c.gridy = 6;
		this.add(panel5, c);
		c.gridy = 7;
		this.add(panel7, c);
		c.gridy = 8;
		this.add(panel8, c);
		c.gridy = 9;
		this.add(panel9, c);
		c.gridy = 10;
		this.add(panel6, c);
	}
}
