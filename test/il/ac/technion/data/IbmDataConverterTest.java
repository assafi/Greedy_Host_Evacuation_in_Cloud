/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 08/04/2012
 */
package il.ac.technion.data;


import il.ac.technion.datacenter.physical.Placement;

import java.io.IOException;
import java.io.InputStream;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class IbmDataConverterTest {

	private static final String fileName = "data1.csv";
	private InputStream csvIn = null;
	private DataConverter dc = null;
	
	@Before
	public void setUp() throws Exception {
		csvIn = getClass().getResourceAsStream(fileName);
		dc = new IbmDataCoverter();
	}
	
	@Test
	public void IbmDataTest() throws IOException, DataException {
		Placement p = dc.convert(csvIn);
		Assert.assertEquals(137, p.numHosts());
		Assert.assertEquals(1740, p.numVMs());
	}
}
