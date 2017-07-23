package zy.UI;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFormattedTextField;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import zy.date.DateChooser;
import zy.date.DateChooserChinese;

public class DateTextFiled extends JFormattedTextField {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Component panel;

	private final boolean toGetChineseNumberCharacter;
	
	private static final Logger logger = LogManager.getLogger(DateTextFiled.class.getName());

	public DateTextFiled(boolean getChineseNumberCharacter, Component p) {
		super();

		panel = p;
		toGetChineseNumberCharacter = getChineseNumberCharacter;
		this.setPreferredSize(new Dimension(200, 30));

		this.setMaximumSize(new Dimension(200, 30));

		this.setMinimumSize(new Dimension(200, 30));
		this.addMouseListener(new MouseListener() {

			public void mouseClicked(MouseEvent mouseevent) { // event for
																// clicking
																// textfield
				logger.info("start deal with mouse click");
				// System.out.println("------MyTest-----");
				// DateChooser mDateChooser = new DateChooser(tfDate);
				DateChooser mDateChooser = null;
				if (toGetChineseNumberCharacter) {
					mDateChooser = new DateChooserChinese(DateTextFiled.this);
				} else {
					mDateChooser = new DateChooser(DateTextFiled.this);
				}
				// set DateChooser popup position
				Point p = panel.getLocationOnScreen();
				p.y = p.y + 30;

				mDateChooser.showDateChooser(p);

				panel.requestFocusInWindow();
				
				logger.info("end deal with mouse click");
			}

			public void mouseEntered(MouseEvent mouseevent) {

				// TODO Auto-generated method stub

			}

			public void mouseExited(MouseEvent mouseevent) {

				// TODO Auto-generated method stub

			}

			public void mousePressed(MouseEvent mouseevent) {

				// TODO Auto-generated method stub

			}

			public void mouseReleased(MouseEvent mouseevent) {

				// TODO Auto-generated method stub

			}
		});

	}
}
