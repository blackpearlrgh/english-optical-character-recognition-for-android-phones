package cat.uvic.calculs;

import jjil.algorithm.ErrorCodes;
import jjil.algorithm.Gray3Bands2Rgb;
import jjil.algorithm.RgbSelectGray;
import jjil.core.Error;
import jjil.core.Gray8Image;
import jjil.core.Image;
import jjil.core.PipelineStage;
import jjil.core.RgbImage;
import jjil.core.Sequence;

/**
 * rotate RGB image
 * 
 * @author ANNA
 */
public class RgbRotate extends PipelineStage {
	double theta;

	/**
	 * @param theta
	 * @throws Error
	 */
	public RgbRotate(double theta) throws jjil.core.Error {
		this.theta = theta;
	}

	@Override
	public void push(Image img) throws Error {
		if (!(img instanceof RgbImage)) {
			throw new Error(Error.PACKAGE.ALGORITHM,
					ErrorCodes.IMAGE_NOT_RGBIMAGE, img.toString(), null, null);
		}
		Sequence seqR = new Sequence(new RgbSelectGray(RgbSelectGray.RED));
		seqR.add(new GrayRotate(this.theta));
		Sequence seqG = new Sequence(new RgbSelectGray(RgbSelectGray.GREEN));
		seqG.add(new GrayRotate(this.theta));
		Sequence seqB = new Sequence(new RgbSelectGray(RgbSelectGray.BLUE));
		seqB.add(new GrayRotate(this.theta));
		seqR.push(img);
		seqG.push(img);
		seqB.push(img);
		super.setOutput(Gray3Bands2Rgb.push((Gray8Image) seqR.getFront(),
				(Gray8Image) seqG.getFront(), (Gray8Image) seqB.getFront()));
	}
}