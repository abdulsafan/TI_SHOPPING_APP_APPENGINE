package com.example.remindme;
import java.io.IOException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;


import com.example.remindme.storedataendpoint.Storedataendpoint;
import com.example.remindme.storedataendpoint.model.CollectionResponseStoreData;
import com.example.remindme.storedataendpoint.model.StoreData;
import com.example.remindme.storeeventendpoint.Storeeventendpoint;
import com.example.remindme.storeeventendpoint.model.CollectionResponseStoreEvent;
import com.example.remindme.storeeventendpoint.model.StoreEvent;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.jackson2.JacksonFactory;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Attendees;
import android.provider.CalendarContract.Calendars;
import android.provider.CalendarContract.Events;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddEvent extends Activity{
	// Parameters for quering the calendar
	// Projection array. Creating indices for this array instead of doing
	// dynamic lookups improves performance.
	public static final String[] EVENT_PROJECTION = new String[] {
			Calendars._ID, // 0
			Calendars.ACCOUNT_NAME, // 1
			Calendars.CALENDAR_DISPLAY_NAME // 2
	};

	// The indices for the projection array above.
	private static final int PROJECTION_ID_INDEX = 0;
	private static final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
	private static final int PROJECTION_DISPLAY_NAME_INDEX = 2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_event);
		
	}

	public void onClick(View view) {
		// Intent calIntent = new Intent(Intent.ACTION_INSERT);
		// calIntent.setData(CalendarContract.Events.CONTENT_URI);
		// startActivity(calIntent);
		// Getting title of event
		//Put up the Yes/No message box
		boolean canEdit;
    	
		EditText title = (EditText) findViewById(R.id.title);
		String Title = title.getText().toString();
		
		EditText location = (EditText) findViewById(R.id.location);
		String Location = location.getText().toString();

		EditText description = (EditText) findViewById(R.id.description);
		String Description = description.getText().toString();
		
		Intent i = getIntent();
        // getting attached intent data
        String GroupName = i.getStringExtra("GroupName");
		Intent intent = new Intent(Intent.ACTION_INSERT);
		intent.setType("vnd.android.cursor.item/event");
		Random rand = new Random();
		
		String EventID = Title + rand.nextInt(10000)+1;
		intent.putExtra(Events._ID, EventID);
		intent.putExtra(Events.TITLE, Title);
		intent.putExtra(Events.EVENT_LOCATION, Location);
	 	intent.putExtra(Events.DESCRIPTION, Description);
	 	intent.putExtra(Events.GUESTS_CAN_MODIFY,false);
	 	if(GroupName!="NONE"){
	 		CollectionResponseStoreData groupLists = null; 
	 			try {
	    			groupLists = new GetGroupsTask().execute().get();
	    		} catch (InterruptedException e) {
	    			// TODO Auto-generated catch block
	    			e.printStackTrace();
	    		} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
	            List<StoreData> groupList= groupLists.getItems();
	              String[] groups=new String[groupList.size()];
	              StoreData init;
	              String email="";
	              for(int a=0; a<groupList.size(); a++){
	            	init=groupList.get(a);
	            	groups[a]=init.getGroup();
	            	if(GroupName.equalsIgnoreCase(groups[a])){
	            		email=init.getGroupEmail();	
	            	}
	              }
	              String emails[]=null;
	              if(email!=""){
	            	 emails = email.split(";");  
	              }	 
	              String femails="";
	              for(int c=0; c<emails.length; c++){
	            	  femails+=emails[c]+",";
	              }
	              femails.substring(0,femails.length()-1);
	         	  intent.putExtra(Intent.EXTRA_EMAIL, femails);
	 	}
		// Setting dates
		GregorianCalendar calDate = new GregorianCalendar();
		String sDate= calDate.toString();
		String sTime= calDate.getTime().toString();
		intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
				calDate.getTimeInMillis());
		intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,
				calDate.getTimeInMillis());
		
		// Make it a full day event
		//intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);

		// Making it private and shown as busy
		intent.putExtra(Events.ACCESS_LEVEL, Events.ACCESS_PRIVATE);
		intent.putExtra(Events.AVAILABILITY, Events.AVAILABILITY_BUSY);
		new CreateEventTask().execute(Title,Location,Description,sDate,sTime,GroupName,EventID);
		startActivity(intent);
		this.finish();
	}

	public void queryCalendar(View view) {
		// Run query
		Cursor cur = null;
		ContentResolver cr = getContentResolver();
		Uri uri = Calendars.CONTENT_URI;
		String selection = "((" + Calendars.ACCOUNT_NAME + " = ?) AND ("
				+ Calendars.ACCOUNT_TYPE + " = ?))";
		
		// Replace this with your own user and account type
		String[] selectionArgs = new String[] { "safan.abdul@gmail.com",
				"com.google" };
		// Submit the query and get a Cursor object back.
		cur = cr.query(uri, EVENT_PROJECTION, selection, selectionArgs, null);
		Toast.makeText(this, String.valueOf(cur.getCount()), Toast.LENGTH_LONG)
				.show();
		// Use the cursor to step through the returned records
		while (cur.moveToNext()) {
			long calID = 0;
			String displayName = null;
			String accountName = null;

			// Get the field values
			calID = cur.getLong(PROJECTION_ID_INDEX);
			displayName = cur.getString(PROJECTION_DISPLAY_NAME_INDEX);
			accountName = cur.getString(PROJECTION_ACCOUNT_NAME_INDEX);

			// Do something with the values...
			Toast.makeText(this, "Calendar " + displayName, Toast.LENGTH_SHORT)
					.show();
		}
	}
	private class GetGroupsTask extends AsyncTask<Void, Void, CollectionResponseStoreData> {
        
    	protected CollectionResponseStoreData doInBackground(Void... params) {
    		Storedataendpoint.Builder endpointBuilder = new Storedataendpoint.Builder(AndroidHttp.newCompatibleTransport(), new JacksonFactory(), null);
    		endpointBuilder = CloudEndpointUtils.updateBuilder(endpointBuilder);
    		CollectionResponseStoreData result;
    		Storedataendpoint endpoint = endpointBuilder.build();
    		try {
    			result = endpoint.listStoreData().execute();
    		} catch (IOException e){
    			e.printStackTrace();
    			result=null;
    		}
    		return result;
    		}
    }
    private class CreateEventTask extends AsyncTask<String, Void, Void> {

        /**
         * Calls appropriate CloudEndpoint to indicate that user checked into a place.
         *
         * @param params the place where the user is checking in.
         */
        @Override
        protected Void doInBackground(String... params) {
          StoreEvent storeevent = new com.example.remindme.storeeventendpoint.model.StoreEvent();

          // Set the ID of the store where the user is.
          storeevent.setEventName(params[0]);
          storeevent.setLocation(params[1]);
          storeevent.setDescription(params[2]);
          storeevent.setEventDate(params[3]);
          storeevent.setEventTime(params[4]);
          storeevent.setGroup(params[5]);
          storeevent.setEventID(params[6]);
          Storeeventendpoint.Builder builder = new Storeeventendpoint.Builder(
              AndroidHttp.newCompatibleTransport(), new JacksonFactory(), null);

          builder = CloudEndpointUtils.updateBuilder(builder);

          Storeeventendpoint endpoint = builder.build();

          try {
            endpoint.insertStoreEvent(storeevent).execute();
          } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }

          return null;
        }
    }
}
