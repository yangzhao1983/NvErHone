package zy.date;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import zy.utils.UIUtils;

public class DateChooser extends JPanel implements ActionListener,
		ChangeListener {

	private int startYear = 1980;
	private int lastYear = 2050;

	private int width = 270;
	private int height = 200;

	private Color backGroundColor = java.awt.Color.gray; // background
	private Color palletTableColor = java.awt.Color.white; // chooser background
	private Color todayBackColor = java.awt.Color.orange; // background color
															// for today
	private Color weekFontColor = java.awt.Color.blue; // color for week
														// character
	private Color dateFontColor = java.awt.Color.black; // color for day
														// character
	private Color weekendFontColor = java.awt.Color.red; // color for weekend
															// character

	/**
	 * color for control bar
	 */
	private Color controlLineColor = java.awt.Color.pink; // background color
															// for control bar
	private Color controlTextColor = java.awt.Color.white; // color for control
															// bar tag character
															// private Color
															// rbFontColor =
															// java.awt.Color.white;
															// // color for
															// character
	// private Color rbBorderColor = java.awt.Color.red; // color for border
	// private Color rbButtonColor = java.awt.Color.pink; // color for button
	// private Color rbBtFontColor = java.awt.Color.red; // color for button
	// // character

	private JDialog dialog; // to display date chooser
	private JSpinner yearSpin; // to control year
	private JSpinner monthSpin; // to control month
	private JSpinner hourSpin; // to control hour
	private JSpinner minuteSpin;// to control minute

	private JButton[][] daysButton = new JButton[6][7]; // to display the week
														// for every day of
														// current year.

	protected JFormattedTextField jFormattedTextField; // to display the
														// formated
														// input for current
														// date
	private Calendar c = getCalendar();

	private Calendar cal = Calendar.getInstance();

	private int currentDay = cal.get(Calendar.DAY_OF_MONTH);

	public DateChooser(JFormattedTextField jftf) {
		jFormattedTextField = jftf;

		// to set the layout and border
		setLayout(new BorderLayout());
		setBorder(new LineBorder(backGroundColor, 2));
		setBackground(backGroundColor);

		// initialize and add child panel
		JPanel topYearAndMonth = createYearAndMonthPanal();
		add(topYearAndMonth, BorderLayout.NORTH);

		JPanel centerWeekAndDay = createWeekAndDayPanal();
		add(centerWeekAndDay, BorderLayout.CENTER);
	}

	/**
	 * Function :
	 * 
	 * To create year and month panel
	 * 
	 * @return
	 */
	private JPanel createYearAndMonthPanal() {
		int currentYear = c.get(Calendar.YEAR);
		int currentMonth = c.get(Calendar.MONTH) + 1;
		// int currentHour = c.get(Calendar.HOUR_OF_DAY);
		// int currentMinute = c.get(Calendar.MINUTE);

		JPanel result = new JPanel();
		result.setLayout(new FlowLayout());
		result.setBackground(controlLineColor);

		yearSpin = new JSpinner(new SpinnerNumberModel(currentYear, startYear,
				lastYear, 1));
		yearSpin.setPreferredSize(new Dimension(48, 20));
		yearSpin.setName("Year");
		yearSpin.setEditor(new JSpinner.NumberEditor(yearSpin, "####"));
		yearSpin.addChangeListener(this);
		result.add(yearSpin);

		JLabel yearLabel = new JLabel("年");
		yearLabel.setForeground(controlTextColor);
		result.add(yearLabel);

		monthSpin = new JSpinner(new SpinnerNumberModel(currentMonth, 1, 12, 1));
		monthSpin.setPreferredSize(new Dimension(35, 20));
		monthSpin.setName("Month");
		monthSpin.addChangeListener(this);
		result.add(monthSpin);

		JLabel monthLabel = new JLabel("月");
		monthLabel.setForeground(controlTextColor);
		result.add(monthLabel);

		// hourSpin = new JSpinner(new SpinnerNumberModel(currentHour, 0, 23,
		// 1));
		// hourSpin.setPreferredSize(new Dimension(35, 20));
		// hourSpin.setName("Hour");
		// hourSpin.addChangeListener(this);
		// result.add(hourSpin);
		//
		// JLabel hourLabel = new JLabel("时");
		// hourLabel.setForeground(controlTextColor);
		// result.add(hourLabel);
		//
		// minuteSpin = new JSpinner(new SpinnerNumberModel(currentMinute, 0,
		// 59,
		// 1));
		// minuteSpin.setPreferredSize(new Dimension(35, 20));
		// minuteSpin.setName("Minute");
		// minuteSpin.addChangeListener(this);
		// result.add(minuteSpin);
		//
		// JLabel minuteLabel = new JLabel("分");
		// minuteLabel.setForeground(controlTextColor);
		// result.add(minuteLabel);

		return result;
	}

	/**
	 * Function To create week panel for every day in current month
	 * 
	 * @return
	 * 
	 */
	private JPanel createWeekAndDayPanal() {
		String colname[] = { "日", "一", "二", "三", "四", "五", "六" };
		JPanel result = new JPanel();

		// set font
		result.setFont(new Font("宋体", Font.PLAIN, 12));

		result.setLayout(new GridLayout(7, 7));
		result.setBackground(Color.white);
		JLabel cell; // to display week

		for (int i = 0; i < 7; i++) {
			cell = new JLabel(colname[i]);
			cell.setHorizontalAlignment(JLabel.CENTER);
			if (i == 0 || i == 6)
				cell.setForeground(weekendFontColor);
			else
				cell.setForeground(weekFontColor);
			result.add(cell);
		}

		int actionCommandId = 0;

		for (int i = 0; i < 6; i++)
			for (int j = 0; j < 7; j++) {
				JButton numberButton = new JButton();
				numberButton.setBorder(null);
				numberButton.setHorizontalAlignment(SwingConstants.CENTER);
				numberButton.setActionCommand(String.valueOf(actionCommandId));
				numberButton.addActionListener(this);
				numberButton.setBackground(palletTableColor);

				numberButton.setForeground(dateFontColor);
				if (j == 0 || j == 6)
					numberButton.setForeground(weekendFontColor);
				else
					numberButton.setForeground(dateFontColor);

				daysButton[i][j] = numberButton;
				numberButton.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						if (e.getClickCount() == 2) {
							// mouse double click
							closeAndSetDate();
						}
					}
				});

				result.add(numberButton);
				actionCommandId++;
			}
		return result;
	}

	private JDialog createDialog(Frame owner) {
		JDialog result = new JDialog(owner, "日期时间选择", true);
		result.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		result.getContentPane().add(this, BorderLayout.CENTER);
		result.pack();
		result.setSize(width, height);
		return result;

	}

	public void showDateChooser(Point position) {

		Object tmpobj = SwingUtilities.getWindowAncestor(jFormattedTextField);
		if (tmpobj.getClass().isInstance(new JDialog())
				|| tmpobj.getClass().getSuperclass().isInstance(new JDialog())) {
			JDialog ownerdialog = (JDialog) SwingUtilities
					.getWindowAncestor(jFormattedTextField);
			Frame owner = (Frame) SwingUtilities.getWindowAncestor(ownerdialog);
			if (dialog == null || dialog.getOwner() != owner) {
				dialog = createDialog(owner);
			}
			dialog.setLocation(getAppropriateLocation(owner, position));
		} else if (tmpobj.getClass().isInstance(new JFrame())
				|| tmpobj.getClass().getSuperclass().isInstance(new JFrame())) {
			JFrame ownerFrame = (JFrame) SwingUtilities
					.getWindowAncestor(jFormattedTextField);
			if (dialog == null || dialog.getOwner() != ownerFrame) {
				dialog = createDialog(ownerFrame);

			}
			dialog.setLocation(getAppropriateLocation(ownerFrame, position));

		}
		flushWeekAndDay();
		dialog.setVisible(true);
	}

	Point getAppropriateLocation(Frame owner, Point position) {
		Point result = new Point(position);
		Point p = owner.getLocation();
		int offsetX = (position.x + width) - (p.x + owner.getWidth());
		int offsetY = (position.y + height) - (p.y + owner.getHeight());
		if (offsetX > 0) {
			result.x -= offsetX;
		}
		if (offsetY > 0) {
			result.y -= offsetY;
		}
		return result;

	}

	public void closeAndSetDate() {
		setDate(c.getTime());
		dialog.dispose();
	}

	private Calendar getCalendar() {
		Calendar result = Calendar.getInstance();
		result.setTime(getDate());
		return result;

	}

	private int getSelectedYear() {
		return ((Integer) yearSpin.getValue()).intValue();
	}

	private int getSelectedMonth() {
		return ((Integer) monthSpin.getValue()).intValue();
	}

	private int getSelectedHour() {
		return ((Integer) hourSpin.getValue()).intValue();
	}

	private int getSelectedMinute() {
		return ((Integer) minuteSpin.getValue()).intValue();
	}

	private void dayColorUpdate(boolean isOldDay) {
		int day = c.get(Calendar.DAY_OF_MONTH);
		System.out.println(day + "-----day-----");
		c.set(Calendar.DAY_OF_MONTH, currentDay);
		System.out.println("当前日期day:" + c.get(Calendar.DATE));
		int actionCommandId = day - 2 + c.get(Calendar.DAY_OF_WEEK);

		int i = actionCommandId / 7;
		int j = actionCommandId % 7;
		if (isOldDay) {
			if (j == 6 || j == 0) {
				daysButton[i][j].setForeground(weekendFontColor);
			} else {
				daysButton[i][j].setForeground(dateFontColor);
			}

		} else
			daysButton[i][j].setForeground(todayBackColor);
	}

	private void flushWeekAndDay() {
		c.set(Calendar.DAY_OF_MONTH, currentDay);
		int maxDayNo = c.getActualMaximum(Calendar.DAY_OF_MONTH);
		int dayNo = 2 - c.get(Calendar.DAY_OF_WEEK);
		System.out.println("某月日期值范围：" + dayNo + "----" + maxDayNo);

		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 7; j++) {
				String s = "";
				if (dayNo >= 1 && dayNo <= maxDayNo)
					s = String.valueOf(dayNo);
				daysButton[i][j].setText(s);
				dayNo++;
			}
		}
		dayColorUpdate(false);

	}

	public void stateChanged(ChangeEvent e) {
		JSpinner source = (JSpinner) e.getSource();
		if (source.getName().equals("Minute")) {
			c.set(Calendar.MINUTE, getSelectedMinute());

			return;

		}
		if (source.getName().equals("Hour")) {
			c.set(Calendar.HOUR_OF_DAY, getSelectedHour());
			return;
		}

		dayColorUpdate(true);
		if (source.getName().equals("Year")) {
			c.set(Calendar.YEAR, getSelectedYear());
		}

		if (source.getName().equals("Month")) {
			c.set(Calendar.MONTH, getSelectedMonth() - 1);
		}
		flushWeekAndDay();
	}

	public void actionPerformed(ActionEvent e) {
		JButton source = (JButton) e.getSource();
		if (source.getText().length() == 0)
			return;

		dayColorUpdate(true);
		source.setForeground(todayBackColor);

		int newDay = Integer.parseInt(source.getText());
		c.set(Calendar.DAY_OF_MONTH, newDay);

	}

	/**
	 * set date
	 * 
	 * @param date
	 */
	public void setDate(Date date) {
		jFormattedTextField.setText(UIUtils.convertFormatedDate2Chinese(
				getDefaultDateFormat().format(date), "-"));
	}

	public Date getDate() {
		try {
			String dateString = jFormattedTextField.getText();
			System.out.println(dateString + "---------");
			return getDefaultDateFormat().parse(dateString);

		} catch (ParseException e) {
			return getNowDate();
		} catch (Exception ee) {
			return getNowDate();
		}
	}

	private static Date getNowDate() {

		return Calendar.getInstance().getTime();
	}

	protected static SimpleDateFormat getDefaultDateFormat() {

		return new SimpleDateFormat("yyyy-MM-dd");
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
