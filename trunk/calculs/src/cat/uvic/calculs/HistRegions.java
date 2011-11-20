package cat.uvic.calculs;

import java.util.ArrayList;

/**
 * all objects HistResion of one histogram together.
 * 
 * @author ANNA
 */
public class HistRegions {
	ArrayList<HistRegion> histRegions;
	private long total;
	private String result;
	private long width;

	/**
	 * @return width of regions
	 */
	public long getWidth() {
		return width;
	}

	/**
	 * @param width
	 */
	public void setWidth(long width) {
		this.width = width;
	}

	/**
	 * @return result
	 */
	public String getResult() {
		return result;
	}

	/**
	 * @param result
	 */
	public void setResult(String result) {
		this.result = result;
	}

	/**
	 * @return total
	 */
	public long getTotal() {
		return total;
	}

	/**
	 * @param total
	 */
	public void setTotal(long total) {
		this.total = total;
	}

	/*** @param total */
	public HistRegions(long total) {
		super();
		this.total = total;
	}

	/**
	 * @return array of regions
	 */
	public ArrayList<HistRegion> getHistRegions() {
		if (this.histRegions == null)
			this.histRegions = new ArrayList<HistRegion>();
		return histRegions;
	}

	/**
	 * @param histRegions
	 */
	public void setHistRegions(ArrayList<HistRegion> histRegions) {
		this.histRegions = histRegions;
	}

	/**
	 * @param lowQ
	 * @return 10 if it is 10 €, 5 if it is 5€, 0 when not found, 2050 if can be
	 *         20€ or 50€
	 */
	public int analiseNumber(boolean lowQ) {
		String pattern = "";
		int position = -1;
		int pos1 = -1;
		HistRegion elem;
		for (int i = 0; i < this.getHistRegions().size(); i++) {
			elem = this.getHistRegions().get(i);
			if (pattern.equals("")) {
				if (elem.getType().equals(HistRegion.Tipus_ONE)
						&& elem.getPercent() > 12) {
					pattern = "1";
					position = i;
				}
			} else if (pattern.equals("1")) {
				if (elem.getType().equals(HistRegion.Tipus_ZERO)) {
					pattern = "10";
				} else {
					pattern = "";
					position = -1;
				}
			} else if (pattern.equals("10")) {
				pos1 = i;
				if (elem.getType().equals(HistRegion.Tipus_ONE)) {
					pattern = "101";
				} else {
					pattern = "";
					position = -1;
				}
			}
		}
		if (pattern.equals("101")) {
			int total = this.getHistRegions().get(position).getCount()
					+ this.getHistRegions().get(position + 1).getCount()
					+ this.getHistRegions().get(position + 2).getCount();
			this.getHistRegions()
					.get(position)
					.setPercentLocal(
							(this.getHistRegions().get(position).getCount() * 100)
									/ total);
			this.getHistRegions()
					.get(position + 1)
					.setPercentLocal(
							(this.getHistRegions().get(position + 1).getCount() * 100)
									/ total);
			this.getHistRegions()
					.get(position + 2)
					.setPercentLocal(
							(this.getHistRegions().get(position + 2).getCount() * 100)
									/ total);
			double w1 = (double) this.width
					/ (double) this.getHistRegions().get(position).getCount();
			double w2 = (double) this.width
					/ (double) this.getHistRegions().get(position + 1)
							.getCount();
			double oneXone = (double) this.getHistRegions().get(position)
					.getCount()
					/ (double) this.getHistRegions().get(position + 2)
							.getCount();
			this.result = pattern + ";;WIDTH;" + this.width + ";;W1:;" + w1
					+ ";;W2:;" + w2 + ";;ONEXONE:;" + oneXone;
			if (!lowQ && (oneXone > 0.3 && oneXone < 0.7)) {
				return 10;
			} else if (lowQ && (oneXone > 0.3 && oneXone < 0.7)) {
				return 10;
			} else if (!lowQ && (oneXone > 1.5 && oneXone < 3)) {
				return 5;
			} else if (lowQ && (oneXone > 1.0 && oneXone < 3)) {
				return 5;
			} else if (w1 == 0 && w2 == 0)
				return 11;
			else if ((w1 >= 0.7 && w1 <= 1) || (oneXone > 3)
					|| (oneXone > 0.7 && oneXone < 1.4))
				return 2050;
			else
				return 101;
		} else if (pos1 >= 0) {
			double w1 = (double) this.width
					/ (double) this.getHistRegions().get(position).getCount();
			if (w1 >= 0.7 && w1 <= 1)
				return 2050;
			else
				return 0;
		} else
			return 0;
	}

	public String toString() {
		String result = "";
		for (int i = 0; i < this.getHistRegions().size(); i++) {
			result = result + this.getHistRegions().get(i).getType() + ";"
					+ this.getHistRegions().get(i).getCount() + ";"
					+ this.getHistRegions().get(i).getStrPercent() + ";;";
		}
		return result;
	}

	/**
	 * @param lowQ
	 * @return true if is 50 €
	 */
	public boolean analise50(boolean lowQ) {
		if (this.getHistRegions().size() == 7) {
			if (this.getHistRegions().get(1).getPercent() > 15
					&& this.getHistRegions().get(1).getType()
							.equals(HistRegion.Tipus_ONE)
					&& this.getHistRegions().get(5).getType()
							.equals(HistRegion.Tipus_ONE)
					&& this.getHistRegions().get(5).getPercent() > 15
					&& this.getHistRegions().get(2).getPercent() < 15
					&& this.getHistRegions().get(4).getPercent() < 15
					&& this.getHistRegions().get(3).getPercent() < this
							.getHistRegions().get(1).getPercent()
					&& this.getHistRegions().get(3).getPercent() < this
							.getHistRegions().get(5).getPercent())
				return true;
			else
				return false;
		} else
			return false;
	}
}
