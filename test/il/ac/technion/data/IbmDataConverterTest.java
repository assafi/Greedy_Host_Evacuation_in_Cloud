/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 08/04/2012
 */
package il.ac.technion.data;


import il.ac.technion.config.TestConfiguration;
import il.ac.technion.datacenter.physical.Placement;
import junit.framework.Assert;

import org.junit.Test;

public class IbmDataConverterTest {

	private static final String configFileName = IbmDataConverterTest.class.getResource("test_config.xml").getPath();
	private DataConverter dc = new DataCoverterImpl();
	
	@Test
	public void IbmDataTest() throws Exception {
		Placement p = dc.convert(new TestConfiguration(configFileName));
		Assert.assertEquals(137 + 10, p.numHosts());
		Assert.assertEquals(1740, p.numVMs());
	}
}
