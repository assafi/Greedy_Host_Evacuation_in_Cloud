/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 12/03/2012
 */
package il.ac.technion.gap.max.localratio;

import java.util.Collection;

public class ProfitsMatrix {
	private int[][] profits;

	public ProfitsMatrix(int[][] _profits) {
		this.profits = _profits;
	}

	/**
	 * Will always be called with a PM of at least two columns. The new PM will
	 * contain the residual profit matrix excluding the first column.
	 * 
	 * @param pm
	 *          The last step profit matrix
	 * @param chosenIndexes
	 *          The indexes of the chosen items by Knapsack done on the
	 *          pm.firstColumnIndex column.
	 * @throws LastProfitColumnException
	 *           In case pm does not include at least two columns
	 */
	private ProfitsMatrix(ProfitsMatrix pm, Collection<Integer> chosenIndexes)
			throws LastProfitColumnException {
		if (pm.lastColumn())
			throw new LastProfitColumnException();
		this.profits = new int[pm.profits.length - 1][pm.profits[0].length];
		for (int i = 0; i < profits.length; i++) {
			for (int j = 0; j < profits[i].length; j++) {
				if (chosenIndexes.contains(j)) {
					profits[i][j] = pm.profits[i + 1][j] - pm.profits[0][j];
				} else {
					profits[i][j] = pm.profits[i + 1][j];
				}
			}
		}
	}

	public int[] getCurrentColumn() {
		return profits[0];
	}

	public ProfitsMatrix getResidualProfitMatrix(Collection<Integer> chosenIndexes)
			throws LastProfitColumnException {
		return new ProfitsMatrix(this, chosenIndexes);
	}

	public boolean lastColumn() {
		return profits.length == 1;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return new ProfitsMatrix(profits.clone());
	}
}
