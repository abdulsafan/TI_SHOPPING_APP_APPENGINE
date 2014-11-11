package com.example.remindme;

import java.text.Format;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Home extends Activity implements OnClickListener {
	private Cursor mCursor = null;
	private static final String[] COLS = new String[]
	{ CalendarContract.Events.TITLE, CalendarContract.Events.DTSTART};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		mCursor = getContentResolver().query(
				CalendarContract.Events.CONTENT_URI, COLS, null, null, null);
				mCursor.moveToFirst();
				Button b = (Button)findViewById(R.id.next);
				b.setOnClickListener(this);
				b = (Button)findViewById(R.id.previous);
				b.setOnClickListener(this);
				onClick(findViewById(R.id.previous));
	}
	
	@Override
	public void onClick(View v) {
	TextView tv = (TextView)findViewById(R.id.data);


	String title = "N/A";


	Long start = 0L;


	switch(v.getId()) {
	case R.id.next:
	if(!mCursor.isLast()) mCursor.moveToNext();
	break;
	case R.id.previous:
	if(!mCursor.isFirst()) mCursor.moveToPrevious();
	break;
	}
	Format df = DateFormat.getDateFormat(this);
	Format tf = DateFormat.getTimeFormat(this);
	try {
	title = mCursor.getString(0);
	start = mCursor.getLong(1);
	} catch (Exception e) {
		System.out.println("ERROR!!");
	}
	tv.setText(title+" on "+df.format(start)+" at "+tf.format(start));
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//MenuInflater inflater = getMenuInflater();
		menu.add(1,1,1,"").setTitle(R.string.addevent);
		menu.add(1,2,2,"").setTitle(R.string.addpeople);
		menu.add(1,3,3,"").setTitle(R.string.chat);
		return super.onCreateOptionsMenu(menu);
		//inflater.inflate(R.menu.home, menu);
		//return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		switch (id){
		case 1:
			scheduleAlarm(this.findViewById(android.R.id.content));
			return true;
		case 2:
			//addpeople function
			return true;
		case 3:
			//chat function
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
//		if (id == R.id.action_settings) {
//			return true;
//		}
//		return super.onOptionsItemSelected(item);
	}
	public void scheduleAlarm(View view) {
		Long time = new GregorianCalendar().getTimeInMillis() + 5000;
		Intent alarmIntent = new Intent(this, SetAlarm.class);
		AlarmManager alarmMan = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		alarmMan.set(AlarmManager.RTC_WAKEUP, time, PendingIntent.getBroadcast(
				this, 1, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT));
		
	}
}
