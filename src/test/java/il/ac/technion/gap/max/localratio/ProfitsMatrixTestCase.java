/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 12/03/2012
 */
package il.ac.technion.gap.max.localratio;

import java.util.ArrayList;
import java.util.Collection;

import junit.framework.Assert;

import org.junit.Ignore;
import org.junit.Test;

public class ProfitsMatrixTestCase {

	@Test
	public void testGetResidualProfitMatrix() throws LastProfitColumnException {
		ProfitsMatrix pm1 = new ProfitsMatrix(new double[][] {{1},{2}});
		Assert.assertNotNull(pm1.getCurrentColumn());
		Assert.assertEquals(1.0, pm1.getCurrentColumn()[0]);
		Collection<Integer> idxCollection = new ArrayList<Integer>(1);
		idxCollection.add(0);
		ProfitsMatrix pm2 = pm1.getResidualProfitMatrix(idxCollection);
		Assert.assertNotNull(pm2.getCurrentColumn());
		Assert.assertEquals(1.0, pm1.getCurrentColumn()[0]);
		Assert.assertTrue(pm2.lastColumn());
		Assert.assertFalse(pm1.lastColumn());
		Assert.assertEquals(1.0, pm1.getCurrentColumn()[0]);
	}
	
	@Ignore
	@Test(expected = com.google.java.contract.PreconditionError.class)
	public void testGetLastResidualProfitMatrix() {
		ProfitsMatrix pm1 = new ProfitsMatrix(new double[][] {{1}});
		pm1.getResidualProfitMatrix(null);
	}

}
