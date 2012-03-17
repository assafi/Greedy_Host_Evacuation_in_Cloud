/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 17/03/2012
 */
package il.ac.technion.conversion;

import il.ac.technion.misc.Host;
import junit.framework.Assert;

import org.junit.Test;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;


public class HostConversionTest {

	@Test
	public void testConversion() {
		Host h = new Host(1,10,100);
		
		String xmlDesc =
			"<host>\n" + 
			"  <bin>\n" + 
			"    <id>1</id>\n" + 
			"    <capacity>10</capacity>\n" + 
			"  </bin>\n" +
			"  <activationCost>100</activationCost>\n" +
			"</host>";
		
		XStream xStream = new XStream(new DomDriver());
		xStream.autodetectAnnotations(true);
		Assert.assertEquals(xmlDesc,xStream.toXML(h));
		
		Host h2 = (Host)xStream.fromXML(xmlDesc);
		Assert.assertEquals(h,h2);
	}
}
