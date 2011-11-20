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
 * Activity responsible for hologram recognition
 * 
 * @author ANNA
 */
public class NextActivity3 extends Activity {
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
						Intent in = new Intent(NextActivity3.this,
								cat.uvic.ui.FinalActivity.class);
						startActivity(in);
						finish();
					} else {
						Intent in = new Intent(NextActivity3.this,
								cat.uvic.ui.NextActivity4.class);
						in.putExtra("STATE", state);
						startActivity(in);
						finish();
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		if (state == 3) {
			this.state = 4;
			Transformation trans;
			try {
				trans = Transformation.getInstance(null);
				if (!trans.isWrong()) {
					ResultBitmap res = new ResultBitmap();
					if (trans.recognise50()) {
						state = 0;
						trans.setRecognition(50);
					}
					res.setRegion(trans.getRegion());
					System.out.println("L: " + trans.getRegion().left + " R: "
							+ trans.getRegion().right + "B: "
							+ trans.getRegion().bottom + " T: "
							+ trans.getRegion().top + "W: "
							+ trans.getRegion().width() + " H: "
							+ trans.getRegion().height());
					res.setBitmap(trans.getCurrentImg());
					mm.setRect(res.getRegion());
					mm.setBtm(res.getBitmap());
					mm.setResult(trans.getRecognition() + "");
				} else {
					this.state = 0;
					mm.setResult("0");
				}
				mm.invalidate();
			} catch (IOException e) {
				state = 0;
				e.printStackTrace();
			} catch (Error e) {
				state = 0;
				e.printStackTrace();
			}
		}
	}
}