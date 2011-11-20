package cat.uvic.calculs;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Vector;

import cat.uvic.android.ImageException;
import cat.uvic.android.ResultBitmap;
import cat.uvic.android.RgbImageAndroid;
import android.graphics.Bitmap;
import android.graphics.Rect;
import jjil.algorithm.*;
import jjil.core.Error;
import jjil.core.Gray8Image;
import jjil.core.Image;
import jjil.core.RgbImage;
import jjil.core.RgbVal;
import jjil.core.Sequence;

/**
 * the main class that is putting all operations together. Is responsible for
 * localisation, checking position, rotating and recognition of a banknote.
 * Class implements Singleton pattern. It means that there is just one instance
 * of the class in the program.
 * 
 * @author ANNA
 */
public class Transformation {
	private Image img; // source image
	private String path;
	File result;
	Rect region;
	Vector<HoughLine> lines;
	private Gray8Image grayH;
	private Gray8Image grayV;
	private int threshold;
	private int recognition;
	private Image currentImg;
	private boolean wrong = false;

	/**
	 * @return true if the image is corrupted, false if everything is correct
	 */
	public boolean isWrong() {
		return wrong;
	}

	/**
	 * @param wrong
	 */
	public void setWrong(boolean wrong) {
		this.wrong = wrong;
	}

	private boolean lowQ = false;
	/**
*
*/
	public String error;
	HoughLine arc = null;
	DecimalFormat df = new DecimalFormat("##,##");
	private ResultBitmap image;

	/**
	 * @return Bitmap of current processed image
	 * @throws Error
	 */
	public Bitmap getCurrentImg() throws Error {
		if (this.currentImg instanceof Gray8Image) {
			Gray8Rgb g2r = new Gray8Rgb();
			g2r.push(this.currentImg);
			RgbImage rgb = (RgbImage) g2r.getFront();
			return RgbImageAndroid.toBitmap((RgbImage) rgb);
		} else
			return RgbImageAndroid.toBitmap((RgbImage) img);
	}

	/**
	 * @param currentImg
	 */
	public void setCurrentImg(Image currentImg) {
		this.currentImg = currentImg;
	}

	/**
		*
		*/
	public static String EUR_5 = "5";
	/**
		*
		*/
	public static String EUR_10 = "10";
	/**
		*
		*/
	public static String EUR_20 = "20";
	/**
		*
		*/
	public static String EUR_50 = "50";

	/**
	 * @return number of recognition (5,10,20,50,0 or 2050)
	 */
	public int getRecognition() {
		return recognition;
	}

	/**
	 * @param recognition
	 */
	public void setRecognition(int recognition) {
		this.recognition = recognition;
	}

	String report;

	/**
	 * @return vector of HoughLines (if any)
	 */
	public Vector<HoughLine> getLines() {
		if (this.lines == null)
			this.lines = new Vector<HoughLine>();
		return lines;
	}

	/**
	 * @param lines
	 */
	public void setLines(Vector<HoughLine> lines) {
		this.lines = lines;
	}

	/**
	 * @return rectangle of current processed region
	 */
	public Rect getRegion() {
		return region;
	}

	/**
	 * @param region
	 */
	public void setRegion(Rect region) {
		this.region = region;
	}

	private Transformation() {
	}

	private Transformation(String _img) throws IOException {
		this.path = "resources/IMG_2.jpg";
		this.result = new File(this.path);
	}

	private Transformation(Bitmap _img) throws IOException {
		this.path = "resources/IMG_2.jpg";
		this.result = new File(this.path);
		this.image = new ResultBitmap(_img, region);
		RgbImageAndroid.toRgbImage(_img);
		this.img = RgbImageAndroid.toRgbImage(_img);
	}

	private static Transformation instance;

	/**
	 * @param _img
	 * @return instance of Transformation
	 * @throws IOException
	 */
	public static Transformation getInstance(Bitmap _img) throws IOException {
		if (instance == null)
			instance = new Transformation(_img);
		return instance;
	}

	/**
	 * destroy the instance of Transformation class
	 */
	public static void destroy() {
		instance = null;
	}

	/**
	 * @return Bitmap of final result of transformation
	 * @throws Error
	 * @throws Exception
	 */
	public ResultBitmap transform() throws Error, Exception {
		this.region = this.findBill(this.img, true);
		this.cutImage();
		this.region = this.findBill(this.img, false);
		this.checkPosition();
		if (wrong)
			return image;
		if (!this.recognise50()) {
			analiseNumber();
			// if (wrong) return image;
			if (this.recognition != 5 && this.recognition != 10
					&& this.recognition != 1010 && this.recognition != 101) {
				this.recognition = this.analiseColor();
			}
			// this.report = this.report+";;Error:;"+this.error;
		} else
			this.recognition = 50;
		if (this.recognition == 2050)
			this.recognition = 20;
		if (this.recognition == 1010)
			this.recognition = 10;
		if (this.recognition != 5 && this.recognition != 10
				&& this.recognition != 20 && this.recognition != 50)
			this.recognition = 0;
		Gray8Rgb g2r = new Gray8Rgb();
		g2r.push(grayH);
		Bitmap result = RgbImageAndroid.toBitmap((RgbImage) img);
		return new ResultBitmap(result, region);
	}

	/**
	 * @return img
	 */
	public Image getImg() {
		return img;
	}

	/**
	 * @param img
	 */
	public void setImg(Image img) {
		this.img = img;
	}

	/**
	 * @throws Error
	 * @throws Exception
	 */
	public void checkPosition() throws Error, Exception {
				long start = System.currentTimeMillis();
				double divRegion = (double) (this.region.width())
				/ (double) (this.region.height());
				System.out.println("checkPositionDiv: " + divRegion);
				if (divRegion < 1.9 || divRegion > 2.1) {
				this.rotate(this.grayH);
				if (this.arc != null) {
				Sequence seq = new Sequence();
				if (this.arc.theta > 0.1) {
				seq.add(new RgbRotate(Math.PI -
				this.arc.theta));
				seq.push(img);
				this.img = (RgbImage) seq.getFront();
				}
				} // else
				// throw new ImageException(
				// "Can't find the banknote.(HoughTransform)");
				}
				this.cutImage();
				divRegion = (double) (this.grayH.getWidth())
				/ (double) (this.grayH.getHeight());
				if ((divRegion < 1.5 || divRegion > 2.4) && (this.arc !=
				null)) {
				this.error = "Can't find the banknote.(HoughTransform)";
				this.recognition = 0;
				this.wrong = true;
				return;
				}
				Sequence seq = new Sequence();
				this.region = new Rect(0, this.grayH.getHeight() / 3,
				(this.grayH.getWidth()), (2 *
				this.grayH.getHeight()) / 3);
				seq.add(new Gray8Subimage(new Rect(0, this.img.getHeight()
				/ 3,
				(this.img.getWidth()), (2 *
				this.img.getHeight()) / 3)));
				seq.push(this.grayH);
				Gray8Image kk = (Gray8Image) seq.getFront();
				byte[] data = kk.getData();
				long sumWhite = 0;
				long sumBlack = 0;
				long sum1 = 0;
				long sum2 = 0;
				for (int i = 0; i < kk.getWidth(); i++)
				for (int j = 0; j < kk.getHeight(); j++) {
				if (data[j * kk.getWidth() + i] ==
				Byte.MIN_VALUE)
				sumBlack++;
				else if (data[j * kk.getWidth() + i] ==
				Byte.MAX_VALUE) {
				sumWhite++;
				if (i < kk.getWidth() / 2)
				sum1++;
				else
				sum2++;
				}
				}
				if (sum1 > sum2) {
				seq = new Sequence();
				seq.add(new RgbRotate(Math.PI));
				seq.push(img);
				img = seq.getFront();
				seq = new Sequence();
				seq.add(new GrayRotate(Math.PI));
				seq.push(this.grayH);
				this.grayH = (Gray8Image) seq.getFront();
				}
				double res = (double) sumBlack / (double) sumWhite;
				if ((res > 25)) {
				this.lowQ = true;
				this.error = "Low quality";
				this.recognition = 0;
				this.findBill(this.img, true);
				}
				long stop = System.currentTimeMillis();
				this.currentImg = this.img;
				System.out.println("checkPosition: " + (stop - start));
				}

	private HoughLine rotate(Gray8Image im) throws Exception, Error
				{
				long start = System.currentTimeMillis();
				Sequence seq = new Sequence();
				FindRegion ff = new FindRegion(false, 20, 10);
				double divRegion = (double) (this.region.width())
				/ (double) (this.region.height());
				double min = Math.abs(divRegion - 2);
				int idx = 0;
				int[] hist2;
				int[] hist;
				Gray8Image copyH = (Gray8Image) this.grayH.clone();
				Gray8Image copyV = (Gray8Image) this.grayV.clone();
				Gray8Image maxH = copyH;
				Gray8Image maxV = copyV;
				if (this.arc != null && this.arc.theta > 0.1) {
					if (divRegion < 1.9 || divRegion > 2.1) {
					seq = new Sequence();
					System.out.println("R: " + this.arc.r + "Theta: "+ this.arc.theta);
					seq.add(new GrayFastRotate(Math.PI -
					this.arc.theta));
					seq.push(this.grayH);
					copyH = (Gray8Image) seq.getFront();
					seq.push(this.grayV);
					copyV = (Gray8Image) seq.getFront();
					hist2 =
					Gray8HistHorizontal.computeHistogram(copyV);
					hist =
					Gray8HistVertical.computeHistogram(copyH);
					this.region = ff.findRegionOfInteres(hist,
					hist2);
					divRegion = (double) (this.region.width())
					/ (double) (this.region.height());
					if (min > Math.abs(divRegion - 2)) {
					maxH = copyH;
					maxV = copyV;
					min = Math.abs(divRegion - 2);
					idx = 0;
					}
					}
					}
					if (divRegion < 1.9 || divRegion > 2.1) {
					HoughTransform h = new HoughTransform(im.getWidth(),
					im.getHeight());
					h.addPoints(grayH);
					Vector<HoughLine> tmp = new Vector<HoughLine>();
					tmp = h.getLines(50);
					this.lines = tmp;
					long stop = System.currentTimeMillis();
					System.out.println("HoughLine: " + (stop - start));
					for (int i = 0; i < tmp.size(); i++) {
					if (tmp.get(i).theta > 0.1
					&& (divRegion <= 1.9 || divRegion
					>= 2.1)) {
					seq = new Sequence();
					System.out.println("R: " + tmp.get(i).r +
					" Theta: "
					+ tmp.get(i).theta + " Div:"
					+ divRegion);
					start = System.currentTimeMillis();
					seq.add(new GrayFastRotate(Math.PI -
					tmp.get(i).theta));
					seq.push(this.grayH);
					copyH = (Gray8Image) seq.getFront();
					seq.push(this.grayV);
					copyV = (Gray8Image) seq.getFront();
					stop = System.currentTimeMillis();
					start = System.currentTimeMillis();
					hist2 =
					Gray8HistHorizontal.computeHistogram(copyV);
					hist =
					Gray8HistVertical.computeHistogram(copyH);
					this.region =
					ff.findRegionOfInteres(hist, hist2);
					stop = System.currentTimeMillis();
					divRegion = (double)
					(this.region.width())
					/ (double)
					(this.region.height());
					if (min > Math.abs(divRegion - 2)) {
					maxH = copyH;
					maxV = copyV;
					min = Math.abs(divRegion - 2);
					idx = i;
					}
					}
					}
					hist2 = Gray8HistHorizontal.computeHistogram(maxV);
					hist = Gray8HistVertical.computeHistogram(maxH);
					this.region = ff.findRegionOfInteres(hist, hist2);
					divRegion = (double) (this.region.width())
					/ (double) (this.region.height());
					if (divRegion >= 1.9 || divRegion <= 2.1)
					this.arc = tmp.get(idx);
					else
					this.arc = null;
					stop = System.currentTimeMillis();
					}
					this.grayH = maxH;
					this.grayV = maxV;
					return this.arc;
					}

	/**
	 * @param img
	 * @param margin
	 * @return rectangle where a bill is
	 * @throws Error
	 */
	public Rect findBill(Image img, boolean margin) throws Error {
		// long start = System.currentTimeMillis();
		FindRegion ff = new FindRegion(margin);
		// long start = System.currentTimeMillis();
		Sequence seq = new Sequence();
		if (img instanceof RgbImage) {
			seq.add(new RgbAvgGray());
			seq.push(img);
			this.grayH = (Gray8Image) seq.getFront();
		}
		// aqui l'algoritme Otsu
		OtsuThresholder thresholder = new OtsuThresholder();
		threshold = thresholder.doThreshold(((Gray8Image) grayH).getData(),
				null);
		seq = new Sequence();
		if (lowQ)
			seq.add(new Gray8HistEq());
		seq.add(new Gray8CannyVert(50));
		seq.add(new Gray8Threshold(threshold / 10, false));
		seq.add(new Gray8Erose());
		seq.add(new Gray8Dilate());
		seq.push(grayH);
		this.grayV = (Gray8Image) seq.getFront();
		seq = new Sequence();
		if (lowQ)
			seq.add(new Gray8HistEq());
		seq.add(new Gray8CannyHoriz(50));
		seq.add(new Gray8Threshold(threshold / 10, false));
		seq.add(new Gray8Erose());
		seq.add(new Gray8Dilate());
		seq.push(grayH);
		this.grayH = (Gray8Image) seq.getFront();
		int[] hist2 = Gray8HistHorizontal.computeHistogram(grayV);
		int[] hist = Gray8HistVertical.computeHistogram(grayH);
		this.region = ff.findRegionOfInteres(hist, hist2);
		this.currentImg = this.grayH;
		return region;
	}

	/**
	 * @return recognized denomination or 0
	 * @throws Error
	 * @throws ImageException
	 */
	public int analiseColor() throws Error, ImageException {
		// long start = System.currentTimeMillis();
		Sequence seq = new Sequence();
		long red = 0;
		long blue = 0;
		RgbImage hh = (RgbImage) this.img.clone();
		this.region = new Rect(5 * img.getWidth() / 7, img.getHeight() / 15,
				(img.getWidth() / 8) + (5 * img.getWidth() / 7),
				(img.getHeight() / 5) + (img.getHeight() / 15));
		seq.add(new RgbImageSubImage(this.region));
		seq.push(img);
		// RGB
		hh = (RgbImage) seq.getFront();
		// YCbCr
		seq = new Sequence();
		seq.add(new Rgb2YCbCr());
		seq.push(hh);
		RgbImage hh2 = (RgbImage) seq.getFront();
		Gray8Image g = new Gray8Image(hh2.getWidth(), hh2.getHeight());
		g.fill(new jjil.core.Rect(0, 0, g.getWidth(), g.getHeight()),
				Byte.MIN_VALUE);
		byte[] data = g.getData();
		int[] hdata = hh2.getData();
		int w = g.getWidth();
		int h = g.getHeight();
		int cb, cr;
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				cb = (RgbVal.getG(hdata[i * w + j]) - Byte.MIN_VALUE);
				cr = (RgbVal.getB(hdata[i * w + j]) - Byte.MIN_VALUE);
				if (Math.abs(cb - cr) > 5 && (Math.abs(255 - cb - cr) > 5))
					data[i * w + j] = 0;
				else if ((Math.abs(cb - cr) <= 5)
						|| (Math.abs(255 - cb - cr) <= 5)) {
					data[i * w + j] = 10;
				}
			}
		}
		seq = new Sequence();
		seq.add(new RgbHsv());
		seq.push(hh);
		hh2 = (RgbImage) seq.getFront();
		hdata = hh2.getData();
		w = g.getWidth();
		h = g.getHeight();
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				cb = (RgbVal.getR(hdata[i * w + j]) - Byte.MIN_VALUE);
				if (cb >= 150 && cb <= 200 && data[i * w + j] == 0) {
					blue++;
					data[i * w + j] = Byte.MAX_VALUE;
				} else if (data[i * w + j] == 10 && cb >= 0 && cb <= 20) {
					red++;
					data[i * w + j] = Byte.MAX_VALUE;
				} else if (data[i * w + j] == 0)
					data[i * w + j] = Byte.MIN_VALUE;
			}
		}
		System.out.println("TOTAL: " + w * h + ", Blue: " + blue + ", Red: "
				+ red);
		this.grayH = (Gray8Image) g.clone();
		this.currentImg = img;
		if (blue > 0 && blue > red
				&& ((g.getWidth() * g.getHeight()) / blue) < 230)
			return 20;
		else if (red > 0 && red > blue
				&& ((g.getWidth() * g.getHeight()) / red) < 230)
			return 50;
		else
			return 0;
	}

	/**
	 * @return result of number analysis (5, 10, 0 or 2050€)
	 * @throws Error
	 */
	public int analiseNumber() throws Error {
		Sequence seq = new Sequence();
		this.region = new Rect(img.getWidth() / 45, 2 * img.getHeight() / 3,
				img.getWidth() / 7 + img.getWidth() / 45, img.getHeight() / 3
						+ 2 * img.getHeight() / 3);
		seq.add(new Gray8Subimage(this.region));
		seq.push(this.grayH);
		Gray8Image ii = (Gray8Image) seq.getFront();
		new Gray8HistVertical();
		int[] histV = Gray8HistVertical.computeHistogram(ii);
		HistRegions regionsV = new HistRegions(histV.length);
		HistRegion elemV = null;
		int count = 0;
		int prev = -1;
		long sumWhite = 0;
		long sumBlack = 0;
		byte[] data = ii.getData();
		for (int i = 0; i < ii.getHeight() * ii.getWidth(); i++) {
			if (data[i] == Byte.MIN_VALUE)
				sumBlack++;
			else if (data[i] == Byte.MAX_VALUE)
				sumWhite++;
		}
		if (((double) sumBlack / (double) sumWhite > 20)) {
			this.lowQ = true;
			this.error = "Low quality number";
			this.recognition = 1010;
			return 0;
		}
		String type = null;
		int border = 1;
		for (int i = 0; i < histV.length; i++) {
			if (elemV == null && histV[i] < border) {
				elemV = new HistRegion(HistRegion.Tipus_ZERO);
				prev = histV[i];
				count++;
			} else if (elemV == null && histV[i] > border) {
				elemV = new HistRegion(HistRegion.Tipus_ONE);
				prev = histV[i];
				count++;
			} else if (elemV != null && histV[i] < border && prev < border) {
				prev = histV[i];
				count++;
			} else if (elemV != null && histV[i] > border && prev < border) {
				elemV.setCount(count);
				elemV.setPercent((count * 100) / histV.length);
				if (elemV.getCount() > 2 && !elemV.getType().equals(type)) {
					regionsV.getHistRegions().add(elemV);
					type = elemV.getType();
					count = 0;
					elemV = null;
				} else {
					prev = histV[i];
					count++;
				}
			} else if (elemV != null && histV[i] > border && prev > border) {
				prev = histV[i];
				count++;
			} else if (elemV != null && histV[i] < border && prev > border) {
				elemV.setCount(count);
				elemV.setPercent((count * 100) / histV.length);
				if (elemV.getCount() > 2 && !elemV.getType().equals(type)) {
					regionsV.getHistRegions().add(elemV);
					type = elemV.getType();
					count = 0;
					elemV = null;
				} else {
					prev = histV[i];
					count++;
				}
			}
		}
		if (elemV == null || type == null) {
			this.wrong = true;
			this.error = "Can't find the number";
			this.recognition = 0;
			return 0;
		}
		elemV.setCount(count);
		elemV.setPercent((count * 100) / histV.length);
		regionsV.getHistRegions().add(elemV);
		if (regionsV.getHistRegions().get(0).getType()
				.equals(HistRegion.Tipus_ONE)) {
			regionsV.getHistRegions().remove(0);
		}
		if (regionsV.getHistRegions().size() > 3) {
			if (regionsV.getHistRegions()
					.get(regionsV.getHistRegions().size() - 1).getType()
					.equals(HistRegion.Tipus_ZERO))
				border = border
						+ regionsV.getHistRegions()
								.get(regionsV.getHistRegions().size() - 1)
								.getCount()
						+ regionsV.getHistRegions()
								.get(regionsV.getHistRegions().size() - 2)
								.getCount()
						+ +regionsV.getHistRegions()
								.get(regionsV.getHistRegions().size() - 3)
								.getCount();
			else
				border = border
						+ regionsV.getHistRegions()
								.get(regionsV.getHistRegions().size() - 1)
								.getCount()
						+ regionsV.getHistRegions()
								.get(regionsV.getHistRegions().size() - 2)
								.getCount();
		} else
			border = 1;
		int width = 0;
		for (int i = 0; i < regionsV.getHistRegions().size(); i++) {
			if (regionsV.getHistRegions().get(i).getCount() > width) {
				width = regionsV.getHistRegions().get(i).getCount();
			}
		}
		seq = new Sequence();
		this.region = new Rect(img.getWidth() / 80, 2 * img.getHeight() / 3,
				img.getWidth() / 80 + img.getWidth() / 7, 2 * img.getHeight()
						/ 3 + img.getHeight() / 3 - border);
		seq.add(new Gray8Subimage(this.region));
		seq.push(this.grayH);
		ii = (Gray8Image) seq.getFront();
		new Gray8HistHorizontal();
		int[] histH = Gray8HistHorizontal.computeHistogram(ii);
		HistRegions regions = new HistRegions(histH.length);
		regions.setWidth(width);
		HistRegion elem = null;
		count = 0;
		prev = -1;
		for (int i = 0; i < histH.length; i++) {
			if (elem == null && histH[i] <= border) {
				elem = new HistRegion(HistRegion.Tipus_ZERO);
				prev = histH[i];
				count++;
			} else if (elem == null && histH[i] > border) {
				elem = new HistRegion(HistRegion.Tipus_ONE);
				prev = histH[i];
				count++;
			} else if (elem != null && histH[i] <= border && prev <= border) {
				prev = histH[i];
				count++;
			} else if (elem != null && histH[i] > border && prev <= border) {
				elem.setCount(count);
				elem.setPercent((count * 100) / histH.length);
				regions.getHistRegions().add(elem);
				count = 0;
				elem = new HistRegion(HistRegion.Tipus_ONE);
				prev = histH[i];
				count++;
			} else if (elem != null && histH[i] > border && prev > border) {
				prev = histH[i];
				count++;
			} else if (elem != null && histH[i] <= border && prev > border) {
				elem.setCount(count);
				elem.setPercent((count * 100) / histH.length);
				regions.getHistRegions().add(elem);
				count = 0;
				elem = new HistRegion(HistRegion.Tipus_ZERO);
				prev = histH[i];
				count++;
			}
		}
		elem.setCount(count);
		elem.setPercent((count * 100) / histH.length);
		regions.getHistRegions().add(elem);
		this.recognition = regions.analiseNumber(this.lowQ);
		this.currentImg = grayH;
		// long stop = System.currentTimeMillis();
		// System.out.println("analiseNumber: "+(stop-start));
		return 0;
	}

	/**
	 * @return cut of RgbImage
	 * @throws Error
	 */
	public RgbImage cutImage() throws Error {
		// long start = System.currentTimeMillis();
		Sequence seq = new Sequence();
		seq.add(new RgbImageSubImage(region));
		seq.push(img);
		img = seq.getFront();
		seq = new Sequence();
		seq.add(new Gray8Subimage(region));
		seq.push(this.grayH);
		this.grayH = (Gray8Image) seq.getFront();
		// long stop = System.currentTimeMillis();
		// System.out.println("cutImage: "+(stop-start));
		return (RgbImage) img;
	}

	/**
	 * @return true if denomination is 50€ and false if not
	 * @throws Error
	 */
	public boolean recognise50() throws Error {
		Sequence seq = new Sequence();
		this.region = new Rect((25 * img.getWidth()) / 30, img.getHeight() / 2,
				(img.getWidth() / 7) + ((25 * img.getWidth()) / 30),
				(img.getHeight() / 2) + (img.getHeight() / 3));
		seq.add(new Gray8Subimage(this.region));
		seq.push(this.grayH);
		Gray8Image ii = (Gray8Image) seq.getFront();
		int[] hist2 = Gray8HistHorizontal.computeHistogram(ii);
		HistRegions regions = new HistRegions(hist2.length);
		HistRegion elem = null;
		int count = 0;
		int prev = -1;
		for (int i = 0; i < hist2.length; i++) {
			if (elem == null && hist2[i] <= 0) {
				elem = new HistRegion(HistRegion.Tipus_ZERO);
				prev = hist2[i];
				count++;
			} else if (elem == null && hist2[i] > 0) {
				elem = new HistRegion(HistRegion.Tipus_ONE);
				prev = hist2[i];
				count++;
			} else if (elem != null && hist2[i] <= 0 && prev <= 0) {
				prev = hist2[i];
				count++;
			} else if (elem != null && hist2[i] > 0 && prev <= 0) {
				elem.setCount(count);
				elem.setPercent((count * 100) / hist2.length);
				regions.getHistRegions().add(elem);
				count = 0;
				elem = new HistRegion(HistRegion.Tipus_ONE);
				prev = hist2[i];
				count++;
			} else if (elem != null && hist2[i] > 0 && prev > 0) {
				prev = hist2[i];
				count++;
			} else if (elem != null && hist2[i] <= 0 && prev > 0) {
				elem.setCount(count);
				elem.setPercent((count * 100) / hist2.length);
				regions.getHistRegions().add(elem);
				count = 0;
				elem = new HistRegion(HistRegion.Tipus_ZERO);
				prev = hist2[i];
				count++;
			}
		}
		elem.setCount(count);
		elem.setPercent((count * 100) / hist2.length);
		regions.getHistRegions().add(elem);
		this.currentImg = grayH;
		return regions.analise50(this.lowQ);
	}

	/**
	 * Set final result image
	 */
	public void setFinalResult() {
		this.currentImg = img;
		this.region = new Rect(0, 0, img.getWidth(), img.getHeight());
	}
}