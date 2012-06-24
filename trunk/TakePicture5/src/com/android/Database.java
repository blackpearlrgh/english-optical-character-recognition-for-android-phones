package com.android;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Database {

	public static final String KEY_ROWID = "_id";
	public static final String LETTER = "letter";
	public static final String CHAIN_CODE = "CC"; // changed it to _id to be
	// able to use it in queries
	// since it is the primary
	// key

	private static final String DATABASE_NAME = "OCR";
	private static final String DATABASE_TABLE = "ARIAL";
	private static final int DATABASE_VERSION = 1;

	private DbHelper ourhelper;
	private final Context ourcontext;
	private SQLiteDatabase OurDatabase;

	private class DbHelper extends SQLiteOpenHelper

	{

		public DbHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			// TODO Auto-generated constructor stub

		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub

			db.execSQL(" CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE + " ("
					+ KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ CHAIN_CODE + " TEXT NOT NULL, " + LETTER + " TEXT NOT NULL);");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			onCreate(db);
		}
	}

	public Database(Context c) {
		ourcontext = c;
	}

	public Database open() {
		ourhelper = new DbHelper(ourcontext);
		OurDatabase = ourhelper.getWritableDatabase();
		return this;
	}

	public void close() {
		ourhelper.close();
	}

	public void InsertEnteries() {
		ContentValues cv;
		cv = new ContentValues();

		cv.put(CHAIN_CODE, "23222232222322222000066660000012220000766666656666");
		cv.put(LETTER, "A");
		OurDatabase.insert(DATABASE_TABLE, null, cv);
		cv.clear();

		cv.put(CHAIN_CODE, "22222222222222222000000000007776666656666665544444");
		cv.put(LETTER, "B");
		OurDatabase.insert(DATABASE_TABLE, null, cv);
		cv.clear();

		cv.put(CHAIN_CODE, "33332222221210000000777665443334455666677001100066");
		cv.put(LETTER, "C");
		OurDatabase.insert(DATABASE_TABLE, null, cv);
		cv.clear();

		cv.put(CHAIN_CODE, "22222222222222222000000000007776666666666655444444");
		cv.put(LETTER, "D");
		OurDatabase.insert(DATABASE_TABLE, null, cv);
		cv.clear();

		cv.put(CHAIN_CODE, "22222222220000000066444444660000066444446600000765");
		cv.put(LETTER, "E");
		OurDatabase.insert(DATABASE_TABLE, null, cv);
		cv.clear();

		cv.put(CHAIN_CODE, "22222222222220076666700000664444456700000066444444");
		cv.put(LETTER, "F");
		OurDatabase.insert(DATABASE_TABLE, null, cv);
		cv.clear();

		cv.put(CHAIN_CODE, "33322222210000000766665444432100234455666700010076");
		cv.put(LETTER, "G");
		OurDatabase.insert(DATABASE_TABLE, null, cv);
		cv.clear();

		cv.put(CHAIN_CODE, "22222222220076667000122210066666666664422224445666");
		cv.put(LETTER, "H");
		OurDatabase.insert(DATABASE_TABLE, null, cv);
		cv.clear();

		cv.put(CHAIN_CODE, "22222222222222222000000000007776666656666665544444");
		cv.put(LETTER, "I");
		OurDatabase.insert(DATABASE_TABLE, null, cv);
		cv.clear();

		cv.put(CHAIN_CODE, "22222222222224565444222211000000776666666666666665");
		cv.put(LETTER, "J");
		OurDatabase.insert(DATABASE_TABLE, null, cv);
		cv.clear();

		cv.put(CHAIN_CODE, "22222222222220076667011110006655555677776444333356");
		cv.put(LETTER, "K");
		OurDatabase.insert(DATABASE_TABLE, null, cv);
		cv.clear();

		cv.put(CHAIN_CODE, "22222222222222222000000000007665444444466666666666");
		cv.put(LETTER, "L");
		OurDatabase.insert(DATABASE_TABLE, null, cv);
		cv.clear();

		cv.put(CHAIN_CODE, "22222222220076712000660220076666666665443222235666");
		cv.put(LETTER, "M");
		OurDatabase.insert(DATABASE_TABLE, null, cv);
		cv.clear();

		cv.put(CHAIN_CODE, "22222222222200066666701111100066666666666644422222");
		cv.put(LETTER, "N");
		OurDatabase.insert(DATABASE_TABLE, null, cv);
		cv.clear();

		cv.put(CHAIN_CODE, "43332322222222211000000000007767666666666555544444");
		cv.put(LETTER, "O");
		OurDatabase.insert(DATABASE_TABLE, null, cv);
		cv.clear();

		cv.put(CHAIN_CODE, "22222222222222222000766666600000007766666665444444");
		cv.put(LETTER, "P");
		OurDatabase.insert(DATABASE_TABLE, null, cv);
		cv.clear();

		cv.put(CHAIN_CODE, "43332322222222211000000000000006665666666666656555");
		cv.put(LETTER, "Q");
		OurDatabase.insert(DATABASE_TABLE, null, cv);
		cv.clear();

		cv.put(CHAIN_CODE, "22222222222220076666700111000065555776666554444444");
		cv.put(LETTER, "R");
		OurDatabase.insert(DATABASE_TABLE, null, cv);
		cv.clear();

		cv.put(CHAIN_CODE, "43322222210034432222100000000000766666655570766665");
		cv.put(LETTER, "S");
		OurDatabase.insert(DATABASE_TABLE, null, cv);
		cv.clear();

		cv.put(CHAIN_CODE, "22200000222222222222210007666666666666600007666444");
		cv.put(LETTER, "T");
		OurDatabase.insert(DATABASE_TABLE, null, cv);
		cv.clear();

		cv.put(CHAIN_CODE, "22222222221100000077766666666666443222222223345566");
		cv.put(LETTER, "U");
		OurDatabase.insert(DATABASE_TABLE, null, cv);
		cv.clear();

		cv.put(CHAIN_CODE, "22222122221222122000066766676666766666444422223222");
		cv.put(LETTER, "V");
		OurDatabase.insert(DATABASE_TABLE, null, cv);
		cv.clear();

		cv.put(CHAIN_CODE, "22222222007666602222007666666654322246665442223566");
		cv.put(LETTER, "W");
		OurDatabase.insert(DATABASE_TABLE, null, cv);
		cv.clear();

		cv.put(CHAIN_CODE, "21111133333220007770110000655555677776544433455444");
		cv.put(LETTER, "X");
		OurDatabase.insert(DATABASE_TABLE, null, cv);
		cv.clear();

		cv.put(CHAIN_CODE, "22211111102222222000766666677777777765444433334555");
		cv.put(LETTER, "Y");
		OurDatabase.insert(DATABASE_TABLE, null, cv);
		cv.clear();

		cv.put(CHAIN_CODE, "22000003333333222000000000066644444677777776644444");
		cv.put(LETTER, "Z");
		OurDatabase.insert(DATABASE_TABLE, null, cv);
		cv.clear();
	}

	public void getData() {
		int i = 0;
		String[] Data = new String[] { LETTER, CHAIN_CODE };
		Cursor c = OurDatabase.query(DATABASE_TABLE, Data, CHAIN_CODE, null,
				null, null, null);
		String Result = "";
		int iLetter = c.getColumnIndex(LETTER);
		int iChain = c.getColumnIndex(CHAIN_CODE);

		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			Result = c.getString(iLetter) + "\t" + c.getString(iChain);

			Log.d("DBTest", "Entry " + i + " " + Result);
			i++;
			Result = "";
		}
	}

	public int mod(int x) {
		while (x < 0) {
			x += 8;
		}
		return x;
	}

	public int EuclideanDist(String entry, String chainCode) {

		int CCentry, CC;
		String s, sentry;
		int step = 0;
		Log.d("EuclideanDis Entered", " ");
		Log.d("chainCode", " " + chainCode);
		Log.d("chainCode.length", " " + chainCode.length());
		for (int i = 0; i < chainCode.length(); i++) {
			Log.d("Euclidean Loop", " " + i);
			sentry = "" + entry.charAt(i);
			CCentry = Integer.parseInt(sentry);
			s = "" + chainCode.charAt(i);
			CC = Integer.parseInt(s);
			Log.d("Euclidean Loop", " " + i + " " + CC + " " + CCentry);

			if (CCentry == CC) {
				step += 0;
			} else if ((CCentry == mod(CC - 1)) || (CCentry == (CC + 1) % 8)) {
				step += 1;
			} else if ((CCentry == mod(CC - 2)) || (CCentry == (CC + 2) % 8)) {
				step += 2;
			} else if ((CCentry == mod(CC - 3)) || (CCentry == (CC + 3) % 8)) {
				step += 3;
			} else if ((CCentry == mod(CC - 4)) || (CCentry == (CC + 4) % 8)) {
				step += 4;
			}
			Log.d("Euclidean Step", " " + step);
		}
		Log.d("EuclideanDis Leaving", " ");
		return step;
	}

	public String getLetter(String chainCode) {
		Log.d("Inside getLetter ", " ");
		String entry = null;
		int i = 0, j = 0;
		int euclideanop, smallest;
		List<Integer> euclideanlist = new ArrayList<Integer>();
		ourhelper.getReadableDatabase();
		String[] columns = new String[] { KEY_ROWID, LETTER, CHAIN_CODE };
		Cursor c = OurDatabase.query(DATABASE_TABLE, columns, CHAIN_CODE, null,
				null, null, null);

		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			Log.d("Cursor getLetter Loop", "Inside" + i);
			entry = c.getString(c.getColumnIndex(CHAIN_CODE));
			Log.d("Entry", entry);
			euclideanop = EuclideanDist(entry, chainCode);
			euclideanlist.add(euclideanop);
			Log.d("Euclidean Array", " " + i + " " + euclideanlist.get(i));
			i++;
		}

		Log.d("Curcor getLetter Outside", " ");

		smallest = euclideanlist.get(0);
		int reference = 0;

		for (j = 0; j < euclideanlist.size(); j++) {
			Log.d("smallest euclidean loop", "Inside ");
			if (euclideanlist.get(j) < smallest) {
				smallest = euclideanlist.get(j);
				reference = j;
				Log.d("smallest value", " " + reference + " " + smallest);
			}
		}
		String letter = getLetterByIndex(reference+1);
		Log.d("OUTPUT", letter);
		return letter;
	}

	public String getLetterByIndex(int reference) {

		Log.d("Reference", ""+reference);
		
		SQLiteDatabase db = ourhelper.getReadableDatabase();
		Cursor output = db.rawQuery("SELECT " + LETTER + " FROM "
				+ DATABASE_TABLE + " WHERE " + KEY_ROWID + " = " + reference
				, null);

		for (output.moveToFirst(); !output.isAfterLast();) {
			String letter = output.getString(output.getColumnIndex(LETTER));
			return letter;
		}
		return null;
	}

}