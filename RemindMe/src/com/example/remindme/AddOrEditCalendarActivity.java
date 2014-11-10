package com.example.remindme;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Activity to add or edit a calendar.
 * 
 *
 */
public class AddOrEditCalendarActivity extends Activity {

  private EditText summaryEditText;
  private String id;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.addcalendar);
    summaryEditText = (EditText) findViewById(R.id.summaryText);
    TextView titleTextView = (TextView) findViewById(R.id.textViewTitle);
    id = getIntent().getStringExtra("id");
    if (id != null) {
      titleTextView.setText(R.string.edit);
      summaryEditText.setText(getIntent().getStringExtra("summary"));
    } else {
      titleTextView.setText(R.string.description_add);
    }
  }

  public void onSave(View view) {
    String summary = summaryEditText.getText().toString();
    if (summary.length() > 0) {
      Intent t = new Intent();
      if (id != null) {
        t.putExtra("id", id);
      }
      t.putExtra("summary", summary);
      setResult(Activity.RESULT_OK, t);
    } else {
      setResult(Activity.RESULT_CANCELED);
    }
    finish();
  }

  public void onCancel(View view) {
    setResult(Activity.RESULT_CANCELED);
    finish();
  }
}

