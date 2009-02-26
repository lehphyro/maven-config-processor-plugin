package com.google.code.configprocessor.processing.properties;

import static com.google.code.configprocessor.processing.properties.PropertiesActionProcessor.*;

import org.junit.*;

import com.google.code.configprocessor.processing.*;

public class PropertiesUncommentActionProcessingAdvisorTest extends AbstractPropertiesActionProcessingAdvisorTest {

	@Test
	public void processUncomment() throws Exception {
		Action action = new UncommentAction("property4.value");
		String expected = "property1.value=value1" + LINE_SEPARATOR + "property2.value=" + LINE_SEPARATOR + "# Comment" + LINE_SEPARATOR + "	property3.value=value3 \\" + LINE_SEPARATOR + "value 3 continuation" + LINE_SEPARATOR + "property4.value=value4 \\" + LINE_SEPARATOR + "value 4 continuation" + LINE_SEPARATOR;

		executeTest(action, expected);
	}

}
