package cat.uvic.ui;

import java.io.IOException;
import jjil.core.Error;
import cat.uvic.android.ResultBitmap;
import cat.uvic.calculs.R;
import cat.uvic.calculs.Transformation;
import cat.uvic.calculs.R.layout;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

/**
 * Result activity
 * 
 * @author ANNA
 */
public class FinalActivity extends Activity {
	long state = 0;
	String result;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.image);
		final MyView mm = (MyView) findViewById(R.id.myView);
		final Button buttonNext = (Button) findViewById(R.id.ButtonNext);
		final TextView textViewLog = (TextView) findViewById(R.id.TextViewLog);
		buttonNext.setText(" FINISH ");
		textViewLog.setTextColor(Color.WHITE);
		buttonNext.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					Intent in = new Intent(FinalActivity.this,
							cat.uvic.ui.MainActivity.class);
					startActivity(in);
					finish();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		Transformation trans;
		try {
			trans = Transformation.getInstance(null);
			ResultBitmap res = new ResultBitmap();
			trans.setFinalResult();
			res.setRegion(trans.getRegion());
			res.setBitmap(trans.getCurrentImg());
			mm.setRect(res.getRegion());
			mm.setBtm(res.getBitmap());
			mm.setResult(trans.getRecognition() + "");
			if (trans.getRecognition() > 0)
				result = "RESULT: " + trans.getRecognition() + " €";
			else
				result = "Can't recognize the denomination.";
			textViewLog.setText(" " + result);
			mm.invalidate();
			Transformation.destroy();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Error e) {
			e.printStackTrace();
		}
	}
}