package cat.uvic.calculs;

import jjil.algorithm.Gray3Bands2Rgb;
import jjil.algorithm.Gray8HistEq;
import jjil.algorithm.RgbSelectGray;
import jjil.core.Error;
import jjil.core.Gray8Image;
import jjil.core.Image;
import jjil.core.PipelineStage;
import jjil.core.Sequence;

/***
 * equalize histograms of RGB image
 * 
 * @author ANNA
 */
public class RgbHistEqualize extends PipelineStage {
	/**
*
*/
	public RgbHistEqualize() {
		super();
	}

	@Override
	public void push(Image img) throws Error {
		Sequence seqR = new Sequence(new RgbSelectGray(RgbSelectGray.RED));
		seqR.add(new Gray8HistEq());
		Sequence seqG = new Sequence(new RgbSelectGray(RgbSelectGray.GREEN));
		seqG.add(new Gray8HistEq());
		Sequence seqB = new Sequence(new RgbSelectGray(RgbSelectGray.BLUE));
		seqB.add(new Gray8HistEq());
		seqR.push(img);
		seqG.push(img);
		seqB.push(img);
		super.setOutput(Gray3Bands2Rgb.push((Gray8Image) seqR.getFront(),
				(Gray8Image) seqG.getFront(), (Gray8Image) seqB.getFront()));
	}
}