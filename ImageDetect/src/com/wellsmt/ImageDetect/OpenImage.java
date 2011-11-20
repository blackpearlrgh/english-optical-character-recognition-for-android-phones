package com.wellsmt.ImageDetect;

import java.io.InputStream;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

public class OpenImage extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image);
		Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
		photoPickerIntent.setType("image/*");
		startActivityForResult(photoPickerIntent, 1);
	}

	// Bitmap bytes have to be created via a direct memory copy of the bitmap
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			Uri chosenImageUri = data.getData();
			if (chosenImageUri != null) {
				try {
					InputStream photoStream = getContentResolver()
							.openInputStream(chosenImageUri);
					BitmapFactory.Options opts = new BitmapFactory.Options();
					opts.inSampleSize = 4;
					Bitmap mBitmap = BitmapFactory.decodeStream(photoStream,
							null, opts);
					if (mBitmap != null) {
						ModImage.setJpegData(mBitmap);
					}
					photoStream.close();
					startActivity(new Intent("com.wellsmt.ImageDetect.ModImage"));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}