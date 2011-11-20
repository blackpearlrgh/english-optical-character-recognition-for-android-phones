package cat.uvic.calculs;

import jjil.core.Error;
import jjil.core.Gray8Image;
import jjil.core.Image;
import jjil.core.PipelineStage;

/**
 * dilate a Gray thresholded image
 * <p>
 * 
 * @author ANNA
 */
public class Gray8Dilate extends PipelineStage {
	int many;

	/**
	 * Creates a new instance of Gray8Dilate
	 * 
	 * @param _many
	 *            how many times Dilation should repeat
	 */
	public Gray8Dilate(int _many) {
		super();
		this.many = _many;
	}

	/** Creates a new instance of Gray8Dilate */
	public Gray8Dilate() {
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
				if (image[i * imgW + j] == Byte.MAX_VALUE) {
					if (i > 0 && image[(i - 1) * imgW + j] == Byte.MIN_VALUE)
						image[(i - 1) * imgW + j] = Byte.MAX_VALUE;
					if (j > 0 && image[i * imgW + j - 1] == Byte.MIN_VALUE)
						image[i * imgW + j - 1] = Byte.MAX_VALUE;
				}
			}
		}
		// long stop = System.currentTimeMillis();
		// System.out.println("Dilate: "+(stop-start));
		super.setOutput(new Gray8Image(input.getWidth(), input.getHeight(),image));
	}
}