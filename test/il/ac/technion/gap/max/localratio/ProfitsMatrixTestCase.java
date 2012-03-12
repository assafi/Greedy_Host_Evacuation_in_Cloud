/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 12/03/2012
 */
package il.ac.technion.gap.max.localratio;

import il.ac.technion.gap.max.localratio.LastProfitColumnException;
import il.ac.technion.gap.max.localratio.ProfitsMatrix;

import java.util.ArrayList;
import java.util.Collection;

import junit.framework.Assert;

import org.junit.Test;

public class ProfitsMatrixTestCase {

	/**
	 * Test method for {@link il.ac.technion.max_gap.lr_gap.ProfitsMatrix#getResidualProfitMatrix(java.util.Collection)}.
	 * @throws LastProfitColumnException 
	 */
	@Test
	public void testGetResidualProfitMatrix() throws LastProfitColumnException {
		ProfitsMatrix pm1 = new ProfitsMatrix(new int[][] {{1},{2}});
		Assert.assertNotNull(pm1.getCurrentColumn());
		Assert.assertEquals(1, pm1.getCurrentColumn()[0]);
		Collection<Integer> idxCollection = new ArrayList<Integer>(1);
		idxCollection.add(0);
		ProfitsMatrix pm2 = pm1.getResidualProfitMatrix(idxCollection);
		Assert.assertNotNull(pm2.getCurrentColumn());
		Assert.assertEquals(1, pm1.getCurrentColumn()[0]);
		Assert.assertTrue(pm2.lastColumn());
		Assert.assertFalse(pm1.lastColumn());
		Assert.assertEquals(1, pm1.getCurrentColumn()[0]);
	}
	
	@Test(expected = LastProfitColumnException.class)
	public void testGetLastResidualProfitMatrix() throws LastProfitColumnException {
		ProfitsMatrix pm1 = new ProfitsMatrix(new int[][] {{1}});
		pm1.getResidualProfitMatrix(null);
	}

}
