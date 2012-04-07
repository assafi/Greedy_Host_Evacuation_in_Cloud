/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 12/03/2012
 */
package il.ac.technion.knapsack.dp_knapsack;

import il.ac.technion.knapsack.Item;
import il.ac.technion.knapsack.KnapsackAlg;
import il.ac.technion.knapsack.dp_knapsack.Pair;
import il.ac.technion.knapsack.guice.DP_KnapsackModule;

import java.util.Arrays;

import junit.framework.Assert;

import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class DP_KnapsackTestCase {

	private static final Item item1 = new Item(1,1,1);
	private static final Item item2 = new Item(2,2,2);
	
	private static final Pair noSolution = new Pair(0,0);
	
	private final Injector knapsackInj = Guice.createInjector(new DP_KnapsackModule());
	/**
	 * Test method for {@link il.ac.technion.knapsack.dp_knapsack.DP_Knapsack#solve()}.
	 */
	@Test
	public void testNoSolution() {
		KnapsackAlg DPK_alg = knapsackInj.getInstance(KnapsackAlg.class);
		Assert.assertEquals(noSolution, DPK_alg.solve(Arrays.asList(item1,item2), 0));
	}

	@Test
	public void testSolution() {
		KnapsackAlg DPK_alg = knapsackInj.getInstance(KnapsackAlg.class);
		Assert.assertEquals(new Pair(2,2), DPK_alg.solve(Arrays.asList(item1,item2), 2));
	}
	
	/**
	 * Test for example in <a href=http://en.wikipedia.org/wiki/Knapsack_problem>Wikipedia</a>. 
	 */
	@Test
	public void wikipediaExampleTest() {
		Item green = new Item(1,12,4);
		Item grey = new Item(2,1,2);
		Item yellow = new Item(3,4,10);
		Item red = new Item(4,1,1);
		Item blue = new Item(5,2,2);
		KnapsackAlg DPK_alg = knapsackInj.getInstance(KnapsackAlg.class);
		Pair res = DPK_alg.solve(Arrays.asList(green,grey,yellow,red,blue),15);
		Assert.assertEquals(new Pair(8,15), res); //all but green
		Assert.assertFalse(res.items().contains(green));
		Assert.assertTrue(res.items().contains(grey));
	}
}
