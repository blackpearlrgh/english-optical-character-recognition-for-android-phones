package cat.uvic.calculs;

import java.text.DecimalFormat;

/**
 * represents part of a histogram, that is zero or more that zero. Used in
 * looking for number pattern.
 * 
 * @author ANNA
 */
public class HistRegion {
	/**
*
*/
	public static String Tipus_ZERO = "0";
	/**
*
*/
	public static String Tipus_ONE = "1";
	private String type;
	private int count;
	private double percent;
	private double percentLocal;

	/**
	 * @return local percent of region
	 */
	public double getPercentLocal() {
		return percentLocal;
	}

	/**
	 * @param percentLocal
	 */
	public void setPercentLocal(double percentLocal) {
		this.percentLocal = percentLocal;
	}

	/**
	 * @return percent of region in String (#,##) format
	 */
	public String getStrPercent() {
		DecimalFormat df = new DecimalFormat("#,##");
		return df.format(percent);
	}

	/**
	 * @return local percent of region in String (#,##) format
	 */
	public String getStrPercentLocal() {
		DecimalFormat df = new DecimalFormat("#,##");
		return df.format(percentLocal);
	}

	/**
	 * @return percent of region
	 */
	public double getPercent() {
		return percent;
	}

	/**
	 * @param percent
	 */
	public void setPercent(double percent) {
		this.percent = percent;
	}

	/**
	 * @return count of pixels of region
	 */
	public int getCount() {
		return count;
	}

	/**
	 * @param count
	 */
	public void setCount(int count) {
		this.count = count;
	}

	/**
	 * @return type of HistRegion (ONE or ZERO)
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @param type
	 */
	public HistRegion(String type) {
		super();
		this.type = type;
	}
}