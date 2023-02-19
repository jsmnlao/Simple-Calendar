package simpleCalendar;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
/**
 * Fall 2022: CS 151 Programming Assignment 5 (Simple GUI Calendar) Solution
 * @author Jasmine Lao
 * @version 1.0 11/7/2022
 */

/**
 * An EventsInDates object holds a specific date and an ArrayList of Event objects that occur on that sdate.
 */
public class EventsInDates implements Serializable {
	
	private LocalDate date;
	private ArrayList<Event> events;
	
	/**
	 * EventsInDates constructor that initializes instance variables.
	 * @param date
	 */
	public EventsInDates(LocalDate date) {
		this.date = date;
		this.events = new ArrayList<Event>();
	}
	
	//*********GETTER METHODS*********//
	
	/**
	 * Returns date in LocalDate form.
	 * @return LocalDate
	 */
	public LocalDate getDate() {
		return this.date;
	}
	
	/**
	 * Returns size of events array list.
	 * @return int
	 */
	public int getEventsSize() {
		return events.size();
	}
	
	/**
	 * Returns ArrayList of Events for this object.
	 * @return ArrayList<Event>
	 */
	public ArrayList<Event> getDatesEvents(){
		return this.events;
	}
	
	//*********OTHER METHODS*********//
	
	/**
	 * Returns string representation of EventInDates object.
	 * @return String
	 */
	public String toString(){
		String result = "";
		for(Event e: events) {
			result += e.toString();
		}
		return result;
	}

	/**
	 * Add new Event into EventInDates array list.
	 * @param newEvent
	 */
	public void addEventsInDates(Event newEvent) {
		if(newEvent.getDate().equals(date)) {
			events.add(newEvent);
		}
	}
}
