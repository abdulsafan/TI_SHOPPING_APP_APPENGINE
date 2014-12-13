package com.example.remindme;
import com.example.remindme.storedataendpoint.Storedataendpoint;
import com.example.remindme.storedataendpoint.model.StoreData;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.jackson2.JacksonFactory;
import java.io.IOException;

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
import android.widget.EditText;
import android.widget.Toast;
 
public class CreateGroup extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.creategroup);
        
        
    }

    public void onClick(View view) {
    	String Groupname=null;
        String Participants=null;
        
		EditText name = (EditText) findViewById(R.id.groupname);
		Groupname = name.getText().toString();
		
		EditText emails = (EditText) findViewById(R.id.groupemails);
		String accountEmail=UserEmailFetcher(CreateGroup.this);
		Participants = emails.getText().toString()+";"+accountEmail;
		
		new CreateGroupTask().execute(Groupname,Participants);
		Context context=getApplicationContext();
		Toast.makeText(context, "Group Created", Toast.LENGTH_SHORT).show();
		this.finish();
    }
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//MenuInflater inflater = getMenuInflater();
		menu.add(1,1,1,"").setTitle(R.string.managepeople);
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
			i=new Intent(this, ManagePeople.class);
			startActivity(i);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
    
    private class CreateGroupTask extends AsyncTask<String, Void, Void> {

        /**
         * Calls appropriate CloudEndpoint to indicate that user checked into a place.
         *
         * @param params the place where the user is checking in.
         */
        @Override
        protected Void doInBackground(String... params) {
          StoreData storedata = new com.example.remindme.storedataendpoint.model.StoreData();

          // Set the ID of the store where the user is.
          storedata.setGroup(params[0]);
          storedata.setGroupEmail(params[1]);
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
