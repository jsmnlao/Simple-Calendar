package simpleCalendar;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Fall 2022: CS 151 Programming Assignment 5 (Simple GUI Calendar) Solution
 * @author Jasmine Lao
 * @version 1.0 11/7/2022
 */

/**
 * The CalendarView class is a JFrame that holds the GUI part of the calendar and
 * also implements ChangeListener so it can attach to the model. This class contains 
 * all the instance variables (ie. JComponents, JButtons, etc) necessary to for the GUI
 * implementation and also the methods that help display the GUI part onto the JFrame. 
 */
public class CalendarView extends JFrame implements ChangeListener, Serializable{

	private CalendarModel model;
	private JPanel topBar;
	private JPanel monthPanel;
	private JTextArea eventArea;

	private JButton previousButton;
	private JButton nextButton;
	private JButton quitButton;
	private JButton createButton;
	
	private JButton currentDay;
	private ArrayList<JButton> daysAdded;
	private Calendar calendar;
	static JDialog popup;
	private JButton isClicked;
		
	/**
	 * CalendarView constructor that takes in a CalendarModel, loads events into program, and
	 * can be treated as a JFrame that holds components of the calendar.  
	 * @param model
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public CalendarView(CalendarModel model) throws ClassNotFoundException, IOException {
		this.model = model;
		daysAdded = new ArrayList<>();
		model.loadProgram();
		calendar = Calendar.getInstance();

		this.setTitle("Simple GUI Calandar");
		this.setSize(650,400);
		this.setLayout(new BorderLayout());

		topBar = new JPanel(new FlowLayout());
		monthPanel = getCalendarView();
		eventArea = getDayEvents();
		eventArea.setEditable(false);

		previousButton = getPreviousButton();
		nextButton = getNextButton();
		quitButton = getQuitButton();
		createButton = getCreateButton();

		topBar.add(previousButton);
		topBar.add(createButton);
		topBar.add(nextButton);


		this.add(topBar, BorderLayout.NORTH);
		this.add(monthPanel, BorderLayout.WEST);
		this.add(eventArea, BorderLayout.CENTER);
		this.add(quitButton, BorderLayout.SOUTH);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}


	/**
	 * Returns JPanel with month view.
	 * @return JPanel containing month view with day JButtons
	 */
	public JPanel getCalendarView() {
		JPanel tempPanel = new JPanel(new BorderLayout()); //main panel for calendar view

		//Display month and year text to NORTH of panel
		JLabel monthYearLabel = new JLabel();
		String monthString = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US);
		int year = calendar.get(Calendar.YEAR); 
		int month = calendar.get(Calendar.MONTH) + 1; 
		int date = calendar.get(Calendar.DATE); 
		monthYearLabel.setText(monthString + " " + year);
		tempPanel.add(monthYearLabel, BorderLayout.NORTH);

		//Displays JButton days in month form
		JPanel monthView = new JPanel(new GridLayout(0,7,5,5));
		String[] headers = {"S", "M", "T", "W", "T", "F", "S"};
		//Display day of the week headers on top of month view
		for(int i = 0; i < 7; i++) {
			JLabel header = new JLabel();
			header.setText("      " + headers[i]);
			monthView.add(header);
		}
		
		// To figure out the day of week of the 1st day of the given month
		LocalDate dayOfWeek = LocalDate.of(year, month, 1);
		int startingDayOfWeek = -1;
		if (dayOfWeek.getDayOfWeek().toString().equals("MONDAY")) {
			startingDayOfWeek = 1;
		} 
		else if (dayOfWeek.getDayOfWeek().toString().equals("TUESDAY")) {
			startingDayOfWeek = 2;
		} 
		else if (dayOfWeek.getDayOfWeek().toString().equals("WEDNESDAY")) {
			startingDayOfWeek = 3;
		} 
		else if (dayOfWeek.getDayOfWeek().toString().equals("THURSDAY")) {
			startingDayOfWeek = 4;
		} 
		else if (dayOfWeek.getDayOfWeek().toString().equals("FRIDAY")) {
			startingDayOfWeek = 5;
		} 
		else if (dayOfWeek.getDayOfWeek().toString().equals("SATURDAY")) {
			startingDayOfWeek = 6;
		} 
		else if (dayOfWeek.getDayOfWeek().toString().equals("SUNDAY")) {
			startingDayOfWeek = 7;
		}
		// Prints spaces before 1st day of month to match day of week
		for (int j = 0; j < startingDayOfWeek; j++) {
			JButton emptyDay = new JButton();
			monthView.add(emptyDay);
		}
		// Print actual calendar
		int[] daysPerMonth = { 0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
		for(int i = 1; i < daysPerMonth[month]+1; i++) {
//			int todaysMonth = LocalDate.now().getMonthValue();
			String dateString = Integer.toString(date);
			JButton day = new JButton("" + i);
			day.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					clickedButton();
					deOutlineDate(currentDay);
					deOutlineDate(isClicked);
					isClicked = day;
					outlineDate(day);
					clickedButton();
				}
			});
			isClicked = day;
			if(day.getText().equals(dateString) && isClicked == day) {
				isClicked = day;
				currentDay = day; //set current day to this JButton reference if date matches
				outlineDate(day);
			}
			daysAdded.add(day);
			monthView.add(day); //add JButton to monthView panel
		}
		monthPanel = tempPanel;
		tempPanel.add(monthView, BorderLayout.CENTER);
		return tempPanel;

	}
	
	/**
	 * Return true if current JButton is clicked, false otherwise.
	 * @param day
	 * @return boolean value
	 */
	public boolean isClickedButton(JButton day) {
		return day == isClicked;
	}
	
	/**
	 * Updates event area if JButton is clicked.
	 */
	private void clickedButton() {
		String date = isClicked.getText();
		int dateInInt = Integer.parseInt(date);
		int month = calendar.get(Calendar.MONTH);
		int year = calendar.get(Calendar.YEAR);
		
		calendar.set(Calendar.DATE, dateInInt);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.YEAR, year);

		updateEventArea();
	}
	
	/**
	 * Outlines JButton with yellow background and black border.
	 * @param day
	 */
	public void outlineDate(JButton day) {
		day.setBackground(Color.YELLOW); //set current day to YELLOW if month is current month
		day.setBorder(BorderFactory.createMatteBorder(2,2,2,2, Color.BLACK));
	}
	
	/**
	 * "Deoutlines" JButton and reverts to original background and border.
	 * @param day
	 */
	public void deOutlineDate(JButton day) {
		day.setBackground(new JButton().getBackground());
		day.setBorder(new JButton().getBorder());

	}

	/**
	 * Returns JButton that updates calendar view and event area to display previous day.
	 * @return JButton
	 */
	public JButton getPreviousButton() {
		JButton previous = new JButton("< Previous");
		previous.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				updateCalendarView(-1);
				updateEventArea();
			}

		});
		return previous;
	}
	

	/**
	 * Returns JButton that updates calendar view and event area to display next day.
	 * @return JButton
	 */
	public JButton getNextButton() {
		JButton next = new JButton("Next >");
		next.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				updateCalendarView(1);
				updateEventArea();
				model.getDateAndEvents(calendar);
			}

		});
		return next;
	}
	
	/**
	 * Updates event area to show corresponding date and events.
	 */
	public void updateEventArea() {
		this.remove(eventArea);
		eventArea = new JTextArea();
		eventArea.setEditable(false);
		String events = model.getDateAndEvents(calendar);
		eventArea.setText(events);
		this.add(eventArea, BorderLayout.CENTER);
		revalidate();
		repaint();
	}
	
	/**
	 * Returns JButton that displays JDialog popup to create a new event when clicked.
	 * @return JButton
	 */
	private JButton getCreateButton() {
		JButton create = new JButton("CREATE");
		create.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showPopup();
			}
		});
		return create;
	}
	
	/**
	 * Displays JDialog popup with labels, textfields, and save JButton to get and save user input for new event.
	 */
	private void showPopup(){
		int clicks = 1;
		if(clicks > 1) {
			updateEventArea();
		}
		
		JButton clickedButton = isClicked;
		if(clickedButton == null) {
			JOptionPane.showMessageDialog(clickedButton, "Please click on a date first!");
		}
		if(clickedButton.getText() != currentDay.getText()) {
			clickedButton = isClicked;
		}
		
		String initalDate = clickedButton.getText();
		int initialMonth = calendar.get(Calendar.MONTH) + 1;
		int initialYear = calendar.get(Calendar.YEAR);
		
		popup = new JDialog();
		JPanel allFields = new JPanel(new GridLayout(4,2));
		popup.setSize(400, 150);
		popup.setLayout(new BorderLayout());
		
		JLabel title = new JLabel("Title of Event:");
		JTextField titleField = new JTextField();
		
		JLabel date = new JLabel("Date of Event (MM/DD/YYYY):");
		JTextField dateField = new JTextField();
		dateField.setText(initialMonth + "/" + initalDate + "/" + initialYear);
		
		JLabel start = new JLabel("Start Time (24-hour format):");
		JTextField startField = new JTextField();
		
		JLabel end = new JLabel("End time (24-hour format):");
		JTextField endField = new JTextField();
		
		JButton save = new JButton("SAVE");
		save.addActionListener(new ActionListener() {
		
			public void actionPerformed(ActionEvent e) {
				String title = titleField.getText();
				String date = dateField.getText();
				String start = startField.getText();
				String end = endField.getText();
				boolean result = model.saveEvent(title, date, start, end);
//				if(CalendarModel.savingEventStatus == false) {					
//					JOptionPane.showMessageDialog(popup, "Event not created! Fields are incorrect! Try again.", "Error Message", JOptionPane.ERROR_MESSAGE);
//				}
				if(result == false) {
					JOptionPane.showMessageDialog(popup, "Event not created! Conflicts with another event! Try again.", "Error Message", JOptionPane.ERROR_MESSAGE);
				}

				popup.setVisible(false);
				popup = new JDialog();
				updateEventArea();
			}			
		});

		
		clicks++;
		allFields.add(title);
		allFields.add(titleField);
		allFields.add(date);
		allFields.add(dateField);
		allFields.add(start);
		allFields.add(startField);
		allFields.add(end);
		allFields.add(endField);
		
		popup.add(allFields, BorderLayout.CENTER);
		popup.add(save, BorderLayout.SOUTH);
		popup.setVisible(true);
	}
	
	
	/**
	 * Updates calendar view when previous/next button is pressed.
	 * @param LocalDate date
	 */
	public void updateCalendarView(int i) {
		this.remove(monthPanel);
		monthPanel = new JPanel(); 
		calendar.add(Calendar.DATE, i);
		getCalendarView();
		this.add(monthPanel, BorderLayout.WEST);
		revalidate();
		repaint();
	}
	
	/**
	 * Returns JTextArea containg a specific day's events.
	 * @return JTextArea
	 */
	public JTextArea getDayEvents() {
		eventArea = new JTextArea();
		String dayEvents = model.getDateAndEvents(calendar);
		eventArea.append(dayEvents);
		return eventArea;
	}
	
	/**
	 * Returns current day's JButton
	 * @return JButton
	 */
	public JButton getCurrentDay() {
		return currentDay;
	}

	/**
	 * Returns JButton saves current events to a file and quits the program.
	 * @return JButton
	 */
	public JButton getQuitButton() {
		JButton quit = new JButton("QUIT");
		quit.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				try {
					model.quitProgram();
				} catch (IOException x) {
					x.getMessage();
				}
				JOptionPane.showMessageDialog(null, "Current events have been saved!");
				closeFrame();
			}

		});
		return quit;
	}
	
	/**
	 * Closes the JFrame.
	 */
	public void closeFrame() {
		this.setVisible(false);
	}
	
	/**
	 * Repaints component when called.
	 * @param ChangeEvent
	 */
	public void stateChanged(ChangeEvent e) {
		repaint();
	}

	/**
	 * Attaches CalendarModel to this.
	 * @param CalendarModel model
	 */
	public void attach(CalendarModel model) {
		this.model = model;
	}


}
