/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 05/04/2012
 */
package il.ac.technion.datacenter;

import il.ac.technion.datacenter.sla.SLA;

public enum VmType {
	
 GENERIC,
 SMALL, MEDIUM,LARGE;
 
 public final int ram;
 public final double contractCost;
 public final SLA sla;
 
 public VmType(int ram, double contractCost, SLA sla) {
	 this.ram = ram;
	 this.contractCost = contractCost;
	 this.sla = sla;
 }
}

