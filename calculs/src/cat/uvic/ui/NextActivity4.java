package cat.uvic.ui;

import java.io.IOException;
import jjil.core.Error;
import cat.uvic.android.ResultBitmap;
import cat.uvic.calculs.Transformation;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

/**
 * Activity responsible for denomination analysis
 * 
 * @author ANNA
 */
public class NextActivity4 extends Activity {
	long state = 0;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		state = this.getIntent().getLongExtra("STATE", 0);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.image);
		final MyView mm = (MyView) findViewById(R.id.myView);
		final Button buttonNext = (Button) findViewById(R.id.ButtonNext);
		buttonNext.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					if (state == 0) {
						Intent in = new Intent(NextActivity4.this,
								cat.uvic.ui.FinalActivity.class);
						startActivity(in);
						finish();
					} else {
						Intent in = new Intent(NextActivity4.this,
								cat.uvic.ui.NextActivity5.class);
						in.putExtra("STATE", state);
						startActivity(in);
						finish();
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		if (state == 4) {
			this.state = 5;
			Transformation trans;
			try {
				trans = Transformation.getInstance(null);
				ResultBitmap res = new ResultBitmap();
				trans.analiseNumber();
				res.setRegion(trans.getRegion());
				res.setBitmap(trans.getCurrentImg());
				mm.setRect(res.getRegion());
				mm.setBtm(res.getBitmap());
				mm.setResult(trans.getRecognition() + "");
				mm.invalidate();
			} catch (IOException e) {
				state = 0;
				e.printStackTrace();
			} catch (Error e) {
				state = 0;
				e.printStackTrace();
			}
		} else {
			state = 0;
		}
	}
}