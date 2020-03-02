package com.example.amproj;

import java.util.ArrayList;
import java.util.Calendar;
import com.example.amproj.R.id;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

public class AddCustomEvent extends Activity {

	EditText et;
	Spinner rpt;

	private int mYear;
	private int mMonth;
	private int mDay;
	static final int DATE_DIALOG_ID = 0;

	private EditText tvDisplayTime;
	private int hour;
	private int minute;
	static final int TIME_DIALOG_ID = 999;

	Intent intnt;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_custom);

		DataBaseConnectivityClass dbrmv = new DataBaseConnectivityClass(this);
		String sid = dbrmv.getId("custom");
		dbrmv.removeUnnecessaryData(sid); // from contact_group table

		((Spinner) findViewById(id.c_repeat)).setSelection(0);

		intnt = getIntent();
		String evtid = intnt.getStringExtra("Event_Id");
		if (!evtid.equals("create")) {
			setEventData(evtid);
			((Button) findViewById(id.c_bsv)).setText("Update");
		}
		else
			((Spinner) findViewById(id.c_repeat)).setSelection(0);		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.add_custom_event, menu);
		return true;
	}

	// use for update Event
	private void setEventData(String evtid) {
		EditText ttl = (EditText) findViewById(id.c_ttl);
		EditText not = (EditText) findViewById(id.c_not);
		EditText sndto = (EditText) findViewById(id.c_sndto);
		EditText date = (EditText) findViewById(id.c_dat);
		EditText time = (EditText) findViewById(id.c_tim);
		// EditText eml = (EditText) findViewById(id.c_email);
		Spinner repeat = (Spinner) findViewById(id.c_repeat);

		ArrayList<String> alevt = new ArrayList<String>();
		alevt = new DataBaseConnectivityClass(getBaseContext())
				.getEventData(evtid);

		try {
			ttl.setText(alevt.get(1).toString());
			not.setText(alevt.get(2).toString());
			sndto.setText(alevt.get(3).toString());
			date.setText(alevt.get(4).toString());
			time.setText(alevt.get(5).toString());
			// eml.setText(alevt.get(6).toString());
			String[] srpt = { "Once", "Daily", "Weekly", "Monthly", "Yearly" };
			int i;
			for (i = 0; i < 5; i++)
				if (srpt[i].equals((alevt.get(6)).toString()))
					break;
			repeat.setSelection(i);
		} catch (Exception e) {
			Toast.makeText(getBaseContext(),
					"Exception Update Controll..\n" + e, Toast.LENGTH_LONG)
					.show();
		}
	}

	public void datpikOnButtonClick(View v) {
		et = (EditText) findViewById(R.id.c_dat);
		EditText datePick = (EditText) findViewById(R.id.c_dat);
		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
		updateDisplay();

		datePick.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				showDialog(DATE_DIALOG_ID);

			}
		});

	}

	private void updateDisplay() {
		et.setText(new StringBuilder().append(mDay).append("-")
				.append(mMonth + 1).append("-").append(mYear).append(" "));
	}

	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			updateDisplay();
		}
	};

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID: // set date picker as current time
			return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,
					mDay);

		case TIME_DIALOG_ID:
			// set time picker as current time
			return new TimePickerDialog(this, timePickerListener, hour, minute,
					false);
		}

		return null;
	}// date complete

	// set time

	public void timeSetOnClick(View v) {
		tvDisplayTime = (EditText) findViewById(R.id.c_tim);
		setCurrentTimeOnView();

		tvDisplayTime.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showDialog(TIME_DIALOG_ID);
			}
		});
	}

	public void setCurrentTimeOnView() {
		// (TimePicker) findViewById(R.id.timePicker1);

		final Calendar c = Calendar.getInstance();
		hour = c.get(Calendar.HOUR_OF_DAY);
		minute = c.get(Calendar.MINUTE);

		// set current time into textview
		tvDisplayTime.setText(new StringBuilder().append(pad(hour)).append(":")
				.append(pad(minute)));

	}

	private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int selectedHour,
				int selectedMinute) {
			hour = selectedHour;
			minute = selectedMinute;

			// set current time into textview
			tvDisplayTime.setText(new StringBuilder().append(pad(hour))
					.append(":").append(pad(minute)));
		}
	};

	private static String pad(int c) {
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);
	}// time seted

	public void contOnClickButton(View v) {

		final String evttl = intnt.getStringExtra("Event_Id");
		AlertDialog.Builder builder = null;

		if (!evttl.equals("create")) {
			// dialogu box ok or cancel

			builder = new AlertDialog.Builder(AddCustomEvent.this);
			builder.setTitle("Select Options...");
			builder.setMessage("Yse: To re-select contacts...\nNo: To at it is...");

			builder.setPositiveButton("Yes",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							contactSelection(evttl);
						}
					});
			builder.setNegativeButton("No",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							Toast.makeText(getBaseContext(),
									"Not Modified Contacts", Toast.LENGTH_SHORT)
									.show();
						}
					});
			AlertDialog alr = builder.create();
			alr.show();
			return;

		} else {
			contactSelection("create");
		}

	}

	public void contactSelection(String evid) {
		Intent inttbl = new Intent(getBaseContext(), PhoneContact.class);
		inttbl.putExtra("tablesname", "custom");
		inttbl.putExtra("update", evid);
		startActivity(inttbl);
	}

	public void btnsvOnClickListener(View v) {
		EditText etmbno = (EditText) findViewById(R.id.c_sndto);
		// String srtwith = etmbno.getText().toString();
		if (etmbno.equals("") && (etmbno.getText().toString()).length() != 10) {
			Toast.makeText(getBaseContext(), "FILLED INVALID MOBILE NUMBER",
					Toast.LENGTH_SHORT).show();
			return;
		}

		rpt = (Spinner) findViewById(R.id.c_repeat);
		String spnrpt = "Yearly";
		spnrpt = rpt.getSelectedItem().toString();

		DataBaseConnectivityClass dbc = new DataBaseConnectivityClass(this);

		String evntid = intnt.getStringExtra("Event_Id");
		if (evntid.equals("create")) {
			dbc.sqlInsertQuery("custom", ((EditText) findViewById(id.c_ttl))
					.getText().toString(), ((EditText) findViewById(id.c_not))
					.getText().toString(),
					((EditText) findViewById(id.c_sndto)).getText().toString(),
					((EditText) findViewById(id.c_dat)).getText().toString(),
					((EditText) findViewById(id.c_tim)).getText().toString(),
					"", spnrpt.toString());// rpt.toString());
		} else {
			dbc.updateBirthdEvent("custom", evntid,
					((EditText) findViewById(id.c_ttl)).getText().toString(),
					((EditText) findViewById(id.c_not)).getText().toString(),
					((EditText) findViewById(id.c_sndto)).getText().toString(),
					((EditText) findViewById(id.c_dat)).getText().toString(),
					((EditText) findViewById(id.c_tim)).getText().toString(),
					"", spnrpt.toString());
		}
		if (setSheduleSms())
			finish();
		else
			return;
	}

	public boolean setSheduleSms() {
		Intent myIntent = new Intent(AddCustomEvent.this, WakeupShedule.class);

		Bundle bundle = new Bundle();
		bundle.putCharSequence("eventType", "Custom");
		bundle.putCharSequence("evtTitle", ((EditText) findViewById(id.c_ttl))
				.getText().toString());
		bundle.putCharSequence("emailAddtess", "custom");
		myIntent.putExtras(bundle);

		PendingIntent pendingIntent = PendingIntent.getService(
				AddCustomEvent.this, 0, myIntent, 0);
		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		String[] d = new String[] { "", "", "" };
		String[] t = new String[] { "", "" };
		String dt = ((EditText) findViewById(id.c_dat)).getText().toString();
		String tm = ((EditText) findViewById(id.c_tim)).getText().toString();
		int j = 0;
		for (int i = 0; i < dt.length(); i++) {
			if (dt.charAt(i) != ('-') && dt.charAt(i) != (' '))
				d[j] += dt.charAt(i);
			if (dt.charAt(i) == ('-') || dt.charAt(i) == (' '))
				j++;
		}
		j = 0;
		for (int i = 0; i < tm.length(); i++) {
			if (tm.charAt(i) != (':'))
				t[j] += tm.charAt(i);
			if (tm.charAt(i) == (':'))
				j++;
		}

		int postpond = new ComputeTime(d, t).getTime();

		boolean tf = true;
		if (postpond < 0) {
			Toast.makeText(getBaseContext(), "Shedule time is incorrect...",
					Toast.LENGTH_LONG).show();
			tf = false;
		} else {
			calendar.add(Calendar.MINUTE, postpond);
			// calendar.add(Calendar.SECOND, 10);
			alarmManager.set(AlarmManager.RTC_WAKEUP,
					calendar.getTimeInMillis(), pendingIntent);
		}

		// to Cancel shedule
		// AlarmManager alarmManager =
		// (AlarmManager)getSystemService(ALARM_SERVICE);
		// alarmManager.cancel(pendingIntent);
		return tf;
	}
}
