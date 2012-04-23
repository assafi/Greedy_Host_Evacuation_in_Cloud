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

import com.google.inject.Injector;

public abstract class DataConverter {
	
	public final Injector slaInjector;
	
	public DataConverter(Injector inj) {
		this.slaInjector = inj;
	}
	
	public abstract Placement convert(File file) throws IOException, DataException;
	public abstract Placement convert(InputStream csvIn) throws IOException, DataException;
}
