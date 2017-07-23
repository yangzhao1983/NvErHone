package zy.UI;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.swing.JPanel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import zy.dso.ImageComment;
import zy.utils.UIUtils;

/**
 * This panel will hole the picture.
 * 
 * 
 * @author yangzhao
 * 
 */
public class PicPanel extends JPanel implements DropTargetListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger logger = LogManager.getLogger(PicPanel.class.getName());
	
	/**
	 * Get picture from this panel.
	 * 
	 * @return
	 */
	public ImageComment getImage() {
		return image;
	}
	
	@Override
	public void dragEnter(DropTargetDragEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dragExit(DropTargetEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dragOver(DropTargetDragEvent arg0) {
		// TODO Auto-generated method stub

	}

	@SuppressWarnings("rawtypes")
	public void drop(DropTargetDropEvent dtde) {
		if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
			dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);

			List list;
			try {
				list = (List) (dtde.getTransferable()
						.getTransferData(DataFlavor.javaFileListFlavor));

				Iterator iterator = list.iterator();
				while (iterator.hasNext()) {
					File f = (File) iterator.next();
					ImageComment image = UIUtils
							.getPicture(f.getAbsolutePath());
					this.image = image;
					this.repaint();
				}
			} catch (UnsupportedFlavorException e) {
				logger.error(e.getMessage());
				logger.error("UnsupportedFlavorException");
				e.printStackTrace();
			} catch (IOException e) {
				logger.error(e.getMessage());
				logger.error("IOException");
				e.printStackTrace();
			}
			dtde.dropComplete(true);
			// this.updateUI();
		} else {
			dtde.rejectDrop();
		}

	}

	@Override
	public void dropActionChanged(DropTargetDragEvent arg0) {
		// TODO Auto-generated method stub

	}

	private ImageComment image;

	/**
	 * Used when initializing
	 * 
	 * @param image
	 */
	public PicPanel(ImageComment image) {
		this.image = image;
		new DropTarget(this, DnDConstants.ACTION_COPY_OR_MOVE, this);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (this.image != null) {
			// Image image = ImageIO.read(new File(
			// "D:\\useful files\\travel\\����\\pic\\pintu4.jpg"));

			// scale the picture
			g.drawImage(
					image.getImage().getScaledInstance(this.getWidth(),
							this.getHeight(), Image.SCALE_SMOOTH), 0, 0, null);
		}

	}
}
