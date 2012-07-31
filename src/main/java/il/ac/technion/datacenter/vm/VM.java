/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 12/03/2012
 */
package il.ac.technion.datacenter.vm;

import il.ac.technion.datacenter.physical.Host;
import il.ac.technion.datacenter.sla.SLA;
import il.ac.technion.misc.HashCodeUtil;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.Period;

import com.google.inject.Inject;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@XStreamAlias("VM")
public class VM {
	public final int id;
	public final SLA sla;
	public final VmDesciption type; 
	
	@XStreamOmitField
	private int fHashCode = 0;
	
	private Map<Host, Period> bootTimes = new HashMap<Host, Period>(); 
	
	@Inject
	public VM(int id, SLA sla, VmDesciption type) {
		this.id = id;
		this.sla = sla;
		this.type = type;
	}
	
	public VM(int id, int size, double contractCost, SLA sla) {
		this(id,size,contractCost, Period.ZERO, sla);
	}
	
	public VM(int id, int size, double contractCost, Period defaultBootTime, SLA sla) {
		this.id = id;
		this.sla = sla;
		this.type = new VmDesciption("Generic", size, defaultBootTime, contractCost);
	}
	
	public Period addBootTime(Host h, Period p) {
		return bootTimes.put(h, p);
	}
	
	public Period removeBootTime(Host h) {
		return bootTimes.remove(h);
	}
	
	public double cost(Host h) {
		Period hostBootTime = h.bootTime();
		Period vmBootTime = bootTimes.containsKey(h) ? bootTimes.get(h) : type.defaultBootTime;
		Period estimatedTotalBootTime = hostBootTime.plus(vmBootTime);
		return sla.compensation(estimatedTotalBootTime) 
				* type.getContractCost();
	}
	
	public int size() {
		return type.size;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof VM))
			return false;
		VM vm = (VM)obj;
		return id == vm.id && sla.equals(vm.sla) && 
			type.equals(vm.type);
	}
	
	@Override
	public int hashCode() {
		if (fHashCode  == 0) {
			int result = HashCodeUtil.SEED;
			result = HashCodeUtil.hash(result, id);
			result = HashCodeUtil.hash(result, sla);
			result = HashCodeUtil.hash(result, type);
			result = HashCodeUtil.hash(result, bootTimes);
			fHashCode = result;
		}
		return fHashCode;
	}
	
	@Override
	public String toString() {
		return "VM#" + id + " [" + type.size + "]";
	}
}
