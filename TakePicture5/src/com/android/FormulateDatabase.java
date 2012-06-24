package com.android;

import java.util.ArrayList;
import java.util.List;

import android.R.string;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;

public class FormulateDatabase extends Activity{

	
	
	//ImgProcessing imgclass = new ImgProcessing();
	
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		if (getIntent().getExtras() != null) {
            //for(String a : getIntent().getExtras().getStringArrayList("chainCode")) {
              //  Log.d("=======","Data " + a);
			
			ArrayList<String>chainCode= getIntent().getExtras().getStringArrayList("chainCode");
			
		     String mytext=FormulateDatabase(chainCode);
		     
		    Intent mydbIntent = new Intent ();
			mydbIntent.putExtra("text", mytext);
			setResult(RESULT_OK,mydbIntent);
			
            }
        }
		
	

	public String  FormulateDatabase(ArrayList<String> chainCode) {
	   
//    	Log.v("ChainCode"+ chainCode.get(1), null);
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

		for (int i = 0; i < chainCode.size(); i++) {//imgclass.chainCode.size()
			Log.d("Chaincode LOOP", "elements  " + i);
			//text.concat(DB.getLetter(chainCode.get(i)));
			text=DB.getLetter(chainCode.get(i));//imgclass.chainCode.get(i)
			FinalOP += text;
			Log.d("TEXT", FinalOP);
		}

		DB.close();
		
		return FinalOP;
	}
}
