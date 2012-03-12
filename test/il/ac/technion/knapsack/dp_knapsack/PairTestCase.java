/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 12/03/2012
 */
package il.ac.technion.knapsack.dp_knapsack;

import il.ac.technion.knapsack.dp_knapsack.Pair;
import il.ac.technion.misc.Item;
import junit.framework.Assert;

import org.junit.Test;


public class PairTestCase {

	private final Pair base = new Pair(new Item(1,0,0));
	
	@Test
	public void testDomination() {
		
		Assert.assertTrue(base.dominate(base));
		
		Pair dominator = new Pair(base, new Item(2,0,1));
		Assert.assertTrue(dominator.dominate(base));
		Assert.assertFalse(base.dominate(dominator));
		
		Pair nutral = new Pair(base, new Item(3,1,1));
		Assert.assertFalse(base.dominate(nutral));
		Assert.assertFalse(nutral.dominate(base));
	}
}
