package cat.uvic.calculs;

import android.graphics.Rect;

/**
 * class responsible for looking for interesting region based on histograms
 * (horizontal and vertical)
 * 
 * @author ANNA
 */
public class FindRegion {
	private boolean margin;
	private int perV = 10;
	private int perH = 4;

	/**
	 * @param marg
	 *            margin value
	 */
	public FindRegion(boolean marg) {
		super();
		this.margin = marg;
	}

	/**
	 * @param marg
	 *            global margin value
	 * @param pV
	 *            border value for vertical histogram
	 * @param pH
	 *            border value for horizontal histogram
	 */
	public FindRegion(boolean marg, int pV, int pH) {
		super();
		this.perH = pH;
		this.perV = pV;
		this.margin = marg;
	}

	/*
	 * Rectangle: (x,y)|------------------| + Width, Height | | | | | |
	 * |------------------|
	 */
	/**
	 * @param histH
	 *            horizontal histogram
	 * @param histV
	 *            vertical histogram
	 * @return rectangle of interesting region
	 */
	public Rect findRegionOfInteres(int[] histH, int[] histV) {
		int maxH = 0;
		int w = 0, h = 0;
		for (int i = 0; i < histH.length; i++) {
			maxH = Math.max(histH[i], maxH);
		}
		int maxV = 0;
		for (int i = 0; i < histV.length; i++) {
			maxV = Math.max(histV[i], maxV);
		}
		int x = maxH;
		int y = maxV;
		for (int i = 0; i < histH.length; i++) {
			if ((histH[i] > (maxH / perH))) {
				y = Math.min(y, i);
				w = Math.max(w, i);
			}
		}
		for (int i = 0; i < histV.length; i++) {
			if ((histV[i] > (maxV / perV))) {
				x = Math.min(x, i);
				h = Math.max(h, i);
			}
		}
		if (this.margin) {
			int marg = (int) (Math.max(histH.length, histV.length) * 0.1);
			// System.out.println("Margin:"+marg);
			int xx = (x > marg ? x - marg : 0);
			int yy = (y > marg ? y - marg : 0);
			int hh = (h - xx + marg <= histV.length - xx ? h - xx + marg
					: histV.length - xx);
			int ww = (w - yy + marg <= histH.length - yy ? w - yy + marg
					: histH.length - yy);
			return new Rect(xx, yy, xx + hh, yy + ww);
		} else
			return new Rect(x, y, h, w);
	}
}
