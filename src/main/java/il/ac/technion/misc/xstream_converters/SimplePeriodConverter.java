/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 22/03/2012
 */
package il.ac.technion.misc.xstream_converters;

import il.ac.technion.misc.PeriodConverter;

import org.joda.time.Period;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class SimplePeriodConverter implements Converter {

	@Override
	public boolean canConvert(Class clazz) {
		return Period.class == clazz;
	}

	@Override
	public void marshal(Object source, HierarchicalStreamWriter writer,
			MarshallingContext context) {
		Period bootTime = (Period)source;
		writer.setValue(Long.toString(PeriodConverter.toSeconds(bootTime)));
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext context) {
		return Period.seconds((Integer.valueOf(reader.getValue())));
	}
}
