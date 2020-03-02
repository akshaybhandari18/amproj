package com.example.amproj;

import java.util.ArrayList;
import android.os.Bundle;
import android.app.ListActivity;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ViewAllEvent extends ListActivity {

	ArrayList<String> alttl = new ArrayList<String>();
	ArrayList<String> alid = new ArrayList<String>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.view_all_event);

		alttl = new DataBaseConnectivityClass(getBaseContext()).getEvents();
		alid = new DataBaseConnectivityClass(getBaseContext()).getEventIds();

		ArrayAdapter<String> arr = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, alttl);
		setListAdapter(arr);
		ListView lv = this.getListView();
		// lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		// lv.setBackgroundColor(Color.BLACK);
		lv.setBackgroundResource(R.color.SkyBlue);
		registerForContextMenu(lv);
		lv.setScrollbarFadingEnabled(true);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) { // TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.contextmenu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();

		Object ottl = alttl.get((int) info.id);
		int pos = alttl.indexOf(ottl);

		switch (item.getItemId()) {
		case R.id.item1:
			Intent i=null;
			String sid=alid.get(pos);
			if(sid.startsWith("B"))
				i = new Intent(getApplicationContext(), AddBirthdayActivity.class);
			else if(sid.startsWith("A"))
				i = new Intent(getApplicationContext(), AddAnnyversaryEvent.class);
			else if(sid.startsWith("C"))
				i = new Intent(getApplicationContext(), AddCustomEvent.class);
			
			i.putExtra("Event_Title", ottl.toString());
			i.putExtra("Event_Id", alid.get(pos));
			startActivity(i);
			return true;

		case R.id.item2:
			Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();
			new DataBaseConnectivityClass(getBaseContext()).removeEvent(alid.get(pos),
					ottl.toString());
			finish();
			return true;

		default:
			return super.onContextItemSelected(item);
		}
	}
	/*
	 * @Override public boolean onCreateOptionsMenu(Menu menu) { MenuInflater
	 * inflater = getMenuInflater(); inflater.inflate(R.menu.optionmenu, menu);
	 * return super.onCreateOptionsMenu(menu); }
	 * 
	 * public boolean onOptionsItemSelected(MenuItem item) { // TODO
	 * Auto-generated method stub switch (item.getItemId()) { case R.id.item1:
	 * Toast.makeText(this, item.getTitle(), Toast.LENGTH_LONG).show(); return
	 * true; case R.id.item2: Toast.makeText(this, item.getTitle(),
	 * Toast.LENGTH_LONG).show(); return true;
	 * 
	 * } return super.onOptionsItemSelected(item); }
	 */
}
