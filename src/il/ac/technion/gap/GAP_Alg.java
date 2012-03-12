/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 12/03/2012
 */
package il.ac.technion.gap;

import il.ac.technion.misc.Bin;

public interface GAP_Alg {
	
	/**
	 * @param binsCapacities
	 * @param _itemSizes
	 * @param _itemProfits
	 * @return
	 * @throws GAP_Exception
	 */
	Bin[] solve(int[] binsCapacities, int[][] _itemSizes, int[][] _itemWeights)
			throws GAP_Exception;
}
