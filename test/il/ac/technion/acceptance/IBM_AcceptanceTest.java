/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 08/04/2012
 */
package il.ac.technion.acceptance;

import il.ac.technion.config.TestConfiguration;

public class IBM_AcceptanceTest extends AbstractAcceptanceTest {
	private static final String configFileName = "test_config.xml";
	
	public IBM_AcceptanceTest() throws Exception {
		super(new TestConfiguration(IBM_AcceptanceTest.class.getResource(configFileName).getPath()));
	}
}
