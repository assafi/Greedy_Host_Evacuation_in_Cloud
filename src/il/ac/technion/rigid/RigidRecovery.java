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

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class RigidRecovery {

	private GAP_Alg gap;
	
	@Inject
	public RigidRecovery(@Named("Min GAP") GAP_Alg gap) {
		this.gap = gap;
	}
	
	public RecoveryPlan solve(Collection<Host> hosts) {
		RecoveryPlan rp = new RecoveryPlan();
		for (Host host : hosts) {
			Collection<Host> filteredHosts = new ArrayList<Host>(hosts);
			filteredHosts.remove(host);
			rp.add(solveGAP(filteredHosts,host.vms()));
		}
	}

	/**
	 * @param filteredHosts
	 * @param vms
	 * @return
	 */
	private RecoveryPlan solveGAP(Collection<Host> filteredHosts, Collection<VM> vms) {
		int[] binsCapacities = prepareCapacities(filteredHosts);
		int[][] itemSizes = prepareSizes(filteredHosts.size(), vms);
		return null;
	}

	private int[][] prepareSizes(int numBins, Collection<VM> vms) {
		int[][] itemSizes = new int[numBins][vms.size()];
		return null;
	}

	private int[] prepareCapacities(Collection<Host> filteredHosts) {
		int[] binsCapacities = new int[filteredHosts.size()];
		int i = 0;
		Iterator<Host> iter = filteredHosts.iterator();
		while (iter.hasNext()) {
			binsCapacities[i++] = iter.next().freeCapacity();
		}
		return binsCapacities;
	}
}
