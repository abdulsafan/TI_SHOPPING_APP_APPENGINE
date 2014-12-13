package com.example.remindme;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.google.api.services.calendar.Calendar;
import com.example.remindme.storedataendpoint.Storedataendpoint;
import com.example.remindme.storedataendpoint.model.StoreData;
import com.example.remindme.storeeventendpoint.Storeeventendpoint;
import com.example.remindme.storeeventendpoint.model.CollectionResponseStoreEvent;
import com.example.remindme.storeeventendpoint.model.Key;
import com.example.remindme.storeeventendpoint.model.StoreEvent;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.jackson2.JacksonFactory;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class Event extends Activity {
	String EventID=null;
	Key ID;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.event);
        String Etitle = null;
        String Edescription=null;
        String Elocation=null;
        String Edate=null;
        TextView Title = (TextView) findViewById(R.id.tit);
        TextView loca = (TextView) findViewById(R.id.loca);
        TextView des = (TextView) findViewById(R.id.des);
        TextView dat = (TextView) findViewById(R.id.dat);
        
        Intent i = getIntent();
        // getting attached intent data
        String EventName = i.getStringExtra("event");
        // displaying selected product name
        
        CollectionResponseStoreEvent eventLists  = null; 
		try {
			eventLists = new GetEventTask().execute().get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        List<StoreEvent> eventList= eventLists.getItems();
        StoreEvent initial;
        String[] title=new String[eventList.size()];
        String[] Description=new String[eventList.size()];
        String[] Location=new String[eventList.size()];
        String[] Group=new String[eventList.size()];
        String[] Date=new String[eventList.size()];
        String[] EventId=new String[eventList.size()];

        ArrayList<String> Events = new ArrayList<String>();
        ArrayList<String> EventIds = new ArrayList<String>();
        String efind="";
        for(int a=0; a<eventList.size(); a++){
        	initial=eventList.get(a);
        	title[a]=initial.getEventName();
        	Description[a]=initial.getDescription();
        	Location[a]=initial.getLocation();
        	Group[a]=initial.getGroup();
        	Date[a]=initial.getEventTime();
        	EventId[a]=initial.getEventID();
        	efind=title[a]+" "+Date[a];
        	if(EventName.equalsIgnoreCase(efind)){
        		ID=initial.getKey();
        		EventID=EventId[a];
        		Etitle = title[a];
                Edescription=Description[a];
                Elocation=Location[a];
                Edate=Date[a];
                break;
        	}
        }
        Title.setText(Etitle);
        des.setText(Edescription);        
        loca.setText(Elocation);        
        dat.setText(Edate);
	}
	private class GetEventTask extends AsyncTask<Void, Void, CollectionResponseStoreEvent> {
        
    	protected CollectionResponseStoreEvent doInBackground(Void... params) {
    		Storeeventendpoint.Builder endpointBuilder = new Storeeventendpoint.Builder(AndroidHttp.newCompatibleTransport(), new JacksonFactory(), null);
    		endpointBuilder = CloudEndpointUtils.updateBuilder(endpointBuilder);
    		CollectionResponseStoreEvent result;
    		Storeeventendpoint endpoint = endpointBuilder.build();
    		try {
    			result = endpoint.listStoreEvent().execute();
    		} catch (IOException e){
    			e.printStackTrace();
    			result=null;
    		}
    		return result;
    		}
    }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//MenuInflater inflater = getMenuInflater();
		menu.add(1,1,1,"").setTitle("Edit Event");
		menu.add(1,2,2,"").setTitle("Delete");
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
			//Edit Event
			System.out.println(EventID);
			return true;
		case 2:
			new RemoveEventTask().execute(ID);
			new RemoveEvent().execute(EventID);
			this.finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}
	private class RemoveEvent extends AsyncTask<String, Void, Void> {
		
		
		@Override
		protected Void doInBackground(String... params) {
			Calendar service = new Calendar.Builder( AndroidHttp.newCompatibleTransport(), new JacksonFactory(), null)
		    .setApplicationName("RemindMe").build();

		// Delete an event
		try {
			service.events().delete("primary", params[0]).execute();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		}
	}
	private class RemoveEventTask extends AsyncTask<Key, Void, Void> {
        
        @Override
        protected Void doInBackground(Key... params) {
          
          Storeeventendpoint.Builder builder = new Storeeventendpoint.Builder(
              AndroidHttp.newCompatibleTransport(), new JacksonFactory(), null);

          builder = CloudEndpointUtils.updateBuilder(builder);

          Storeeventendpoint endpoint = builder.build();
          Long EventID=params[0].getId();

          try {
            endpoint.removeStoreEvent(EventID).execute();
          } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }

          return null;
        }
    }

}
