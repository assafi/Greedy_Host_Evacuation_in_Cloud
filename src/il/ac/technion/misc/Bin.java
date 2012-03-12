/**
 * Greedy_Recovery - Software Design, 236700 - Technion
 * 
 * Author: Assaf Israel, 2012
 */
package il.ac.technion.misc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

public class Bin {

	public final int capacity;
	private int remainingCapacity;
	public final int id;
	
	private Collection<Item> assignedItems = new LinkedList<Item>();
	
	@Override
	public String toString() {
		StringBuilder desc =  new StringBuilder();
		desc.append("Bin #" + id + " [" + capacity + "]: ");
		for (Item item : assignedItems) {
			desc.append(item.toString());
		}
		return desc.toString();
	}
	
	public Bin(int _id, int _capacity) {
		this.capacity = _capacity;
		this.remainingCapacity = _capacity;
		this.id = _id;
	}

	public void assign(Item item) {
		assignedItems.add(item);
		remainingCapacity -= item.size;
	}
	
	public void unassign(Item item) {
		assignedItems.remove(item);
		remainingCapacity += item.size;
	}
	
	public Collection<Item> assignedItems() {
		return new ArrayList<Item>(assignedItems);
	}
	
	public int remainingCapacity() {
		return remainingCapacity;
	}
}
