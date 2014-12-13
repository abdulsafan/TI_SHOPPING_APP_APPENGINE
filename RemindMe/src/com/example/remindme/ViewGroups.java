package com.example.remindme;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.example.remindme.storedataendpoint.Storedataendpoint;
import com.example.remindme.storedataendpoint.model.CollectionResponseStoreData;
import com.example.remindme.storedataendpoint.model.StoreData;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.jackson2.JacksonFactory;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
 
public class ViewGroups extends ListActivity {
    CollectionResponseStoreData groupLists = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
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
          String[] emails=new String[groupList.size()];
          StoreData init;
          for(int i=0; i<groupList.size(); i++){
        	init=groupList.get(i);
        	groups[i]=init.getGroup();
        	emails[i]=init.getGroupEmail();
        }

        // Binding resources Array to ListAdapter
        this.setListAdapter(new ArrayAdapter<String>(this, R.layout.view_group, R.id.label, groups));
        ListView lv = getListView();
 
        // listening to single list item on click
        lv.setOnItemClickListener(new OnItemClickListener() {
          public void onItemClick(AdapterView<?> parent, View view,
              int position, long id) {
               
              // selected item 
              String name = ((TextView) view).getText().toString();
               
              // Launching new Activity on selecting single List Item
              Intent i = new Intent(getApplicationContext(), group.class);
              // sending data to new activity
              i.putExtra("name", name);
              startActivity(i);
             
          }
        });
    }
    @Override
    public void onResume() {
        super.onResume();        
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
          String userEmail= UserEmailFetcher(ViewGroups.this);
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
        // Binding resources Array to ListAdapter
        this.setListAdapter(new ArrayAdapter<String>(this, R.layout.view_group, R.id.label, groups));
        ListView lv = getListView();
 
        // listening to single list item on click
        lv.setOnItemClickListener(new OnItemClickListener() {
          public void onItemClick(AdapterView<?> parent, View view,
              int position, long id) {
               
              // selected item 
              String name = ((TextView) view).getText().toString();
               
              // Launching new Activity on selecting single List Item
              Intent i = new Intent(getApplicationContext(), group.class);
              // sending data to new activity
              i.putExtra("name", name);
              startActivity(i);
             
          }
        }); 
    }
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//MenuInflater inflater = getMenuInflater();
		menu.add(1,1,1,"").setTitle(R.string.creategroup);
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
			i=new Intent(this, CreateGroup.class);
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