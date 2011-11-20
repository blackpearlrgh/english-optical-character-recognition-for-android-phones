package cat.uvic.calculs;

import jjil.algorithm.ErrorCodes;
import jjil.core.Error;
import jjil.core.Image;
import jjil.core.PipelineStage;
import jjil.core.RgbImage;
import jjil.core.RgbVal;

/**
 * pass RGB image to YcbCr colorspace
 * 
 * @author ANNA
 */
public class Rgb2YCbCr extends PipelineStage {
	public void push(Image imageInput) throws Error {
		if (!(imageInput instanceof RgbImage)) {
			throw new Error(Error.PACKAGE.ALGORITHM,
					ErrorCodes.IMAGE_NOT_RGBIMAGE, imageInput.toString(), null,
					null);
		}
		RgbImage rgbInput = (RgbImage) imageInput;
		RgbImage result = new RgbImage(rgbInput.getWidth(),
				rgbInput.getHeight());
		int[] rgbData = rgbInput.getData();
		int[] out = result.getData();
		int nR, nG, nB;
		double Y, Cb, Cr;
		for (int i = 0; i < rgbInput.getWidth() * rgbInput.getHeight(); i++) {
			nR = RgbVal.getR(rgbData[i]) - Byte.MIN_VALUE;
			nG = RgbVal.getG(rgbData[i]) - Byte.MIN_VALUE;
			nB = RgbVal.getB(rgbData[i]) - Byte.MIN_VALUE;
			// Y = 0.299*R + 0.587*G + 0.114*B
			// Cb = (B-Y)*0.564 + 0.5
			// Cr = (R-Y)*0.713 + 0.5
			Y = 0.299 * nR + 0.587 * nG + 0.114 * (nB);
			Cb = (nB - Y) * 0.564 + 0.5;
			Cr = (nR - Y) * 0.713 + 0.5;
			out[i] = RgbVal.toRgb((byte) (Y + Byte.MIN_VALUE),
					(byte) (Cb + Byte.MIN_VALUE), (byte) (Cr + Byte.MIN_VALUE));
		}
		super.setOutput(result);
	}
}