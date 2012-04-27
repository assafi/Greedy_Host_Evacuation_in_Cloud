/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 23/04/2012
 */
package il.ac.technion.config;

import il.ac.technion.datacenter.physical.Host;
import il.ac.technion.datacenter.sla.Range;
import il.ac.technion.datacenter.sla.guice.AppEngineSLAModule;
import il.ac.technion.datacenter.sla.guice.CustomSLAModule;
import il.ac.technion.datacenter.sla.guice.HA_TableSLAModule;
import il.ac.technion.datacenter.sla.guice.LinearSLAModule;
import il.ac.technion.gap.guice.ProductionGAPModule;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.joda.time.Period;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class TestConfiguration {
	
	private File dataFile ;
	private List<Host> backupHosts;
	private Period vmBootTime;
	private Period hostBootTime;
	private Injector injector;
	private double aCost;

	public TestConfiguration(String configFilePath) throws IOException, XPathExpressionException, ParserConfigurationException, SAXException, ConfigurationException {
		File cFile = new File(configFilePath);
		if (!cFile.exists()) {
			throw new IOException("File not found");
		}
		
		if (!cFile.canRead()) {
			throw new IOException("No read permissions");
		}
		
		parseFile(cFile);
	}

	private void parseFile(File cFile) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException, ConfigurationException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(cFile);
		
		XPathFactory xFactory = XPathFactory.newInstance();
		XPath xpath = xFactory.newXPath();
		
		dataFile = getDataFile(doc,xpath);
		backupHosts = getBackupHosts(doc,xpath);
		vmBootTime = getVmBootTime(doc,xpath);
		injector = getInjector(doc,xpath);
	}

	private File getDataFile(Document doc, XPath xpath) throws XPathExpressionException, ConfigurationException {
		XPathExpression expr = xpath.compile("//dataFile/text()");
		String dataFilePath = (String)expr.evaluate(doc,XPathConstants.STRING);
		
		dataFilePath = getClass().getResource(dataFilePath).getPath();
		File dataFile = new File(dataFilePath);
		
		if (!dataFile.exists()) {
			throw new ConfigurationException("Invalid data file");
		}
		
		if (!dataFile.canRead()) {
			throw new ConfigurationException("No read permissions for data file");
		}
		
		return dataFile;
	}
	
	private List<Host> getBackupHosts(Document doc, XPath xpath) throws XPathExpressionException, ConfigurationException {

		int numHosts = getNumberFromXML("//backupHosts/@num",doc,xpath);
		int hostsCapacity = getNumberFromXML("//backupHosts/capacity/text()",doc,xpath);
		aCost = getDoubleFromXML("//activationCost/text()",doc,xpath);
		int bootTimeInSeconds = getNumberFromXML("//backupHosts/hostBootTime/text()",doc,xpath);
		
		hostBootTime = Period.seconds(bootTimeInSeconds);
		
		List<Host> backupHosts = new ArrayList<Host>(numHosts);
		for (int i = 0; i < numHosts; i++) {
			backupHosts.add(new Host(i, hostsCapacity, aCost, hostBootTime));
		}
		return backupHosts;
	}
	
	private double getDoubleFromXML(String xpathStr, Document doc, XPath xpath) throws XPathExpressionException, ConfigurationException {
		XPathExpression expr = xpath.compile(xpathStr);
		String numString = (String)expr.evaluate(doc,XPathConstants.STRING);
		
		try {
			double num = Double.parseDouble(numString);
			return num;
		} catch (NumberFormatException nfe) {
			throw new ConfigurationException("Bad number", nfe);
		}
	}

	private int getNumberFromXML(String xpathStr, Document doc, XPath xpath) throws XPathExpressionException, ConfigurationException {
		XPathExpression expr = xpath.compile(xpathStr);
		String numString = (String)expr.evaluate(doc,XPathConstants.STRING);
		
		try {
			int num = Integer.parseInt(numString);
			return num;
		} catch (NumberFormatException nfe) {
			throw new ConfigurationException("Bad number", nfe);
		}
	}

	private Period getVmBootTime(Document doc, XPath xpath) throws XPathExpressionException, ConfigurationException {
		int vmBootTimeInSeconds = getNumberFromXML("//vmBootTime/text()", doc, xpath);
		return Period.seconds(vmBootTimeInSeconds);
	}
	
	private Injector getInjector(Document doc, XPath xpath) throws XPathExpressionException, ConfigurationException {
		XPathExpression expr = xpath.compile("//SLA/@type");
		String slaType = (String)expr.evaluate(doc,XPathConstants.STRING);
		AbstractModule productionModule = new ProductionGAPModule();
		
		if ("appEngine".equals(slaType))
			return Guice.createInjector(productionModule, new AppEngineSLAModule());
		
		if ("linear".equals(slaType))
			return Guice.createInjector(productionModule, new LinearSLAModule());
		
		if ("ha".equals(slaType)) 
			return Guice.createInjector(productionModule, new HA_TableSLAModule());
		
		if ("custom".equals(slaType)) {
			Map<Range,Double> table = getRanges(doc,xpath);
			return Guice.createInjector(productionModule, new CustomSLAModule(table));
		}
			
		throw new ConfigurationException("Bad SLA type");
	}

	private Map<Range,Double> getRanges(Document doc, XPath xpath) throws XPathExpressionException, ConfigurationException {
		Map<Range,Double> $ = new HashMap<Range, Double>();
		XPathExpression expr = xpath.compile("//range");
		NodeList nl = (NodeList)expr.evaluate(doc,XPathConstants.NODESET);
		
		for (int i = 0; i < nl.getLength(); i++) {
			Node rNode = nl.item(i);
			double start = getDoubleFromNode("start",rNode);
			double end = getDoubleFromNode("end",rNode);
			double cost = getDoubleFromNode("cost",rNode);
			
			$.put(new Range(start, end),cost);
		}
		return $;
	}

	private double getDoubleFromNode(String tag, Node rNode) throws ConfigurationException {
		NodeList nl = rNode.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {
			Node n = nl.item(i);
			if (n.getNodeName() == tag) {
				try {
					return Double.parseDouble(n.getTextContent());
				} catch (NumberFormatException nfe) {
					throw new ConfigurationException("Bad double", nfe);
				}
			}
		}
		throw new ConfigurationException("Invalid config file - missing " + tag + " tag");
	}

	public File getDataFile() {
		return dataFile;
	}

	public List<Host> getBackupHosts() {
		return backupHosts;
	}

	public Period getVmBootTime() {
		return vmBootTime;
	}

	public Period getHostBootTime() {
		return hostBootTime;
	}

	public Injector getInjector() {
		return injector;
	}

	public double getHostActivationCost() {
		return aCost;
	}
}
