/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 12/03/2012
 */
package il.ac.technion.xstream_conversion;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ 
il.ac.technion.xstream_conversion.HostConversionTest.class,
il.ac.technion.xstream_conversion.SLAConversionTest.class,
il.ac.technion.xstream_conversion.VMConversionTest.class,
il.ac.technion.xstream_conversion.PlacementConversionTest.class,
})
public class AllTests {}
