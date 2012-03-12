/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 12/03/2012
 */
package il.ac.technion.knapsack.guice;

import il.ac.technion.knapsack.KnapsackAlg;
import il.ac.technion.knapsack.dp_knapsack.DP_Knapsack;

import com.google.inject.Guice;
import com.google.inject.Provides;

public class DP_KnapsackModule extends KnapsackModule {

	@Override
	protected void configure() {
	}

	@Provides
	public KnapsackAlg getKnapsack() {
		return new DP_Knapsack(Guice.createInjector(new EfficientPairsModule()));
	}
}
