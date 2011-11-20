package cat.uvic.calculs;

import jjil.core.Gray8Image;

/**
 * horizontal histogram for Gray image.
 * 
 * @author ANNA
 */
public class Gray8HistHorizontal {
	/**
	 * Creates a new instance of Gray8HistHorizontal *
	 */
	public Gray8HistHorizontal() {
		super();
	}

	/**
	 * @param image
	 * @return array of integer that represents sequence of histogram values
	 */
	public static int[] computeHistogram(Gray8Image image) {
		int[] result = new int[image.getWidth()];
		// long start = System.currentTimeMillis();
		int h = image.getHeight();
		int w = image.getWidth();
		for (int i = 0; i < w; i++) {
			result[i] = 0;
		}
		byte[] data = image.getData();
		for (int m = 0; m < w; m++) {
			for (int j = 0; j < h; j++) {
				result[m] = result[m] + data[j * w + m] - Byte.MIN_VALUE;
			}
		}
		// long stop = System.currentTimeMillis();
		// System.out.println("Gray8HistHorizontal: "+(stopstart));
		return result;
	}
}
