package com.android;

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
		    myImgInput = (Bitmap) getIntent().getParcelableExtra("myImgInput");	
			Log.d("Test myImgInput", ""+ myImgInput);
			String text=FormulateDB();
			textview.setText(text);
		    
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
	
	ImgProcessing imgclass = new ImgProcessing();
	  
	public String FormulateDB() {
		
	//	myImgInput=BitmapFactory.decodeResource(getResources(), R.drawable.comp);
		imgclass.operate(myImgInput);
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
