/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 19/04/2012
 */
package il.ac.technion.acceptance;

import il.ac.technion.config.TestConfiguration;

public class IBM_AcceptanceTestWithHaSla_Small_BackupHosts extends
		AbstractAcceptanceTest {

	private static final String configFileName = "test_config_small_backup.xml";

	public IBM_AcceptanceTestWithHaSla_Small_BackupHosts() throws Exception {
		super(new TestConfiguration(
				IBM_AcceptanceTestWithHaSla_Small_BackupHosts.class.getResource(
						configFileName).getPath()));
	}
}
