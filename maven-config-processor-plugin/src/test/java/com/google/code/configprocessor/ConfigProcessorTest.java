/*
 * Copyright (C) 2009 Leandro de Oliveira Aparecido <lehphyro@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.code.configprocessor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.codehaus.plexus.util.IOUtil;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import com.google.code.configprocessor.expression.ExpressionResolver;
import com.google.code.configprocessor.io.DefaultFileResolver;
import com.google.code.configprocessor.io.FileResolver;
import com.google.code.configprocessor.log.LogAdapter;
import com.google.code.configprocessor.processing.properties.AbstractPropertiesActionProcessingAdvisorTest;
import com.google.code.configprocessor.processing.properties.PropertiesActionProcessorTest;

public class ConfigProcessorTest {

	private ConfigProcessor configProcessor;
	@Rule
	public TemporaryFolder folder= new TemporaryFolder();


	@Before
	public void setup() throws Exception {
		configProcessor = createConfigProcessor(null, false, null);
	}

	@Test(expected = ConfigProcessorException.class)
	public void testNullPattern() throws Exception {
		File baseDir = createStrictMock(File.class);
		expect(baseDir.isDirectory()).andReturn(true);
		expect(baseDir.exists()).andReturn(true);
		configProcessor.getMatchingFiles(null);
	}

	@Test(expected = ConfigProcessorException.class)
	public void testEmptyPattern() throws Exception {
		File baseDir = createStrictMock(File.class);
		expect(baseDir.isDirectory()).andReturn(true);
		expect(baseDir.exists()).andReturn(true);
		configProcessor.getMatchingFiles("");
	}

	@Test
	public void testGetTypeFromTransformation() throws Exception {
		Transformation transformation = new Transformation();
		transformation.setType(Transformation.PROPERTIES_TYPE);
		assertEquals(Transformation.PROPERTIES_TYPE, configProcessor.getInputType(transformation));
	}

	@Test
	public void testGuessTypeFromInputFile() throws Exception {
		assertEquals(Transformation.PROPERTIES_TYPE, configProcessor.getInputType(transformationWithInput("test.properties")));
		assertEquals(Transformation.XML_TYPE, configProcessor.getInputType(transformationWithInput("test.xml")));
		assertEquals(Transformation.XML_TYPE, configProcessor.getInputType(transformationWithInput("test.something")));
	}

	private Transformation transformationWithInput(String input) {
		Transformation transformation = new Transformation();
		transformation.setInput(input);
		return transformation;
	}

	@Test
	public void testTransformationFromClasspathResource() throws Exception
	{
		File configFile = folder.newFile();
		PrintWriter writer = new PrintWriter(new FileOutputStream(configFile));
		writer.println("<?xml  version=\"1.0\" encoding=\"iso-8859-1\"?>");
		writer.println("<processor>");
		writer.println("<modify>");
		writer.println("<name>property1.value</name>");
		writer.println("<value>NEWTESTVALUE</value>");
		writer.println("</modify>");
		writer.println("</processor>");
		writer.close();
		File outputDirectory = null;
		File output = folder.newFile();
		boolean useOutputDirectory = false;
		FileResolver fileResolver = new DefaultFileResolver();
		configProcessor = createConfigProcessor(outputDirectory, useOutputDirectory, fileResolver);
		configProcessor.init();
		Transformation transformation = new Transformation();
		transformation.setInput("classpath:" + PropertiesActionProcessorTest.PROPERTIES_PATH);
		transformation.setOutput(output.getAbsolutePath());
		transformation.setConfig(configFile.getAbsolutePath());
		ExpressionResolver resolver = getExpressionResolver();
		configProcessor.execute(resolver, transformation);
		Properties properties = new Properties();
		properties.load(new FileInputStream(output.getAbsolutePath()));
		assertEquals("NEWTESTVALUE", properties.getProperty("property1.value"));
	}

	protected ExpressionResolver getExpressionResolver() {
		return new AbstractPropertiesActionProcessingAdvisorTest.TestExpressionResolver();
	}

	private ConfigProcessor createConfigProcessor(File outputDirectory,
						   boolean useOutputDirectory,
						   FileResolver fileResolver)
	{
		Map<String, String> namespaceContexts = null;
		File baseDir = null;
		LogAdapter log = new TestLogAdapter();
		List<ParserFeature> parserFeatures = null;
		boolean failOnMissingXPath = true;
		int indentSize = 4;
		int lineWidth = 80;
		return new ConfigProcessor("UTF-8", indentSize, lineWidth, namespaceContexts, baseDir,
			outputDirectory, useOutputDirectory, log, fileResolver, parserFeatures,
			failOnMissingXPath);
	}

	public static class TestLogAdapter implements LogAdapter {

		public void info(String msg) {
			System.out.println(msg);
		}

		public void debug(String msg) {
			System.out.println(msg);
		}

		public void warn(String msg) {
			System.out.println(msg);
		}

		public void error(String msg, Throwable t) {
			System.out.println(msg);
			t.printStackTrace(System.out);
		}

		public void verbose(String msg) {
			System.out.println(msg);
		}

	}
}
