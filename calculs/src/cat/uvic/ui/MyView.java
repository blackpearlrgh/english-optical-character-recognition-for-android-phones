package cat.uvic.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * Customized View that show the image with drawn region of interes.
 * 
 * @author ANNA
 */
public class MyView extends View {
	Bitmap btm;
	Rect rect;
	String result;

	/**
	 * @return result image Bitmap
	 */
	public Bitmap getBtm() {
		return btm;
	}

	/**
	 * @param btm
	 */
	public void setBtm(Bitmap btm) {
		this.btm = btm;
	}

	/**
	 * @return region of analysis
	 */
	public Rect getRect() {
		return rect;
	}

	/**
	 * @param rect
	 */
	public void setRect(Rect rect) {
		this.rect = rect;
	}

	/**
	 * @return result of recognition
	 */
	public String getResult() {
		return result;
	}

	/**
	 * @param result
	 */
	public void setResult(String result) {
		this.result = result;
	}

	/**
	 * @param context
	 */
	public MyView(Context context) {
		super(context);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public MyView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * @param context
	 *            current Context
	 * @param bitmap
	 *            * Image bitmap
	 * @param region
	 *            Rectangle region
	 * @param string
	 *            result string Constructor of My View
	 */
	public MyView(Context context, Bitmap bitmap, Rect region, String string) {
		super(context);
		this.btm = bitmap;
		this.rect = region;
		result = string;
		this.setHorizontalScrollBarEnabled(true);
		this.setVerticalScrollBarEnabled(true);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawBitmap(btm, 0, 0, null);
		Paint p = new Paint();
		p.setColor(Color.MAGENTA);
		canvas.drawBitmap(btm, new Matrix(), p);
		if (rect != null && rect.width() > 0 && rect.height() > 0) {
			canvas.drawLine(rect.left, rect.top, rect.left + rect.width(),
					rect.top, p);
			canvas.drawLine(rect.left, rect.top, rect.left,
					rect.top + rect.height(), p);
			canvas.drawLine(rect.left, rect.top + rect.height(), rect.left
					+ rect.width(), rect.top + rect.height(), p);
			canvas.drawLine(rect.left + rect.width(), rect.top, rect.left
					+ rect.width(), rect.top + rect.height(), p);
		}
		canvas.save();
		invalidate();
	}
}