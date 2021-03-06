package com.example.amproj;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

public class AddAnnyversaryEvent extends Activity {

	EditText et;
	Spinner rpt;

	private int mYear;
	private int mMonth;
	private int mDay;
	static final int DATE_DIALOG_ID = 0;

	private EditText ettime;
	// private TimePicker timePicker1;
	private int hour;
	private int minute;
	static final int TIME_DIALOG_ID = 999;

	Intent intnt;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_annyversary);

		DataBaseConnectivityClass dbrmv = new DataBaseConnectivityClass(this);
		String sid = dbrmv.getId("anniversary");
		dbrmv.removeUnnecessaryData(sid); // from contact_group table

		intnt = getIntent();
		String evtid = intnt.getStringExtra("Event_Id");
		if (!evtid.equals("create")) {
			setEventData(evtid);
			((Button) findViewById(id.a_absv)).setText("Update");
		}
		else
			((Spinner) findViewById(id.a_repeat)).setSelection(4);		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_annyversary_event, menu);
		return true;
	}

	// use for update Event
	private void setEventData(String evtid) {
		EditText ttl = (EditText) findViewById(id.a_ttl);
		EditText not = (EditText) findViewById(id.a_note);
		EditText sndto = (EditText) findViewById(id.a_send_to);
		EditText date = (EditText) findViewById(id.a_date);
		EditText time = (EditText) findViewById(id.a_time);
		EditText eml = (EditText) findViewById(id.a_email);
		Spinner repeat = (Spinner) findViewById(id.a_repeat);

		ArrayList<String> alevt = new ArrayList<String>();
		alevt = new DataBaseConnectivityClass(getBaseContext())
				.getEventData(evtid);

		try {
			ttl.setText(alevt.get(1).toString());
			not.setText(alevt.get(2).toString());
			sndto.setText(alevt.get(3).toString());
			date.setText(alevt.get(4).toString());
			time.setText(alevt.get(5).toString());
			eml.setText(alevt.get(6).toString());
			String[] srpt = { "Once", "Daily", "Weekly", "Monthly", "Yearly" };
			int i;
			for (i = 0; i < 5; i++)
				if (srpt[i].equals((alevt.get(7)).toString()))
					break;
			// Toast.makeText(getBaseContext(),
			// "repeat: "+alevt.get(7).toString(), Toast.LENGTH_SHORT).show();
			repeat.setSelection(i);
		} catch (Exception e) {
			Toast.makeText(getBaseContext(), "Exception setText.." + e,
					Toast.LENGTH_LONG).show();
		}
	}

	public void datpikOnA_ButtonClick(View v) {
		et = (EditText) findViewById(R.id.a_date);
		EditText datePick = (EditText) findViewById(R.id.a_date);
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
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,
					mDay);

		case TIME_DIALOG_ID:
			return new TimePickerDialog(this, timePickerListener, hour, minute,
					false);

		}
		return null;
	}// set dated

	public void timeSetOnA_Click(View v) {
		ettime = (EditText) findViewById(R.id.a_time);
		setCurrentTimeOnView();

		ettime.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showDialog(TIME_DIALOG_ID);
			}
		});
	}

	public void setCurrentTimeOnView() {
		// timePicker1 = (TimePicker) findViewById(R.id.timePicker1);

		final Calendar c = Calendar.getInstance();
		hour = c.get(Calendar.HOUR_OF_DAY);
		minute = c.get(Calendar.MINUTE);

		// set current time into textview
		ettime.setText(new StringBuilder().append(pad(hour)).append(":")
				.append(pad(minute)));

	}

	private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int selectedHour,
				int selectedMinute) {
			hour = selectedHour;
			minute = selectedMinute;

			// set current time into textview
			ettime.setText(new StringBuilder().append(pad(hour)).append(":")
					.append(pad(minute)));
		}
	};

	private static String pad(int c) {
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);
	}// time seted

	public void contactsOnClickButton(View v) {

		final String evttl = intnt.getStringExtra("Event_Id");
		AlertDialog.Builder builder = null;

		if (!evttl.equals("create")) {
			// dialogu box ok or cancel
			Toast.makeText(getBaseContext(), "Re-Selection contacts...",
					Toast.LENGTH_SHORT).show();

			builder = new AlertDialog.Builder(AddAnnyversaryEvent.this);
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
		inttbl.putExtra("tablesname", "anniversary");
		inttbl.putExtra("update", evid);
		startActivity(inttbl);
	}

	// E-MAIL VALIDATION...
	public boolean isEmailValid(String email) {
		boolean isValid = false;

		String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
		CharSequence inputStr = email;

		Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(inputStr);
		if (matcher.matches()) {
			isValid = true;
		}
		return isValid;
	}

	public void bbdsvOnClickListener(View v) {
		EditText etml = (EditText) findViewById(R.id.a_email);
		String emailval = (etml.getText()).toString();
/*		if (!isEmailValid(emailval)) {
			Toast.makeText(getBaseContext(), "FILL VALID EMAIL",
					Toast.LENGTH_SHORT).show();
			return;
		}
*/
		EditText etmbno = (EditText) findViewById(R.id.a_send_to);
		// String srtwith = etmbno.getText().toString();
		if (etmbno.equals("") && (etmbno.getText().toString()).length() != 10) {
			Toast.makeText(getBaseContext(), "INVALID MOBILE NUMBER",
					Toast.LENGTH_SHORT).show();
			return;
		}

		if (setSheduleSms())
			finish();
		else
			return;

		rpt = (Spinner) findViewById(R.id.a_repeat);
		String repeat = "Yearly";
		repeat = rpt.getSelectedItem().toString();
		DataBaseConnectivityClass dbc = new DataBaseConnectivityClass(this);

		String evntid = intnt.getStringExtra("Event_Id");
		if (evntid.equals("create")) {
			dbc.sqlInsertQuery("anniversary",
					((EditText) findViewById(id.a_ttl)).getText().toString(),
					((EditText) findViewById(id.a_note)).getText().toString(),
					((EditText) findViewById(id.a_send_to)).getText()
							.toString(), ((EditText) findViewById(id.a_date))
							.getText().toString(),
					((EditText) findViewById(id.a_time)).getText().toString(),
					((EditText) findViewById(id.a_email)).getText().toString(),
					repeat.toString());
		} else {
			dbc.updateBirthdEvent("anniversary", evntid,
					((EditText) findViewById(id.a_ttl)).getText().toString(),
					((EditText) findViewById(id.a_note)).getText().toString(),
					((EditText) findViewById(id.a_send_to)).getText()
							.toString(), ((EditText) findViewById(id.a_date))
							.getText().toString(),
					((EditText) findViewById(id.a_time)).getText().toString(),
					((EditText) findViewById(id.a_email)).getText().toString(),
					repeat.toString());
		}
		/*
		 * if (setSheduleSms()) finish(); else return;
		 */
	}

	public boolean setSheduleSms() {

		Intent myIntent = new Intent(AddAnnyversaryEvent.this,
				WakeupShedule.class);

		Bundle bundle = new Bundle();
		bundle.putCharSequence("eventType", "Anniversary");
		bundle.putCharSequence("evtTitle", ((EditText) findViewById(id.a_ttl))
				.getText().toString());
		bundle.putCharSequence("emailAddtess",
				((EditText) findViewById(id.a_email)).getText().toString());
		myIntent.putExtras(bundle);

		PendingIntent pendingIntent = PendingIntent.getService(
				AddAnnyversaryEvent.this, 0, myIntent, 0);
		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		String[] d = new String[] { "", "", "" };
		String[] t = new String[] { "", "" };
		String dt = ((EditText) findViewById(id.a_date)).getText().toString();
		String tm = ((EditText) findViewById(id.a_time)).getText().toString();
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
