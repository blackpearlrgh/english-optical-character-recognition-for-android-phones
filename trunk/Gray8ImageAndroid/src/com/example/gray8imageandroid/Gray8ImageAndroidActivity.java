package com.example.gray8imageandroid;

import android.app.Activity;
import jjil.core.Gray8Image;
import android.os.Bundle;

public class Gray8ImageAndroid extends Gray8Image {

	public Gray8ImageAndroid(int cWidth, int cHeight, byte bValue) {
		super(cWidth, cHeight, bValue);
		// TODO Auto-generated constructor stub
	}
	
	
}

public class Gray8ImageAndroidActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
}