package cat.uvic.calculs;

import jjil.algorithm.ErrorCodes;
import jjil.core.Error;
import jjil.core.Gray8Image;
import jjil.core.Image;
import jjil.core.PipelineStage;
import jjil.core.Rect;

/**
 * rotate a Gray image
 * 
 * @author ANNA
 */
public class GrayRotate extends PipelineStage {
	double theta;

	/**
	 * @param theta
	 *            arc value
	 * @throws Error
	 */
	public GrayRotate(double theta) throws jjil.core.Error {
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
		if (theta > 2 * Math.PI)
			theta -= 2 * Math.PI;
		Gray8Image result = (Gray8Image) img;
		double sint = Math.sin(theta);
		double cost = Math.cos(theta);
		byte[] data = ((Gray8Image) img).getData();
		if (theta == Math.PI) {
			result = new Gray8Image(img.getWidth(), img.getHeight());
			byte[] out = result.getData();
			for (int i = 0; i < result.getWidth(); i++) {
				for (int j = 0; j < result.getHeight(); j++) {
					x = result.getWidth() - i - 1;
					y = result.getHeight() - j - 1;
					out[(i) + (j) * result.getWidth()] = data[x + y
							* img.getWidth()];
				}
			}
		} else {
			result = new Gray8Image((int) Math.floor(img.getWidth()
					* Math.abs(cost) + img.getHeight() * Math.abs(sint)),
					(int) Math.floor(img.getHeight() * Math.abs(cost)
							+ img.getWidth() * Math.abs(sint)));
			result.fill(new Rect(0, 0, result.getWidth(), result.getHeight()),
					Byte.MIN_VALUE);
			byte[] out = result.getData();
			int wr = result.getWidth();
			int hr = result.getHeight();
			int wi = img.getWidth();
			int hi = img.getHeight();
			for (int i = 0; i < wr; i++) {
				for (int j = 0; j < hr; j++) {
					// (x,y,1) = (cos, sin ..) * (x', y', 1)
					x = (int) (((cost * i) + (sint * j) + (-x0 * (cost - 1) - y0
							* sint)));
					y = (int) (((-sint * i) + (cost * j) + (-y0 * (cost - 1) + x0
							* sint)));
					if (x < wi && y < hi && x + y * wi < data.length
							&& x + y * wi >= 0 && x > 0)
						out[(int) ((i) + (j) * wr)] = data[x + y * wi];
				}
			}
		}
		super.setOutput(result);
	}
}