/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 28/03/2012
 */
package il.ac.technion.xstream_conversion;

import il.ac.technion.datacenter.sla.SLA;
import il.ac.technion.datacenter.sla.SLABuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import junit.framework.Assert;

import org.junit.Test;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;



public class SLAConversionTest {

	private final static String xmlFile = "test-resources//sla.xml";
	
	@Test
	public void testConversion() throws IOException {
		SLA sla = new SLABuilder().appEngineSLA();
		
		XStream xStream = new XStream(new DomDriver());
		xStream.autodetectAnnotations(true);
		
		xStream.toXML(sla, new FileWriter(xmlFile));
		
		SLA sla2 = (SLA)xStream.fromXML(new FileReader(xmlFile));
		Assert.assertTrue(new File(xmlFile).exists());
		Assert.assertEquals(sla,sla2);
	}
}
