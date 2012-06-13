package com.android;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ShowText extends Activity {
	Button btnhome,btnsave;
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		    setContentView(R.layout.showpic);
		    
		    
		    setupUI();
		    addListeners();
		    
		    
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
		   Log.v(this.toString(), "setupUI");
	   }
	  
	   
	   OnClickListener listen=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
		
				 Log.v(this.toString(), "Inside on click listener for screen 2.");
				//Class myClass2 = Class.forName("com.android.TakePicture3Activity");
//				 Intent data = new Intent(ShowText.this,myClass2);			
//				startActivity(data)	;
				finish();
					
		
		}
	};
}
