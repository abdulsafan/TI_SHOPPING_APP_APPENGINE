package com.example.remindme;

import com.google.api.services.calendar.model.CalendarList;

import java.io.IOException;

class AsyncLoadCalendars extends CalendarAsyncTask {

	  AsyncLoadCalendars(CalendarSampleActivity calendarSample) {
	    super(calendarSample);
	  }

	  @Override
	  protected void doInBackground() throws IOException {
	    CalendarList feed = client.calendarList().list().setFields(CalendarInfo.FEED_FIELDS).execute();
	    model.reset(feed.getItems());
	  }

	  static void run(CalendarSampleActivity calendarSample) {
	    new AsyncLoadCalendars(calendarSample).execute();
	  }
}