package cat.uvic.calculs;

import android.graphics.Rect;
import jjil.algorithm.ErrorCodes;
import jjil.core.Error;
import jjil.core.Image;
import jjil.core.PipelineStage;
import jjil.core.RgbImage;

/**
 * cut defined part of RGB image
 * 
 * @author ANNA
 */
public class RgbImageSubImage extends PipelineStage {
	Rect rect;

	/**
	 * @param _rect
	 * @throws Error
	 */
	public RgbImageSubImage(Rect _rect) throws jjil.core.Error {
		this.rect = _rect;
	}

	/**
	 * Reduces an RgbImage by a factor horizontally and vertically through
	 * averaging. The reduction factor must be an even multiple of the image
	 * size.
	 * 
	 * @param img
	 *            the input image.
	 * @throws jjil.core.Error
	 *             if the input image is not gray, or the reduction factor does
	 *             not evenly divide the image size.
	 */
	public void push(Image img) throws jjil.core.Error {
		if (!(img instanceof RgbImage)) {
			throw new Error(Error.PACKAGE.ALGORITHM,
					ErrorCodes.IMAGE_NOT_RGBIMAGE, img.toString(), null, null);
		}
		RgbImage result = new RgbImage(rect.width(), rect.height());
		int[] rgbData = ((RgbImage) img).getData();
		int[] out = result.getData();
		// System.out.println("RGBImageSubImageRect: L:"+rect.left+" R: "+rect.right+" T: "+rect.top+" B: "+rect.bottom+"Width: "+rect.width()+" Height: "+rect.height());
		for (int i = 0; i < rect.height(); i++) {
			System.arraycopy(rgbData, (i + rect.top) * img.getWidth()
					+ rect.left, out, i * (rect.width()), rect.width());
		}
		super.setOutput(result);
	}
}