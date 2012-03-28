/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 28/03/2012
 */
package il.ac.technion.xstream_conversion;

import il.ac.technion.datacenter.Host;
import il.ac.technion.datacenter.VM;
import il.ac.technion.datacenter.sla.SLABuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import junit.framework.Assert;

import org.joda.time.Period;
import org.junit.Test;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;



public class VMConversionTest {

	private final static String xmlFile = "test-resources//vm.xml";
	
	@Test
	public void testConversion() throws IOException {
		Host h1 = new Host(0, 512, 0.0, Period.minutes(3));
		VM vm1 = new VM(0, 512, 100.0, new SLABuilder().appEngineSLA());
		vm1.addBootTime(h1, Period.hours(1));
		
		XStream xStream = new XStream(new DomDriver());
		xStream.autodetectAnnotations(true);
		
		xStream.toXML(vm1, new FileWriter(xmlFile));
		
		VM vm2 = (VM)xStream.fromXML(new FileReader(xmlFile));
		Assert.assertTrue(new File(xmlFile).exists());
		Assert.assertEquals(vm1,vm2);
	}
}
