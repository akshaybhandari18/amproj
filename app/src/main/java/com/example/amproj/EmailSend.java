package com.example.amproj;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.widget.Toast;

public class EmailSend extends Activity {

	String colId, eType, eTitle, sndTo;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.emailsend);
		
		Intent it = getIntent();
		colId = it.getStringExtra("colsId");
		sndTo = it.getStringExtra("sendToMail");
		eTitle = it.getStringExtra("evTitle");
		eType=it.getStringExtra("evtType");

		playSound();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.delete_events, menu);
		return true;
	}

	public void playSound() {
		AlertDialog.Builder builder = null;

		builder = new AlertDialog.Builder(EmailSend.this);
		builder.setTitle(""+eType);
		builder.setMessage("Event Title: \n"+eTitle);

/*		final Ringtone ringtone = RingtoneManager.getRingtone(
				getApplicationContext(),
				RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM));
		if (ringtone != null) {
			ringtone.play();
		}
*/
		builder.setPositiveButton("Stop",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
	//					ringtone.stop();
						modifyCompletedEvent();
						
						try {
							Intent email = new Intent(Intent.ACTION_SEND);
							email.putExtra(Intent.EXTRA_EMAIL, new String[] { sndTo });
							// email.putExtra(Intent.EXTRA_CC, new String[]{ to});
							// email.putExtra(Intent.EXTRA_BCC, new String[]{to});
							email.putExtra(Intent.EXTRA_SUBJECT, eType+" Wish...");
							email.putExtra(Intent.EXTRA_TEXT,
									"Many Many Happy Returns Of The Day...\n Wish u Happy "+eType+"...");

							// need this to prompts email client only
							email.setType("message/rfc822");
							email.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
							email.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							if(!sndTo.equals("custom")){
								startActivity(Intent.createChooser(email, "Choose an Email client :"));
							}
						} catch (Exception e) {
							Toast.makeText(getApplicationContext(), "EmailSending :" + e,
									Toast.LENGTH_LONG).show();
						}

						EmailSend.this.finish();
						
					}
				});

/*		builder.setNegativeButton("Close",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						modifyCompletedEvent();
						ringtone.stop();
						EmailSend.this.finish();
					}
				});
				
*/
		
		AlertDialog alr = builder.create();
		alr.show();
	}

	public void modifyCompletedEvent() {
		DataBaseConnectivityClass dbcc = new DataBaseConnectivityClass(getBaseContext());

		String rptmod = dbcc.getRepeatTime(colId);
		if (!rptmod.equals("")) {
			if (rptmod.equals("Once")) {
				dbcc.removeEvent(colId, "");
			}
			else if (rptmod.equals("Daily")) {
				dbcc.updateEventMode(colId, rptmod);
			}
			else if (rptmod.equals("Weekly")) {
				dbcc.updateEventMode(colId, rptmod);
			}
			else if (rptmod.equals("Monthly")) {
				dbcc.updateEventMode(colId, rptmod);
			}
			else if (rptmod.equals("Yearly")) {
				dbcc.updateEventMode(colId, rptmod);
			}
		}
	}
}
