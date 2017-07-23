package zy.UI;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import zy.dso.DocObject;
import zy.utils.UIConsts;
import zy.utils.UIUtils;

/**
 * ¼ø¶¨ÒÀ¾Ý
 * 
 * @author yangzhao
 *
 */
public class EvidenceItemsPanel extends ContentPanel implements
		IContentPanelControl {

	private Integer getItemIndex(String strItem) {

		// TODO: how to compare and extract the items in the word doc?
		return 1;
	}

	@Override
	public void saveContent(DocObject obj) {

		obj.getGists().clear();
		if (evidenceItems != null) {
			for (JCheckBox j : jcbs) {
				if (j.isSelected()) {
					obj.getGists().add(this.getItemIndex(j.getText()));
				}
			}
		}
	}

	@Override
	public void clearContent() {
		if (evidenceItems != null) {
			for (JCheckBox j : jcbs) {
				j.setSelected(false);
			}
		}
	}

	@Override
	public void setContent(DocObject obj) {

		for (JCheckBox j : jcbs) {
			j.setSelected(false);
		}

		for (Integer index : obj.getGists()) {
			jcbs.get(index).setSelected(true);
		}

	}

	private List<JCheckBox> jcbs;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JLabel jlTitle;

	private JScrollPane scrb;
	private JPanel pItems;

	private List<String> evidenceItems;

	public EvidenceItemsPanel() {

		super();
		jcbs = new ArrayList<JCheckBox>();
		jlTitle = new JLabel(UIConsts.LABEL_EVIDENCE_TITLE);
		pItems = new JPanel();
		pItems.setLayout(new GridLayout(0, 1));
		scrb = new javax.swing.JScrollPane(pItems);
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		// this.add(jlTitle,BorderLayout.NORTH);
		this.add(jlTitle);
		this.add(scrb);
		scrb.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		// TODO:
		evidenceItems = new ArrayList<String>();
		evidenceItems
				.add("xXXXXXXXXXXXXXXXXXX1==================================================================================================");
		evidenceItems.add("xXXXXXXXXXXXXXXXXXX2");
		evidenceItems.add("xXXXXXXXXXXXXXXXXXX2");
		evidenceItems.add("xXXXXXXXXXXXXXXXXXX2");
		evidenceItems.add("xXXXXXXXXXXXXXXXXXX2");
		evidenceItems.add("xXXXXXXXXXXXXXXXXXX2");
		evidenceItems.add("xXXXXXXXXXXXXXXXXXX2");
		evidenceItems.add("xXXXXXXXXXXXXXXXXXX2");
		evidenceItems.add("xXXXXXXXXXXXXXXXXXX2");
		evidenceItems.add("xXXXXXXXXXXXXXXXXXX2");
		evidenceItems.add("xXXXXXXXXXXXXXXXXXX2");
		evidenceItems.add("xXXXXXXXXXXXXXXXXXX2");
		evidenceItems.add("xXXXXXXXXXXXXXXXXXX2");
		evidenceItems.add("xXXXXXXXXXXXXXXXXXX2");
		evidenceItems.add("xXXXXXXXXXXXXXXXXXX2");
		evidenceItems.add("xXXXXXXXXXXXXXXXXXX2");
		evidenceItems.add("xXXXXXXXXXXXXXXXXXX2");
		evidenceItems.add("xXXXXXXXXXXXXXXXXXX2");
		evidenceItems.add("xXXXXXXXXXXXXXXXXXX2");
		evidenceItems.add("xXXXXXXXXXXXXXXXXXX2");
		evidenceItems.add("xXXXXXXXXXXXXXXXXXX2");
		evidenceItems.add("xXXXXXXXXXXXXXXXXXX2");
		evidenceItems.add("xXXXXXXXXXXXXXXXXXX2");
		initPanel();
	}

	/**
	 * Initialize picture panels
	 * 
	 */
	private void initPanel() {

		JCheckBox jcb;
		for (String evidenceItem : this.evidenceItems) {

			jcb = new JCheckBox(UIUtils.createdMultiLineString(evidenceItem));
			pItems.add(jcb);
			jcbs.add(jcb);
		}
	}
}
