/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 11/04/2012
 */
package il.ac.technion.knapsack.dp_knapsack;

import il.ac.technion.knapsack.Item;
import il.ac.technion.knapsack.KnapsackAlg;
import il.ac.technion.knapsack.dp_knapsack.ep.Pair;

import java.util.Arrays;

import junit.framework.Assert;

import org.junit.Test;

/**
 * @author Assaf
 *
 */
public abstract class KnapsackTestCase {
	private static final Item item1 = new Item(1,1,1);
	private static final Item item2 = new Item(2,2,2);
	
	private static final Pair noSolution = new Pair(0,0);
	
	private KnapsackAlg knapsack;
	
	protected KnapsackTestCase(KnapsackAlg knapsack) {
		this.knapsack = knapsack;
	}
	
	@Test
	public void testNoSolution() {
		Assert.assertEquals(noSolution, knapsack.solve(Arrays.asList(item1,item2), 0));
	}

	@Test
	public void testSolution() {
		Assert.assertEquals(new Pair(2,2), knapsack.solve(Arrays.asList(item1,item2), 2));
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
		Pair res = knapsack.solve(Arrays.asList(green,grey,yellow,red,blue),15);
		Assert.assertEquals(new Pair(8,15), res); //all but green
		Assert.assertFalse(res.items().contains(green));
		Assert.assertTrue(res.items().contains(grey));
	}
}
