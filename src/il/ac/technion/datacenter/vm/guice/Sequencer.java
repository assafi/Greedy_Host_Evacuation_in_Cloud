package il.ac.technion.datacenter.vm.guice;

public enum Sequencer {
	INSTANCE;
	
	private int counter = 0;
	
	public int get() {
		return counter++;
	}
	
	public void reset() {
		this.counter = 0;
	}
}
