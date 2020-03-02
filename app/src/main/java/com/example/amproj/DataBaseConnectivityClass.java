package com.example.amproj;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class DataBaseConnectivityClass extends SQLiteOpenHelper {

	static final String dbname = "EVENT_REMINDER";
	static final int version = 1;
	private static Context context;

	static String colId = "ID";
	static final String colsndto = "SEND_TO", colsttl = "TITLE",
			colnot = "NOTE", coledat = "E_DATE", coletim = "E_TIME",
			colrepit = "REPEAT", coleml = "E_MAIL";
	static final String clcntId = "ID", clcntmb = "MOBILE";

	static final String tblbrth = "birthday", tblcntgrp = "contact_group";

	static long mbno = 0L;

	public DataBaseConnectivityClass(Context context) {
		super(context, dbname, null, version);
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	public DataBaseConnectivityClass() {
		super(context, dbname, null, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase dbc) {
		// TODO Auto-generated method stub

		String CREATE_BIRTHDAY_TABLE = "CREATE TABLE " + tblbrth + " (" + colId
				+ " TEXT PRIMARY KEY," + colsttl + " TEXT," + colnot + " TEXT,"
				+ colsndto + " LONG," + coledat + " TEXT," + coletim + " TEXT,"
				+ coleml + " TEXT," + colrepit + " TEXT" + ")";
		dbc.execSQL(CREATE_BIRTHDAY_TABLE);

		String CREATE_ANNIVERSARY_TABLE = "CREATE TABLE anniversary"
				+ " (ID TEXT PRIMARY KEY, TITLE TEXT, NOTE TEXT, SEND_TO LONG, E_DATE TEXT, E_TIME TEXT, E_MAIL TEXT, REPEAT TEXT )";

		dbc.execSQL(CREATE_ANNIVERSARY_TABLE);

		String CREATE_CUSTOM_TABLE = "CREATE TABLE custom"
				+ " (ID TEXT PRIMARY KEY, TITLE TEXT, NOTE TEXT, SEND_TO LONG, E_DATE TEXT, E_TIME TEXT, REPEAT TEXT )";
		dbc.execSQL(CREATE_CUSTOM_TABLE);

		String CREATE_CONTACT_NO_TABLE = "CREATE TABLE " + tblcntgrp + " ("
				+ clcntId + " TEXT," + clcntmb + " LONG" + ")";
		dbc.execSQL(CREATE_CONTACT_NO_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase dbu, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		dbu.execSQL("DROP TABLE IF EXISTS " + tblbrth);
		dbu.execSQL("DROP TABLE IF EXISTS anniversary");
		dbu.execSQL("DROP TABLE IF EXISTS custom");
		dbu.execSQL("DROP TABLE IF EXISTS " + tblcntgrp);
		onCreate(dbu);
	}

	public void sqlInsertQuery(String tblnm, String ttl, String not,
			String snd, String dat, String tim, String eml, String rpt) {

		int flag = 0;
		SQLiteDatabase dbw = null;

		try {
			long dmo = Long.parseLong(snd.toString());
			mbno = Long.valueOf(dmo);

			String cntid = (getCountRow(tblnm)).toString();
			dbw = this.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put(colId, cntid);
			values.put(colsttl, ttl);
			values.put(colnot, not);
			values.put(colsndto, mbno);
			values.put(coledat, dat);
			values.put(coletim, tim);

			if (!tblnm.startsWith("c"))
				values.put(coleml, eml);

			values.put(colrepit, rpt);
			dbw.insert(tblnm, null, values);

			flag = 1;
		} catch (Exception e) {
			Toast.makeText(context, tblnm + "Insert Query Error : \n" + e, Toast.LENGTH_LONG)
					.show();
		}
		dbw.close();
		if (flag == 1)
			Toast.makeText(context,
					"SUCCESSFULLY INSERTED " + tblnm + " EVENT",
					Toast.LENGTH_SHORT).show();
		else
			Toast.makeText(context, tblnm + " EVENT NOT INSERTED...",
					Toast.LENGTH_LONG).show();
	}

	public String getCountRow(String table_name) {

		int counter = 0;
		SQLiteDatabase sq = null;
		try {
			sq = this.getReadableDatabase();
			Cursor c = sq.rawQuery("select * from " + table_name, null);
			counter = c.getCount();

		} catch (Exception e) {
			Toast.makeText(context, "getCountRow() Error: \n" + e,
					Toast.LENGTH_LONG).show();
		}
		sq.close();

		String returnvar = "X";
		if (table_name.startsWith("a"))
			returnvar = ("A" + (counter + 1)).toString();
		if (table_name.startsWith("b"))
			returnvar = ("B" + (counter + 1)).toString();
		if (table_name.startsWith("c"))
			returnvar = ("C" + (counter + 1)).toString();

		return (returnvar);
	}

	public void sqlInsertContacts(String[] cntct, String ptblnm, String pupd) {

		String cnt = "";
		if (!pupd.equals("create")) {
			removeCntGrp(pupd);
			cnt = pupd;
		} else
			cnt = getCountRow(ptblnm);

		mbno = 0L;
		int flag = 0;

		SQLiteDatabase db = null;
		try {
			db = this.getWritableDatabase();
			ContentValues values = new ContentValues();

			for (String ncnt : cntct) {

				if (ncnt != null) {

					String strnol = ncnt.replace("-", "");
					long dmo = Long.parseLong(strnol);
					mbno = Long.valueOf(dmo);

					values.put("ID", cnt);
					values.put("MOBILE", mbno);
					db.insert(tblcntgrp, null, values);

				}
			}
			flag = 1;
		} catch (Exception e) {
			Toast.makeText(context,
					"EXCEP. AT INSERTCONTACT: \n" + e + cntct[1],
					Toast.LENGTH_LONG).show();
		}
		db.close();

		if (flag == 1)
			Toast.makeText(context, " SUCCESSFULLY INSERTED CONTACTS ",
					Toast.LENGTH_SHORT).show();
		else
			Toast.makeText(context, " CONTACTS NOT INSERTED....",
					Toast.LENGTH_LONG).show();

	}

	public void removeUnnecessaryData(String colmsId) {

		int valu = Integer.parseInt(colmsId.substring(1));

		String dlt = "";
		if (colmsId.startsWith("B"))
			dlt = "B" + (valu + 1);
		else if (colmsId.startsWith("A"))
			dlt = "A" + (valu + 1);
		else if (colmsId.startsWith("C"))
			dlt = "C" + (valu + 1);

		SQLiteDatabase sql = this.getWritableDatabase();

		try {
			Cursor cur = sql.query(tblcntgrp, new String[] { clcntId }, null,
					null, null, null, null);
			while (cur.moveToNext()) {

				String s1 = cur.getString(0);
				int valu1 = Integer.parseInt(s1.substring(1));

				String dlt1 = "";
				if (s1.startsWith("B")) {
					dlt1 = "B" + valu1;
				} else if (s1.startsWith("A")) {
					dlt1 = "A" + valu1;
				} else if (s1.startsWith("C")) {
					dlt1 = "C" + valu1;
				}

				if (dlt.equals(dlt1)
						|| (Integer.parseInt(colmsId.substring(1))) < (Integer
								.parseInt(s1.substring(1)))) {
	
					sql.delete(tblcntgrp, clcntId + "=?",
							new String[] { dlt.toString() });
				}
			}

		} catch (Exception e) {
			Toast.makeText(context, "At RemoveUnecesaryData: \n" + e, Toast.LENGTH_LONG)
					.show();
		}
		sql.close();

	}// remove extra inserted mb no revoved from contacg_grouptable

	public String getId(String ptblsnm) {
		String s = "0";

		if (ptblsnm.startsWith("a"))
			s = "A0";
		else if (ptblsnm.startsWith("b"))
			s = "B0";
		else if (ptblsnm.startsWith("c"))
			s = "C0";

		SQLiteDatabase sql = this.getReadableDatabase();
		try {
			Cursor cur = sql.query(ptblsnm, new String[] { colId }, null, null,
					null, null, null);

			while (cur.moveToNext()) {
				s = cur.getString(0);
			}
		} catch (Exception e) {
			Toast.makeText(context, "Retun get id error: " + e,
					Toast.LENGTH_LONG).show();
		}
		sql.close();
		return s;
	}

	// to return total no of events...
	public int getCountRow() {
		int counter = 0;
		SQLiteDatabase sq = null;
		try {
			sq = this.getReadableDatabase();
			Cursor c = sq.rawQuery("select * from birthday", null);
			counter = c.getCount();

			c = sq.rawQuery("select * from anniversary", null);
			counter += c.getCount();

			c = sq.rawQuery("select * from custom", null);
			counter += c.getCount();

		} catch (Exception e) {
			Toast.makeText(context, "getCountRow Error: " + e,
					Toast.LENGTH_LONG).show();
		}
		sq.close();
		return counter;
	}

	public ArrayList<String> getEvents() {

		ArrayList<String> srtn = new ArrayList<String>();

		String[] tblnms = new String[] { "birthday", "anniversary", "custom" };
		SQLiteDatabase sqevt = null;

		try {
			for (int j = 0; j < 3; j++) {

				sqevt = this.getReadableDatabase();
				Cursor c = sqevt.query(tblnms[j], new String[] { colsttl },
						null, null, null, null, null);
				while (c.moveToNext()) {
					String stmp = c.getString(0);
					srtn.add(stmp);
				}
				c.close();
			}
			sqevt.close();
		} catch (Exception e) {
			Toast.makeText(context, "Exception at getEvents..." + e,
					Toast.LENGTH_LONG).show();
		}
		return srtn;
	}

	public ArrayList<String> getEventIds() {
		ArrayList<String> srtn = new ArrayList<String>();

		String[] tblnms = new String[] { "birthday", "anniversary", "custom" };
		SQLiteDatabase sqevt = null;

		try {
			sqevt = this.getReadableDatabase();
			Cursor c = sqevt.query(tblnms[0], new String[] { colId }, null,
					null, null, null, null);
			while (c.moveToNext()) {
				String stmp = c.getString(0);
				srtn.add(stmp);
			}
			c.close();

			sqevt = this.getReadableDatabase();
			c = sqevt.query(tblnms[1], new String[] { colId }, null, null,
					null, null, null);
			while (c.moveToNext()) {
				String stmp = c.getString(0);
				srtn.add(stmp);
			}
			c.close();

			sqevt = this.getReadableDatabase();
			c = sqevt.query(tblnms[2], new String[] { colId }, null, null,
					null, null, null);
			while (c.moveToNext()) {
				String stmp = c.getString(0);
				srtn.add(stmp);
			}
			c.close();
			sqevt.close();
		} catch (Exception e) {
			Toast.makeText(context, "Exception at getEventsId..." + e,
					Toast.LENGTH_LONG).show();
		}
		return srtn;
	}

	public ArrayList<String> getEventData(String evtid) {
		ArrayList<String> arlst = new ArrayList<String>();
		String etblnm = "";

		if (evtid.startsWith("B"))
			etblnm = "birthday";
		else if (evtid.startsWith("A"))
			etblnm = "anniversary";
		else if (evtid.startsWith("C"))
			etblnm = "custom";

		SQLiteDatabase esql = null;
		Cursor c = null;
		try {
			esql = this.getReadableDatabase();
			if (!evtid.startsWith("C")) {
				// Cursor cur = sql.query(emptable, new
				// String[]{colname},colId+"=?",new
				// String[]{String.valueOf(id)}, null, null, null);

				c = esql.query(etblnm, new String[] { colId, colsttl, colnot,
						colsndto, coledat, coletim, coleml, colrepit }, null,
						null, null, null, null);
			} else {
				c = esql.query(etblnm, new String[] { colId, colsttl, colnot,
						colsndto, coledat, coletim, colrepit }, null, null,
						null, null, null);
			}
			while (c.moveToNext()) {
				if (c.getString(0).equals(evtid)) {
					arlst.add(c.getString(0).toString());
					arlst.add(c.getString(1).toString());
					arlst.add(c.getString(2).toString());
					arlst.add(String.valueOf(c.getLong(3)));
					arlst.add(c.getString(4).toString());
					arlst.add(c.getString(5).toString());

					if (!evtid.startsWith("C")) {
						arlst.add(c.getString(6).toString());
						arlst.add(c.getString(7).toString());
					} else
						arlst.add(c.getString(6).toString());

				}
			}
			esql.close();
		} catch (Exception e) {
			Toast.makeText(context, "Exception getEventdata: " + e,
					Toast.LENGTH_LONG).show();
		}
		return arlst;
	}

	public void removeEvent(String id, String ttl) {

		String dltTable = "";
		if (id.startsWith("B"))
			dltTable = "birthday";
		else if (id.startsWith("A"))
			dltTable = "anniversary";
		else if (id.startsWith("C"))
			dltTable = "custom";

		try {
			SQLiteDatabase dltsql = this.getWritableDatabase();
			dltsql.delete(dltTable, colId + "=?",
					new String[] { String.valueOf(id) });
			dltsql.delete(tblcntgrp, colId + "=?",
					new String[] { String.valueOf(id) });
			dltsql.close();
		} catch (Exception e) {
			Toast.makeText(context, "Remove Event error; " + e + ttl,
					Toast.LENGTH_LONG).show();
		}
		Toast.makeText(context, "Successfully Removed...", Toast.LENGTH_SHORT)
				.show();
	}

	public void removeCntGrp(String evtid) {
		SQLiteDatabase sql = this.getWritableDatabase();
		sql.delete(tblcntgrp, colId + "=?",
				new String[] { String.valueOf(evtid) });
		sql.close();
	}

	public void updateBirthdEvent(String ptblnm, String pid, String pttl,
			String pnot, String psnd2, String pdt, String ptm, String pml,
			String prpt) {
		try {
			long dmo = Long.parseLong(psnd2.toString());
			mbno = Long.valueOf(dmo);

			SQLiteDatabase sql = this.getWritableDatabase();
			ContentValues cv = new ContentValues();
			cv.put(colsttl, pttl);
			cv.put(colnot, pnot);
			cv.put(colsndto, mbno);
			cv.put(coledat, pdt);
			cv.put(coletim, ptm);
			if (!ptblnm.startsWith("c"))
				cv.put(coleml, pml);
			cv.put(colrepit, prpt);
			sql.update(ptblnm, cv, colId + "=?",
					new String[] { String.valueOf(pid) });
			// Cursor s
			// =sql.rawQuery("update "+emptable+" set "+colname+" ='"+value+"'"+" where "+colId
			// + " = "+id,null);
			sql.close();
		} catch (Exception e) {
			Toast.makeText(context, pid + "..update event exce; " + e,
					Toast.LENGTH_LONG).show();
		}
		Toast.makeText(context, "Successfully Updated...", Toast.LENGTH_SHORT)
				.show();
	}

	public ArrayList<String> getExecuteEventId() {
		ArrayList<String> evtIds = new ArrayList<String>();
		String[] tblnm = new String[] { "birthday", "anniversary", "custom" };
		SQLiteDatabase sql = this.getReadableDatabase();
		try {
			for (int i = 0; i < 3; i++) {
				Cursor cur = sql.query(tblnm[i], new String[] { colId, coledat,
						coletim }, null, null, null, null, null);

				while (cur.moveToNext()) {
					if (isCurrentEvent(cur.getString(1), cur.getString(2))) {
						evtIds.add(cur.getString(0).toString());
					}
				}
			}
		} catch (Exception e) {
			Toast.makeText(context, "executeEventId error: " + e,
					Toast.LENGTH_LONG).show();
		}
		sql.close();
		return evtIds;
	}

	public boolean isCurrentEvent(String dt, String tm) {
		String[] d = new String[] { "", "", "" };
		String[] t = new String[] { "", "" };

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
		Calendar c = Calendar.getInstance();
		c.set(Integer.parseInt(d[2]), Integer.parseInt(d[1]),
				Integer.parseInt(d[0]), Integer.parseInt(t[0]),
				Integer.parseInt(t[1]));
		Date dat1 = c.getTime(); // event date with time...

		String[] cdat = new String[] { "", "", "", "", "", "" };
		Calendar cdt = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		String scdt = formatter.format(cdt.getTime());

		j = 0;
		for (int i = 0; i < scdt.length(); i++) {
			if (scdt.charAt(i) != ('/') && scdt.charAt(i) != (' ')
					&& scdt.charAt(i) != (':'))
				cdat[j] += scdt.charAt(i);
			if (scdt.charAt(i) == ('/') || scdt.charAt(i) == (' ')
					|| scdt.charAt(i) == (':'))
				j++;
		}

		c.set(Integer.parseInt(cdat[2]), Integer.parseInt(cdat[1]),
				Integer.parseInt(cdat[0]), Integer.parseInt(cdat[3]),
				Integer.parseInt(cdat[4]));
		Date dat2 = c.getTime();
		if (dat1.compareTo(dat2) == 0){
			return true;
		}
		else
			return false;
	}

	public ArrayList<String> getMobileNo(String ptbnm, String pid) {
		ArrayList<String> mn = new ArrayList<String>();
		SQLiteDatabase sql = this.getReadableDatabase();
		try {
			Cursor cur = sql.query(ptbnm, new String[] { colsndto }, colId
					+ "=?", new String[] { String.valueOf(pid) }, null, null,
					null);
			if (cur != null) {
				cur.moveToFirst();
				mn.add(cur.getString(0));
			}

			cur = sql.query(tblcntgrp, new String[] { colId, clcntmb }, null,
					null, null, null, null);
			while (cur.moveToNext()) {
				if ((cur.getString(0)).equals(pid)) {
					mn.add(String.valueOf(cur.getLong(1)));
				}
			}
		} catch (Exception e) {
			Toast.makeText(context, "getMobileNo error: " + e,
					Toast.LENGTH_LONG).show();
		}
		sql.close();
		return mn;
	}

	public String getRepeatTime(String cid) {
		String mod = "";
		String stblnm = "";
		if (cid.startsWith("B"))
			stblnm = "birthday";
		else if (cid.startsWith("A"))
			stblnm = "anniversary";
		else if (cid.startsWith("C"))
			stblnm = "custom";

		SQLiteDatabase sqlRead = this.getReadableDatabase();

		Cursor cur = sqlRead.query(stblnm, new String[] { colrepit }, colId
				+ "=?", new String[] { String.valueOf(cid) }, null, null, null);
		if (cur != null) {
			cur.moveToFirst();
			mod = (cur.getString(0)).toString();
		}
		sqlRead.close();

		return mod;
	}

	public void updateEventMode(String pcid, String prm) {
		String tblnm = "";
		if (pcid.startsWith("B"))
			tblnm = "birthday";
		else if (pcid.startsWith("A"))
			tblnm = "anniversary";
		else if (pcid.startsWith("C"))
			tblnm = "custom";

		SQLiteDatabase sql = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		try {

			if (pcid.startsWith("A") || pcid.startsWith("B")) {

				String[] cdat = new String[] { "", "", "", "", "", "" };
				Calendar cdt = Calendar.getInstance();
				SimpleDateFormat formatter = new SimpleDateFormat(
						"dd/MM/yyyy HH:mm:ss");
				String scdt = formatter.format(cdt.getTime());

				int j = 0;
				for (int i = 0; i < scdt.length(); i++) {
					if (scdt.charAt(i) != ('/') && scdt.charAt(i) != (' ')
							&& scdt.charAt(i) != (':'))
						cdat[j] += scdt.charAt(i);
					if (scdt.charAt(i) == ('/') || scdt.charAt(i) == (' ')
							|| scdt.charAt(i) == (':'))
						j++;
				}
				int yr = Integer.parseInt(cdat[2]) + 1;
				String dat = cdat[0] + "-" + cdat[1] + "-" + String.valueOf(yr);

				cv.put(coledat, dat);
				sql.update(tblnm, cv, colId + "=?",
						new String[] { String.valueOf(pcid) });
				sql.close();

			} else if (pcid.startsWith("C")) {
				if (prm.equals("Daily")) {
					String nst = getNewSheduleTime("Daily");
					updateRepeatMode(nst, pcid);
				}
				if (prm.equals("Weekly")) {
					String nst = getNewSheduleTime("Weekly");
					updateRepeatMode(nst, pcid);
				}
				if (prm.equals("Monthly")) {
					String nst = getNewSheduleTime("Monthly");
					updateRepeatMode(nst, pcid);
				}
				if (prm.equals("Yearly")) {
					String nst = getNewSheduleTime("Yearly");
					updateRepeatMode(nst, pcid);
				}
			}

		} catch (Exception e) {
			Toast.makeText(context, "update event mode exce; " + e,
					Toast.LENGTH_LONG).show();
		}
	}

	public String getNewSheduleTime(String m) {
		String[] cdat = new String[] { "", "", "", "", "", "" };
		Calendar cdt = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		String scdt = formatter.format(cdt.getTime());

		int j = 0;
		for (int i = 0; i < scdt.length(); i++) {
			if (scdt.charAt(i) != ('/') && scdt.charAt(i) != (' ')
					&& scdt.charAt(i) != (':'))
				cdat[j] += scdt.charAt(i);
			if (scdt.charAt(i) == ('/') || scdt.charAt(i) == (' ')
					|| scdt.charAt(i) == (':'))
				j++;
		}

		String dat = cdat[0] + "-" + cdat[1] + "-" + cdat[2];
		if (m.equals("Daily")) {
			int dy = Integer.parseInt(cdat[0]) + 1;
			dat = String.valueOf(dy) + "-" + cdat[1] + "-" + cdat[2];
		} else if (m.equals("Weekly")) {
			int wkl = Integer.parseInt(cdat[0]) + 7;
			dat = String.valueOf(wkl) + "-" + cdat[1] + "-" + cdat[2];
		} else if (m.equals("Monthly")) {
			int mnt = Integer.parseInt(cdat[1]) + 1;
			dat = cdat[0] + "-" + String.valueOf(mnt) + "-" + cdat[2];
		} else if (m.equals("Yearly")) {
			int yr = Integer.parseInt(cdat[2]) + 1;
			dat = cdat[0] + "-" + cdat[1] + "-" + String.valueOf(yr);
		}

		return dat;
	}

	public void updateRepeatMode(String dat, String pcid) {
		try {
			SQLiteDatabase sql = this.getWritableDatabase();
			ContentValues cv = new ContentValues();
			cv.put(coledat, dat);
			sql.update("custom", cv, colId + "=?",
					new String[] { String.valueOf(pcid) });
			sql.close();
		} catch (Exception e) {
			Toast.makeText(context, "Update RepeatMode Exception: "+e, Toast.LENGTH_LONG).show();
		}
	}
}
