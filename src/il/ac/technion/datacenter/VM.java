/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 12/03/2012
 */
package il.ac.technion.datacenter;

import il.ac.technion.datacenter.sla.SLA;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.Period;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("VM")
public class VM {
	public final int id;
	public final int ram;
	public final double contractCost; 
	public final SLA sla;
	
	private Map<Host, Period> bootTimes = new HashMap<Host, Period>(); 
	
	public VM(int id, int ram, double contractCost, SLA sla) {
		this.id = id;
		this.ram = ram;
		this.contractCost = contractCost;
		this.sla = sla;
	}
	
	public VM(int id, int ram, double contractCost, SLA sla, Map<Host, Period> bootTimes) {
		this(id,ram,contractCost,sla);
		this.bootTimes = bootTimes;
	}
	
	public Period addBootTime(Host h, Period p) {
		return bootTimes.put(h, p);
	}
	
	public Period removeBootTime(Host h) {
		return bootTimes.remove(h);
	}
	
	public double cost(Host h) {
		Period hostBootTime = h.bootTime();
		Period vmBootTime = bootTimes.containsKey(h) ? bootTimes.get(h) : new Period();
		Period estimatedTotalBootTime = hostBootTime.plus(vmBootTime);
		return sla.compensation(estimatedTotalBootTime) * contractCost;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof VM))
			return false;
		VM vm = (VM)obj;
		return id == vm.id && ram == vm.ram &&
			contractCost == vm.contractCost &&
			sla.equals(vm.sla);
	}
}
