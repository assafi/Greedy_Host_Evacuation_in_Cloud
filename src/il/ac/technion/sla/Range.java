/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 17/03/2012
 */
package il.ac.technion.sla;

import il.ac.technion.misc.HashCodeUtil;

import com.google.java.contract.Requires;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 *	Represents a left-closed numeric range [a,b). 
 */
@XStreamAlias("range")
public class Range {

	public final double left;
	public final double right;
	
	@XStreamOmitField
	private int fHashCode = 0; //Lazy evaluation
	
	@Requires("left < right")
	public Range(double left, double right) {
		this.left = left;
		this.right = right;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!(obj instanceof Range)) return false;
		Range r = (Range)obj;
		return left == r.left && right == r.right;
	}
	
	@Override
	public String toString() {
		return "[" + left + "," + right + ")";
	}
	
	@Override
	public int hashCode() {
		if (fHashCode == 0) {
			int result = HashCodeUtil.SEED;
			result = HashCodeUtil.hash(result, left);
			result = HashCodeUtil.hash(result, right);
			fHashCode = result;
		}
		return fHashCode;
	}
	
	public boolean inRange(double value) {
		return left < value && value <= right;
	}
}
