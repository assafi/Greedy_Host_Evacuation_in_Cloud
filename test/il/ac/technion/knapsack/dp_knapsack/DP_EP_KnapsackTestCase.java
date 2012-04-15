/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 12/03/2012
 */
package il.ac.technion.knapsack.dp_knapsack;

import il.ac.technion.knapsack.KnapsackAlg;
import il.ac.technion.knapsack.guice.DP_EP_KnapsackModule;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class DP_EP_KnapsackTestCase extends KnapsackTestCase {

	private final static Injector knapsackInj = Guice.createInjector(new DP_EP_KnapsackModule());
	private final static KnapsackAlg knapsack = knapsackInj.getInstance(KnapsackAlg.class);

	public DP_EP_KnapsackTestCase() {
		super(knapsack);
	}
}
