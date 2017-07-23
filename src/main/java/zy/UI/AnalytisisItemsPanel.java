package zy.UI;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.border.Border;

import zy.dso.AnalysisItem;
import zy.dso.DocObject;
import zy.dso.ImageComment;
import zy.utils.UIConsts;

/**
 * ·ÖÎöËµÃ÷
 * 
 * @author yangzhao
 * 
 */
public class AnalytisisItemsPanel extends ContentPanel {

	private JFrame parent;
	
	private List<JRadioButton> jrbList = new ArrayList<JRadioButton>();
	
	protected void setRadioButton(int orientation){
		jrbList.get(orientation).setSelected(true);
	}
	
	protected boolean getRadioButton(){
		if(jrbList.get(UIConsts.INDEX_ORIENTATION_LANDSCAPE).isSelected()){
			return UIConsts.ORIENTATION_LANDSCAPE;
		}else{
			return UIConsts.ORIENTATION_PORTRAIT;
		}
	}
	
	@Override
	public void saveContent(DocObject obj) {

		obj.setAnalysisPicsList(getItems());
		obj.setAnalysisOrientation(this.getRadioButton());
	}

	@Override
	public void clearContent() {
		setItems(null);
		initItemPanel();
	}

	@Override
	public void setContent(DocObject obj) {
		if(obj.isAnalysisOrientation() == UIConsts.ORIENTATION_LANDSCAPE){
			this.setRadioButton(UIConsts.INDEX_ORIENTATION_LANDSCAPE);
		}else{
			this.setRadioButton(UIConsts.INDEX_ORIENTATION_PORTRAIT);
		}
		setItems(obj.getAnalysisPicsList());
		initItemPanel();
	}
	
	/**
	 * Create radio button group.
	 * 
	 * @param elements
	 * @param title
	 * @return
	 */
	public Container createRadioButtonGrouping(String elements[],
			String title) {
		
		JPanel panel = new JPanel(new GridLayout(1, 0));

		if (title != null) {
			Border border = BorderFactory.createTitledBorder(title);
			panel.setBorder(border);
		}

		ButtonGroup	group = new ButtonGroup();

		JRadioButton aRadioButton;

		for (int i = 0, n = elements.length; i < n; i++) {
			aRadioButton = new JRadioButton(elements[i]);
			panel.add(aRadioButton);
			group.add(aRadioButton);
			jrbList.add(aRadioButton);
		}
		return panel;
	}

	/**
	 * Set the items to panel.
	 * 
	 * @param items
	 */
	private void setItems(List<AnalysisItem<ImageComment>> items) {
		this.analysisItems = items;
	}

	private List<AnalysisItem<ImageComment>> getItems() {
		analysisItems.clear();
		AnalysisItem<ImageComment> ai;
		for(AnalysisItemPanel aip : itemPanels){
			ai = new AnalysisItem<ImageComment>();
			ai.setAnalysisItemStr(aip.getAnalysis());
			ai.addAll(aip.getPics());
			ai.setHeight(2518012);
			ai.setWidth(3060000);			
			analysisItems.add(ai);
		}
		return analysisItems;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JLabel jlTitle;

	private JScrollPane scrb;
	private JPanel pItems;
	private GridBagConstraints c;

	// button for adding new items to the end.
	private JButton jbAdd;

	private List<AnalysisItem<ImageComment>> analysisItems;

	public JScrollPane getScrb() {
		return scrb;
	}

	private List<AnalysisItemPanel> itemPanels;

	public AnalytisisItemsPanel(JFrame frame) {

		super();
		this.parent = frame;
		itemPanels = new ArrayList<AnalysisItemPanel>();
		jlTitle = new JLabel(UIConsts.LABEL_ANALYSIS_TITLE);

		pItems = new JPanel(new GridBagLayout());
		c = new GridBagConstraints();
		scrb = new javax.swing.JScrollPane(pItems);

		Container layoutContainer = createRadioButtonGrouping(
				UIConsts.RDB_ITEMS, UIConsts.RDB_TITILE);
		
		// this.add(jlTitle, BorderLayout.NORTH);
		// this.add(scrb, BorderLayout.CENTER);

		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.add(jlTitle);
		this.add(scrb);

		jbAdd = new JButton(UIConsts.BTN_EDIT_ADD_PIC_PANEL);
		jbAdd.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				addItemPanel(itemPanels.size(), null);
			}
		});
		
		JPanel jp = new JPanel();
		jp.setLayout(new FlowLayout());
		jp.add(layoutContainer);
		jp.add(jbAdd);
		
		this.add(jp);
	}

	public List<AnalysisItemPanel> getItemPanels() {
		return itemPanels;
	}

	/**
	 * Initialize picture panels
	 * 
	 */
	private void initItemPanel() {

		if(itemPanels != null){
			itemPanels.clear();
		}
		// add picture panels
		if (analysisItems == null || analysisItems.size() == 0) {
			addItemPanel(0, null);
		} else {
			this.addMultiItemPanel(analysisItems);
		}
	}

	/**
	 * Add pictures
	 * 
	 * @param pos
	 */
	public void addItemPanel(int pos, AnalysisItem<ImageComment> item) {
		if(item == null){
			item = new AnalysisItem<ImageComment>();
			item.setAnalysisItemStr("");
			item.add(null);
		}
		
		doAddItemPanel(pos, item);

		reDisplayItems();

		// getpPics().add(p,-1);
		// getpPics().revalidate();
	}

	private void doAddItemPanel(int pos, AnalysisItem<ImageComment> item) {

		AnalysisItemPanel p = new AnalysisItemPanel(this, parent, item);

		this.itemPanels.add(pos, p);
	}

	/**
	 * Add multi-pictures
	 * 
	 * @param pics
	 */
	public void addMultiItemPanel(List<AnalysisItem<ImageComment>> items) {
		int i = 0;

		for (AnalysisItem<ImageComment> item : items) {
			doAddItemPanel(i, item);
			i++;
		}

		reDisplayItems();
	}

	public void removeItem(int pos) {
		if (pos >= 0 && this.itemPanels.size() > pos) {
			this.itemPanels.remove(pos);
			this.reDisplayItems();
		}
	}

	private void reDisplayItems() {
		int i = 0;
		pItems.removeAll();

		pItems.setVisible(false);

		for (JPanel p : this.itemPanels) {

			c.gridx = 0;
			c.gridy = i;
			i++;
			pItems.add(p, c);
		}

		pItems.setVisible(true);

		pItems.revalidate();
	}
}
