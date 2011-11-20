package cat.uvic.calculs;

import android.graphics.Rect;
import jjil.algorithm.ErrorCodes;
import jjil.core.Error;
import jjil.core.Gray8Image;
import jjil.core.Image;
import jjil.core.PipelineStage;

/**
 * cut part of a Gray image
 * 
 * @author ANNA
 */
public class Gray8Subimage extends PipelineStage {
	Rect rect;

	/**
	 * Creates a new instance of Gray8Subimage.
	 * 
	 * @param _rect
	 * @throws jjil.core.Error
	 *             if the target width or height is less than or equal to zero.
	 */
	public Gray8Subimage(Rect _rect) throws jjil.core.Error {
		this.rect = _rect;
	}

	public void push(Image img) throws jjil.core.Error {
		if (!(img instanceof Gray8Image)) {
			throw new Error(Error.PACKAGE.ALGORITHM,
					ErrorCodes.IMAGE_NOT_RGBIMAGE, img.toString(), null, null);
		}
		Gray8Image result = new Gray8Image(rect.width(), rect.height());
		byte[] data = ((Gray8Image) img).getData();
		byte[] out = result.getData();
		for (int i = 0; i < rect.height(); i++) {
			System.arraycopy(data, (i + rect.top) * img.getWidth() + rect.left,
					out, i * (rect.width()), rect.width());
		}
		super.setOutput(result);
	}
}