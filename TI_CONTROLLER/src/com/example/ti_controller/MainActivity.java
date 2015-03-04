package com.example.ti_controller;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.example.ti_controller.deviceinfoendpoint.Deviceinfoendpoint;
import com.example.ti_controller.deviceinfoendpoint.model.CollectionResponseDeviceInfo;
import com.example.ti_controller.deviceinfoendpoint.model.DeviceInfo;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.jackson2.JacksonFactory;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Button button1 = (Button) findViewById(R.id.find);
	      button1.setOnClickListener(new Button.OnClickListener() {
	    	  	@Override
				public void onClick(View v) {
					
					getResults();
					// TODO Auto-generated method stub
				}
			});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	   // Get the shopping history
	   private void getResults() {
		   
		   String id = Secure.getString(getBaseContext().getContentResolver(),Secure.ANDROID_ID); 
		   TextView myAwesomeTextView = (TextView)findViewById(R.id.message);
		   DeviceInfo result=null;
		   try {
				result = new GetSingleMessageTask().execute(id).get();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		   /*
		   CollectionResponseDeviceInfo messages = null;
			try {
				messages = new GetMessageTask().execute().get();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			List<DeviceInfo> messageList= messages.getItems();

			String[] Messages=new String[messageList.size()];
		     String[] User_ID=new String[messageList.size()];
		     DeviceInfo init;
		   
			   for(int i=0; i<messageList.size(); i++){
		    	 init=messageList.get(i);
		    	 Messages[i]=init.getDeviceInformation();
		    	 User_ID[i]=init.getDeviceRegistrationID();
		   	    }
		     String Blah="";
		     for(int i=0; i<messageList.size(); i++){		    	 
		    	 	if(User_ID[i].equals(id)){
		    	 		Blah+=Messages[i];
		    	 	} 	
		     }
		   */
		   	 String ShoppingList=result.getDeviceInformation();
    		 myAwesomeTextView.setText(result.getDeviceInformation());
    		 playAd(ShoppingList);
		    
	   }
	   public void playAd(String Message){
		   //Add Algorithm based on the message on the device
		   Field[] fields=R.raw.class.getFields();
		   String filename="";
		   for(int count=0; count < fields.length; count++){
		        // Use that if you just need the file name
		         filename += fields[count].getName();
		   }
		   
		   if(filename.contains(Message)){
			   Context context = this.getBaseContext();
			   MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.febreeze);
			   mediaPlayer.start();
		   }
		   else{
			   
			   System.out.println("NO MATCH");
		   }
   
		   
	   }
	   
	   private class GetMessageTask extends AsyncTask<Void, Void, CollectionResponseDeviceInfo> {
		    
	   	protected CollectionResponseDeviceInfo doInBackground(Void... params) {
	   		Deviceinfoendpoint.Builder endpointBuilder = new Deviceinfoendpoint.Builder(AndroidHttp.newCompatibleTransport(), new JacksonFactory(), null);
	   		endpointBuilder = CloudEndpointUtils.updateBuilder(endpointBuilder);
	   		CollectionResponseDeviceInfo result;
	   		Deviceinfoendpoint endpoint = endpointBuilder.build();
	   		try {
	   			result = endpoint.listDeviceInfo().execute();
	   		} catch (IOException e){
	   			e.printStackTrace();
	   			result=null;
	   		}
	   		return result;
	   		}
	   }
	   private class GetSingleMessageTask extends AsyncTask<String, Void, DeviceInfo> {
		    
		   	protected DeviceInfo doInBackground(String... params) {
		   		Deviceinfoendpoint.Builder endpointBuilder = new Deviceinfoendpoint.Builder(AndroidHttp.newCompatibleTransport(), new JacksonFactory(), null);
		   		endpointBuilder = CloudEndpointUtils.updateBuilder(endpointBuilder);
		   		DeviceInfo result;
		   		Deviceinfoendpoint endpoint = endpointBuilder.build();
		   		try {
		   			result = endpoint.getDeviceInfo(params[0]).execute();
		   		} catch (IOException e){
		   			e.printStackTrace();
		   			result=null;
		   		}
		   		return result;
		   		}
		   }
}
