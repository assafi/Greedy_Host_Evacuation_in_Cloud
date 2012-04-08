/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 7 באפר 2012
 */
package il.ac.technion.gap;

import il.ac.technion.datacenter.physical.Host;
import il.ac.technion.datacenter.vm.VM;
import il.ac.technion.knapsack.Bin;
import il.ac.technion.knapsack.Item;

import java.util.Iterator;
import java.util.List;

public class GapUtils {
	public static double[][] prepareWeightsMatrix(List<Host> hosts, List<VM> vms) {
		double[][] itemPrices = new double[hosts.size()][vms.size()];
		for (int binIdx = 0; binIdx < hosts.size(); binIdx++) {
			Host h = hosts.get(binIdx);
			for (int vmIdx = 0; vmIdx < vms.size(); vmIdx++) {
				VM vm = vms.get(vmIdx);
				itemPrices[binIdx][vmIdx] = vm.cost(h);
			}
		}
		return itemPrices;
	}

	public static int[][] prepareSizesMatrix(int numBins, List<VM> vms) {
		int[][] itemSizes = new int[numBins][vms.size()];
		for (int binIdx = 0; binIdx < numBins; binIdx++) {
			for (int vmIdx = 0; vmIdx < vms.size(); vmIdx++) {
				itemSizes[binIdx][vmIdx] = vms.get(vmIdx).size();
			}
		}
		return itemSizes;
	}

	public static int[] prepareCapacitiesVector(List<Host> hosts) {
		int[] binsCapacities = new int[hosts.size()];
		int i = 0;
		Iterator<Host> iter = hosts.iterator();
		while (iter.hasNext()) {
			binsCapacities[i++] = iter.next().freeCapacity();
		}
		return binsCapacities;
	}

	public static void binsToAssignments(Bin[] bins, List<Host> hosts,
			List<VM> vms) {
		for (Bin bin : bins) {
			Host h = hosts.get(bin.id);
			for (Item item : bin.assignedItems()) {
				VM v = vms.get(item.id);
				h.assign(v);
			}
		}		
	}
}
