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

import com.google.inject.Inject;

public abstract class GAP_Alg {
	
	protected GapUtils gu;
	
	@Inject
	public GAP_Alg(GapUtils gu) {
		this.gu = gu;
	}
	
	public abstract Bin[] solve(int[] binsCapacities, int[][] _itemSizes, double[][] _itemWeights);
	
	public Bin[] solve(List<Host> hosts, List<VM> vms, boolean assign) {
		int[] binsCapacities = gu.prepareCapacitiesVector(hosts);
		int[][] itemSizes = gu.prepareSizesMatrix(hosts.size(), vms);
		double[][] itemCosts = gu.prepareWeightsMatrix(hosts, vms);
		
		Bin[] answer = solve(binsCapacities,itemSizes,itemCosts);
		
		if (assign)
			gu.binsToAssignments(answer,hosts,vms);
		
		return answer;
	}
}
