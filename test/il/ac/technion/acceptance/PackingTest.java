/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 15/05/2012
 */
package il.ac.technion.acceptance;

import il.ac.technion.config.TestConfiguration;



public class PackingTest extends AbstractAcceptanceTest {
	private static final String configFileName = "test_config_packing.xml";

	public PackingTest() throws Exception {
		super(new TestConfiguration(IBM_AcceptanceTest.class.getResource(configFileName).getPath()));
	}
}
