/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 12/03/2012
 */
package il.ac.technion.datacenter;

import il.ac.technion.knapsack.Bin;
import il.ac.technion.knapsack.Item;
import il.ac.technion.misc.HashCodeUtil;
import il.ac.technion.misc.xstream_converters.BinConverter;
import il.ac.technion.misc.xstream_converters.SimplePeriodConverter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.joda.time.Period;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@XStreamAlias("host")
public class Host {

	@XStreamConverter(BinConverter.class)
	private Bin bin;
	private List<VM> vms = new LinkedList<VM>(); 
	private boolean active = false;
	
	@XStreamConverter(SimplePeriodConverter.class)
	private final Period bootTime;
	
	@XStreamAlias("activationCost")
	final public double activationCost;
	
	@XStreamOmitField
	private int fHashCode = 0;

	public Host(Bin bin, double activationCost, Period bootTime) {
		this.bin = bin;
		this.activationCost = activationCost;
		this.bootTime = bootTime;
		this.active = (activationCost == 0.0);
	}
	
	public Host(int id, int ram, double activationCost, Period bootTime) {
		this(new Bin(id, ram),activationCost,bootTime);
	}
	
	public Host(Host h) {
		this(h.bin,h.activationCost,h.bootTime);
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

	public Period bootTime() {
		if (active) 
			return new Period();
		return bootTime;
	}
	
	public boolean isActive() {
		return active;
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
	
	public List<VM> vms() {
		return new ArrayList<VM>(vms);
	}
	
	public Collection<Item> items() {
		return bin.assignedItems();
	}
	
	public int freeCapacity() {
		return bin.remainingCapacity();
	}
	
	@Override
	public int hashCode() {
		if (fHashCode  == 0) {
			int result = HashCodeUtil.SEED;
			result = HashCodeUtil.hash(result, bin);
			result = HashCodeUtil.hash(result, activationCost);
			result = HashCodeUtil.hash(result, bootTime);
			fHashCode = result;
		}
		return fHashCode;
	}
	
	public int id() {
		return bin.id;
	}
}
