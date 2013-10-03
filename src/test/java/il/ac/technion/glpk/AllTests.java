/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 12/03/2012
 */
package il.ac.technion.glpk;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ 
VMRPHighLevelImplTests.class,
VMRPLowLevelImplTests.class,})
public class AllTests {}
