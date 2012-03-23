/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 12/03/2012
 */
package il.ac.technion.gap.min.conversion;

import il.ac.technion.gap.GAP_Alg;
import il.ac.technion.gap.GAP_Exception;
import il.ac.technion.gap.min.conversion.guice.MinGAP_LRMax_Module;
import il.ac.technion.misc.Bin;
import il.ac.technion.misc.Item;

import java.util.LinkedList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class Min_Max_GAP_Conversion_TestCase {

	Injector inj = Guice.createInjector(new MinGAP_LRMax_Module());
	
	@Test
	public void testModule() {
		GAP_Alg minGap = inj.getInstance(GAP_Alg.class);
		Assert.assertEquals(Min_Max_GAP_Conversion.class, minGap.getClass());
	}
	
	@Test
	public void testPartialSolution() throws GAP_Exception {
		int[] bins = new int[] { 2, 3, 4 };
		int[][] sizes = new int[][] { 
				{ 1, 2, 2, 1 }, 
				{ 1, 3, 3, 2 },
				{ 1, 3, 4, 3 } };
		double[][] costs = new double[][] { 
				{ 3, 1, 5, 25 }, 
				{ 1, 1, 15, 15 },
				{ 5, 1, 25, 5 } };
		testSolution(bins, sizes, costs,3);
	}

	private void testSolution(int[] bins, int[][] sizes, double[][] costs,int expected)
			throws GAP_Exception {
		GAP_Alg minGap = inj.getInstance(GAP_Alg.class);
		Bin[] answer = minGap.solve(bins, sizes, costs);
		for (Bin bin : answer) {
			System.out.println(bin);
		}
		
		int numAssignedItems = countItems(answer);
		Assert.assertEquals(3, numAssignedItems);
		
		assertUnassignedItems(answer,sizes);
	}
	
	private void assertUnassignedItems(Bin[] bins, int[][] sizes) {
		List<Integer> assignedItems = assignedItems(bins); 
		for (int i = 0; i < sizes[0].length; i++) {
			if (!assignedItems.contains(i)) {
				for (int j = 0; j < sizes.length; j++) {
					Assert.assertTrue(sizes[j][i] > bins[j].remainingCapacity());
				}
			}
		}
		
	}

	private List<Integer> assignedItems(Bin[] bins) {
		List<Integer> aItems = new LinkedList<Integer>();
		for (Bin bin : bins) {
			for (Item item : bin.assignedItems()) {
				aItems.add(item.id);
			}
		}
		return aItems;
	}

	private int countItems(Bin[] answer) {
		int count = 0; 
		for (Bin bin : answer) {
			count += bin.assignedItems().size();
		}
		return count;
	}

	@Test 
	public void testFullSolution() throws GAP_Exception {
		GAP_Alg minGap = inj.getInstance(GAP_Alg.class);
		int[] bins = new int[] { 2, 4, 4 };
		int[][] sizes = new int[][] { 
				{ 1, 2, 2, 1 }, 
				{ 1, 3, 3, 2 },
				{ 1, 3, 4, 3 } };
		double[][] profits = new double[][] { 
				{ 3, 1, 5, 25 }, 
				{ 1, 1, 15, 15 },
				{ 5, 1, 25, 5 } };
		Bin[] answer = minGap.solve(bins, sizes, profits);
		for (Bin bin : answer) {
			System.out.println(bin);
		}
		int numAssignedItems = countItems(answer);
		Assert.assertEquals(4, numAssignedItems);
		
		assertUnassignedItems(answer,sizes);		
	}
}
