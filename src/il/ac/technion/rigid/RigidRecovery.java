/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 22/03/2012
 */
package il.ac.technion.rigid;

import il.ac.technion.gap.GAP_Alg;
import il.ac.technion.misc.Host;
import il.ac.technion.misc.VM;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class RigidRecovery {

	private GAP_Alg gap;
	
	@Inject
	public RigidRecovery(@Named("Min GAP") GAP_Alg gap) {
		this.gap = gap;
	}
	
	public RecoveryPlan solve(List<Host> hosts) {
		RecoveryPlan rp = new RecoveryPlan();
		for (Host host : hosts) {
			List<Host> filteredHosts = new ArrayList<Host>(hosts);
			filteredHosts.remove(host);
//			rp.add(solveGAP(filteredHosts,host.vms()));
		}
		return null;
	}

	/**
	 * @param filteredHosts
	 * @param vms
	 * @return
	 */
	private RecoveryPlan solveGAP(List<Host> filteredHosts, List<VM> vms) {
		int[] binsCapacities = prepareCapacities(filteredHosts);
		int[][] itemSizes = prepareSizes(filteredHosts.size(), vms);
		double[][] itemPrices = preparePrices(filteredHosts, vms);
		gap.solve(binsCapacities, itemSizes, itemPrices);
		return null;
	}

	private double[][] preparePrices(List<Host> hosts, List<VM> vms) {
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

	private int[][] prepareSizes(int numBins, List<VM> vms) {
		int[][] itemSizes = new int[numBins][vms.size()];
		for (int binIdx = 0; binIdx < numBins; binIdx++) {
			for (int vmIdx = 0; vmIdx < vms.size(); vmIdx++) {
				itemSizes[binIdx][vmIdx] = vms.get(vmIdx).ram;
			}
		}
		return itemSizes;
	}

	private int[] prepareCapacities(List<Host> filteredHosts) {
		int[] binsCapacities = new int[filteredHosts.size()];
		int i = 0;
		Iterator<Host> iter = filteredHosts.iterator();
		while (iter.hasNext()) {
			binsCapacities[i++] = iter.next().freeCapacity();
		}
		return binsCapacities;
	}
}
