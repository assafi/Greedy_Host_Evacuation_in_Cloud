/**
 * Greedy_Host_Evacuation_in_Cloud - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2013
 * Created: Jun 27, 2013
 */
package il.ac.technion.glpk;

import static org.gnu.glpk.GLPK.glp_get_col_name;
import static org.gnu.glpk.GLPK.glp_get_num_cols;
import static org.gnu.glpk.GLPK.glp_get_obj_name;
import static org.gnu.glpk.GLPK.glp_mip_col_val;
import static org.gnu.glpk.GLPK.glp_mip_obj_val;
import il.ac.technion.datacenter.physical.Host;
import il.ac.technion.datacenter.vm.VM;
import il.ac.technion.misc.CollectionUtils;
import il.ac.technion.misc.Predicate;
import il.ac.technion.rigid.RecoveryPlan;

import java.util.List;
import java.util.Map;

import org.gnu.glpk.glp_prob;

class GLPKUtils {
	public static void writeMipSolution(glp_prob lp) {
		String probName = glp_get_obj_name(lp);
		double val = glp_mip_obj_val(lp);
		
		System.out.println(probName + " = " + val);
		System.out.println("****************");
		for (int i = 1; i <= glp_get_num_cols(lp); i++) {
			String varName = glp_get_col_name(lp,i);
			double colVal = glp_mip_col_val(lp,i);
			System.out.println(varName + " = " + colVal);
		}
	}
	
	public static RecoveryPlan extractRecoveryPlan(final glp_prob lp, final int y[], final int m[][],
			final Map<Host, List<Integer>> hostIdxMapping, final Map<VM, Integer> vmIdxMapping) {
		RecoveryPlan rp = new RecoveryPlan(hostIdxMapping.keySet());
		
		for (final Host h : hostIdxMapping.keySet()) {
			// Activate backup hosts
			List<Integer> markedHostEntities = CollectionUtils.filter(hostIdxMapping.get(h), new Predicate<Integer>(){
				@Override
				public boolean accept(Integer i) {
					return glp_mip_col_val(lp,y[i]) == 1.0;
				}
			});

			if (CollectionUtils.find(markedHostEntities, new Predicate<Integer>(){
				@Override
				public boolean accept(Integer i) {
					return glp_get_col_name(lp,y[i]).contains("off");
				}
			})) {
				h.deactivate();
			} else if (!markedHostEntities.isEmpty()){
				h.activate();
			}

			/*
			 * Ohh, how I wish Java was functional...
			 */
			rp.add(h, CollectionUtils.filter(vmIdxMapping.keySet(), new Predicate<VM>(){
				@Override
				public boolean accept(final VM vm) {
					return CollectionUtils.find(hostIdxMapping.get(h), new Predicate<Integer>(){
						@Override
						public boolean accept(Integer i) {
							return glp_mip_col_val(lp,m[i][vmIdxMapping.get(vm)]) == 1.0;
						}
					});
				}
			}));
		}
		return rp;
	}
}
