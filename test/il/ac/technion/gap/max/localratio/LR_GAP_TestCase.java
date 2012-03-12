/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 12/03/2012
 */
package il.ac.technion.gap.max.localratio;

import il.ac.technion.gap.GAP_Alg;
import il.ac.technion.gap.GAP_Exception;
import il.ac.technion.gap.max.guice.LR_GAP_Module;
import il.ac.technion.misc.Bin;
import il.ac.technion.misc.Item;
import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class LR_GAP_TestCase {
	private Injector inj = Guice.createInjector(new LR_GAP_Module());
	private GAP_Alg gap;

	@Before
	public void init() {
		gap = inj.getInstance(GAP_Alg.class);
	}

	/**
	 * Example taken from <a href=
	 * "http://www.cs.technion.ac.il/~lirank/pubs/2006-IPL-Generalized-Assignment-Problem.pdf"
	 * ><b>An Efficient Approximation for the Generalized Assignment
	 * Problem</b></a>. Assumes optimal solution of Knapsack.
	 * 
	 * @throws GAP_Exception
	 */
	@Test
	public void testGAP() throws GAP_Exception {
		int[] bins = new int[] { 2, 3, 4 };
		int[][] sizes = new int[][] { 
				{ 1, 2, 2, 1 }, 
				{ 1, 3, 3, 2 },
				{ 1, 3, 4, 3 } };
		int[][] profits = new int[][] { 
				{ 3, 1, 5, 25 }, 
				{ 1, 1, 15, 15 },
				{ 5, 1, 25, 5 } };
		Bin[] answer = gap.solve(bins, sizes, profits);
		Assert.assertEquals(2, answer[0].capacity);
		Assert.assertTrue(answer[0].assignedItems().contains(new Item(0,1, 3)));
		Assert.assertTrue(answer[0].assignedItems().contains(new Item(3,1, 25)));
		Assert.assertEquals(0, answer[1].assignedItems().size());
		Assert.assertEquals(1, answer[2].assignedItems().size());
		Assert.assertTrue(answer[2].assignedItems().contains(new Item(2,4, 25)));
	}
}
