package zy.UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import zy.doc.traverse.BiCellTraverse;
import zy.dso.ImageComment;
import zy.utils.UIConsts;

public class PicturePanel extends JPanel implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger logger = LogManager.getLogger(BiCellTraverse.class.getName());
	
	/**
	 * Get picture from children PicPanel.
	 * 
	 * @return
	 */
	public ImageComment getPic() {
		ImageComment imgComment = this.jpPic.getImage();
		if (imgComment != null) {
			imgComment.setComment(this.jtfComments.getText().trim());
		}
		return imgComment;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		int pos = this.parent.getPicPanels().indexOf(this);

		if (e.getSource() == btnDel) {
			// Create and set up the window.
			logger.info("Start to Del picture");
			operatePic(false, pos);
			logger.info("end to Del picture");
		} else if (e.getSource() == btnAddAfter) {
			logger.info("Start to add after picture " + pos);
			operatePic(true, pos + 1);
			logger.info("End to add after picture");
		} else {
			logger.info("Start to add before picture "  + pos);
			operatePic(true, pos);
			logger.info("End to add before picture");
		}
	}

	private void operatePic(boolean isAdd, int pos) {

		if (isAdd) {
			// 1. add pic
			parent.addPicturePanel(pos, null);
		} else {
			// 2. delete pic
			parent.removePicture(pos);
		}
	}

	private JTextField jtfComments;

	private PicPanel jpPic;

	private JButton btnDel;

	private JButton btnAddAfter;

	private JButton btnAddBefore;

	private PicturesDialog parent;

	private ImageComment picture;

	/**
	 * Constructor, taking 2 parameters.
	 * 
	 * @param parent
	 *            the parent component.
	 * @param image
	 *            the image to be displayed; if it is null, then an empty
	 *            picPanel will be created.
	 */
	public PicturePanel(PicturesDialog parent, ImageComment image) {
		this.parent = parent;
		this.picture = image;
		init();
	}

	public void init() {

		this.setPreferredSize(new Dimension(300, 350));

		this.setMaximumSize(new Dimension(300, 350));

		this.setMinimumSize(new Dimension(300, 350));

		this.setLayout(new BorderLayout());

		this.setBorder(BorderFactory.createLineBorder(Color.black));

		// Set up the content pane.
		addComponentsToPane();
	}

	public void addComponentsToPane() {

		jtfComments = new JTextField();
		jtfComments.setPreferredSize(new Dimension(200, 20));
		if (this.picture != null) {
			jtfComments.setText(this.picture.getCommentWithoutNumber());
		} else {
			jtfComments.setText("");
		}
		jpPic = new PicPanel(this.picture);

		jpPic.setPreferredSize(new Dimension(200, 280));

		btnDel = new JButton(UIConsts.BTN_EDIT_ADD_PIC_DELETE);
		btnDel.setPreferredSize(new Dimension(200, 30));
		btnDel.addActionListener(this);

		btnAddAfter = new JButton(UIConsts.BTN_EDIT_ADD_PIC_AFTER);
		btnAddAfter.setPreferredSize(new Dimension(200, 30));
		btnAddAfter.addActionListener(this);

		btnAddBefore = new JButton(UIConsts.BTN_EDIT_ADD_PIC_BEFORE);
		btnAddBefore.setPreferredSize(new Dimension(200, 30));
		btnAddBefore.addActionListener(this);

		GridLayout btnLayout = new GridLayout(0, 3);
		JPanel p1 = new JPanel();
		p1.setLayout(btnLayout);

		p1.add(btnDel);
		p1.add(btnAddAfter);
		p1.add(btnAddBefore);

		JPanel panel = new JPanel();

		BoxLayout experimentLayout = new BoxLayout(panel, BoxLayout.PAGE_AXIS);
		panel.setLayout(experimentLayout);
		// panel.setBorder(paneEdge);

		panel.add(jtfComments);
		panel.add(p1);
		panel.add(jpPic);

		this.add(panel, BorderLayout.CENTER);
	}
}
