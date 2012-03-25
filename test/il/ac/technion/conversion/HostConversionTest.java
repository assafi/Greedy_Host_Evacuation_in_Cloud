/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 17/03/2012
 */
package il.ac.technion.conversion;

import il.ac.technion.misc.Host;
import il.ac.technion.misc.VM;
import il.ac.technion.sla.SLABuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import junit.framework.Assert;

import org.joda.time.Period;
import org.junit.Test;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;


public class HostConversionTest {
	
	private final static String xmlFile = "test-resources//host.xml";

	@Test
	public void testConversion() throws IOException {
		Host h = new Host(1,10,100,Period.millis(1000));
		h.assign(new VM(1,1,new SLABuilder(Period.millis(1000)).appEngineSLA()));		
		
		XStream xStream = new XStream(new DomDriver());
		xStream.autodetectAnnotations(true);
		
		xStream.toXML(h, new FileWriter(xmlFile));
		
		Host h2 = (Host)xStream.fromXML(new FileReader(xmlFile));
		Assert.assertTrue(new File(xmlFile).exists());
		Assert.assertEquals(h,h2);
	}
}
