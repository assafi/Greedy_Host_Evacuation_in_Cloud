/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 7 ���� 2012
 */
package il.ac.technion.data;

import il.ac.technion.datacenter.physical.Placement;

import java.io.File;

public interface DataConverter {
	public Placement convert(File file);
}