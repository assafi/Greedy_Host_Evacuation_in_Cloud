/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 05/04/2012
 */
package il.ac.technion.datacenter.vm;

import com.thoughtworks.xstream.annotations.XStreamOmitField;

import il.ac.technion.datacenter.sla.SLA;
import il.ac.technion.misc.HashCodeUtil;

public class VmDesciption {
	public final String description;
	public final int size;
	public final double contractCost;
	public final SLA sla;

	@XStreamOmitField
	private int fHashCode = 0;
	
	public VmDesciption(String description,  int size, double contractCost, SLA sla) {
		this.description = description;
		this.size = size;
		this.contractCost = contractCost;
		this.sla = sla;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof VmDesciption))	{
			return false;
		}
		VmDesciption vmd = (VmDesciption)obj;
		return description.equals(vmd.description) &&
				size == vmd.size &&
				contractCost == vmd.contractCost &&
				sla.equals(vmd.sla);
	}
	
	@Override
	public int hashCode() {
		if (fHashCode  == 0) {
			int result = HashCodeUtil.SEED;
			result = HashCodeUtil.hash(result, description);
			result = HashCodeUtil.hash(result, size);
			result = HashCodeUtil.hash(result, contractCost);
			result = HashCodeUtil.hash(result, sla);
			fHashCode = result;
		}
		return fHashCode;
	}
}

