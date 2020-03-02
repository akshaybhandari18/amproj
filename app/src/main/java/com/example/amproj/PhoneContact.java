package com.example.amproj;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class PhoneContact extends ListActivity {

	private static int cntind = 1000;
	private static int[] sltcnt = new int[1000];
	public String[] s = new String[cntind];
	public String[] sn = new String[cntind];
	public static String[] snm;
	public static String[] sno;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

//		ListView lvsb=this.getListView();
	//	lvsb.setScrollBarStyle(0);

		for (int i = 0; i < cntind; i++) {
			s[i] = new String();
			sn[i] = new String();
		}

		Cursor cursor = getContentResolver().query(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null,
				null, null);
		int ino = 0;

		while (cursor.moveToNext()) {

			s[ino] = new String(
					cursor.getString(cursor
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));// name
																									// of
																									// contact

			sn[ino] = new String(
					cursor.getString(cursor
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));// mobile
																								// no.
			ino++;
		}

		snm = new String[ino];
		sno = new String[ino];

		String[] cntct = new String[ino];
		for (int i = 0; i < ino; i++) {
			// snm[i] = s[i];
			// sno[i] = sn[i];
			cntct[i] = s[i] + "\n" + sn[i];
		}

		ArrayAdapter<String> arr = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_multiple_choice, cntct);
		setListAdapter(arr);
		this.getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		this.getListView().setBackgroundResource(R.color.SkyBlue);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.phoneoptionmenu, menu);
		return super.onCreateOptionsMenu(menu);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
	
		int cntlen = 0;
		switch (item.getItemId()) {

		case R.id.submit:
			SparseBooleanArray selectedPos = getListView()
					.getCheckedItemPositions();
			ListAdapter lAdapter = getListAdapter();
			for (int i = 0; i < lAdapter.getCount(); i++) {
				if (selectedPos.get(i)) {
					sltcnt[i] = Integer.parseInt(String.valueOf(lAdapter
							.getItemId(i)));
					sno[i] = sn[sltcnt[i]].toString();
					cntlen++;
				}
			}

			Intent inttbl = getIntent();
			String stblnam = inttbl.getStringExtra("tablesname");
			String stblid=inttbl.getStringExtra("update");

			DataBaseConnectivityClass dbc = new DataBaseConnectivityClass(
					getBaseContext());
			dbc.sqlInsertContacts(sno, stblnam, stblid);
			dbc.close();
			PhoneContact.this.finish();
			return true;

		case R.id.cancel:
			
			Toast.makeText(this,
					item.getTitle() + " THERE IS NO CONTACT SELECTED",
					Toast.LENGTH_LONG).show();
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/*
	 * @Override public void onCreateContextMenu(ContextMenu menu, View v,
	 * ContextMenuInfo menuInfo) { // TODO Auto-generated method stub
	 * super.onCreateContextMenu(menu, v, menuInfo); MenuInflater inflater =
	 * getMenuInflater(); inflater.inflate(R.menu.contextmenu, menu); }
	 */

	/*
	 * @Override public boolean onContextItemSelected(MenuItem item) { // TODO
	 * // Auto-generated method stub
	 * 
	 * AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
	 * .getMenuInfo(); switch (item.getItemId()) { case R.id.item1:
	 * Toast.makeText(this, item.getTitle(), Toast.LENGTH_LONG).show(); return
	 * true; case R.id.item2: Toast.makeText(this, item.getTitle(),
	 * Toast.LENGTH_LONG).show(); return true;
	 * 
	 * default: return super.onContextItemSelected(item); } return true; }
	 */
}
