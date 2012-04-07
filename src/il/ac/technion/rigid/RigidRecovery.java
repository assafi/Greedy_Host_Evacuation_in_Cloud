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
import il.ac.technion.gap.GapUtils;
import il.ac.technion.knapsack.Bin;

import java.util.ArrayList;
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
		int[] binsCapacities = GapUtils.prepareCapacitiesVector(filteredHosts);
		int[][] itemSizes = GapUtils.prepareSizesMatrix(filteredHosts.size(), vms);
		double[][] itemPrices = GapUtils.prepareWeightsMatrix(filteredHosts, vms);
		return gap.solve(binsCapacities, itemSizes, itemPrices);
	}
}
