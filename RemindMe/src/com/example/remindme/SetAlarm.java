package com.example.remindme;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class SetAlarm extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		boolean alarmFlag = false;
		
		//TO alarmFlag = (create alarm with Xun's class.)
		if(alarmFlag){
			Toast.makeText(context, "Alarm Set", Toast.LENGTH_SHORT).show();
		} else{
			Toast.makeText(context, "Alarm Not Set", Toast.LENGTH_SHORT).show();
		}
	}
}
