/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 23/04/2012
 */
package il.ac.technion.config;

import java.io.File;
import java.io.IOException;

public class TestConfiguration {
	
	public TestConfiguration(String configFilePath) throws IOException {
		File cFile = new File(configFilePath);
		if (!cFile.exists() || cFile.canRead()) {
			throw new IOException("Bad file path");
		}
		// TBD
	}
}
