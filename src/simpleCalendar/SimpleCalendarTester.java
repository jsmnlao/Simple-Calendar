package simpleCalendar;

import java.io.IOException;

/**
 * Fall 2022: CS 151 Programming Assignment 5 (Simple GUI Calendar) Solution
 * @author Jasmine Lao
 * @version 1.0 11/7/2022
 */

/**
 * The SimpleCalendarTester creates the model and view of the calendar and attaches the view with the model.
 */
public class SimpleCalendarTester {

	public static void main(String[] args) throws ClassNotFoundException, IOException {
		final CalendarModel model = new CalendarModel();
		final CalendarView view = new CalendarView(model);
//		view.attach(model);
		model.attach(view);
	}
}
