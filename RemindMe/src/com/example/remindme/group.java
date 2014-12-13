package com.example.remindme;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.example.remindme.storedataendpoint.Storedataendpoint;
import com.example.remindme.storedataendpoint.model.CollectionResponseStoreData;
import com.example.remindme.storedataendpoint.model.StoreData;
import com.example.remindme.storeeventendpoint.Storeeventendpoint;
import com.example.remindme.storeeventendpoint.model.CollectionResponseStoreEvent;
import com.example.remindme.storeeventendpoint.model.Key;
import com.example.remindme.storeeventendpoint.model.StoreEvent;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.jackson2.JacksonFactory;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
 
public class group extends Activity{
	String GroupName="";
	StoreData rand=null;
	String []emails=null;
	Key GroupID=null;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.group);
        
        TextView txtProduct = (TextView) findViewById(R.id.product_label);
        
        Intent i = getIntent();
        // getting attached intent data
        GroupName = i.getStringExtra("name");
        // displaying selected product name
        txtProduct.setText(GroupName);
        CollectionResponseStoreData groupLists = null; 
        CollectionResponseStoreEvent eventLists  = null; 
    		try {
    			groupLists = new GetGroupsTask().execute().get();
    			eventLists = new GetEventTask().execute().get();
    		} catch (InterruptedException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} catch (ExecutionException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
            List<StoreData> groupList= groupLists.getItems();
            List<StoreEvent> eventList= eventLists.getItems();
              String[] groups=new String[groupList.size()];
              StoreData init;
              String email="";
              for(int a=0; a<groupList.size(); a++){
            	init=groupList.get(a);
            	groups[a]=init.getGroup();
            	if(GroupName.equalsIgnoreCase(groups[a])){
            		email=init.getGroupEmail();	
            		rand=init;
            	}
              }
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
              	if(GroupName.equalsIgnoreCase(Group[a])){
              		Events.add(Title[a]+ " " + Date[a]);
              		EventIds.add(EventId[a]);
              	}
              	
              }
              String[] groupEvents=null;
              String[] groupEventIDs=null;
              if(Events.size()==0){
            	 groupEvents = new String[1];
            	 groupEvents[0]= "No Group Events Created";
              }
              else{
                  groupEvents = new String[Events.size()];
                  groupEvents = Events.toArray(groupEvents);
                  groupEventIDs = new String[Events.size()];
                  groupEventIDs = Events.toArray(groupEventIDs);
              }
              
              if(email!=""){
            	 emails = email.split(";");  
              }
              else{
            	  emails=new String[1];
            	  emails[0]= "NO MEMBERS IN THIS GROUP";
              }
              
              ListView listView1 = (ListView) findViewById(R.id.plist);
                            
              ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                          android.R.layout.simple_list_item_1, emails);
              
              listView1.setAdapter(adapter);
              ListView listView2 = (ListView) findViewById(R.id.elist);
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
            		rand=init;
            	}
              }
              
              if(email!=""){
            	 emails = email.split(";");  
              }
              else{
            	  emails=new String[1];
            	  emails[0]= "NO MEMBERS IN THIS GROUP";
              }
              
              ListView listView1 = (ListView) findViewById(R.id.plist);
                            
              ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                          android.R.layout.simple_list_item_1, emails);
              
              listView1.setAdapter(adapter);
        
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
        	if(GroupName.equalsIgnoreCase(Group[a])){
        		Events.add(Title[a]+ " " + Date[a]);
        		EventIds.add(EventId[a]);
        		
        	}
        	
        }
        String[] groupEvents=null;
        String[] groupEventIDs=null;
        if(Events.size()==0){
      	 groupEvents = new String[1];
      	 groupEvents[0]= "No Group Events Created";
        }
        else{
            groupEvents = new String[Events.size()];
            groupEvents = Events.toArray(groupEvents);
            groupEventIDs = new String[Events.size()];
            groupEventIDs = Events.toArray(groupEventIDs);
        }
        
        ListView listView2 = (ListView) findViewById(R.id.elist);
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
		menu.add(1,2,2,"").setTitle("Leave Group");
		menu.add(1,3,3,"").setTitle("Add Participants");
		return super.onCreateOptionsMenu(menu);
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
			i=new Intent(this, AddEvent.class);
			i.putExtra("GroupName", GroupName);
			startActivity(i);
			return true;
		case 2:
			String em=" ";
			String User= UserEmailFetcher(group.this);
			for(int c=0; c<emails.length; c++){
				System.out.println(emails[c]);
				if(emails[c].equalsIgnoreCase(User)){
					continue;
				}
				else{
					em+=emails[c]+";";
					System.out.println(em);
				}
			}
			if(em.equalsIgnoreCase(null)){
				em=" ";
			}
			else {
				em=em.substring(0, em.length()-1);
			}
			rand.setGroupEmail(em);
			com.example.remindme.storedataendpoint.model.Key ID=rand.getKey();
			new CreateGroupTask().execute(rand);
			new RemoveGroupTask().execute(ID);
			this.finish();
			return true;
		case 3:
			i=new Intent(this, participants.class);
			i.putExtra("GroupName", GroupName);
			startActivity(i);
			return true;
		default:
			return super.onOptionsItemSelected(item);
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
	private class UpdateStoreTask extends AsyncTask<StoreData, Void, Void> {
        
        @Override
        protected Void doInBackground(StoreData... params) {
          
          Storedataendpoint.Builder builder = new Storedataendpoint.Builder(
              AndroidHttp.newCompatibleTransport(), new JacksonFactory(), null);

          builder = CloudEndpointUtils.updateBuilder(builder);

          Storedataendpoint endpoint = builder.build();
          
          try {
            endpoint.updateStoreData(params[0]).execute();
          } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }

          return null;
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
	private class RemoveGroupTask extends AsyncTask<com.example.remindme.storedataendpoint.model.Key, Void, Void> {
        
        @Override
        protected Void doInBackground(com.example.remindme.storedataendpoint.model.Key... params) {
          
          Storedataendpoint.Builder builder = new Storedataendpoint.Builder(
              AndroidHttp.newCompatibleTransport(), new JacksonFactory(), null);

          builder = CloudEndpointUtils.updateBuilder(builder);

          Storedataendpoint endpoint = builder.build();
          Long EventID=params[0].getId();

          try {
            endpoint.removeStoreData(EventID).execute();
          } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }

          return null;
        }
    }
	
	private class CreateGroupTask extends AsyncTask<StoreData, Void, Void> {

        /**
         * Calls appropriate CloudEndpoint to indicate that user checked into a place.
         *
         * @param params the place where the user is checking in.
         */
        @Override
        protected Void doInBackground(StoreData... params) {
          StoreData storedata = new com.example.remindme.storedataendpoint.model.StoreData();

          // Set the ID of the store where the user is.
          storedata.setGroup(params[0].getGroup());
          storedata.setGroupEmail(params[0].getGroupEmail());
          Storedataendpoint.Builder builder = new Storedataendpoint.Builder(
              AndroidHttp.newCompatibleTransport(), new JacksonFactory(), null);

          builder = CloudEndpointUtils.updateBuilder(builder);

          Storedataendpoint endpoint = builder.build();


          try {
            endpoint.insertStoreData(storedata).execute();
          } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }

          return null;
        }
    }
	
}