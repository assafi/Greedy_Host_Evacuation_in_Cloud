/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 08/04/2012
 */
package il.ac.technion.datacenter.physical.guice;

import il.ac.technion.datacenter.physical.Host;
import il.ac.technion.datacenter.physical.Placement;
import il.ac.technion.datacenter.vm.VM;
import il.ac.technion.gap.GAP_Alg;
import il.ac.technion.gap.GapUtils;
import il.ac.technion.gap.PackingGapUtils;
import il.ac.technion.gap.max.localratio.LR_GAP;
import il.ac.technion.gap.min.conversion.Min_Max_GAP_Conversion;
import il.ac.technion.knapsack.KnapsackAlg;
import il.ac.technion.knapsack.dp_knapsack.matrix.DP_Matrix_Knapsack;

import java.util.List;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.google.inject.name.Names;

public class PlacementModule extends AbstractModule {

	private List<Host> hosts = null;
	private List<VM> vms = null;
	
	public PlacementModule(List<Host> hosts, List<VM> vms) {
		this.hosts = hosts;
		this.vms = vms;
	}
	
	@Override
	protected void configure() {
		bind(GAP_Alg.class).annotatedWith(Names.named("Min GAP")).to(Min_Max_GAP_Conversion.class);
		bind(GAP_Alg.class).annotatedWith(Names.named("Max GAP")).to(LR_GAP.class);
		bind(GapUtils.class).to(PackingGapUtils.class);
		bind(KnapsackAlg.class).toInstance(new DP_Matrix_Knapsack());
	}
	
	@Provides
	public Placement getPlacement(@Named("Max GAP") GAP_Alg gap) {
		return new Placement(hosts,vms,gap);
	}
}
