/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 15 бреб 2012
 */
package il.ac.technion.acceptance;

import il.ac.technion.config.TestConfiguration;

public class DowntimeNoiseTest extends AbstractAcceptanceTest {

	private static final String configFileName = "test_downtime_noise.xml";

	public DowntimeNoiseTest() throws Exception {
		super(new TestConfiguration(DowntimeNoiseTest.class.getResource(configFileName).getPath()));
	}
}
