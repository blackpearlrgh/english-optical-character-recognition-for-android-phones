package com.android;

import java.util.ArrayList;

import android.R.string;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class ShowText extends Activity {
	Button btnhome,btnsave;
    Bitmap myImgInput;
    EditText textview;
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		    setContentView(R.layout.showpic);
		    
		    
		    setupUI();
		    addListeners();	
		    
//
//		    if (getIntent().getExtras() != null) {
//	            for(String a : getIntent().getExtras().getStringArrayList("chainCode")) {
//	                Log.d("=======","Data " + a);
//	            }
//		    }
		    
		    ArrayList<String>chainCode= getIntent().getExtras().getStringArrayList("chainCode");
//		    Log.d("CHain code in showtext 0", ""+ "= " + chainCode.get(0));
//		    Log.d("CHain code in showtext 1", ""+ "= " + chainCode.get(1));
//		    Log.d("CHain code in showtext 2", ""+ "= " + chainCode.get(2));
//		    Log.d("CHain code in showtext 3", ""+ "= " + chainCode.get(3));
//		    Log.d("CHain code in showtext 4", ""+ "= " + chainCode.get(4));
//		    
		    String mytext=FormulateDB(chainCode);
		    
			Log.d("myText", ""+ mytext);
			
			textview.setText(mytext);
			
		    
	}
	   
	   private void addListeners()
	   {
	   btnhome.setOnClickListener(listen);
//	   btnsave.setOnClickListener(listen);
	   Log.v(this.toString(), "Button03 in add listeners.");
	   }
	   
	   private void setupUI()
	   {
		   btnhome=(Button)findViewById(R.id.btnHome);
		   btnsave=(Button)findViewById(R.id.btnSave);
		   textview=(EditText)findViewById(R.id.editText1);
		   
		   Log.v(this.toString(), "setupUI");
	   }
	  
	   
	   OnClickListener listen=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
				 Log.v(this.toString(), "Inside on click listener for screen 2.");
				 finish();
		
		}
	};
	
//	ImgProcessing imgclass = new ImgProcessing();
	  
	public String FormulateDB(ArrayList<String> chainCode) {
		
	//	myImgInput=BitmapFactory.decodeResource(getResources(), R.drawable.comp);
//		imgclass.operate(myImgInput);
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

		for (int i = 0; i < chainCode.size(); i++) {
			Log.d("Chaincode LOOP", "elements  " + i);
			//text.concat(DB.getLetter(chainCode.get(i)));
			text=DB.getLetter(chainCode.get(i));
			FinalOP += text;
			Log.d("TEXT", FinalOP);
		}

		DB.close();
		
		return FinalOP;
	}
 
}
