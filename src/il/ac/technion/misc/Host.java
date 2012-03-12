/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 12/03/2012
 */
package il.ac.technion.misc;

public class Host {

	private Bin bin;
	final public int activationCost;
	
	/**
	 * @param id
	 * @param capacity
	 */
	public Host(int id, int capacity, int activationCost) {
		this.bin = new Bin(id,capacity);
		this.activationCost = activationCost;
	}

}
