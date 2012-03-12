/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 12/03/2012
 */
package il.ac.technion.gap.min.conversion;

import il.ac.technion.gap.GAP_Alg;
import il.ac.technion.gap.GAP_Exception;
import il.ac.technion.misc.AssignedItem;
import il.ac.technion.misc.Bin;
import il.ac.technion.misc.Item;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.google.java.contract.Requires;

public class Min_Max_GAP_Conversion implements GAP_Alg {

	private final GAP_Alg maxGAP;

	@Inject
	public Min_Max_GAP_Conversion(@Named("Max GAP") GAP_Alg _maxGAP) {
		this.maxGAP = _maxGAP;
	}

	/**
	 * In order to solve Min-GAP using Max-GAP we need to address a few issues.
	 * First we transform the costs matrix into a profit matrix. This does not
	 * guaranty any approximation ratio !
	 * 
	 * Farther more, we iteratively activate the Max-GAP algorithm, until all 
	 * items are assigned or until non can be assigned. In case of the former, 
	 * the assignment is returned, otherwise a failure is announced by throwing 
	 * a GAP_Exception.
	 */
	@Override
	public Bin[] solve(int[] binsCapacities, int[][] itemSizes, int[][] _itemCosts)
			throws GAP_Exception {

		int numItems = itemSizes[0].length;
		
		Bin[] bins = null;
		Bin[] $ = createEmptyBins(binsCapacities);

		int[][] itemProfits = costs2Profits(_itemCosts);

		do {
			int[] bCapacities = updatedCapacities(bins, binsCapacities);
			itemProfits = updateProfits(bins, itemProfits);
			bins = maxGAP.solve(bCapacities, itemSizes, itemProfits);
			mergeBins(bins, $, _itemCosts);
		} while (itemsAssigned(bins) && !assignedAll($,numItems));
		return $;
	}

	private boolean assignedAll(Bin[] bins, int numItems) {
		int count = 0;
		for (Bin bin : bins) {
			count += bin.assignedItems().size();
		}
		return count == numItems;
	}

	private Bin[] createEmptyBins(int[] binsCapacities) {
		Bin[] bins = new Bin[binsCapacities.length];
		for (int i = 0; i < bins.length; i++) {
			bins[i] = new Bin(i,binsCapacities[i]);
		}
		return bins;
	}

	private boolean itemsAssigned(Bin[] bins) {
		for (Bin bin : bins) {
			if (bin.assignedItems().size() > 0) {
				return true;
			}
		}
		return false;
	}

	@Requires("source.length == target.length")
	private void mergeBins(Bin[] source, Bin[] target, int[][] itemsCosts) {
		for (int i = 0; i < target.length; i++) {
			for (Item item : source[i].assignedItems()) {
				System.out.println("target[i].id=" + target[i].id);
				System.out.println("item.id=" + item.id);
				int itemCost = itemsCosts[target[i].id][item.id];
				target[i].assign(new AssignedItem(item.id, item.size, itemCost, target[i]));
			}
		}
	}

	private int[][] updateProfits(Bin[] bins, int[][] itemProfits) {
		if (bins == null) return itemProfits;
		for (Bin bin: bins) {
			for (Item item : bin.assignedItems()) {
				for (int j = 0; j < itemProfits.length; j++) {
					itemProfits[j][item.id] = 0; //Maybe should switch to -1
				}
			}
		}
		return itemProfits;
	}

	private int[] updatedCapacities(Bin[] bins, int[] binsCapacities) {
		if (bins == null) return binsCapacities;
		for (int i = 0; i < binsCapacities.length; i++) {
			binsCapacities[i] = bins[i].remainingCapacity();
		}
		return binsCapacities;
	}

	private static int[][] costs2Profits(int[][] costsMatrix) {
		int max = max(costsMatrix);
		int[][] profitMarix = new int[costsMatrix.length][costsMatrix[0].length];
		for (int i = 0; i < costsMatrix.length; i++) {
			for (int j = 0; j < costsMatrix[i].length; j++) {
				profitMarix[i][j] = max - costsMatrix[i][j];
			}
		}
		return profitMarix;
	}

	private static int max(int[][] matrix) {
		int max = 0;

		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				max = Math.max(max, matrix[i][j]);
			}
		}
		return max;
	}

}