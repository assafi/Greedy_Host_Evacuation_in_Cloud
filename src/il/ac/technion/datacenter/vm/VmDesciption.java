/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 05/04/2012
 */
package il.ac.technion.datacenter.vm;

import il.ac.technion.misc.HashCodeUtil;

import org.joda.time.Period;

import com.thoughtworks.xstream.annotations.XStreamOmitField;

public class VmDesciption {
	public final String description;
	public final int size;
	public final double contractCost;
	public final Period defaultBootTime;

	@XStreamOmitField
	private int fHashCode = 0;
	
	public VmDesciption(String description,  int size, Period defaultBootTime, double contractCost) {
		this.description = description;
		this.size = size;
		this.defaultBootTime = defaultBootTime;
		this.contractCost = contractCost;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof VmDesciption))	{
			return false;
		}
		VmDesciption vmd = (VmDesciption)obj;
		return description.equals(vmd.description) &&
				size == vmd.size &&
				contractCost == vmd.contractCost;
	}
	
	@Override
	public int hashCode() {
		if (fHashCode  == 0) {
			int result = HashCodeUtil.SEED;
			result = HashCodeUtil.hash(result, description);
			result = HashCodeUtil.hash(result, size);
			result = HashCodeUtil.hash(result, contractCost);
			fHashCode = result;
		}
		return fHashCode;
	}
}

