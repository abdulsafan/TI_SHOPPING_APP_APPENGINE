package com.example.remindme;

import com.google.api.services.calendar.model.Calendar;

import java.io.IOException;

/**
 * Asynchronously insert a new calendar.
 * 
 *
 */
class AsyncInsertCalendar extends CalendarAsyncTask {

  private final Calendar entry;

  AsyncInsertCalendar(CalendarSampleActivity calendarSample, Calendar entry) {
    super(calendarSample);
    this.entry = entry;
  }

  @Override
  protected void doInBackground() throws IOException {
    Calendar calendar = client.calendars().insert(entry).setFields(CalendarInfo.FIELDS).execute();
    model.add(calendar);
  }
}