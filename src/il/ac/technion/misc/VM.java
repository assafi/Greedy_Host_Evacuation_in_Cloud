/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 12/03/2012
 */
package il.ac.technion.misc;

import il.ac.technion.sla.SLA;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.Period;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("VM")
public class VM {
	public final int id;
	public final int ram;
	public final SLA sla;
	private Map<Host, Period> bootTimes = new HashMap<Host, Period>(); 
	
	public VM(int id, int ram, SLA sla) {
		this.id = id;
		this.ram = ram;
		this.sla = sla;
	}
	
	public VM(int id, int ram, SLA sla, Map<Host, Period> bootTimes) {
		this(id,ram,sla);
		this.bootTimes = bootTimes;
	}
	
	public Period addBootTime(Host h, Period p) {
		return bootTimes.put(h, p);
	}
	
	public Period removeBootTime(Host h) {
		return bootTimes.remove(h);
	}
	
	public double cost(Host h) {
		if (bootTimes.containsKey(h)) {
			return sla.compensation(bootTimes.get(h));
		} 
		return sla.compensation();
	}
}
