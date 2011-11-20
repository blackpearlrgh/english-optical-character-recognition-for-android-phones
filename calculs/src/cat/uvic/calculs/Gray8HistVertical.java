package cat.uvic.calculs;

import jjil.core.Gray8Image;

/**
 * vertical histogram for Gray image.
 * 
 * @author ANNA
 */
public class Gray8HistVertical {
	/**
	 * @param image
	 * @return array of integer that represents sequence of histogram values
	 */
	public static int[] computeHistogram(Gray8Image image) {
		int[] result = new int[image.getHeight()];
		int h = image.getHeight();
		int w = image.getWidth();
		for (int i = 0; i < h; i++) {
			result[i] = 0;
		}
		byte[] data = image.getData();
		for (int m = 0; m < h; m++) {
			for (int j = 0; j < w; j++) {
				result[m] = result[m] + data[m * w + j] - Byte.MIN_VALUE;
			}
		}
		return result;
	}
}