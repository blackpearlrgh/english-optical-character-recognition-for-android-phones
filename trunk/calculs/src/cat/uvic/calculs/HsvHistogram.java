package cat.uvic.calculs;

import java.io.IOException;

import cat.uvic.android.ImageException;

import android.graphics.Color;
import jjil.core.Gray8Image;
import jjil.core.RgbImage;
import jjil.core.RgbVal;

/**
 * colour histogram for an image in HSV colorspace.
 * 
 * @author ANNA
 */
public class HsvHistogram {
	/**
*
*/
	public static String H_HIST = "H";
	/**
*
*/
	public static String V_HIST = "V";
	/**
*
*/
	public static String S_HIST = "S";
	/**
*
*/
	public int[] colors = { Color.rgb(255, 0, 0), Color.rgb(255, 127, 0),
			Color.rgb(255, 255, 0), Color.rgb(127, 255, 0),
			Color.rgb(0, 255, 0), Color.rgb(0, 255, 127),
			Color.rgb(0, 255, 255), Color.rgb(0, 127, 255),
			Color.rgb(0, 0, 255), Color.rgb(127, 0, 255),
			Color.rgb(255, 0, 255), Color.rgb(255, 0, 127) };
	private String tipus;
	private String log = "";

	/**
	 * @return log (for debug)
	 */
	public String getLog() {
		return log;
	}

	/**
	 * @param line
	 */
	public void setLog(String line) {
		this.log = line;
	}

	/**
	 * @return tipus
	 */
	public String getTipus() {
		return tipus;
	}

	/**
	 * @param tipus
	 */
	public void setTipus(String tipus) {
		this.tipus = tipus;
	}

	/**
	 * @param tipus
	 */
	public HsvHistogram(String tipus) {
		super();
		this.tipus = tipus;
	}

	/**
*
*/
	public HsvHistogram() {
		super();
		this.tipus = "H";
	}

	/**
	 * @param image
	 * @return histogram
	 */
	public int[] computeHistogram(RgbImage image) {
		int[] result = new int[12];
		int[] hsvData = ((RgbImage) image).getData();
		int step = (2 * Byte.MAX_VALUE + 12) / 12;
		int total = 0;
		int max = 0;
		this.log = "";
		int min = 256;
		for (int i = 0; i < 12; i++) {
			result[i] = 0;
		}
		int pp = 0;
		int h = image.getHeight();
		int w = image.getWidth();
		for (int m = 0; m < h * w; m++) {
			if (this.tipus.equals("H")) {
				pp = RgbVal.getR(hsvData[m]) - Byte.MIN_VALUE;
			} else if (this.tipus.equals("S")) {
				pp = RgbVal.getG(hsvData[m]) - Byte.MIN_VALUE;
			} else if (this.tipus.equals("V")) {
				pp = RgbVal.getB(hsvData[m]) - Byte.MIN_VALUE;
			}
			result[pp / step] = result[pp / step] + pp;
			if (pp > max)
				max = pp;
			if (pp < min)
				min = pp;
			total = total + pp;
		}
		for (int i = 0; i < 12; i++) {
			result[i] = result[i] * 100 / total;
			this.log = this.log + result[i] + ";";
		}
		return result;
	}

	/**
	 * @param image
	 * @param gray
	 * @return histogram
	 * @throws Exception 
	 * @throws ImageException
	 */
	public int[] computeHistogram(RgbImage image, Gray8Image gray)
			throws ImageException {
		if (gray.getWidth() * gray.getHeight() < image.getWidth()
				* image.getHeight())
			throw new ImageException("Wrong map");
		int[] result = new int[12];
		byte[] map = gray.getData();
		int[] hsvData = ((RgbImage) image).getData();
		int step = (2 * Byte.MAX_VALUE + 12) / 12;
		int total = 0;
		int max = 0;
		this.log = "";
		int min = 256;
		for (int i = 0; i < 12; i++) {
			result[i] = 0;
		}
		int pp = 0;
		int h = image.getHeight();
		int w = image.getWidth();
		for (int m = 0; m < h * w; m++) {
			if (this.tipus.equals("H") && map[m] == Byte.MIN_VALUE) {
				pp = RgbVal.getR(hsvData[m]) - Byte.MIN_VALUE;
			} else if (this.tipus.equals("S") && map[m] == Byte.MIN_VALUE) {
				pp = RgbVal.getG(hsvData[m]) - Byte.MIN_VALUE;
			} else if (this.tipus.equals("V") && map[m] == Byte.MIN_VALUE) {
				pp = RgbVal.getB(hsvData[m]) - Byte.MIN_VALUE;
			}
			result[pp / step] = result[pp / step] + pp;
			if (pp > max)
				max = pp;
			if (pp < min)
				min = pp;
			total = total + pp;
		}
		for (int i = 0; i < 12; i++) {
			if (total > 0) {
				result[i] = result[i] * 100 / total;
				this.log = this.log + result[i] + ";";
			}
		}
		return result;
	}

	/**
	 * @param hist
	 * @return sum of histogram values
	 */
	public long sumHistogram(int[] hist) {
		long sum = 0;
		for (int i = 0; i < hist.length; i++) {
			sum = sum + hist[i];
		}
		return sum;
	}
}