/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 12/03/2012
 */
package il.ac.technion.misc;

import il.ac.technion.misc.converters.BinConverter;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;

@XStreamAlias("host")
public class Host {

	@XStreamConverter(BinConverter.class)
	private Bin bin;

	@XStreamAlias("activationCost")
	final public int activationCost;

	/**
	 * @param id
	 * @param capacity
	 */
	public Host(int id, int capacity, int activationCost) {
		this.bin = new Bin(id, capacity);
		this.activationCost = activationCost;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !Host.class.equals(obj.getClass())) {
			return false;
		}
		Host host = (Host) obj;
		return bin.id == host.bin.id && bin.capacity == host.bin.capacity
				&& activationCost == host.activationCost;
	}
}
