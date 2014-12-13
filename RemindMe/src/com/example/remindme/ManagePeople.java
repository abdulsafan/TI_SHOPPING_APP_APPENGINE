package com.example.remindme;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.example.remindme.storedataendpoint.Storedataendpoint;
import com.example.remindme.storedataendpoint.model.CollectionResponseStoreData;
import com.example.remindme.storedataendpoint.model.StoreData;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.jackson2.JacksonFactory;

import android.app.ListActivity;
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
 
public class ManagePeople extends ListActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
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