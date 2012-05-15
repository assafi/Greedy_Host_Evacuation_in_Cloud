/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 12/03/2012
 */
package il.ac.technion.datacenter.physical;

import il.ac.technion.datacenter.vm.VM;
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

	private static final String delim = System.getProperty("line.separator");
	@XStreamConverter(BinConverter.class)
	private Bin bin;
	private List<VM> vms = new LinkedList<VM>();
	private boolean active = false;
	private PhysicalAffinity affinity = null;

	@XStreamConverter(SimplePeriodConverter.class)
	private final Period bootTime;

	@XStreamAlias("activationCost")
	private double activationCost;

	@XStreamOmitField
	private int fHashCode = 0;

	public Host(Bin bin, double activationCost, Period bootTime) {
		this.bin = bin;
		this.activationCost = activationCost;
		this.bootTime = bootTime;
		this.active = (activationCost == 0.0);
	}

	public Host(int id, int ram, double activationCost, Period bootTime) {
		this(new Bin(id, ram), activationCost, bootTime);
	}

	public Host(Host h) {
		this(h.bin, h.activationCost, h.bootTime);
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
			return Period.ZERO;
		return bootTime;
	}

	public boolean isActive() {
		return active;
	}

	public void activate() {
		this.active = true;
	}

	public void deactivate() {
		this.active = false;
	}

	public double cost() {
		return (active ? activationCost : 0.0);
	}

	public boolean assign(VM vm) {
		Item i = new Item(vm.id, vm.size(), vm.cost(this));
		return bin.assign(i) && vms.add(vm);
	}

	public boolean unassign(VM vm) {
		Item i = new Item(vm.id, vm.size(), vm.cost(this));
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

	public int usedCapacity() {
		return bin.capacity - freeCapacity();
	}

	@Override
	public int hashCode() {
		if (fHashCode == 0) {
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
	
	public boolean join(PhysicalAffinity a) {
		if (null != affinity) {
			affinity.leave(this);
		}
		return a.join(this);
	}
	
	public boolean leave() {
		if (null != affinity) {
			return affinity.leave(this);
		}
		return false;
	}

	public Host clone(int id) {
		return new Host(id,bin.capacity,activationCost,bootTime);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Host #" + id() + " [ Total: " + bin.capacity + " | Used: "
				+ usedCapacity() + " | Free: " + freeCapacity() + " ] - ");
		if (!active)
			sb.append("in");
		sb.append("active ");
		if (vms.isEmpty()) {
			sb.append("and empty");
			return sb.toString();
		}
		sb.append(delim + "VMs: ");
		for (VM vm : vms) {
			sb.append(vm + ", ");
		}
		return sb.toString();
	}

	public void setActivationCost(double ac) {
		this.activationCost = ac;
	}
}
