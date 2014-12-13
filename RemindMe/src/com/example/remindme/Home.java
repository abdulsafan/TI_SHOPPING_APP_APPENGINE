package com.example.remindme;

import java.io.IOException;
import java.text.Format;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.example.remindme.storedataendpoint.Storedataendpoint;
import com.example.remindme.storedataendpoint.model.CollectionResponseStoreData;
import com.example.remindme.storedataendpoint.model.StoreData;
import com.example.remindme.storeeventendpoint.Storeeventendpoint;
import com.example.remindme.storeeventendpoint.model.CollectionResponseStoreEvent;
import com.example.remindme.storeeventendpoint.model.StoreEvent;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.jackson2.JacksonFactory;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class Home extends Activity {
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
		          ArrayList <String> rgroups=new ArrayList<String>();
		          String[] emails=new String[groupList.size()];
		          StoreData init;
		          String userEmail= UserEmailFetcher(Home.this);
		          for(int i=0; i<groupList.size(); i++){
		        	init=groupList.get(i);
		        	if(init.getGroupEmail().contains(userEmail)){
		        		rgroups.add(init.getGroup());
		        	}
		          }
		        String[] groups=null;
		        if(rgroups.size()<1){
		        	groups=new String[1];
		        	groups[0]= "No Groups Created";
		        }
		        else{
		        	groups=new String[rgroups.size()];
		            groups=rgroups.toArray(groups);
		        }
		        
		        List<StoreEvent> eventList= eventLists.getItems();

		        StoreEvent initial;
		        String[] Title=new String[eventList.size()];
		        String[] Description=new String[eventList.size()];
		        String[] Location=new String[eventList.size()];
		        String[] Group=new String[eventList.size()];
		        String[] Date=new String[eventList.size()];
		        String[] EventId=new String[eventList.size()];

		        ArrayList<String> Events = new ArrayList<String>();
		        ArrayList<String> EventIds = new ArrayList<String>();

		        for(int a=0; a<eventList.size(); a++){
		      	initial=eventList.get(a);
		        	Title[a]=initial.getEventName();
		        	Description[a]=initial.getDescription();
		        	Location[a]=initial.getLocation();
		        	Group[a]=initial.getGroup();
		        	Date[a]=initial.getEventTime();
		        	EventId[a]=initial.getEventID();
		        	for(int f=0; f<groups.length; f++){
		        		if(Group[a].equalsIgnoreCase(groups[f])){
		        			Events.add(Title[a]+ " " + Date[a]);
			        		EventIds.add(EventId[a]);
		        		}
		        	}	
		        }
		        String[] groupEvents=null;
		        String[] groupEventIDs=null;
		        System.out.println(Events.size());
		        if(Events.size()==0){
		      	 groupEvents = new String[1];
		      	 groupEvents[0]= "No Events";
		        }
		        else{
		            groupEvents = new String[Events.size()];
		            groupEvents = Events.toArray(groupEvents);
		            groupEventIDs = new String[Events.size()];
		            groupEventIDs = Events.toArray(groupEventIDs);
		        }
		        
				ListView listView2 = (ListView) findViewById(R.id.myevents);
		        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
		                    android.R.layout.simple_list_item_1, groupEvents); 
		        listView2.setAdapter(adapter1);
		     // listening to single list item on click
		        listView2.setOnItemClickListener(new OnItemClickListener() {
		          public void onItemClick(AdapterView<?> parent, View view,
		              int position, long id) {
		               
		              // selected item 
		              String event = ((TextView) view).getText().toString();
		               
		              // Launching new Activity on selecting single List Item
		              Intent i = new Intent(getApplicationContext(), Event.class);
		              // sending data to new activity
		              i.putExtra("event", event);
		              startActivity(i);

		          }
		        });
	}
	@Override
    public void onResume() {
        super.onResume();   
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
          ArrayList <String> rgroups=new ArrayList<String>();
          String[] emails=new String[groupList.size()];
          StoreData init;
          String userEmail= UserEmailFetcher(Home.this);
          for(int i=0; i<groupList.size(); i++){
        	init=groupList.get(i);
        	if(init.getGroupEmail().contains(userEmail)){
        		rgroups.add(init.getGroup());
        	}
          }
        String[] groups=null;
        if(rgroups.size()<1){
        	groups=new String[1];
        	groups[0]= "No Groups Created";
        }
        else{
        	groups=new String[rgroups.size()];
            groups=rgroups.toArray(groups);
        }
        
        List<StoreEvent> eventList= eventLists.getItems();

        StoreEvent initial;
        String[] Title=new String[eventList.size()];
        String[] Description=new String[eventList.size()];
        String[] Location=new String[eventList.size()];
        String[] Group=new String[eventList.size()];
        String[] Date=new String[eventList.size()];
        String[] EventId=new String[eventList.size()];

        ArrayList<String> Events = new ArrayList<String>();
        ArrayList<String> EventIds = new ArrayList<String>();

        for(int a=0; a<eventList.size(); a++){
      	initial=eventList.get(a);
        	Title[a]=initial.getEventName();
        	Description[a]=initial.getDescription();
        	Location[a]=initial.getLocation();
        	Group[a]=initial.getGroup();
        	Date[a]=initial.getEventTime();
        	EventId[a]=initial.getEventID();
        	for(int f=0; f<groups.length; f++){
        		if(Group[a].equalsIgnoreCase(groups[f])){
        			Events.add(Title[a]+ " " + Date[a]);
	        		EventIds.add(EventId[a]);
        		}
        	}	
        }
        String[] groupEvents=null;
        String[] groupEventIDs=null;
        System.out.println(Events.size());
        if(Events.size()==0){
      	 groupEvents = new String[1];
      	 groupEvents[0]= "No Events";
        }
        else{
            groupEvents = new String[Events.size()];
            groupEvents = Events.toArray(groupEvents);
            groupEventIDs = new String[Events.size()];
            groupEventIDs = Events.toArray(groupEventIDs);
        }
        
		ListView listView2 = (ListView) findViewById(R.id.myevents);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, groupEvents); 
        listView2.setAdapter(adapter1);
     // listening to single list item on click
        listView2.setOnItemClickListener(new OnItemClickListener() {
          public void onItemClick(AdapterView<?> parent, View view,
              int position, long id) {
               
              // selected item 
              String event = ((TextView) view).getText().toString();
               
              // Launching new Activity on selecting single List Item
              Intent i = new Intent(getApplicationContext(), Event.class);
              // sending data to new activity
              i.putExtra("event", event);
              startActivity(i);

          }
        });
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//MenuInflater inflater = getMenuInflater();
		menu.add(1,1,1,"").setTitle(R.string.addevent);
		menu.add(1,2,2,"").setTitle(R.string.creategroup);
		menu.add(1,3,3,"").setTitle(R.string.viewgroups);
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
		Intent i;
		switch (id){
		case 1:
			scheduleAlarm(this.findViewById(android.R.id.content));
			return true;
		case 2:
			i=new Intent(this, CreateGroup.class);
			startActivity(i);
			return true;
		case 3:
			i=new Intent(this, ViewGroups.class);
			startActivity(i);
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
		Intent i=new Intent(this, AddEvent.class);
		i.putExtra("GroupName", "NONE");
		startActivityForResult(i,0);
		Long time = new GregorianCalendar().getTimeInMillis() + 5000;
		Intent alarmIntent = new Intent(this, SetAlarm.class);
		AlarmManager alarmMan = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		alarmMan.set(AlarmManager.RTC_WAKEUP, time, PendingIntent.getBroadcast(
				this, 1, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT));
		
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
	public String UserEmailFetcher(Context context){
	    AccountManager accountManager = AccountManager.get(context); 
	    Account[] accounts = accountManager.getAccountsByType("com.google");
	    Account account;
	    if (accounts.length > 0) {
	      account = accounts[0];      
	    } else {
	      account = null;
	    }
	    if (account == null) {
  	      return null;
  	    } else {
  	      return account.name;
  	    }	  
    }
	
}
