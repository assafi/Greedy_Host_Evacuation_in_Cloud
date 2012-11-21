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
import il.ac.technion.gap.guice.ProductionModule;

import java.io.File;
import java.io.IOException;
import java.net.URL;
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
	private int numAffinities;
	private boolean pack;
	private double baseCost;
	private double costPerSize;
	private int numExpensiveRacks;
	private boolean distributedExpensive;
	private int numExpensiveVMs;
	private boolean expensive;
	private DowntimeType downtimeType;
	private double downtimeNoise;
	
	public enum DowntimeType {
		Fixed, Random
	}

	public TestConfiguration(String configFilePath) throws IOException, XPathExpressionException, ParserConfigurationException, SAXException, ConfigurationException {
		this(new File(configFilePath));
	}
	
	public TestConfiguration(File cFile) throws IOException, XPathExpressionException, ParserConfigurationException, SAXException, ConfigurationException {
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
		
		setDataFile(doc,xpath);
		setBackupHosts(doc,xpath);
		setNumAffinities(doc,xpath);
		setVmBootTime(doc,xpath);
		setVmDowntime(doc,xpath);
		setInjector(doc,xpath);
	}

	private void setDataFile(Document doc, XPath xpath) throws XPathExpressionException, ConfigurationException {
		XPathExpression expr = xpath.compile("//dataFile/text()");
		String dataFilePath = (String)expr.evaluate(doc,XPathConstants.STRING);
		
		dataFile = new File(dataFilePath);
		if (!dataFile.exists()) {
			/*
			 * Trying relative path to class
			 */
			URL dataFileURL = getClass().getResource(dataFilePath);
			if (dataFileURL == null) {
				throw new ConfigurationException("Invalid data file: " + dataFilePath);
			}
			dataFilePath = dataFileURL.getPath();
			dataFile = new File(dataFilePath);
		}
		
		if (!dataFile.canRead()) {
			throw new ConfigurationException("No read permissions for data file");
		}
		
		pack = getBooleanFromXML("//pack/text()",doc,xpath);
		expensive = getBooleanFromXML("//pack/@expensive",doc,xpath);
		if (expensive) {
			distributedExpensive = getBooleanFromXML("//pack/@distributeExpensive",doc,xpath);
			if (distributedExpensive) {
				numExpensiveVMs = getNumberFromXML("//pack/@numExpensiveVMs",doc,xpath);
			} else {
				numExpensiveRacks = getNumberFromXML("//pack/@numExpensiveRacks",doc,xpath);
			}
		}
	}

	private void setVmDowntime(Document doc, XPath xpath) throws XPathExpressionException, ConfigurationException {
		if (!nodeExists(doc, xpath, "//downtime")) {
			this.downtimeType = DowntimeType.Fixed;
			this.downtimeNoise = 0;
			return;
		}
		this.downtimeType = getBooleanFromXML("//downtime/@random",doc,xpath) ? DowntimeType.Random : DowntimeType.Fixed;  
		this.downtimeNoise = getDoubleFromXML("//downtime/noise", doc, xpath);
		if (downtimeNoise < 0 || downtimeNoise > 1) {
			throw new ConfigurationException("Donwtime noise (" + downtimeNoise + ") must be in [0,1] range");
		}
	}
	
	private boolean nodeExists(Document doc, XPath xpath, String nodePath) throws XPathExpressionException {
		if (null == nodePath || "".equals(nodePath)) {
			return false;
		}
		XPathExpression expr = xpath.compile("boolean(" + nodePath + ")");
		return (Boolean)expr.evaluate(doc, XPathConstants.BOOLEAN);
	}
	
	private void setBackupHosts(Document doc, XPath xpath) throws XPathExpressionException, ConfigurationException {

		int numHosts = getNumberFromXML("//backupHosts/@num",doc,xpath);
		int hostsCapacity = getNumberFromXML("//backupHosts/capacity/text()",doc,xpath);
		baseCost = getDoubleFromXML("//activationCost/text()",doc,xpath);
		costPerSize = getDoubleFromXML("//activationCostPerSize/text()",doc,xpath);
		int bootTimeInSeconds = getNumberFromXML("//backupHosts/hostBootTime/text()",doc,xpath);
		
		hostBootTime = Period.seconds(bootTimeInSeconds);
		
		backupHosts = new ArrayList<Host>(numHosts);
		for (int i = 0; i < numHosts; i++) {
			backupHosts.add(new Host(i, hostsCapacity, baseCost + costPerSize * hostsCapacity , hostBootTime));
		}
	}

	private void setNumAffinities(Document doc, XPath xpath) throws XPathExpressionException, ConfigurationException {
		numAffinities = getNumberFromXML("//test/numAffinities",doc,xpath);
	}

	private boolean getBooleanFromXML(String xpathStr, Document doc, XPath xpath) throws XPathExpressionException {
		XPathExpression expr = xpath.compile(xpathStr);
		String boolStr = (String)expr.evaluate(doc,XPathConstants.STRING); 
		return Boolean.parseBoolean(boolStr);
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

	private void setVmBootTime(Document doc, XPath xpath) throws XPathExpressionException, ConfigurationException {
		int vmBootTimeInSeconds = getNumberFromXML("//vmBootTime/text()", doc, xpath);
		vmBootTime = Period.seconds(vmBootTimeInSeconds);
	}
	
	private void setInjector(Document doc, XPath xpath) throws XPathExpressionException, ConfigurationException {
		XPathExpression expr = xpath.compile("//SLA/@type");
		String slaType = (String)expr.evaluate(doc,XPathConstants.STRING);
		AbstractModule productionModule = new ProductionModule();
		
		if ("appEngine".equals(slaType))
			injector =  Guice.createInjector(productionModule, new AppEngineSLAModule());
		else if ("linear".equals(slaType))
			injector = Guice.createInjector(productionModule, new LinearSLAModule());
		else if ("ha".equals(slaType)) 
			injector = Guice.createInjector(productionModule, new HA_TableSLAModule());
		else if ("custom".equals(slaType)) {
			Map<Range,Double> table = getRanges(doc,xpath);
			injector = Guice.createInjector(productionModule, new CustomSLAModule(table));
		} else {
			throw new ConfigurationException("Bad SLA type");
		}
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

	public Injector getSlaInjector() {
		return injector;
	}

	public double getHostActivationCost(int size) {
		return baseCost + size * costPerSize;
	}

	public int getNumAffinities() {
		return numAffinities;
	}

	public boolean pack() {
		return pack;
	}
	
	public int getNumExpensiveRacks() {
		return numExpensiveRacks;
	}

	public boolean distributedExpensive() {
		return distributedExpensive;
	}

	public int getNumExpensiveVMs() {
		return numExpensiveVMs;
	}

	public boolean expensiveProcessing() {
		return expensive;
	}
	
	public DowntimeType downtimeType() {
		return downtimeType;
	}
	
	public double downtimeNoise() {
		return downtimeNoise;
	}
}
