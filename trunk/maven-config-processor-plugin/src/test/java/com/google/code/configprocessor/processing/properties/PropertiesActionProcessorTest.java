package com.google.code.configprocessor.processing.properties;

import static org.junit.Assert.*;
import static com.google.code.configprocessor.processing.properties.PropertiesActionProcessor.*;

import java.io.*;

import org.codehaus.plexus.component.configurator.expression.*;
import org.junit.*;

import com.google.code.configprocessor.*;
import com.google.code.configprocessor.parsing.*;
import com.google.code.configprocessor.processing.*;

public class PropertiesActionProcessorTest {

	private static final String PROPERTIES_PATH = "/com/google/code/configprocessor/data/properties-target-config.properties";
	private static final String PROPERTIES_CONFIG = "/com/google/code/configprocessor/data/properties-processing-configuration.xml";
	
	private InputStream input;
	private ByteArrayOutputStream output;
	private ActionProcessor processor;
	
	@Before
	public void setup() {
		processor = new PropertiesActionProcessor(new ExpressionResolver(new DefaultExpressionEvaluator()));
		input = getClass().getResourceAsStream(PROPERTIES_PATH);
		output = new ByteArrayOutputStream();
	}

	@Test
	public void processAddFirst() throws Exception {
		Action action = new AddAction("teste-property", "test-value", null, "property1.value");
		String expected = "teste-property=test-value" + LINE_SEPARATOR + "property1.value=value1" + LINE_SEPARATOR + "property2.value=" + LINE_SEPARATOR + "# Comment" + LINE_SEPARATOR + "	property3.value=value3 \\" + LINE_SEPARATOR + "value 3 continuation" + LINE_SEPARATOR;
		
		executeTest(action, expected);
	}

	@Test
	public void processAddAfterProperty() throws Exception {
		Action action = new AddAction("teste-property", "test-value", "property1.value", null);
		String expected = "property1.value=value1" + LINE_SEPARATOR + "teste-property=test-value" + LINE_SEPARATOR + "property2.value=" + LINE_SEPARATOR + "# Comment" + LINE_SEPARATOR + "	property3.value=value3 \\" + LINE_SEPARATOR + "value 3 continuation" + LINE_SEPARATOR;

		executeTest(action, expected);
	}
	
	@Test
	public void processAddBeforeCommentedProperty() throws Exception {
		Action action = new AddAction("teste-property", "test-value", null, "property3.value");
		String expected = "property1.value=value1" + LINE_SEPARATOR + "property2.value=" + LINE_SEPARATOR + "# Comment" + LINE_SEPARATOR + "teste-property=test-value" + LINE_SEPARATOR + "	property3.value=value3 \\" + LINE_SEPARATOR + "value 3 continuation" + LINE_SEPARATOR;

		executeTest(action, expected);
	}

	@Test
	public void processAddLast() throws Exception {
		Action action = new AddAction("teste-property", "test-value", "property3.value", null);
		String expected = "property1.value=value1" + LINE_SEPARATOR + "property2.value=" + LINE_SEPARATOR + "# Comment" + LINE_SEPARATOR + "	property3.value=value3 \\" + LINE_SEPARATOR + "value 3 continuation" + LINE_SEPARATOR + "teste-property=test-value" + LINE_SEPARATOR;

		executeTest(action, expected);
	}
	
	@Test
	public void processModifyFirst() throws Exception {
		Action action = new ModifyAction("property1.value", "modified-value");
		String expected = "property1.value=modified-value" + LINE_SEPARATOR + "property2.value=" + LINE_SEPARATOR + "# Comment" + LINE_SEPARATOR + "	property3.value=value3 \\" + LINE_SEPARATOR + "value 3 continuation" + LINE_SEPARATOR;

		executeTest(action, expected);
	}

	@Test
	public void processModifyLast() throws Exception {
		Action action = new ModifyAction("property3.value", "modified-value");
		String expected = "property1.value=value1" + LINE_SEPARATOR + "property2.value=" + LINE_SEPARATOR + "# Comment" + LINE_SEPARATOR + "	property3.value=modified-value" + LINE_SEPARATOR;

		executeTest(action, expected);
	}

	@Test
	public void processRemoveFirst() throws Exception {
		Action action = new RemoveAction("property1.value");
		String expected = "property2.value=" + LINE_SEPARATOR + "# Comment" + LINE_SEPARATOR + "	property3.value=value3 \\" + LINE_SEPARATOR + "value 3 continuation" + LINE_SEPARATOR;

		executeTest(action, expected);
	}

	@Test
	public void processRemoveMiddle() throws Exception {
		Action action = new RemoveAction("property2.value");
		String expected = "property1.value=value1" + LINE_SEPARATOR + "# Comment" + LINE_SEPARATOR + "	property3.value=value3 \\" + LINE_SEPARATOR + "value 3 continuation" + LINE_SEPARATOR;

		executeTest(action, expected);
	}

	@Test
	public void processRemoveLast() throws Exception {
		Action action = new RemoveAction("property3.value");
		String expected = "property1.value=value1" + LINE_SEPARATOR + "property2.value=" + LINE_SEPARATOR + "# Comment" + LINE_SEPARATOR;

		executeTest(action, expected);
	}

	@Test
	public void processNestedActions() throws Exception {
		NestedAction nestedAction = new NestedAction();
		nestedAction.addAction(new AddAction("teste-property", "test-value", null, "property1.value"));
		nestedAction.addAction(new ModifyAction("property1.value", "modified-value"));
		nestedAction.addAction(new RemoveAction("property3.value"));
		String expected = "teste-property=test-value" + LINE_SEPARATOR + "property1.value=modified-value" + LINE_SEPARATOR + "property2.value=" + LINE_SEPARATOR + "# Comment" + LINE_SEPARATOR;

		executeTest(nestedAction, expected);
	}

	@Test
	public void processParsedAction() throws Exception {
		ProcessingConfigurationParser parser = new ProcessingConfigurationParser();
		Action action = parser.parse(getClass().getResourceAsStream(PROPERTIES_CONFIG));
		String expected = "teste-property=test-value" + LINE_SEPARATOR + "property1.value=modified-value" + LINE_SEPARATOR + "property2.value=" + LINE_SEPARATOR + "# Comment" + LINE_SEPARATOR;
		
		executeTest(action, expected);
	}
	
	protected void executeTest(Action action, String expected) throws Exception {
		processor.process(new InputStreamReader(input), new OutputStreamWriter(output), action);
		assertEquals(expected, getOutput());
	}

	protected String getOutput() {
		return new String(output.toByteArray());
	}
}
