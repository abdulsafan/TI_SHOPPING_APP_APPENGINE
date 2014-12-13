package com.example.remindme;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.example.remindme.storedataendpoint.Storedataendpoint;
import com.example.remindme.storedataendpoint.model.CollectionResponseStoreData;
import com.example.remindme.storedataendpoint.model.StoreData;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.jackson2.JacksonFactory;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class participants extends Activity{
	String GroupName="";
	StoreData rand=null;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.addparticipants);
        Intent i = getIntent();
        // getting attached intent data
        GroupName = i.getStringExtra("GroupName");
	}
	public void onClick(View view) {
		EditText emails = (EditText) findViewById(R.id.groupemails);
  		String es= emails.getText().toString();
		CollectionResponseStoreData groupLists = null; 
		System.out.println(es);
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
  
    		StoreData init=null;
              String email="";
              for(int a=0; a<groupList.size(); a++){
            	init=groupList.get(a);
            	groups[a]=init.getGroup();
            	System.out.println(groups[a]);
            	System.out.println(GroupName);
            	if(GroupName.equalsIgnoreCase(groups[a])){
            		email=init.getGroupEmail();	
            		rand=init;
            	}
              }
            
      		email=email+";"+es;
      		rand.setGroupEmail(email);
      		com.example.remindme.storedataendpoint.model.Key ID=rand.getKey();
			new CreateGroupTask().execute(rand);
			new RemoveGroupTask().execute(ID);
			this.finish();
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
}