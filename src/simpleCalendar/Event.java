package simpleCalendar;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.GregorianCalendar;

/**
 * Fall 2022: CS 151 Programming Assignment 5 (Simple GUI Calendar) Solution
 * @author Jasmine Lao
 * @version 1.0 11/7/2022
 */

/**
 * An Event object holds name, date, interval (and start/end date if recurring), 
 * and contains methods to manipulate these fields and check for conflicts in time.
 */
public class Event implements Comparable<Event>, Serializable{
	
	private String name;
	private LocalDate date;
	private LocalTime startTime;
	private LocalTime endTime;
	
	/**
	 * Non-argument constructor that initializes the instance variables to null.
	 */
	public Event() {
		this.name = null;
		this.date = null;
		this.startTime = null;
		this.endTime = null;
	}
	
	/**
	 * Constructor that initializes instance variables with given parameters
	 * @param eventName
	 * @param d, date
	 * @param s, start time
	 * @param e, end time
	 */
	public Event(String eventName, LocalDate d, LocalTime s, LocalTime e) {
		this.name = eventName;
		this.date = d;
		this.startTime = s;
		this.endTime = e;
	}
	
	
	//*********SETTER METHODS*********//
	
	/**
	 *  Sets given name to instance variable name.
	 *  @param: n, a String that holds name
	 */
	public void setName(String n) {
		this.name = n;
	}
	
	/**
	 * Sets given date to instance variable date and converts String date (MM/DD/YYYY) to LocalDate (yyyy-MM-d) format.
	 * @param d, a String that holds date
	 */
	public void setDate(LocalDate d) {
		this.date = d;
	}
	
	
	/**
	 * Sets start date of recurring event.
	 * @param start, the given starting date
	 */
	public void setStartDate(LocalTime start) {
		this.startTime = start;
	}
	
	/**
	 * Sets end date of recurring event.
	 * @param end, the given ending date
	 */
	public void setEndDate(LocalTime end) {
		this.endTime = end;
	}
	
	
	//*********GETTER METHODS*********//
	
	/**
	 * Returns name of event.
	 * @return String name
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Returns date of event in format: MM/DD/YYYY.
	 * @return the date (MM/DD/YY) as String
	 */
	public LocalDate getDate() {
		return this.date;
	}
	
	/**
	 * Returns start time in LocalDate format.
	 * @return LocalTime
	 */
	public LocalTime getStartTime() {
		return this.startTime;
	}
	
	/**
	 * Returns end time in LocalDate format.
	 * @return LocalTime
	 */
	public LocalTime getEndTime() {
		return this.endTime;
	}
	
	//*********OTHER METHODS*********//
	
	/**
	 * Returns a simple String representation of an event.
	 * @return the String consisting of name, date, start, and end times
	 */
	public String toString() {
		return this.getName() + ": " + this.getStartTime() + " - " + this.getEndTime() + "\n";
	}
	
	/**
	 * Returns true if this Event equals that Event.
	 * @param Object, the other object
	 * @return boolean value
	 */
	@Override
	public boolean equals(Object otherObj) {
		if(this == otherObj) {
			return true;
		}
		if(otherObj == null) {
			return false;
		}
		if(this.getClass() != otherObj.getClass()) {
			return false;
		}
		Event that = (Event) otherObj;
		return (this.getDate() == that.getDate()) && (this.getStartTime() == that.getStartTime());
	}
	
	/**
	 * Compares two events.
	 * @return int, an integer that represents if this event is before, after, or same as that event
	 */
	public int compareTo(Event that) {
		return this.getStartTime().compareTo(that.getStartTime());
	}
	
	/**
	 * Checks for event conflicts given date by comparing date, interval, start/end time.
	 * @param that event to compare
	 * @return true if conflict occurs, false otherwise
	 */
	public boolean checkForConflict(Event that) {
		if(this.getDate().equals(that.getDate())) {			
			if(this.getStartTime().isBefore(that.getStartTime()) 
					&& this.getStartTime().isBefore(that.getEndTime())
					&& this.getEndTime().isAfter(that.getStartTime())
					&& this.getEndTime().isBefore(that.getEndTime())){
				return true;
			}
			if(this.getStartTime().isAfter(that.getStartTime())
					&& this.getStartTime().isBefore(that.getEndTime())
					&& this.getEndTime().isAfter(that.getStartTime())
					&& this.getEndTime().isAfter(that.getEndTime())) {
				return true;
			}
			if(this.getStartTime().isBefore(that.getStartTime())
				&& this.getStartTime().isBefore(that.getEndTime())
				&& this.getEndTime().isBefore(that.getEndTime())
				&& this.getEndTime().isBefore(that.getStartTime())){
					return false;
			}
			if(that.getStartTime().isBefore(this.getStartTime())
					&& that.getStartTime().isBefore(this.getEndTime())
					&& that.getEndTime().isBefore(this.getEndTime())
					&& that.getEndTime().isBefore(this.getStartTime())){
						return false;
				}
			return true;
		}
		return false;
	}
}

