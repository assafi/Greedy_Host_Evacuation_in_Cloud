/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 12/03/2012
 */
package il.ac.technion.gap.guice;

import il.ac.technion.gap.GAP_Alg;
import il.ac.technion.gap.GapUtils;
import il.ac.technion.gap.max.localratio.LR_GAP;
import il.ac.technion.gap.min.conversion.Min_Max_GAP_Conversion;
import il.ac.technion.knapsack.KnapsackAlg;
import il.ac.technion.knapsack.dp_knapsack.matrix.DP_Matrix_Knapsack;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

public class ProductionModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(GAP_Alg.class).to(Min_Max_GAP_Conversion.class);
		bind(GAP_Alg.class).annotatedWith(Names.named("Min GAP")).to(Min_Max_GAP_Conversion.class);
		bind(GAP_Alg.class).annotatedWith(Names.named("Max GAP")).to(LR_GAP.class);
		bind(GapUtils.class).toInstance(new GapUtils());
		bind(KnapsackAlg.class).toInstance(new DP_Matrix_Knapsack());
	}
	
//	@Provides
//	public KnapsackAlg getKnapsack() {
//		DP_EP_KnapsackModule dpm = new DP_EP_KnapsackModule();
//		return dpm.getEP_Knapsack();
//	}
}
