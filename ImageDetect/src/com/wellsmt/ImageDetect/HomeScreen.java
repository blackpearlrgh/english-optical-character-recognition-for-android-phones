package com.wellsmt.ImageDetect;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class HomeScreen extends Activity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		final ImageView mImageView = (ImageView) findViewById(R.id.image);
		mImageView.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Toast.makeText(HomeScreen.this, "2D Gaussian...",
						Toast.LENGTH_LONG);
			}
		});
		final ImageButton buttonCapture = (ImageButton) findViewById(R.id.button_capture);
		buttonCapture.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent("com.wellsmt.ImageDetect.Preview"));
			}
		});
		final ImageButton buttonOpen = (ImageButton) findViewById(R.id.button_open);
		buttonOpen.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Perform action on click
				startActivity(new Intent("com.wellsmt.ImageDetect.OpenImage"));
			}
		});
	}
}