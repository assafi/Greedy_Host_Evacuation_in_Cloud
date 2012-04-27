/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 7 באפר 2012
 */
package il.ac.technion.data;

import il.ac.technion.config.TestConfiguration;
import il.ac.technion.datacenter.physical.Host;
import il.ac.technion.datacenter.physical.Placement;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public abstract class DataConverter {
	
	public abstract Placement convert(TestConfiguration tConfig) throws IOException, DataException;
}
