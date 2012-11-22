/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 12/03/2012
 */
package il.ac.technion.datacenter.physical;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	il.ac.technion.datacenter.physical.HostTest.class,
	il.ac.technion.datacenter.physical.PlacementTest.class,
})
public class AllTests {}
