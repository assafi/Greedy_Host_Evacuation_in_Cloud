/**
 * Greedy_Host_Evacuation_in_Cloud - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2013
 * Created: Jun 30, 2013
 */
package il.ac.technion.experiments;

import il.ac.technion.datacenter.physical.PhysicalAffinity;
import il.ac.technion.datacenter.physical.Placement;
import il.ac.technion.glpk.VMRPLowLevelImpl;
import il.ac.technion.rigid.RecoveryPlan;

import java.util.List;

import org.apache.log4j.Logger;

import com.google.inject.Inject;

public class OptimalRecovery_Experiment implements Experimentable {
	
	private static Logger logger = Logger.getLogger(OptimalRecovery_Experiment.class);
	private static final String delim = System.getProperty("line.separator");
	
	private VMRPLowLevelImpl vmrp = null;
	private Placement p = null;
	private int numRacks = 1;
	
	@Inject
	public OptimalRecovery_Experiment(VMRPLowLevelImpl vmrp, Placement p, int numRacks) {
		this.vmrp = vmrp;
		this.p = p;
		this.numRacks = numRacks;
	}
	
	@Override
	public String runExperiment() {
		p.reset();
		logger.info(delim + "===== Optimal Rack Recovery with Backup Hosts Activation =====");

		List<PhysicalAffinity> la = p.groupHostsToNAffinities("Rack",numRacks);
		int maxNumberOfHostsInRack = la.get(0).size();
		logger.info("Max number of hosts in rack: " + maxNumberOfHostsInRack);

		RecoveryPlan rp;
		try {
			rp = vmrp.solve(la);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return "No solution";
		}
		logger.info(delim + rp.summary());
//		 logger.info(delim + rp);
		return rp.csvReport();
	}

}
