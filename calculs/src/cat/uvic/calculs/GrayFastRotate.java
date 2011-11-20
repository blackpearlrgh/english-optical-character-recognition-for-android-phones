package cat.uvic.calculs;

import jjil.algorithm.ErrorCodes;
import jjil.core.Error;
import jjil.core.Gray8Image;
import jjil.core.Image;
import jjil.core.PipelineStage;
import jjil.core.Rect;

/**
 * rotate a Gray thresholded image
 * 
 * @author ANNA
 */
public class GrayFastRotate extends PipelineStage {
	double theta;

	/**
	 * @param theta
	 *            arc value
	 */
	public GrayFastRotate(double theta) {
		this.theta = theta;
	}

	@Override
	public void push(Image img) throws Error {
		if (!(img instanceof Gray8Image)) {
			throw new Error(Error.PACKAGE.ALGORITHM,
					ErrorCodes.IMAGE_NOT_GRAY8IMAGE, img.toString(), null, null);
		}
		int x = 0;
		int y = 0;
		int x0 = img.getWidth() / 2;
		int y0 = img.getHeight() / 2;
		double sint = Math.sin(theta);
		double cost = Math.cos(theta);
		// Gray8Image result = new
		// Gray8Image((int)Math.sqrt(img.getWidth()*img.getWidth()+img.getHeight()*img.getHeight())
		// ,
		// (int)Math.sqrt(img.getWidth()*img.getWidth()+img.getHeight()*img.getHeight()));
		Gray8Image result = new Gray8Image((int) Math.floor(img.getWidth()
				* Math.abs(cost) + img.getHeight() * Math.abs(sint)),
				(int) Math.floor(img.getHeight() * Math.abs(cost)
						+ img.getWidth() * Math.abs(sint)));
		byte[] data = ((Gray8Image) img).getData();
		result.fill(new Rect(0, 0, result.getWidth(), result.getHeight()),
				Byte.MIN_VALUE);
		byte[] out = result.getData();
		// int rotx=(result.getWidth()-img.getWidth())/2;
		// int roty=(result.getHeight()-img.getHeight())/2;
		int wi = img.getWidth();
		int hi = img.getHeight();
		int wr = result.getWidth();
		for (int i = 0; i < wi; i++) {
			for (int j = 0; j < hi; j++) {
				if (data[i + j * wi] == Byte.MAX_VALUE) {
					x = (int) (((cost * i) - (sint * j) + (x0 * (1 - cost) + y0
							* sint)));
					y = (int) (((sint * i) + (cost * j) + (y0 * (1 - cost) - x0
							* sint)));
					if ((int) ((x) + (y) * wr) >= 0
							&& (int) ((x) + (y) * wr) < out.length)
						// out[(int)((x+rotx)+(y+roty)*result.getWidth())]=data[i+j*wi];
						out[(int) ((x) + (y) * wr)] = data[i + j * wi];
				}
			}
		}
		super.setOutput(result);
	}
}