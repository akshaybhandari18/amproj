package com.example.amproj;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends Activity {

	LinearLayout layout;
	public Handler hndl;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		layout = new LinearLayout(this);
		layout.setBackgroundColor(0x0000FF00);
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.mainoptionmenu, menu);
		return true;
		/*
		 * MenuInflater inflater = getMenuInflater();
		 * inflater.inflate(R.menu.activity_main, menu); return
		 * super.onCreateOptionsMenu(menu);
		 */
	}

	public void addOnButtonClick(View v) {
		Intent i = new Intent(getBaseContext(), SelectEventActivity.class);
		startActivity(i);
	}

	public void viewOnButtonClick(View v) {
		Intent i = new Intent(getBaseContext(), ViewAllEvent.class);
		startActivity(i);
	}

	

	public void delteOnButtonClick(View v) {
		Intent i = new Intent(getBaseContext(), EmailSend.class);
		startActivity(i);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		Intent i;
		switch (item.getItemId()) {

		case R.id.Add:
			i = new Intent(getBaseContext(), SelectEventActivity.class);
			startActivity(i);
			Toast.makeText(this, item.getTitle(), Toast.LENGTH_LONG).show();
			return true;

		case R.id.View:			
			Toast.makeText(this, item.getTitle(), Toast.LENGTH_LONG).show();
			i = new Intent(getBaseContext(), ViewAllEvent.class);
			startActivity(i);
			return true;		
		}
		return super.onOptionsItemSelected(item);
	}
	

}
