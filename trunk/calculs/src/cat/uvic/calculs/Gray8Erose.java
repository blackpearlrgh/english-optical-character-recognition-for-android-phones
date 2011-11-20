package cat.uvic.calculs;

import jjil.core.Gray8Image;
import jjil.core.Image;
import jjil.core.PipelineStage;

/**
 * erode a Gray thresholded image
 * 
 * @author ANNA
 * 
 */
public class Gray8Erose extends PipelineStage {
	int many;

	/**
	 * @param _many
	 *            how many times Erosion should repeat
	 */
	public Gray8Erose(int _many) {
		super();
		this.many = _many;
	}

	/** Creates a new instance of Gray8Erose */
	public Gray8Erose() {
		super();
		this.many = 0;
	}

	@Override
	public void push(Image img) throws Error {
		if (!(img instanceof Gray8Image)) {
			throw new IllegalArgumentException(img.toString()
					+ " should be a Gray8Image, but isn't");
		}
		// long start = System.currentTimeMillis();
		Gray8Image input = (Gray8Image) img;
		byte[] image = input.getData();
		int h = input.getHeight();
		int w = input.getWidth();
		int imgW = img.getWidth();
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				if (image[i * imgW + j] == Byte.MIN_VALUE) {
					if (i > 0 && image[(i - 1) * imgW + j] == Byte.MAX_VALUE)
						image[(i - 1) * imgW + j] = Byte.MIN_VALUE;
					if (j > 0 && image[i * imgW + j - 1] == Byte.MAX_VALUE)
						image[i * imgW + j - 1] = Byte.MIN_VALUE;
				}
			}
		}
		// long stop = System.currentTimeMillis();
		// System.out.println("Erose: "+(stop-start));
		super.setOutput(new Gray8Image(input.getWidth(), input.getHeight(),image));
	}
}
