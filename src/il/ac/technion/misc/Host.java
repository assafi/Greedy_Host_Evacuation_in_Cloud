/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 12/03/2012
 */
package il.ac.technion.misc;

import il.ac.technion.misc.converters.BinConverter;
import il.ac.technion.misc.converters.SimplePeriodConverter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import org.joda.time.Period;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;

@XStreamAlias("host")
public class Host {

	@XStreamConverter(BinConverter.class)
	private Bin bin;
	private Collection<VM> vms = new LinkedList<VM>(); 
	private boolean active = false;
	
	@XStreamConverter(SimplePeriodConverter.class)
	public final Period bootTime;
	
	@XStreamAlias("activationCost")
	final public double activationCost;

	public Host(int id, int capacity, double activationCost, Period bootTime) {
		this.bin = new Bin(id, capacity);
		this.activationCost = activationCost;
		this.bootTime = bootTime;
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
	
	public void activate() {
		this.active = true;
	}
	
	public void diactivate() {
		this.active = false;
	}
	
	public double cost() {
		return (active ? activationCost : 0.0);
	}
	
	public boolean assign(VM vm) {
		Item i = new Item(vm.id,vm.ram,vm.cost(this));
		return bin.assign(i) && vms.add(vm);
	}
	
	public boolean unassign(VM vm) {
		Item i = new Item(vm.id,vm.ram,vm.cost(this));
		return vms.remove(vm) && bin.unassign(i);
	}
	
	public Collection<VM> vms() {
		return new ArrayList<VM>(vms);
	}
	
	public Collection<Item> items() {
		return bin.assignedItems();
	}
	
	public int freeCapacity() {
		return bin.remainingCapacity();
	}
}
