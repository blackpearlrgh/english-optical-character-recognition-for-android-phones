package com.android;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.ImageColumns;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class TakePicture5Activity extends Activity {
	/** Called when the activity is first created. */
	private static final int SELECT_PICTURE = 1;
	private static final int OPEN_CAMERA = 2;
	private static final int MenuOptions = 3;
	private static final int Database = 4;

	private String selectedImagePath;
	Button b1, b2, Displaytext, Menubtn;
	ImageView iv;
	TextView tv;
	int position;
	Intent ImgProintent;
	Bitmap myImgInput;
	int flag = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		setupui();
		addListners();
	}

	private void addListners() {
		b2.setOnClickListener(openCamera);
		b1.setOnClickListener(openGallaery);
		Displaytext.setOnClickListener(display_text);
		Menubtn.setOnClickListener(MenuList);
		Log.v(this.toString(), "Inside addListeners.");

	}

	private void setupui() {
		b1 = (Button) findViewById(R.id.button1);
		b2 = (Button) findViewById(R.id.button2);
		Displaytext = (Button) findViewById(R.id.DisplayTxt);
		Menubtn = (Button) findViewById(R.id.MenuBtn);
		iv = (ImageView) findViewById(R.id.imageView1);
		Log.v(this.toString(), "SEUTUPUI.");

	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// //////////////////////////////////////////////////////////////////////StartActivtyfor
		// result for camera
		if (requestCode == OPEN_CAMERA) {
			if (resultCode == RESULT_OK) {
				if (data.hasExtra("data")) {
					/*
					 * if the data has an extra called "data" we assume the
					 * returned data is from the usual camera app
					 */
					// retrieve the bitmap from the intent
					BitmapFactory.Options options = new BitmapFactory.Options();

					options.inScaled = false;
					options.inPurgeable = true;
					options.inInputShareable = true;

					Bitmap thumbnail = (Bitmap) data.getExtras().get("data");

					// update the image view with the bitmap

					iv.setImageBitmap(thumbnail);

					if (myImgInput != null) {
						myImgInput.recycle();
						myImgInput = null;
					}
					myImgInput = thumbnail; // HADEEEEEL LOOOOK HEREEEE
											// BITMAPDRAWABLE

					Log.d("Test myImgInput", "" + myImgInput);
					Log.d("Channels", " " + myImgInput.getDensity());
					Log.d("Width", " " + myImgInput.getWidth());
					Log.d("Height", " " + myImgInput.getHeight());
					Log.d("Config", " " + myImgInput.getConfig());

				} else if (data.getExtras() == null) {
					/*
					 * if there are no extras we assume its the miui camera
					 * (which returns the path to the image in the returned
					 * data)
					 */
					Toast.makeText(getApplicationContext(),
							"No extras to retrieve!", Toast.LENGTH_SHORT)
							.show();
					// retrieve the path from the intent using
					// data.getData().getPath() and create a BitmapDrawable
					// using this path
					BitmapDrawable thumbnail = new BitmapDrawable(
							getResources(), data.getData().getPath());

					// myImgInput=BitmapFactory.decodeResource(getResources(),
					// R.drawable.abccapital);
					// update the image view with the newly created drawable
					// iv.setImageDrawable(thumbnail);
					myImgInput = ((BitmapDrawable) thumbnail).getBitmap(); // //HADEEEEEL
																			// LOOOOK
																			// HEREEEE
																			// BITMAPDRAWABLE
					// myImgInput = (thumbnail).getBitmap();
					iv.setImageBitmap(myImgInput);
				}
			} else if (resultCode == RESULT_CANCELED) {
				Toast.makeText(getApplicationContext(), "Cancelled",
						Toast.LENGTH_SHORT).show();
			}
		}
		// ///////////////////////////////////////////////////////////////////StartActivityForResult
		// For Gallery
		else if (requestCode == SELECT_PICTURE) {
			if (resultCode == RESULT_OK) {
				if (data != null) {
					// our BitmapDrawable for the thumbnail
					BitmapDrawable bmpDrawable = null;
					// try to retrieve the image using the data from the intent
					Cursor cursor = getContentResolver().query(data.getData(),
							null, null, null, null);
					if (cursor != null) {
						BitmapFactory.Options options = new BitmapFactory.Options();

						options.inScaled = false;
						options.inPurgeable = true;
						options.inInputShareable = true;
						options.inPreferredConfig=Bitmap.Config.ARGB_8888;

						/*
						 * if the query worked the cursor will not be null, so
						 * we assume the normal gallery was used to choose the
						 * picture
						 */
						cursor.moveToFirst();
						int idx = cursor.getColumnIndex(ImageColumns.DATA);
						String fileSrc = cursor.getString(idx);
						// Bitmap bitmapPreview =
						// BitmapFactory.decodeFile(fileSrc); //load preview
						// image

						if (myImgInput != null) {
							myImgInput.recycle();
							myImgInput = null;
						}

						myImgInput = BitmapFactory.decodeFile(fileSrc, options);

						// myImgInput = BitmapFactory.decodeFile(fileSrc);
						Log.d("Test myImgInput", "" + myImgInput);
						Log.d("Channels", " " + myImgInput.getDensity());
						Log.d("Width", " " + myImgInput.getWidth());
						Log.d("Height", " " + myImgInput.getHeight());
						Log.d("Config", " " + myImgInput.getConfig());

						// bmpDrawable = new BitmapDrawable(bitmapPreview);//set
						// the BitmapDrawable to the loaded image
					}
					// else
					// {
					// /*if the cursor is null after the query the data returned
					// is different so we assume
					// * the miui gallery was used (so the data contains the
					// path to the image)*/
					// BitmapFactory.Options options = new
					// BitmapFactory.Options();
					// options.inScaled=false;
					// bmpDrawable = new BitmapDrawable(getResources(),
					// data.getData().getPath());
					// myImgInput = ((BitmapDrawable)bmpDrawable).getBitmap();
					// //ADDED THIS ONE HERE HADEEEEEEL
					// }
					// // iv.setImageDrawable(bmpDrawable);//update our
					// imageview with the BitmapDrawable

					// Log.d("Test myImgInput", ""+ myImgInput);
					// Log.d("Channels"," "+ myImgInput.getDensity());
					// Log.d("Width"," "+ myImgInput.getWidth());
					// Log.d("Height"," "+ myImgInput.getHeight());

					iv.setImageBitmap(myImgInput);
					Log.d("Test grayImg", "" + myImgInput);
				} else {
					Toast.makeText(getApplicationContext(), "Cancelled",
							Toast.LENGTH_SHORT).show();
				}
			} else if (resultCode == RESULT_CANCELED) {
				Toast.makeText(getApplicationContext(), "Cancelled",
						Toast.LENGTH_SHORT).show();
			}
		}// end of else
		// /////////////////////////////////////////////////////////start
		// activity for result for Menu
		else if (requestCode == MenuOptions) {
			if (resultCode == RESULT_OK)

			{
				// BitmapFactory.Options options = new BitmapFactory.Options();
				// options.inScaled=false;
				//
				// myImgInput=BitmapFactory.decodeResource(getResources(),
				// R.drawable.abcde,options);
				// // Log.d("Test myImgInput", ""+ myImgInput);
				// Log.d("Channels"," "+ myImgInput.getDensity());
				// Log.d("Width"," "+ myImgInput.getWidth());
				// Log.d("Height"," "+ myImgInput.getHeight());
				//

				//
				// int imageResource = R.drawable.abc;
				// Drawable image = getResources().getDrawable(imageResource);
				// InputStream is =
				// this.getResources().openRawResource(imageResource);
				// myImgInput = BitmapFactory.decodeStream(is);

				// myImgInput=((BitmapDrawable)image).getBitmap();
				// Log.d("Test myImgInput", ""+ myImgInput);
				// Log.d("Channels"," "+ myImgInput.getDensity());
				// Log.d("Width"," "+ myImgInput.getWidth());
				// Log.d("Height"," "+ myImgInput.getHeight());
				//

				// Drawable blankDrawable =
				// context.getResources().getDrawable(id);
				if (myImgInput != null) {
					position = data.getExtras().getInt("position");
					Log.v(this.toString(),
							"I returned to main class with position" + position);
					ImgProcessing myImageOut = new ImgProcessing();
					myImageOut.operate(myImgInput);
					Log.d("Test myImgInput", "" + myImgInput);

					// if(myImgInput!=null)
					// { myImgInput.recycle();
					// myImgInput=null;
					// }
					//
					Log.d("Test myImgInput", "" + myImgInput);
					Bitmap OutImg = myImageOut.MenuOutput(position);
					Log.d("Test OutImg", "" + OutImg);
					iv.setImageBitmap(OutImg);
					Log.d("Test OutImg", "" + OutImg);
					// OutImg.recycle();
					// OutImg=null;
					Log.d("Test OutImg", "" + OutImg);

				} else
					Toast.makeText(getApplicationContext(),
							"Upload a picture first Please", Toast.LENGTH_SHORT)
							.show();
			} else if (resultCode == RESULT_CANCELED) {
				Toast.makeText(getApplicationContext(), "Cancelled",
						Toast.LENGTH_SHORT).show();
			}
		}
	}// end of start activity
		// /////////////////////////////////////////////////////////////////

	public String getPath(Uri uri) {

		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		if (cursor != null) {
			// HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
			// THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		} else
			return null;
	}

	OnClickListener MenuList = new OnClickListener() {
		// @Override
		public void onClick(View v) {
			/*
			 * Intent intent2 = new Intent("com.android.MENU");
			 * startActivity(intent2);
			 */

			try {
				Class myClass = Class.forName("com.android.Menu");
				Intent menuIntent = new Intent(TakePicture5Activity.this,
						myClass);
				startActivityForResult(menuIntent, MenuOptions);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	};

	OnClickListener openCamera = new OnClickListener() {
		// @Override
		public void onClick(View v) {
			// flag=1;
			Intent intent2 = new Intent("android.media.action.IMAGE_CAPTURE");
			startActivityForResult(intent2, OPEN_CAMERA);

		}
	};

	OnClickListener openGallaery = new OnClickListener() {
		// @Override
		public void onClick(View v) {

			Intent intent = new Intent();
			intent.setType("image/*");
			intent.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(
					Intent.createChooser(intent, "Select Picture"),
					SELECT_PICTURE);
		}
	};

	OnClickListener display_text = new OnClickListener() {
		// @Override
		public void onClick(View v) {
			try {
				// BitmapFactory.Options options = new BitmapFactory.Options();
				// options.inScaled=false;
				//
				// myImgInput=BitmapFactory.decodeResource(getResources(),
				// R.drawable.abcde,options);
				//

				if (myImgInput != null)

				{
					ImgProcessing myImageOut = new ImgProcessing();
					myImageOut.operate(myImgInput);

					Class myClass2 = Class.forName("com.android.ShowText");
					Intent dbIntent = new Intent(TakePicture5Activity.this,
							myClass2);

					dbIntent.putStringArrayListExtra("chainCode",
							(ArrayList<String>) myImageOut.chainCode);

					startActivity(dbIntent);
				} else
					Toast.makeText(getApplicationContext(),
							"Upload a picture first Please", Toast.LENGTH_SHORT)
							.show();

			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};

}
