package com.example.remindme;

import com.google.api.client.googleapis.batch.BatchRequest;
import com.google.api.client.googleapis.batch.json.JsonBatchCallback;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.http.HttpHeaders;
import com.google.api.services.calendar.model.Calendar;

import java.io.IOException;
import java.util.List;

/**
 * Asynchronously insert a new calendar.
 * 
 * 
 */
class AsyncBatchInsertCalendars extends CalendarAsyncTask {

  private final List<Calendar> calendars;

  AsyncBatchInsertCalendars(CalendarSampleActivity calendarSample, List<Calendar> calendars) {
    super(calendarSample);
    this.calendars = calendars;
  }

  @Override
  protected void doInBackground() throws IOException {
    BatchRequest batch = client.batch();
    for (Calendar calendar : calendars) {
      client.calendars().insert(calendar).setFields(CalendarInfo.FIELDS)
          .queue(batch, new JsonBatchCallback<Calendar>() {

            public void onSuccess(Calendar calendar, HttpHeaders headers) {
              model.add(calendar);
            }

            @Override
            public void onFailure(GoogleJsonError err, HttpHeaders headers) throws IOException {
              Utils.logAndShowError(activity, CalendarSampleActivity.TAG, err.getMessage());
            }
          });
    }
    batch.execute();
  }
}
