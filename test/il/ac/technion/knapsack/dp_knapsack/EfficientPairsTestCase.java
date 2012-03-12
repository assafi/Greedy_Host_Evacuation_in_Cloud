/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 12/03/2012
 */
package il.ac.technion.knapsack.dp_knapsack;

import il.ac.technion.knapsack.dp_knapsack.guice.IntrospectionEfficientPairsModule;
import il.ac.technion.knapsack.guice.EfficientPairsModule;
import il.ac.technion.misc.Item;

import java.util.Collection;
import java.util.LinkedList;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class EfficientPairsTestCase {

	private Collection<Pair> introCollection = new LinkedList<Pair>();
	private Injector introInjector = null, simpleInjector = null;
	private EfficientPairs eps1 = null, eps2 = null;

	@Before
	public void init() {
		introInjector = Guice.createInjector(new IntrospectionEfficientPairsModule(introCollection));
		simpleInjector = Guice.createInjector(new EfficientPairsModule());
		eps1 = introInjector.getInstance(EfficientPairs.class);
		eps2 = simpleInjector.getInstance(EfficientPairs.class);
	}

	public void testEmptyEPS() {
		Assert.assertEquals(new Pair(0,0), eps1.max());
	}

	/**
	 * Test method for {@link il.ac.technion.knapsack.dp_knapsack.EfficientPairs#build(il.ac.technion.knapsack.dp_knapsack.EfficientPairs, il.ac.technion.misc.Item, int)}.
	 */
	public void zeroCapacity() {
		eps1.build(eps2, new Item(1,1,1), 0);
		Assert.assertEquals(new Pair(0,0), eps1.max());
	}
	
	public void overCapacity() {
		eps1.build(eps2, new Item(1,1,1), 2);
		eps1.build(eps1, new Item(2,2,2), 2);
		Assert.assertEquals(new Pair(new Item(1,1,1)),eps1.max());
	}
	
	@Test
	public void build() {
		Item item1 = new Item(1,1,1);
		Item item2 = new Item(2,1,3);	
		
		eps2.build(eps1, item1, 2);
		eps1.build(eps2, item2, 2);

		Assert.assertEquals(new Pair(2,4),eps1.max());
		Assert.assertEquals(3, introCollection.size());
		Assert.assertTrue(introCollection.contains(new Pair(1,3)));
		Assert.assertFalse(introCollection.contains(new Pair(1,1)));
	}
}
