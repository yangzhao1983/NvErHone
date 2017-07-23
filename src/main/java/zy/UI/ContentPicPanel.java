package zy.UI;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.border.Border;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import zy.dso.ImageArrayList;
import zy.dso.ImageComment;
import zy.utils.UIConsts;
import zy.utils.UIUtils;

public class ContentPicPanel extends ContentPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JLabel jlTitle;
	protected JTextArea jtaContent;
	private JButton btnAddPics;
	private JFrame frame;
	private ImageArrayList<ImageComment> pics;
	private List<ImageArrayList<ImageComment>> lists;
	private KeyEventHandler keHandler;
	
	private static final Logger logger = LogManager.getLogger(ContentPicPanel.class.getName());

	private List<JRadioButton> jrbList = new ArrayList<JRadioButton>();

	public int getCurrentMarkIndex() {
		String content = this.jtaContent.getText();
		int charPos = this.jtaContent.getSelectionStart();
		int num = UIUtils.getCharPosRel2PicMark(charPos, content);
		if (num >= lists.size()) {
			logger.info("list size>lists.size() " + num + ">" +lists.size());
			num = 0;
		}
		return num;
	}

	protected void setRadioButton(int orientation) {
		jrbList.get(orientation).setSelected(true);
	}

	protected boolean getRadioButton() {
		if (jrbList.get(UIConsts.INDEX_ORIENTATION_LANDSCAPE).isSelected()) {
			logger.info("LANDSCAPE");
			return UIConsts.ORIENTATION_LANDSCAPE;
		} else {
			logger.info("PORTRAIT");
			return UIConsts.ORIENTATION_PORTRAIT;
		}
	}

	protected void setImageListList(List<ImageArrayList<ImageComment>> ll) {
		this.lists = ll;
		keHandler.setMapIndex2Pics(lists);
	}

	/**
	 * Get all the image lists.
	 * 
	 * @return
	 */
	protected List<ImageArrayList<ImageComment>> getImageListList() {
		List<ImageArrayList<ImageComment>> picLists = new ArrayList<ImageArrayList<ImageComment>>();

		if (lists == null) {
			return picLists;
		}
		return lists;
	}

	/**
	 * Get all the images.
	 * 
	 * @return
	 */
	protected ImageArrayList<ImageComment> getIamgeList() {
		ImageArrayList<ImageComment> imageList = new ImageArrayList<ImageComment>();

		if (pics == null) {
			return imageList;
		}
		return pics;
	}

	protected void setIamgeList(ImageArrayList<ImageComment> list) {
		pics = list;
	}

	public ContentPicPanel(String title, JFrame frame) {
		this();
		this.frame = frame;
		jlTitle.setText(title);
	}

	/**
	 * Create radio button group.
	 * 
	 * @param elements
	 * @param title
	 * @return
	 */
	public Container createRadioButtonGrouping(String elements[], String title) {

		JPanel panel = new JPanel(new GridLayout(1, 0));

		if (title != null) {
			Border border = BorderFactory.createTitledBorder(title);
			panel.setBorder(border);
		}

		ButtonGroup group = new ButtonGroup();

		JRadioButton aRadioButton;

		for (int i = 0, n = elements.length; i < n; i++) {
			aRadioButton = new JRadioButton(elements[i]);
			panel.add(aRadioButton);
			group.add(aRadioButton);
			jrbList.add(aRadioButton);
		}
		return panel;
	}

	public ContentPicPanel() {

		super();

		jtaContent = new JTextArea();
		jtaContent.setLineWrap(true);
		jtaContent.setWrapStyleWord(true);

		Container layoutContainer = createRadioButtonGrouping(
				UIConsts.RDB_ITEMS, UIConsts.RDB_TITILE);

		this.setLayout(new BorderLayout());
		jlTitle = new JLabel();
		this.add(jlTitle, BorderLayout.NORTH);

		this.add(jtaContent, BorderLayout.CENTER);

		JPanel jp = new JPanel();
		jp.setLayout(new FlowLayout());
		jp.add(layoutContainer);
		btnAddPics = new JButton(UIConsts.BTN_EDIT_PICS);

		btnAddPics.addActionListener(new ActionListener() {// Ϊ��ť�����굥���¼�
					public void actionPerformed(ActionEvent e) {
//						if (ContentPicPanel.this instanceof SurveyNoteContentPanel) {
//
//							PicturesDialog p = new PicturesDialog(lists
//									.get(ContentPicPanel.this
//											.getCurrentMarkIndex()), frame);// ʹMyJDialog����ɼ�
//							p.init();
//						} else {
						logger.info("Start to open dialog for pictures");
							PicturesDialog p = new PicturesDialog(lists
									.get(ContentPicPanel.this
											.getCurrentMarkIndex()), frame);// ʹMyJDialog����ɼ�
							p.init();
//						}
					}
				});
		jp.add(btnAddPics);

		// TODO
		keHandler = new KeyEventHandler(jtaContent);
		jtaContent.addKeyListener(keHandler);

		this.add(jp, BorderLayout.SOUTH);

		jtaContent.setPreferredSize(new Dimension(600, 400));
		jtaContent.setMinimumSize(new Dimension(600, 400));
		jtaContent.setPreferredSize(new Dimension(600, 400));
	}
}
