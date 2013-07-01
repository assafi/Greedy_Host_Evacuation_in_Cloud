/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 20 бреб 2012
 */
package il.ac.technion.experiments;

import il.ac.technion.config.ConfigurationException;
import il.ac.technion.config.TestConfiguration;
import il.ac.technion.data.DataException;
import il.ac.technion.datacenter.physical.Placement;
import il.ac.technion.gap.guice.ProductionModule;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;

public class ExperimentsRunner {

	private static final String delim = System.getProperty("line.separator");
	private static Logger logger = Logger.getLogger(ExperimentsRunner.class);

	private TestConfiguration tConfig;

	private Placement p = null;

	private List<Experimentable> experiments = null;
	
	private static final int NUM_ARGS = 1;
	private static final int E_BADARGS = 65;
	
	public ExperimentsRunner(File configFile) throws IOException, DataException, XPathExpressionException, ParserConfigurationException, SAXException, ConfigurationException {
		this.tConfig = new TestConfiguration(configFile);
		Injector experimentsInjector = Guice.createInjector(new ExperimentsModule(tConfig), new ProductionModule());
		this.experiments = experimentsInjector.getInstance(Key.get(new TypeLiteral<List<Experimentable>>(){}, Experiment.class));
		this.p = experimentsInjector.getInstance(Placement.class);
	}
	
	public static void main(String[] args) {
		
		if (args.length != NUM_ARGS) {
			usage();
			System.exit(E_BADARGS);
		}
		
		String configFilePath = args[0];
		File configFile = new File(configFilePath);
		if (!configFile.exists() || !configFile.canRead()) {
			usage("Invalid config file: " + configFilePath);
			System.exit(E_BADARGS);
		}
		
		try {
			ExperimentsRunner experimentSuite = new ExperimentsRunner(configFile);
			experimentSuite.runExperiments();
		} catch (XPathExpressionException | IOException | DataException
				| ParserConfigurationException | SAXException | ConfigurationException e) {
			System.err.println(e.getMessage());
			System.err.println("See log file for stack trace.");
			logger.fatal(e.getMessage());
			for (StackTraceElement ste : e.getStackTrace()) {
				logger.fatal(ste);
			}
			System.exit(E_BADARGS);
		}
	}
	
	private static void usage() {
		System.err.println("Arguments: <config-file>");
	}

	private static void usage(String errMsg) {
		System.err.println("Error: " + errMsg);
		usage();
	}
	
	private void runExperiments() {
		logger.info("===== Experiments Placement =====" + delim);
		logger.info(p + delim);
		for (Experimentable experiment : experiments) {
			System.out.print(experiment.runExperiment() + ",");
		}
		System.out.println();
	}
}
