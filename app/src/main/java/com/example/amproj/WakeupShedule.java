package com.example.amproj;

import java.util.ArrayList;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.widget.Toast;

public class WakeupShedule extends Service {

	String sendToMail, evtType, evTitle, msg="Default";

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub

		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub

		// Toast.makeText(this, "MyAlarmService.onCreate()", Toast.LENGTH_LONG)
		// .show();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		// Toast.makeText(this, "MyAlarmService.onDestroy()", Toast.LENGTH_LONG)
		// .show();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);

		Bundle bundle = intent.getExtras();
		evtType = (String) bundle.getCharSequence("eventType");
		evTitle = (String) bundle.getCharSequence("evtTitle");
		sendToMail = (String) bundle.getCharSequence("emailAddtess");
		msg = (String) bundle.getCharSequence("smsText");

		DataBaseConnectivityClass dbcc = new DataBaseConnectivityClass();
		ArrayList<String> evIds = (ArrayList<String>) dbcc.getExecuteEventId();
		ArrayList<String> almn = new ArrayList<String>();

		SmsManager smsManager = SmsManager.getDefault();
//		Toast.makeText(this, "Id: "+evIds.get(0), Toast.LENGTH_LONG).show();
		String grpmsg = "";
		// for (int i = 0; i < evIds.size(); i++)
		try {
			if ((evIds.get(0)).startsWith("B")) {
				almn = dbcc.getMobileNo("birthday", evIds.get(0));
				grpmsg = "Today Birthday is:  ";
			} else if ((evIds.get(0)).startsWith("A")) {
				almn = dbcc.getMobileNo("anniversary", evIds.get(0));
				grpmsg = "Today Anniversary is:  ";
			} else if ((evIds.get(0)).startsWith("C"))
				almn = dbcc.getMobileNo("custom", evIds.get(0));
		} catch (Exception e) {
			Toast.makeText(this, "try 1 \n" + e, Toast.LENGTH_LONG).show();
		}
		try {
			for (int j = 0; j < almn.size(); j++) {
				if ((j == 0)) {
					try{
							smsManager.sendTextMessage(almn.get(j), null, ""+msg, null, null);
	//						smsManager.sendTextMessage("5556", null, ""+msg, null, null);
							
						Toast.makeText(this,
							"Send SMS Successfully to " + almn.get(j),
							Toast.LENGTH_SHORT).show();
					}catch(Exception e){
						Toast.makeText(getApplicationContext(), "1: "+e, Toast.LENGTH_LONG).show();
					}
				} else if ((j > 0) && (!evIds.get(0).startsWith("C"))) {
					try{
					smsManager.sendTextMessage(almn.get(j), null, grpmsg + " "
//							smsManager.sendTextMessage("5556", null, grpmsg + " "
							+ almn.get(0), null, null);
					Toast.makeText(this,
							"Send SMS Successfully to " + almn.get(j),
							Toast.LENGTH_SHORT).show();
					}catch(Exception e){
						Toast.makeText(getApplicationContext(), "2: "+e, Toast.LENGTH_LONG).show();
					}
				} else if(evIds.get(0).startsWith("C")){
					try{
					smsManager.sendTextMessage(almn.get(j), null, ""+msg, null, null);
					Toast.makeText(this,
							"Send SMS Successfully to " + almn.get(j),
							Toast.LENGTH_SHORT).show();
					}catch(Exception e){
						Toast.makeText(getApplicationContext(), "3: "+e, Toast.LENGTH_LONG).show();
					}
				}
			}
		} catch (Exception e) {
			Toast.makeText(this, "try 2 \n" + e, Toast.LENGTH_LONG).show();
		}
		Intent it = new Intent(this, EmailSend.class);
		it.putExtra("sendToMail", sendToMail);
		it.putExtra("evTitle", evTitle);
		it.putExtra("evtType", evtType);
		it.putExtra("colsId", String.valueOf(evIds.get(0)));
		it.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
		it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(it);
		/*
		 * SmsManager smsManager = SmsManager.getDefault();
		 * smsManager.sendTextMessage(smsNumberToSend, null, smsTextToSend,
		 * null, null); Toast.makeText(this,
		 * "Message Successfully send to all contacts", Toast.LENGTH_LONG)
		 * .show();
		 */
	}

	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "MyAlarmService.onUnbind()", Toast.LENGTH_LONG)
				.show();
		return super.onUnbind(intent);
	}

}
