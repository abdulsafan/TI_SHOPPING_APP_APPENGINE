package com.example.ti_controller;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.example.ti_controller.deviceinfoendpoint.Deviceinfoendpoint;
import com.example.ti_controller.deviceinfoendpoint.model.CollectionResponseDeviceInfo;
import com.example.ti_controller.deviceinfoendpoint.model.DeviceInfo;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.jackson2.JacksonFactory;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	 private static final int REQUEST_ENABLE_BT = 1;

	ListView listDevicesFound;
	Button btnScanDevice;
	TextView stateBluetooth;
	BluetoothAdapter bluetoothAdapter;
	ArrayAdapter<String> btArrayAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		 btnScanDevice = (Button)findViewById(R.id.scandevice);
	        stateBluetooth = (TextView)findViewById(R.id.bluetoothstate);
	        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	        listDevicesFound = (ListView)findViewById(R.id.devicesfound);
	        btArrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1);
	        listDevicesFound.setAdapter(btArrayAdapter);
	        
	        CheckBlueToothState();
	        
	        btnScanDevice.setOnClickListener(btnScanDeviceOnClickListener);

	        registerReceiver(ActionFoundReceiver, 
	          new IntentFilter(BluetoothDevice.ACTION_FOUND));
		Button button1 = (Button) findViewById(R.id.find);
	      button1.setOnClickListener(new Button.OnClickListener() {
	    	  	@Override
				public void onClick(View v) {
					getResults(btArrayAdapter);
					// TODO Auto-generated method stub
				}
			});
	}
	
	   @Override
	   protected void onDestroy() {
	    // TODO Auto-generated method stub
	    super.onDestroy();
	    unregisterReceiver(ActionFoundReceiver);
	   }
	   private Button.OnClickListener btnScanDeviceOnClickListener = new Button.OnClickListener(){
		  @Override
		  public void onClick(View arg0) {
		   // TODO Auto-generated method stub
		   btArrayAdapter.clear();
		   bluetoothAdapter.startDiscovery();
	     }
	   };
	   private final BroadcastReceiver ActionFoundReceiver = new BroadcastReceiver(){

		   @Override
		   public void onReceive(Context context, Intent intent) {
		    // TODO Auto-generated method stub
		    String action = intent.getAction();
		    if(BluetoothDevice.ACTION_FOUND.equals(action)) {
		              BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
		              btArrayAdapter.add(device.getAddress());
		              btArrayAdapter.notifyDataSetChanged();
		          }
		   }};
		   @Override
		   protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		    // TODO Auto-generated method stub
		    if(requestCode == REQUEST_ENABLE_BT){
		     CheckBlueToothState();
		    }
		   }
		   
		   private void CheckBlueToothState(){
			     if (bluetoothAdapter == null){
			         stateBluetooth.setText("Bluetooth NOT support");
			        }else{
			         if (bluetoothAdapter.isEnabled()){
			          if(bluetoothAdapter.isDiscovering()){
			           stateBluetooth.setText("Bluetooth is currently in device discovery process.");
			          }else{
			           stateBluetooth.setText("Bluetooth is Enabled.");
			           btnScanDevice.setEnabled(true);
			          }
			         }else{
			          stateBluetooth.setText("Bluetooth is NOT Enabled!");
			          Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			             startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
			         }
			        }
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
	   private void getResults(ArrayAdapter<String> devices) {
		   
		   String id = Secure.getString(getBaseContext().getContentResolver(),Secure.ANDROID_ID); 
		   TextView myAwesomeTextView = (TextView)findViewById(R.id.message);
		   
		   DeviceInfo result=null;
		   /*
		   try {
				result = new GetSingleMessageTask().execute(id).get();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			*/
		   
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
		    	 for(int a=0; a<devices.getCount(); a++){
		    		 if(User_ID[i].equals(devices.getItem(a))){
			    	 		Blah+=Messages[i];
			    	 		break;
			    	 	} 
		    	 }	   	
		     }
		   
		   	 //String ShoppingList=result.getDeviceInformation();
    		 //myAwesomeTextView.setText(result.getDeviceInformation());
		     String ShoppingList=Blah;
    		 myAwesomeTextView.setText(Blah);
		     playAd(ShoppingList);
		    
	   }
	   public void playAd(String Message){
		   //Add Algorithm based on the message on the device
		   Field[] fields=R.raw.class.getFields();
		   String []filename=new String[fields.length];
		   for(int count=0; count < fields.length; count++){
		        // Use that if you just need the file name
		         filename[count] = fields[count].getName();
		   }
		   
			   if(filename[0].contains(Message)&& !Message.equals("")){
				   Context context = this.getBaseContext();
				   MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.cereal);
				   mediaPlayer.start();
			   }
			   else if(filename[1].contains(Message)&& !Message.equals("")){
				   Context context = this.getBaseContext();
				   MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.febreeze);
				   mediaPlayer.start();
			   }
			   else if(filename[2].contains(Message)&& !Message.equals("")){
				   Context context = this.getBaseContext();
				   MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.fruit);
				   mediaPlayer.start();
			   }
			   else if(filename[3].contains(Message)&& !Message.equals("")){
				   Context context = this.getBaseContext();
				   MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.furniture);
				   mediaPlayer.start();
			   }
			   else if(filename[4].contains(Message)&& !Message.equals("")){
				   Context context = this.getBaseContext();
				   MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.milk);
				   mediaPlayer.start();
			   }
			   else{
				   Context context = this.getBaseContext();
				   MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.welcome);
				   mediaPlayer.start();
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
