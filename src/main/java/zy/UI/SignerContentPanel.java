package zy.UI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import zy.dso.DocObject;
import zy.utils.UIConsts;
import zy.utils.UIUtils;

/**
 * 司法鉴定人签名
 * 
 * @author yangzhao
 * 
 */
public class SignerContentPanel extends ContentPanel implements
		IContentPanelControl {
	
	private static List<String> signers = new ArrayList<String>();
	
	static{
		signers.addAll(UIConsts.signers);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JLabel jlTitle;
	private JComboBox signerBox1;
	private JComboBox signerBox2;
	private JPanel jpSigners;

	/**
	 * Get the index by id.
	 * 
	 * @param id
	 * @return
	 */
	private int getIndex(String id) {
		int index = 0;
		for (String item : signers) {
			if (UIUtils.strContains(item, id)) {
				return index;
			}
			index++;
		}

		return -1;
	}

	private String getNo(String str){
		if(UIUtils.isEmptyString(str)){
			return "";
		}
		
		String[] rel = str.split(":");
		return rel[1];
	}
	
	@Override
	public void saveContent(DocObject obj) {
		// TODO how to add?
		int selIdx1 = signerBox1.getSelectedIndex();
		int selIdx2 = signerBox2.getSelectedIndex();
		
		String sel1 = signers.get(selIdx1);
		String sel2 = signers.get(selIdx2);
		
		obj.setAuthner1(getNo(sel1));
		obj.setAuthner2(getNo(sel2));
	}

	@Override
	public void clearContent() {
		signerBox1.setSelectedIndex(0);
		signerBox2.setSelectedIndex(0);

	}

	/**
	 * Set the selected index for jcombobox.
	 * 
	 * @param index
	 * @param jcb
	 */
	private void setIndex4List(int index, JComboBox jcb) {
		if (index >= 0 && index < jcb.getItemCount()) {
			jcb.setSelectedIndex(index);
		}
	}

	@Override
	public void setContent(DocObject obj) {

		setIndex4List(getIndex(obj.getAuthner1()), signerBox1);
		setIndex4List(getIndex(obj.getAuthner2()), signerBox2);
	}

	public SignerContentPanel() {

		super();

		Border paneEdge = BorderFactory.createEmptyBorder(50, 10, 400, 100);

		jlTitle = new NameLabel(UIConsts.LABEL_SIGNER_TITLE);
		signerBox1 = new JComboBox();
		signerBox2 = new JComboBox();
		initializeCombos();
		jpSigners = new JPanel(new GridLayout(0, 1));
		jpSigners.setBorder(paneEdge);
		jpSigners.add(signerBox1);
		jpSigners.add(signerBox2);
		this.setLayout(new BorderLayout());
		this.add(jlTitle, BorderLayout.NORTH);

		this.add(jpSigners, BorderLayout.CENTER);

		jpSigners.setPreferredSize(new Dimension(600, 500));
		jpSigners.setMinimumSize(new Dimension(600, 500));
		jpSigners.setPreferredSize(new Dimension(600, 500));
	}

	private void initializeCombos() {

		for (String str : signers) {
			signerBox1.addItem(str);
			signerBox2.addItem(str);
		}

	}
}
