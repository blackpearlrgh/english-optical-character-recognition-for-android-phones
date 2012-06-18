package com.android;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

public class FormulateDatabase extends Activity{

	
	ImgProcessing imgclass = new ImgProcessing();
	
    public String  FormulateDatabase() {
		String FinalOP ="",text="";
		String DB_PATH = "/data/data/com.examples.ocr/databases/OCR";
		SQLiteDatabase checkDB = null;
		boolean DbExist = true;

		Database DB;
		DB = new Database(this);//OCRusingopenCVActivity.this
		// Open Database if exists//
		try {
			checkDB = SQLiteDatabase.openDatabase(DB_PATH, null,
					SQLiteDatabase.OPEN_READONLY);
		}

		catch (SQLiteException e) {
		}

		// if it exists close and set DbExist=true//
		if (checkDB != null) {
			checkDB.close();
			DbExist = false;
		}

		DB = DB.open();

		if (DbExist == true) {
			DB.InsertEnteries();
			DB.getData();
			Log.d("DBTest", "Database Created");
		} else {
			DB.getData();
			Log.d("DBTest", "Database Already Exist");
		}

		for (int i = 0; i < imgclass.chainCode.size(); i++) {
			Log.d("Chaincode LOOP", "elements  " + i);
			//text.concat(DB.getLetter(chainCode.get(i)));
			text=DB.getLetter(imgclass.chainCode.get(i));
			FinalOP += text;
			Log.d("TEXT", FinalOP);
		}

		DB.close();
		
		return FinalOP;
	}
}
