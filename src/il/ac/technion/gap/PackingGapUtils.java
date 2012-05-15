/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 15/05/2012
 */
package il.ac.technion.gap;

import il.ac.technion.datacenter.physical.Host;
import il.ac.technion.datacenter.vm.VM;

import java.util.List;

public class PackingGapUtils extends GapUtils {

	@Override
	public double[][] prepareWeightsMatrix(List<Host> hosts, List<VM> vms) {
		double[][] itemPrices = new double[hosts.size()][vms.size()];
		for (int binIdx = 0; binIdx < hosts.size(); binIdx++) {
			for (int vmIdx = 0; vmIdx < vms.size(); vmIdx++) {
				VM vm = vms.get(vmIdx);
				itemPrices[binIdx][vmIdx] = (double)vm.size(); // This is used for packing scenarios
			}
		}
		return itemPrices;
	}
}
