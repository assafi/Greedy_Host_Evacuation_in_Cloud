package il.ac.technion.config;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.junit.Test;
import org.xml.sax.SAXException;

public class TestConfigurationTest {

	@Test
	public void testGoodPath() throws XPathExpressionException, IOException, ParserConfigurationException, SAXException, ConfigurationException {
		String filePath = getClass().getResource("test_config.xml").getPath();
		new TestConfiguration(filePath);
	}
}
