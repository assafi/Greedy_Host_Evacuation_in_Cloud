/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 08/04/2012
 */
package il.ac.technion.acceptance;

import il.ac.technion.config.TestConfiguration;

public class IBM_AcceptanceTestWithLinearSLA_XL_BackupHosts extends
		AbstractAcceptanceTest {
	private static final String configFileName = "test_config_xl_backup.xml";

	public IBM_AcceptanceTestWithLinearSLA_XL_BackupHosts() throws Exception {
		super(new TestConfiguration(
				IBM_AcceptanceTestWithLinearSLA_XL_BackupHosts.class.getResource(
						configFileName).getPath()));
	}
}
