/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 02/04/2012
 */
package il.ac.technion.xstream_conversion;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;


public abstract class ConversionTest {

	private Object subject;
	private String xmlFile = "test-resources//";
	
	@Before
	public void init() {
		this.subject = getSubject();
		xmlFile += subject.getClass().getSimpleName() + ".xml";
	}
	
	protected abstract Object getSubject();
	
	@Test
	public void conversionTest() throws IOException {
		XStream xStream = new XStream(new DomDriver());
		xStream.autodetectAnnotations(true);


		xStream.toXML(subject, new FileWriter(xmlFile));
		
		Object unmarshalledSubject = xStream.fromXML(new FileReader(xmlFile));
		assertTrue(new File(xmlFile).exists());
		assertEquals(subject, unmarshalledSubject);
	}
}
