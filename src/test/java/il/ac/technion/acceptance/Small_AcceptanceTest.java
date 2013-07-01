/**
 * Greedy_Host_Evacuation_in_Cloud - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2013
 * Created: Jun 30, 2013
 */
package il.ac.technion.acceptance;

import il.ac.technion.config.TestConfiguration;

public class Small_AcceptanceTest extends AbstractAcceptanceTest {

	private static final String configFileName = "small_test_config.xml";
	
	public Small_AcceptanceTest() throws Exception {
		super(new TestConfiguration(IBM_AcceptanceTest.class.getResource(configFileName).getPath()));
	}
}
