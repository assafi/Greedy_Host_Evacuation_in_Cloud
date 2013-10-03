/**
 * Greedy_Host_Evacuation_in_Cloud - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2013
 * Created: Jul 1, 2013
 */
package il.ac.technion.glpk;

import il.ac.technion.datacenter.physical.PhysicalAffinity;
import il.ac.technion.rigid.RecoveryPlan;

import java.util.List;

public interface VMRP {
	public RecoveryPlan solve(List<PhysicalAffinity> pal) throws Exception;
}
