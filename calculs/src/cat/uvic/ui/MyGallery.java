package cat.uvic.ui;

import java.io.InputStream;
import jjil.core.Error;
import cat.uvic.android.ResultBitmap;
import cat.uvic.calculs.Transformation;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.*;

/**
 * Take photo from the gallery
 * 
 * @author ANNA
 */
public class MyGallery extends Activity {
	private boolean debug = false;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		debug = this.getIntent().getBooleanExtra("DEBUG_MODE", false);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
		photoPickerIntent.setType("image/*");
		startActivityForResult(photoPickerIntent, 1);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Transformation.destroy();
		if (resultCode == RESULT_OK) {
			Uri chosenImageUri = data.getData();
			if (chosenImageUri != null) {
				try {
					InputStream photoStream = getContentResolver()
							.openInputStream(chosenImageUri);
					BitmapFactory.Options opts = new BitmapFactory.Options();
					opts.inSampleSize = 6;
					Bitmap mBitmap = BitmapFactory.decodeStream(photoStream,
							null, opts);
					// if (mBitmap.getWidth()>600 ||mBitmap.getHeight()>600){
					// int factor =Math.max(mBitmap.getWidth()/300,
					// mBitmap.getHeight()/300);
					// mBitmap =mBitmap.createScaledBitmap(mBitmap,
					// mBitmap.getWidth()/factor,mBitmap.getHeight()/factor,
					// false);
					// System.out.print("Factor: "+factor);
					// }
					if (debug) {
						Transformation trans = Transformation
								.getInstance(mBitmap);
						try {
							setContentView(R.layout.image);
							final MyView mm = (MyView) findViewById(R.id.myView);
							mm.setRect(trans.findBill(trans.getImg(), true));
							mm.setBtm(trans.getCurrentImg());
							mm.setResult(trans.getRecognition() + "");
							mm.invalidate();
							final Button buttonNext = (Button) findViewById(R.id.ButtonNext);
							buttonNext
									.setOnClickListener(new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											try {
												Intent in = new Intent(
														MyGallery.this,
														cat.uvic.ui.NextActivity.class);
												in.putExtra("STATE", (long) 1);
												startActivity(in);
											} catch (Exception ex) {
												ex.printStackTrace();
											}
										}
									});
						} catch (Error e) {
							e.printStackTrace();
						}
					} else {
						Transformation trans = Transformation
								.getInstance(mBitmap);
						ResultBitmap res = new ResultBitmap();
						try {
							res = trans.transform();
							setContentView(R.layout.image);
							final MyView mm = (MyView) findViewById(R.id.myView);
							mm.setRect(res.getRegion());
							mm.setBtm(res.getBitmap());
							mm.setResult(trans.getRecognition() + "");
							final Button buttonNext = (Button) findViewById(R.id.ButtonNext);
							final TextView textViewLog = (TextView) findViewById(R.id.TextViewLog);
							buttonNext.setText(" FINISH");
							textViewLog.setTextColor(Color.WHITE);
							String result;
							if (trans.getRecognition() > 0)
								result = "RESULT: " + trans.getRecognition()
										+ " €";
							else
								result = "Can'trecognize the denomination.";
							textViewLog.setText(" " + result);
							buttonNext
									.setOnClickListener(new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											try {
												Intent in = new Intent(
														MyGallery.this,
														cat.uvic.ui.MainActivity.class);
												startActivity(in);
												finish();
											} catch (Exception ex) {
												ex.printStackTrace();
											}
										}
									});
							mm.invalidate();
						} catch (Exception es) {
						} catch (Error e) {
							e.printStackTrace();
						}
					}
					photoStream.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}