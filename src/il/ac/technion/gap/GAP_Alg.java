/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 12/03/2012
 */
package il.ac.technion.gap;

import il.ac.technion.datacenter.physical.Host;
import il.ac.technion.datacenter.vm.VM;
import il.ac.technion.knapsack.Bin;

import java.util.List;

public abstract class GAP_Alg {
	
	public abstract Bin[] solve(int[] binsCapacities, int[][] _itemSizes, double[][] _itemWeights);
	
	public void solve(List<Host> hosts, List<VM> vms) {
		int[] binsCapacities = GapUtils.prepareCapacitiesVector(hosts);
		int[][] itemSizes = GapUtils.prepareSizesMatrix(hosts.size(), vms);
		double[][] itemCosts = GapUtils.prepareWeightsMatrix(hosts, vms);
		
		Bin[] answer = solve(binsCapacities,itemSizes,itemCosts);
		
		GapUtils.binsToAssignments(answer,hosts,vms);
	}
}
