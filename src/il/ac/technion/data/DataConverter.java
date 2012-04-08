/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 7 באפר 2012
 */
package il.ac.technion.data;

import il.ac.technion.datacenter.physical.Placement;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public interface DataConverter {
	public Placement convert(File file) throws IOException, DataException;
	public Placement convert(InputStream csvIn) throws IOException, DataException;
}
