/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 17/05/2012
 */
package il.ac.technion.datacenter.physical;

import org.joda.time.Period;

public class HostConfig {

	public final int capacity;
	public final int freeCapacity;
	public final double activationCost;
	public final Period bootTime;
	
	public HostConfig(int capacity, int freeCapacity, double activationCost,
			Period bootTime) {
		this.capacity = capacity;
		this.freeCapacity = freeCapacity;
		this.activationCost = activationCost;
		this.bootTime = bootTime;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!(obj instanceof HostConfig)) return false;
		HostConfig hc = (HostConfig)obj;
		return hc.capacity == capacity && hc.freeCapacity == freeCapacity &&
			hc.activationCost == activationCost && hc.bootTime == bootTime; 
	}

}
