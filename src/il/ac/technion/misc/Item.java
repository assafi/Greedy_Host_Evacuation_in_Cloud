/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 12/03/2012
 */
package il.ac.technion.misc;


public class Item {
	public final int size;
	public final int value;
	public final int id;

	public Item(int id, int size, int value) {
		this.size = size;
		this.value = value;
		this.id = id;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		Item anItem = (Item)obj;
		return this.id == anItem.id && this.size == anItem.size && this.value == anItem.value;
	}
	
	@Override
	public String toString() {
		return "[ ID:" + id + ",S:" + size + ",V:" + value + " ]";
	}
}
