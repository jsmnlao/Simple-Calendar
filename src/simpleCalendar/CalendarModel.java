package simpleCalendar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Fall 2022: CS 151 Programming Assignment 5 (Simple GUI Calendar) Solution
 * @author Jasmine Lao
 * @version 1.0 11/7/2022
 */

/**
 * The CalendarModel class contains the data structures for holding the view and 
 * all events in the calendar. This class is responsible for attaching and mutating
 * these data structures and contains methods that help the view save and display events.  
 */
public class CalendarModel implements Serializable{

	ArrayList<EventsInDates> allEventsPerDate; //Data structure that holds events per date
	ArrayList<ChangeListener> listeners; //Data structure that holds views
	
	static boolean savingEventStatus;
	static String fileName = "events.txt";


	/**
	 * CalendarModel constructor that iniitalizes instance variables
	 */
	public CalendarModel() {
		this.allEventsPerDate = new ArrayList<EventsInDates>();
		this.listeners = new ArrayList<ChangeListener>();
	}

	/**
	 * Adds change listener to listeners array list.
	 * Serves as ATTACHER.
	 * @param listener, listener to be added to ArrayList
	 */
	public void attach(ChangeListener listener) {
		listeners.add(listener);
	}
	
	/**
	 * Returns EventsInDates list that contains list with date.
	 * @param date, date we are looking for
	 * @return EventsInDates
	 */
	public EventsInDates getEventsInDatesForDate(LocalDate date) {
		for(EventsInDates list : allEventsPerDate) {
			if(list.getDate() == date) {
				return list;
			}
		}
		return null;
	}
	
	/**
	 * Returns true if EventsInDates list exists for date, false otherwise.
	 * @param date, date we are looking for
	 * @return boolean value
	 */
	public boolean eventListForDateExists(LocalDate date) {
		for(EventsInDates list : allEventsPerDate) {
			if(list.getDate().equals(date)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Adds an EventsInDates list to the allEventsPerDate array list. Updates listener array list.
	 * Serves as MUTATOR.
	 * @param EventsInDates, event list to be added
	 */
	public void addEventsInDates(EventsInDates eventList) {
		allEventsPerDate.add(eventList);
		ChangeEvent e = new ChangeEvent(this);
		for(ChangeListener listener : listeners) {
			listener.stateChanged(e);
		}
	}

	/**
	 * Returns an allEventsPerDate ArrayList consisting of all event lists.
	 * Serves as ACCESSOR.
	 * @return ArrayList<EventsInDates>, an ArrayList of EventsInDates
	 */
	public ArrayList<EventsInDates> getEvents() {
		return allEventsPerDate;
	}
	
	/**
	 * Saves an event into the corresponding EventsInDate list. 
	 * @param title
	 * @param date
	 * @param start
	 * @param end
	 * @return boolean value: true if successful, false otherwise
	 */
	public boolean saveEvent(String title, String date, String start, String end) {
		savingEventStatus = true;

		try {
			DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/d/yyyy");
			DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm");

			LocalDate dateInLocalDate = LocalDate.parse(date, dateFormatter);
			LocalTime startInLocalTime = LocalTime.parse(start, timeFormatter);
			LocalTime endInLocalTime = LocalTime.parse(end, timeFormatter);

			//Create new Event with parameters
			Event newEvent = new Event();
			newEvent.setName(title);
			newEvent.setDate(dateInLocalDate);
			newEvent.setStartDate(startInLocalTime);
			newEvent.setEndDate(endInLocalTime);
			
			
			for(EventsInDates list : allEventsPerDate) {
				for(Event e : list.getDatesEvents()) {
					if(e.getDate().equals(dateInLocalDate)) {
						if(e.checkForConflict(newEvent) == true) {
							savingEventStatus = false;
							return false;
						}
					}
				}
			}

			boolean contains = false;
			for(EventsInDates list : allEventsPerDate) {
				if(list.getDate().equals(dateInLocalDate)) { 
					contains = true;
					list.addEventsInDates(newEvent);
				}
			}
			if(contains == false) {
				EventsInDates newEventList = new EventsInDates(newEvent.getDate());
				newEventList.addEventsInDates(newEvent);
				this.addEventsInDates(newEventList);
			}

		}
		catch(DateTimeParseException x) {
			savingEventStatus = false;
		}
		return true;
	}

	/**
	 * Displays the date and events for current date.
	 * @param calendar
	 * @return String
	 */
	public String getDateAndEvents(Calendar calendar) {
		String result = "";
		String dayOfWeek = getDayOfWeekName(calendar.get(Calendar.DAY_OF_WEEK));
		int month = calendar.get(Calendar.MONTH)+1;
		int date = calendar.get(Calendar.DATE);
		int year = calendar.get(Calendar.YEAR);
		//Display current date
		result += dayOfWeek + " " + month + "/" + date + "/" + year + "\n	";

		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/d/yyyy");
		String dateInString = month + "/" + date + "/" + year;
		LocalDate dateInLocalDate = LocalDate.parse(dateInString, dateFormatter);
//		boolean listExists = eventListForDateExists(dateInLocalDate);
		
		String events = "";
		//Set listOfEventsForDate equal to EventsInDates with current date
		EventsInDates listOfEventsForDate = new EventsInDates(dateInLocalDate);
		//If EventsInDate already exists, set it equal to that one instead
		for(EventsInDates a : allEventsPerDate) {
			if(a.getDate().equals(dateInLocalDate)) {
//				listOfEventsForDate = a;
				for(Event eventInA: a.getDatesEvents()) {
					listOfEventsForDate.addEventsInDates(eventInA);
				}
			}
		}
		
		//Append each event onto result
		for(Event e : listOfEventsForDate.getDatesEvents()) {
			if(!events.contains(e.toString())) {
				events += e.toString() + "	";
			}
		}
		result += events;
		return result;
	}

	/** 
	 * Returns the correct day of week name given number value.
	 * @param value
	 * @return String
	 */
	public String getDayOfWeekName(int value) {
		String name = "";
		if (value == 1) {
			name = "SUNDAY";
		}
		else if (value == 2) {
			name = "MONDAY";
		} 
		else if (value == 3) {
			name = "TUESDAY";
		} 
		else if (value == 4) {
			name = "WEDNESDAY";
		} 
		else if (value == 5) {
			name = "THURSDAY";
		} 
		else if (value == 6) {
			name = "FRIDAY";
		} 
		else if (value == 7) {
			name = "SATURDAY";
		} 
		return name;
	}

	/**
	 * Creates new file if file with filename doesn't exist.
	 * @throws IOException
	 */
	public void createFile() throws IOException {
		File file = new File("events.txt");
		file.createNewFile();
	}
	
	/**
	 * Loads previously saved events into the program. Uses deserialization.
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void loadProgram() throws IOException, ClassNotFoundException{
		try {
			FileInputStream file = new FileInputStream(fileName);
			ObjectInputStream input = new ObjectInputStream(file);
			ArrayList<EventsInDates> list = (ArrayList<EventsInDates>) input.readObject();
			input.close();
			allEventsPerDate = list;
			file.close();
		}
		catch(FileNotFoundException x){
			System.out.println(x.getMessage());
		}
		catch(IOException x){
			System.out.println(x.getMessage());
		}
		catch(ClassNotFoundException x) {
			System.out.println(x.getMessage());
		}
	}

	/**
	 * Saves current events into a file and quits program. Uses serialization.
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void quitProgram() throws IOException {
		try {
			FileOutputStream file = new FileOutputStream(fileName);
			ObjectOutputStream output = new ObjectOutputStream(file);
			output.writeObject(allEventsPerDate);
			output.close();
			file.close();
		}
		catch(IOException x) {
			x.getMessage();
		}
	}
	
}

