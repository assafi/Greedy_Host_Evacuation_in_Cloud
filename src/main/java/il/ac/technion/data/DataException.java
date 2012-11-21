/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 08/04/2012
 */
package il.ac.technion.data;

public class DataException extends Exception {

	private static final long serialVersionUID = 843250875570779642L;

	public DataException() {
		super("Data handling exception");
	}

	public DataException(String arg0) {
		super(arg0);
	}

	public DataException(Throwable arg0) {
		super(arg0);
	}

	public DataException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public DataException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

}
