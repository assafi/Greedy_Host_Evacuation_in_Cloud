/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 12/03/2012
 */
package il.ac.technion;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ 
il.ac.technion.gap.AllTests.class,
il.ac.technion.knapsack.dp_knapsack.AllTests.class,
il.ac.technion.xstream_conversion.AllTests.class,
il.ac.technion.datacenter.AllTests.class,
il.ac.technion.rigid.AllTests.class,
il.ac.technion.semi_rigid.AllTests.class,})
public class AllTests {}
