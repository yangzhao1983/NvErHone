package zy.date;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JLabel;

import zy.UI.DateTextFiled;

public class MyTest {

	private JFrame jf;

	private DateTextFiled tfDate;

	private JLabel lb;

	public static void main(String[] args) {

		new MyTest().init();

	}

	public void init() {
		jf = new JFrame("日期组件");

		jf.setLayout(null);

		jf.setBounds(10, 10, 300, 200);

		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		lb = new JLabel("选择日期：");

		lb.setBounds(15, 60, 80, 30);

		initTfDate();// initialize text field

		jf.add(tfDate);

		jf.add(lb);

		jf.setVisible(true);

	}

	/*
	 * 
	 * initTfDate
	 * 
	 * 初始化
	 * 
	 * JFormattedTextField tfDate;
	 */

	public void initTfDate() {

		tfDate = new DateTextFiled(true, jf);

		tfDate.setBounds(100, 60, 120, 23);

		tfDate.addMouseListener(new MouseListener() {

			public void mouseClicked(MouseEvent mouseevent) { // event for
																// clicking
																// textfield

				// System.out.println("------MyTest-----");
//				DateChooser mDateChooser = new DateChooser(tfDate);
				DateChooserChinese mDateChooser = new DateChooserChinese(tfDate);

				// set DateChooser popup position
				Point p = tfDate.getLocationOnScreen();
				p.y = p.y + 30;

				mDateChooser.showDateChooser(p);

				tfDate.requestFocusInWindow();

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
