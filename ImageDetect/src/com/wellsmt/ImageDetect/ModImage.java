package com.wellsmt.ImageDetect;

import java.io.InputStream;
import java.io.OutputStream;
import jjil.core.RgbImage;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore.Images.Media;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.Toast;

public class ModImage extends Activity implements Runnable {
	final short RED_CONTEXT = 0;
	final short GREEN_CONTEXT = 1;
	final short BLUE_CONTEXT = 2;
	final short EDGE_CONTEXT = 3;
	final short GRAY_CONTEXT = 4;
	final short ADD_CONTEXT = 5;
	final short BLEND_CONTEXT = 6;
	final short CONV_CONTEXT = 7;
	final short DOT_PROD_CONTEXT = 8;
	final short SCALE_CONTEXT = 9;
	final short D_THRESH_CONTEXT = 10;
	private static final int DIALOG_SINGLE_CHOICE = 0;
	private int end = 0;
	private int start = 0;
	final short MENU_SAVE = 0;
	final short MENU_UNDO = 1;
	final short MENU_PROC_TIME = 2;
	private ProgressDialog myProgressDialog = null;
	private RgbImage prevRgbImage = null;
	private static RgbImage mRgbImage = null;
	private static RgbImage mSecRgbImage = null;
	private static int width;
	private static int height;
	private static int mSecWidth;
	private static int mSecHeight;
	private int secondImage;
	private short currContextSelect;
	private ModImageView mImageView;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Toast.makeText(ModImage.this, "Obtained Image to Process...",
				Toast.LENGTH_LONG).show();
		setContentView(R.layout.image);
		this.mImageView = (ModImageView) findViewById(R.id.detectedImage);
		this.mImageView.resetFaces();
		this.mImageView.resetShowX();
		registerForContextMenu(mImageView);
		if (mRgbImage != null) {
			this.mImageView
					.setImageBitmap(Bitmap.createBitmap(mRgbImage.getData(),
							width, height, Bitmap.Config.ARGB_8888));
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
	switch (id) {
	case DIALOG_SINGLE_CHOICE:
	return new AlertDialog.Builder(ModImage.this)
	.setIcon(R.drawable.icon)
	.setTitle("To Be Added Soon...")
	//.setSingleChoiceItems(R.array.select_dialog_items2, 0, new
	DialogInterface.OnClickListener() {
	// public void onClick(DialogInterface dialog, int whichButton) {
	/* User clicked on a radio button do some stuff */
	// }
	//})
	.setPositiveButton("OK", new DialogInterface.OnClickListener() {
	public void onClick(DialogInterface dialog, int whichButton) {
		/* User clicked Yes so do some stuff */
	}
	})
	.create();
	}
	return null;
	}
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo)
	{
	super.onCreateContextMenu(menu, v, menuInfo);
	menu.add(0, RED_CONTEXT, 0, "Red");
	menu.add(0, GREEN_CONTEXT, 0, "Green");
	menu.add(0, BLUE_CONTEXT, 0, "Blue");
	menu.add(0, EDGE_CONTEXT,0, "Sobel");
	menu.add(0, GRAY_CONTEXT,0, "Gray");
	menu.add(0, ADD_CONTEXT,0, "Add");
	menu.add(0, BLEND_CONTEXT,0, "Blend");
	menu.add(0, CONV_CONTEXT,0, "Convolve");
	menu.add(0, DOT_PROD_CONTEXT,0, "Dot Product");
	menu.add(0, SCALE_CONTEXT,0, "Scale");
	menu.add(0, D_THRESH_CONTEXT,0, "Double Threshold");
	}
	public boolean onContextItemSelected(MenuItem item) {
	currContextSelect = (short)item.getItemId();
	switch (currContextSelect) {
	case RED_CONTEXT:
	if (mRgbImage != null) {
	showOnlyRed();
	mImageView.setImageBitmap( Bitmap.createBitmap(mRgbImage.getData(), width, height,
	Bitmap.Config.ARGB_8888) );
	}
	return true;
	case GREEN_CONTEXT:
	if (mRgbImage != null) {
	showOnlyGreen();
	mImageView.setImageBitmap( Bitmap.createBitmap(mRgbImage.getData(), width,
	height, Bitmap.Config.ARGB_8888) );
	}
	return true;
	case BLUE_CONTEXT:
	if (mRgbImage != null) {
	showOnlyBlue();
	mImageView.setImageBitmap( Bitmap.createBitmap(mRgbImage.getData(), width,
	height, Bitmap.Config.ARGB_8888) );
	}
	return true;
	case EDGE_CONTEXT:
	if (mRgbImage != null)
	{
	sobelImage();
	}
	return true;
	case GRAY_CONTEXT:
	if (mRgbImage != null)
	{
	greyScale(true);
	mImageView.setImageBitmap( Bitmap.createBitmap(mRgbImage.getData(), width,
	height, Bitmap.Config.ARGB_8888) );
	}
	return true;
	case ADD_CONTEXT:
	if( mRgbImage != null)
	{
	additionImage();
	}
	return true;
	case BLEND_CONTEXT:
	if( mRgbImage != null)
	{
	showDialog(DIALOG_SINGLE_CHOICE);
	}
	return true;
	case CONV_CONTEXT:
	if( mRgbImage != null)
	{
	convolveImage();
	}
	return true;
	case DOT_PROD_CONTEXT:
	if( mRgbImage != null)
	{
	showDialog(DIALOG_SINGLE_CHOICE);
	}
	return true;
	case SCALE_CONTEXT:
	if( mRgbImage != null)
	{
	showDialog(DIALOG_SINGLE_CHOICE);
	}
	return true;
	case D_THRESH_CONTEXT:
	if( mRgbImage != null)
	{
	showDialog(DIALOG_SINGLE_CHOICE);
	}
	return true;
	default:
	return super.onContextItemSelected(item);
	}
	}
	public boolean onCreateOptionsMenu(Menu menu)
	{
	menu.add(0, MENU_UNDO, 0, "Undo Last change");
	menu.add(0, MENU_SAVE, 0, "Save Image");
	menu.add(0, MENU_PROC_TIME, 0, "Processing Time");
	return true;
	}
	/* Handles item selections */
	public boolean onOptionsItemSelected(MenuItem item) {
	switch (item.getItemId()) {
	case MENU_UNDO:
	if( prevRgbImage != null)
	{
	mRgbImage = (RgbImage)prevRgbImage.clone();
	mImageView.setImageBitmap( Bitmap.createBitmap(mRgbImage.getData(),
	width, height, Bitmap.Config.ARGB_8888) );
	}
	return true;
	case MENU_SAVE:
	if (mRgbImage != null)
	{
	saveImage();
	}
	return true;
	case MENU_PROC_TIME:
	if (prevRgbImage != null)
	{
	displayProcTime();
	}
	return true;
	}
	return false;
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	switch (keyCode) {
	case KeyEvent.KEYCODE_CAMERA:
	case KeyEvent.KEYCODE_FOCUS:
	startActivity(new Intent("com.wellsmt.ImageDetect.Preview"));
	finish();
	return true;
	}
	return super.onKeyDown(keyCode, event);
	}
	public void showOnlyRed( )
	{
	thresholdColorPixels( 0,255,255 );
	}
	public void showOnlyGreen()
	{
	thresholdColorPixels( 255,0,255 );
	}
	public void showOnlyBlue()
	{
	thresholdColorPixels( 255,255,0 );
	}
	public static void setJpegData(byte[] jpegData)
	{
	Bitmap bitmap = BitmapFactory.decodeByteArray(jpegData, 0, jpegData.length, null);
	mRgbImage = RgbImageAndroid.toRgbImage(bitmap);
	width = bitmap.getWidth();
	height = bitmap.getHeight();
	bitmap.getPixels(mRgbImage.getData(), 0, width, 0, 0, width, height);
	}
	public static void setJpegData(Bitmap temp)
	{
	Bitmap bitmap = temp.copy(Bitmap.Config.ARGB_8888, true);
	mRgbImage = RgbImageAndroid.toRgbImage(bitmap);
	width = bitmap.getWidth();
	height = bitmap.getHeight();
	bitmap.getPixels(mRgbImage.getData(), 0, width, 0, 0, width, height);
	}
	public static void setSecJpegData(Bitmap temp)
	{
	Bitmap bitmap = temp.copy(Bitmap.Config.ARGB_8888, true);
	mSecRgbImage = RgbImageAndroid.toRgbImage(bitmap);
	mSecWidth = bitmap.getWidth();
	mSecHeight = bitmap.getHeight();
	bitmap.getPixels(mSecRgbImage.getData(), 0, mSecWidth, 0, 0, mSecWidth, mSecHeight);
	}
	public void saveImage() {
	ContentValues values = new ContentValues(3);
	values.put(Media.MIME_TYPE, "image/jpeg");
	// Add a new record without the bitmap, but with the values just set.
	// insert() returns the URI of the new record.
	Uri uri = getContentResolver().insert(Media.EXTERNAL_CONTENT_URI, values);
	// Now get a handle to the file for that record, and save the data into it.
	// Here, sourceBitmap is a Bitmap object representing the file to save to the
	database.
	try {
	OutputStream outStream = getContentResolver().openOutputStream(uri);
	Bitmap.createBitmap(mRgbImage.getData(), width, height,
			Bitmap.Config.ARGB_8888).compress(Bitmap.CompressFormat.JPEG, 50, outStream);
			outStream.close();
			Toast.makeText(ModImage.this, "Image Saved...", Toast.LENGTH_LONG).show();
			} catch (Exception e) {
			Toast.makeText(this, "Image Failed to Save...", Toast.LENGTH_LONG).show();
			}
			}
			private void displayProcTime()
			{
			Toast.makeText(this, "(" + width + "x" + height + "), Image Processing Time = " + (end -
			start)*10e-6 + "[sec].", Toast.LENGTH_LONG).show();
			}
			public void thresholdColorPixels( int rthresh,int gthresh, int bthresh ) {
			int[] rgbData = mRgbImage.getData();
			prevRgbImage = (RgbImage) mRgbImage.clone();
			start = (int) System.currentTimeMillis();
			for(int y = 0; y < height; y++)
			{
			int outputOffset = y*width;
			for(int x = 0;x < width; x++)
			{
			int index = outputOffset + x;
			int R = ((rgbData[index]>>16) & 0xff);
			int G = ((rgbData[index]>>8) & 0xff);
			int B = ((rgbData[index]) & 0xff);
			if( R <= rthresh){
			R = 0;
			}
			if( G <= gthresh){
			G = 0;
			}
			if( B <= bthresh){
			B = 0;
			}
			rgbData[index] = 0xff000000 | (R << 16) | (G << 8) | B;
			}
			}
			end = (int) System.currentTimeMillis();
			}
			// converts yuv camera data format to gray scale
			public void greyScale(boolean storePrevious) {
			int[] rgbData = mRgbImage.getData();
			if( storePrevious )
			{
			prevRgbImage = (RgbImage)mRgbImage.clone();
			}
			for(int y = 0; y < height; y++)
			{
			int outputOffset = y*width;
			for(int x = 0;x < width; x++)
			{
			int index = outputOffset + x;
			int R = ((rgbData[index]>>16) & 0xff);
			int G = ((rgbData[index]>>8) & 0xff);
			int B = ((rgbData[index]) & 0xff);
			int grey = (R + G + B)/3;
			rgbData[index] = 0xff000000 | (grey << 16) | (grey << 8) | grey;
			}
			}
			}
			protected void onActivityResult(int requestCode, int resultCode, Intent data)
			{
			super.onActivityResult(requestCode, resultCode, data);
			if (resultCode == RESULT_OK)
			{
			Uri chosenImageUri = data.getData();
			if( chosenImageUri != null)
			{
			try
			{
			InputStream photoStream =
			getContentResolver().openInputStream(chosenImageUri);
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inSampleSize = 4;
			Bitmap bitmap = BitmapFactory.decodeStream(photoStream,
			null, opts);
			if( bitmap != null)
			{
			setSecJpegData(bitmap);
			myProgressDialog =
			ProgressDialog.show(ModImage.this,
			"Calculating...", "Image addition", true, false);
			Thread thread= new Thread(this);
			thread.start();
			switch( secondImage)
			{
			case ADD_CONTEXT:
			break;
			case BLEND_CONTEXT:
			blend();
			break;
			}
			mImageView.setImageBitmap(
			Bitmap.createBitmap(mRgbImage.getData(), width, height, Bitmap.Config.ARGB_8888) );
			}
			photoStream.close();
			}
			catch (Exception e)
			{
			e.printStackTrace();
			}
			}
			}
			}
			public void blend()
			{
			}
			public void additionImage()
			{
			Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
			photoPickerIntent.setType("image/*");
			startActivityForResult(photoPickerIntent, 1);
			}
			public void convolveImage()
			{
			myProgressDialog = ProgressDialog.show(ModImage.this,
			"Calculating...", "Convolve with Averaging Filter", true, false);
			Thread thread= new Thread(this);
			thread.start();
			}
			public void sobelImage()
			{
			myProgressDialog = ProgressDialog.show(ModImage.this,
			"Calculating...", "Sobel", true, false);
			Thread thread= new Thread(this);
			thread.start();
			}
			public void run()
			{
			switch(this.currContextSelect)
			{
			case EDGE_CONTEXT:
			sobel();
			break;
			case CONV_CONTEXT:
			double[]
			template={(1/9.0),(1/9.0),(1/9.0),(1/9.0),(1/9.0),(1/9.0),(1/9.0),(1/9.0),(1/9.0)};
			convolve(template, 3, 3);
			break;
			case ADD_CONTEXT:
			addition();
			break;
			}
			handler.sendEmptyMessage(0);
			}
			private Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
			myProgressDialog.dismiss();
			mImageView.setImageBitmap( Bitmap.createBitmap(mRgbImage.getData(), width,
			height, Bitmap.Config.ARGB_8888) );
			displayProcTime();
			}
			};
			public void sobel()
			{
			prevRgbImage = (RgbImage)mRgbImage.clone();
			start = (int) System.currentTimeMillis();
			float[] template={-1,0,1,-2,0,2,-1,0,1};
			int templateSize=3;
			int[] rgbData = mRgbImage.getData();
			int[] total = new int[width*height];
			int sumY=0;
			int sumX=0;
			int max=0;
			for( int n = 0; n<1; n++)
			{
			for(int x=(templateSize-1)/2; x<width-(templateSize+1)/2;x++)
			{
			for(int y=(templateSize-1)/2; y<height-(templateSize+1)/2;y++)
			{
			sumY=0;
			for(int x1=0;x1<templateSize;x1++)
			{
			for(int y1=0;y1<templateSize;y1++)
			{
			int x2 = (x-(templateSize-1)/2+x1);
			int y2 = (y-(templateSize-1)/2+y1);
			float value = (rgbData[y2*width+x2] & 0xff) *
			(template[y1*templateSize+x1]);
			sumY += value;
			}
			}
			sumX = 0;
			for(int x1=0;x1<templateSize;x1++)
			{
			for(int y1=0;y1<templateSize;y1++)
			{
			int x2 = (x-(templateSize-1)/2+x1);
			int y2 = (y-(templateSize-1)/2+y1);
			float value = (rgbData[y2*width+x2] & 0xff) *
			(template[x1*templateSize+y1]);
			sumX += value;
			}
			}
			total[y*width+x] = (int)Math.sqrt(sumX*sumX+sumY*sumY);
			if(max < total[y*width+x])
			max = total[y*width+x];
			}
			}
			float ratio=(float)max/255;
			for(int x=0; x<width;x++)
			{
			for(int y=0; y<height;y++)
			{
			sumX = (int)(total[y*width+x]/ratio);
			total[y*width+x] = 0xff000000 | ((int)sumX << 16 | (int)sumX << 8 |
			(int)sumX);
			}
			}
			}
			System.arraycopy(
			total,
			0,
			rgbData,
			0,
			width*height);
			end = (int) System.currentTimeMillis();
			}
			public void convolve(double[] mat, int rows, int cols)
			{
			if((rows % 2) == 0 || (cols % 2) == 0)
			{
			}
			else
			{
			start = (int) System.currentTimeMillis();
			int[] rgbData = mRgbImage.getData();
			int[] conv = new int[width*height];
			int sumR = 0;
			int sumG = 0;
			int sumB = 0;
			prevRgbImage = (RgbImage)mRgbImage.clone();
			for(int x=(cols-1)/2; x<width-(cols+1)/2;x++)
			{
			for(int y=(rows-1)/2; y<height-(rows+1)/2;y++)
			{
			sumR=0;
			sumG=0;
			sumB=0;
			for(int x1=0;x1<cols;x1++)
			{
			for(int y1=0;y1<rows;y1++)
			{
			int x2 = (x-(cols-1)/2+x1);
			int y2 = (y-(rows-1)/2+y1);
			int R = ((rgbData[y2*width+x2]>>16) & 0xff);
			int G = ((rgbData[y2*width+x2]>>8) & 0xff);
			int B = ((rgbData[y2*width+x2]) & 0xff);
			sumR += R * (mat[y1*cols+x1]);
			sumG += G * (mat[y1*cols+x1]);
			sumB += B * (mat[y1*cols+x1]);
			}
			}
			conv[y*width+x] = 0xff000000 | ((int)sumR << 16 | (int)sumG << 8 |
			(int)sumB);
			}
			}
			System.arraycopy(
			conv,
			0,
			rgbData,
			0,
			width*height);
			end = (int) System.currentTimeMillis();
			}
			}
			public void addition()
			{
			secondImage = ADD_CONTEXT;
			prevRgbImage = (RgbImage)mRgbImage.clone();
			if( mSecRgbImage != null && mRgbImage != null )
			{
			start = (int) System.currentTimeMillis();
			int largerWidth = 0;
			int largerHeight = 0;
			if( mSecWidth < width)
			{
			largerWidth = width;
			}
			else
			{
			largerWidth = mSecWidth;
			}
			if( mSecHeight < height)
			{
			largerHeight = height;
			}
			else
			{
			largerHeight = mSecHeight;
			}
			int[] rgbData = mRgbImage.getData();
			int[] mSecRgbData = mSecRgbImage.getData();
			int[] total = new int[largerWidth*largerHeight];
			for(int y = 0, y2 = 0; y < height || y2 < mSecHeight; y++, y2++)
			{
			int rgbOutputOffset = y*width;
			int secRgbOutputOffset = y2*mSecWidth;
			for(int x = 0,x2 = 0;x < width || x2 < mSecWidth; x++,x2++)
			{
			int index = rgbOutputOffset + x;
			int secIndex = secRgbOutputOffset + x2;
			int R,G,B,sR,sG,sB;
			if( x < width && y < height)
			{
			R = ((rgbData[index]>>16) & 0xff);
			G = ((rgbData[index]>>8) & 0xff);
			B = ((rgbData[index]) & 0xff);
			}
			else
			{
			R = ((mSecRgbData[secIndex]>>16) & 0xff);
			G = ((mSecRgbData[secIndex]>>8) & 0xff);
			B = ((mSecRgbData[secIndex]) & 0xff);
			}
			if( x2 < mSecWidth && y2 < mSecHeight)
			{
			sR = ((mSecRgbData[secIndex]>>16) & 0xff);
			sG = ((mSecRgbData[secIndex]>>8) & 0xff);
			sB = ((mSecRgbData[secIndex]) & 0xff);
			}
			else
			{
			sR = ((rgbData[index]>>16) & 0xff);
			sG = ((rgbData[index]>>8) & 0xff);
			sB = ((rgbData[index]) & 0xff);
			}
			double mR,mG,mB;
			mR = (R + sR)/2.0;
			mG = (G + sG)/2.0;
			mB = (B + sB)/2.0;
			total[index] = 0xff000000 | ((int)(mR) << 16) | ((int)(mG) << 8) |
			(int)(mB);
			}
			}
			System.arraycopy(
			total,
			0,
			rgbData,
			0,
			width*height);
			end = (int) System.currentTimeMillis();
			}
			}
			}
}