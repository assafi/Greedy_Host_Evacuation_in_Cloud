/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 22/03/2012
 */
package il.ac.technion.rigid;

import il.ac.technion.datacenter.physical.Host;
import il.ac.technion.datacenter.physical.PhysicalAffinity;
import il.ac.technion.datacenter.vm.VM;
import il.ac.technion.gap.GAP_Alg;
import il.ac.technion.knapsack.Bin;

import java.util.ArrayList;
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
	
	public RecoveryPlan affinityRecovery(List<PhysicalAffinity> paList) {
		List<Host> hosts = PhysicalAffinity.extractHosts(paList);
		RecoveryPlan rp = new RecoveryPlan(hosts);
		int vmCount = 0;
		for (PhysicalAffinity pa : paList) {
			List<Host> filteredHosts = new ArrayList<Host>(hosts);
			filteredHosts.removeAll(pa.getHosts());
			List<VM> recoveredVMs = pa.getVMs();
			vmCount += recoveredVMs.size();
			rp.add(solveGAP(filteredHosts,recoveredVMs),filteredHosts,recoveredVMs);
		}
		if (rp.recoveredVMsCount() == vmCount) {
			rp.full();
		}
		return rp;
	}
	
	public RecoveryPlan hostsRecovery(List<Host> hosts) {
		RecoveryPlan rp = new RecoveryPlan(hosts);
		int vmCount = 0;
		for (Host host : hosts) {
			List<Host> filteredHosts = new ArrayList<Host>(hosts);
			filteredHosts.remove(host);
			List<VM> recoveredVMs = host.vms();
			vmCount += recoveredVMs.size();
			rp.add(solveGAP(filteredHosts,recoveredVMs),filteredHosts,recoveredVMs);
		}
		if (rp.recoveredVMsCount() == vmCount) {
			rp.full();
		}
		return rp;
	}

	private Bin[] solveGAP(List<Host> filteredHosts, List<VM> vms) {
		int[] binsCapacities = prepareCapacities(filteredHosts);
		int[][] itemSizes = prepareSizes(filteredHosts.size(), vms);
		double[][] itemPrices = preparePrices(filteredHosts, vms);
		return gap.solve(binsCapacities, itemSizes, itemPrices);
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
				itemSizes[binIdx][vmIdx] = vms.get(vmIdx).size();
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
