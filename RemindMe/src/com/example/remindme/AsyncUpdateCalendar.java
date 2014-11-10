package com.example.remindme;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.calendar.model.Calendar;

import java.io.IOException;

/**
 * Asynchronously updates a calendar with a progress dialog.
 * 
 *
 */
class AsyncUpdateCalendar extends CalendarAsyncTask {

  private final String calendarId;
  private final Calendar entry;

  AsyncUpdateCalendar(CalendarSampleActivity calendarSample, String calendarId, Calendar entry) {
    super(calendarSample);
    this.calendarId = calendarId;
    this.entry = entry;
  }

  @Override
  protected void doInBackground() throws IOException {
    try {
      Calendar updatedCalendar =
          client.calendars().patch(calendarId, entry).setFields(CalendarInfo.FIELDS).execute();
      model.add(updatedCalendar);
    } catch (GoogleJsonResponseException e) {
      // 404 Not Found would happen if user tries to delete an already deleted calendar
      if (e.getStatusCode() != 404) {
        throw e;
      }
      model.remove(calendarId);
    }
  }
}