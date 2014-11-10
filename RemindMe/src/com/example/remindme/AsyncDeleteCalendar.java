package com.example.remindme;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;

import java.io.IOException;

/**
 * Asynchronously delete a calendar.
 * 
 * 
 */
class AsyncDeleteCalendar extends CalendarAsyncTask {

  private final String calendarId;

  AsyncDeleteCalendar(CalendarSampleActivity calendarSample, CalendarInfo calendarInfo) {
    super(calendarSample);
    calendarId = calendarInfo.id;
  }

  @Override
  protected void doInBackground() throws IOException {
    try {
      client.calendars().delete(calendarId).execute();
    } catch (GoogleJsonResponseException e) {
      // 404 Not Found would happen if user tries to delete an already deleted calendar
      if (e.getStatusCode() != 404) {
        throw e;
      }
    }
    model.remove(calendarId);
  }
}
