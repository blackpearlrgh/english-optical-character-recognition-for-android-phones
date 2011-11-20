package com.wellsmt.ImageDetect;

import com.wellsmt.ImageDetect.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class Progress extends Activity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.progress);
		try {
			Thread t = new Thread() {
				public void run() {
					startActivity(new Intent(
							"com.wellsmt.ImageDetect.HomeScreen"));
				}
			};
			t.start();
		} catch (Exception ex) {
			ex.printStackTrace();
			System.err.print("Exception: " + ex.toString()); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}
}