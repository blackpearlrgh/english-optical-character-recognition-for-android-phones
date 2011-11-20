package cat.uvic.ui;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import jjil.core.Error;
import cat.uvic.android.ResultBitmap;
import cat.uvic.calculs.R;
import cat.uvic.calculs.Transformation;
import cat.uvic.calculs.R.layout;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
/**
* Main class that represents the main menu with all Buttons
* listeners implemented
* @author ANNA
*/
public class MainActivity extends Activity {
/** Called when the activity is first created. */
CheckBox debug;
static final String TMP_FILE = "/sdcard/TempPicture.jpg";
@Override
public void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(R.layout.home);
final Button buttonGallery = (Button)
findViewById(R.id.ButtonGallery);
final Button buttonCamera = (Button) findViewById(R.id.ButtonCamera);
debug = (CheckBox) findViewById(R.id.CheckBoxDebug);
final Button buttonExit = (Button) findViewById(R.id.ButtonExit);
final Button buttonAbout = (Button) findViewById(R.id.ButtonAbout);
Transformation.destroy();
buttonGallery.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
try {
Intent in = new
Intent(MainActivity.this,cat.uvic.ui.MyGallery.class);
in.putExtra("DEBUG_MODE", debug.isChecked());
startActivity(in);
finish();
} catch (Exception ex) {
ex.printStackTrace();
}
}
});
buttonAbout.setOnClickListener(new View.OnClickListener()
{
@Override
public void onClick(View v) {
AlertDialog alertDialog;
alertDialog = new
AlertDialog.Builder(MainActivity.this)
.create();
alertDialog.setTitle("About...");
alertDialog
.setMessage("Program created as a part of the diploma project \n"
+ "of 'Master en Tecnologies Aplicades a la Informacio' at the University of Vic. \n"
+ "Author: Anna Sobolewska");
alertDialog.setButton("OK",
new
DialogInterface.OnClickListener() {
@Override
public void
onClick(DialogInterface dialog,
int which) {
dialog.dismiss();
}
});
alertDialog.show();
}
});
buttonExit.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
finish();
System.exit(0);
}
});
buttonCamera.setOnClickListener(new View.OnClickListener()
{
@Override
public void onClick(View v) {
Intent intent = new
Intent("android.media.action.IMAGE_CAPTURE");
Uri uri = Uri.fromFile(new File(TMP_FILE));
intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
startActivityForResult(intent, 0);
}
});
}
@Override
protected void onActivityResult(int requestCode, int resultCode,
Intent data) {
if (requestCode == 0 && resultCode == Activity.RESULT_OK)
{
Bitmap x = null;
try {
File f = new File(TMP_FILE);
try {
Uri u = Uri.fromFile(f);
InputStream photoStream =
getContentResolver()
.openInputStream(u);
BitmapFactory.Options opts = new
BitmapFactory.Options();
opts.inSampleSize = 4;
x =
BitmapFactory.decodeStream(photoStream, null, opts);
f.delete();
} catch (FileNotFoundException e) {
e.printStackTrace();
}
if (debug.isChecked()) {
	Transformation trans =
	Transformation.getInstance(x);
	try {
	setContentView(R.layout.image);
	final MyView mm = (MyView)
	findViewById(R.id.myView);
	mm.setRect(trans.findBill(trans.getImg(), true));
	mm.setBtm(trans.getCurrentImg());
	mm.setResult(trans.getRecognition() + "");
	mm.setHorizontalScrollBarEnabled(true);
	mm.setVerticalScrollBarEnabled(true);
	mm.setScrollContainer(true);
	final Button buttonNext = (Button)
	findViewById(R.id.ButtonNext);
	buttonNext.setOnClickListener(new
	View.OnClickListener() {
	@Override
	public void
	onClick(View v) {
	try {
	Intent in = new Intent(MainActivity.this,cat.uvic.ui.NextActivity.class);
	in.putExtra("STATE", (long) 1);
	startActivity(in);
	finish();
	} catch
	(Exception ex) {
	ex.printStackTrace();
	}
	}
	});
	} catch (Error e) {
	e.printStackTrace();
	}
	} else {
	Transformation trans =
	Transformation.getInstance(x);
	ResultBitmap res = new ResultBitmap();
	try {
	res = trans.transform();
	setContentView(R.layout.image);
	final MyView mm = (MyView)
	findViewById(R.id.myView);
	mm.setRect(res.getRegion());
	mm.setBtm(res.getBitmap());
	mm.setResult(trans.getRecognition() + "");
	final Button buttonNext = (Button)
	findViewById(R.id.ButtonNext);
	final TextView textViewLog = (TextView) findViewById(R.id.TextViewLog);
	buttonNext.setText(" FINISH ");
	textViewLog.setTextColor(Color.WHITE);
	String result;
	if (trans.getRecognition() > 0)
	result = "RESULT: " +
	trans.getRecognition() + " €";
	else
	result = "Can't recognize the denomination.";
	textViewLog.setText(" " +
			result);
			buttonNext
			.setOnClickListener(new
			View.OnClickListener() {
			@Override
			public void
			onClick(View v) {
			try {
			Intent in = new Intent(MainActivity.this,cat.uvic.ui.MainActivity.class);
			startActivity(in);
			finish();
			} catch
			(Exception ex) {
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
			} catch (Exception e) {
			e.printStackTrace();
			}
			}
			}
}