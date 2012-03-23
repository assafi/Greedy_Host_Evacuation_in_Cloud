/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 17/03/2012
 */
package il.ac.technion.conversion;

import il.ac.technion.misc.Host;

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

	@Test
	public void testConversion() throws IOException {
		Host h = new Host(1,10,100,Period.millis(1000));
		
		XStream xStream = new XStream(new DomDriver());
		xStream.autodetectAnnotations(true);
		
		xStream.toXML(h,new FileWriter("test-resources//host.xml"));
		Assert.assertTrue(new File("test-resources//host.xml").exists());
		
		Host h2 = (Host)xStream.fromXML(new FileReader("test-resources//host.xml"));
		Assert.assertEquals(h,h2);
	}
}
