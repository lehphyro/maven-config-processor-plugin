## Configuration options
Below is a plugin configuration with all the available options set:

```xml
<plugin>
    <groupId>com.google.code.maven-config-processor-plugin</groupId>
    <artifactId>config-processor-maven-plugin</artifactId>
    <version>2.6</version>
    <configuration>
        <outputDirectory>processed-files</outputDirectory>
        <useOutputDirectory>true</useOutputDirectory>
        <encoding>ISO-8859-1</encoding>
        <lineWidth>200</lineWidth>
        <indentSize>4</indentSize>
        <failOnMissingXpath>true</failOnMissingXpath>
        <specificProperties>src/main/assembly/qa.properties</specificProperties>
        <skip>false</skip>
        <transformations>
            <transformation>
                <type>properties</type>
                <replacePlaceholders>true</replacePlaceholders>
                <input>src/main/resources/test.properties</input>
                <output>resources/test.properties</output>
                <config>src/main/assembly/properties/properties-processing.xml</config>
            </transformation>
            <transformation>
                <input>src/main/resources/test2.properties</input>
                <output>resources/test2.properties</output>
                <rules>
                    <add>
                        <name>test-property.version</name>
                        <value>2.2</value>
                        <before>property1</before>
                    </add>
                </rules>
            </transformation>
        </transformations>
        <namespaceContexts>
            <s>http://www.springframework.org/schema/beans</s>
        </namespaceContexts>
        <parserFeatures>
            <parserFeature>
                <name>http://apache.org/xml/features/nonvalidating/load-external-dtd</name>
                <value>false</value>
            </parserFeature>
        </parserFeatures>
    </configuration>
    <executions>
        <execution>
            <goals>
                <goal>process</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

The following table shows detailed descriptions of the configuration options:
<table>
  <tr>
    <td>Name</td>
    <td>Type</td>
    <td>Required</td>
    <td>Default Value</td>
    <td>Description</td>
  </tr>
  <tr>
    <td>outputDirectory</td>
    <td>file</td>
    <td>false</td>
    <td>${project.build.directory}</td>
    <td>Output directory of processed files, all output will be relative to this directory unless useOutputDirectory is set to false</td>
  </tr>
  <tr>
    <td>useOutputDirectory</td>
    <td>boolean</td>
    <td>false</td>
    <td>true</td>
    <td>Specify if you want to output all files in directories relative to outputDirectory. Ignore outputDirectory completely if false</td>
  </tr>
  <tr>
    <td>encoding</td>
    <td>string</td>
    <td>false</td>
    <td>UTF-8</td>
    <td>Encoding to use when generating files resulting of the processing</td>
  </tr>
  <tr>
    <td>lineWidth</td>
    <td>integer</td>
    <td>false</td>
    <td>80</td>
    <td>Line width to use when formatting XML output files</td>
  </tr>
  <tr>
    <td>indentSize</td>
    <td>integer</td>
    <td>false</td>
    <td>4</td>
    <td>Number of whitespaces to use when formatting XML output files</td>
  </tr>
  <tr>
    <td>failOnMissingXpath</td>
    <td>boolean</td>
    <td>false</td>
    <td>true</td>
    <td>Indicates whether to fail when XML elements are not found</td>
  </tr>
  <tr>
    <td>specificProperties</td>
    <td>file</td>
    <td>false</td>
    <td>empty</td>
    <td>File to load additional properties from when replacing placeholders of output files. Useful to keep properties for different environments in files specific to each one. Maven/Ant environment properties always take precedence when resolving placeholders. Not supported by Maven 3.x or later, you have to use the properties section of your pom.xml file</td>
  </tr>
  <tr>
    <td>skip</td>
    <td>boolean</td>
    <td>false</td>
    <td>false</td>
    <td>Disables plugin execution</td>
  </tr>
  <tr>
    <td>transformations</td>
    <td>list</td>
    <td>false</td>
    <td>none</td>
    <td>List of files to process with one transformation per file</td>
  </tr>
  <tr>
    <td>type</td>
    <td>string</td>
    <td>false</td>
    <td>auto-detected</td>
    <td>Type of the file being processed. Possible values are: xml, properties. The plugin will try to auto-detect the file type looking for known file name extensions such as .properties for properties files and .xml for XML files</td>
  </tr>
  <tr>
    <td>replacePlaceholders</td>
    <td>boolean</td>
    <td>false</td>
    <td>true</td>
    <td>Specify if you want the plugin to replace text fragments in the form ${} on transformation configuration files using maven/ant environment and specific properties</td>
  </tr>
  <tr>
    <td>input</td>
    <td>file(s)</td>
    <td>true</td>
    <td>none</td>
    <td>Input file(s) to process. It can be a file relative to the project, a URL of a protocol supported by maven like http, jar or a file in the classpath of the plugin made available declaring its jar as a dependency of the plugin. Since version 2.1, it may also be an ant-style pattern to specify a set of files, example: src/main/resources/*.properties</td>
  </tr>
  <tr>
    <td>output</td>
    <td>file</td>
    <td>false</td>
    <td>none</td>
    <td>Output file to generate when processing. If not set, input file will be overwritten</td>
  </tr>
  <tr>
    <td>config</td>
    <td>file</td>
    <td>false</td>
    <td>empty</td>
    <td>Rules configuration file to use when processing the input file. It can be a file relative to the project, a URL supported by maven like HTTP or jar or a file in the classpath of the plugin made available declaring its jar as a dependency of the plugin</td>
  </tr>
  <tr>
    <td>rules</td>
    <td>string</td>
    <td>false</td>
    <td>empty</td>
    <td>Transformation rules can be defined here so you don't have to create a new file to configure them</td>
  </tr>
  <tr>
    <td>namespaceContexts</td>
    <td>map</td>
    <td>false</td>
    <td>empty</td>
    <td>Mapping in the form: identifier => URL of XML namespace. Used when querying namespaced XML files such as spring bean configuration files using XPath</td>
  </tr>
  <tr>
    <td>parserFeatures</td>
    <td>list</td>
    <td>false</td>
    <td>empty</td>
    <td>List of XML parser features to activate/deactivate. The example configures the parser to ignore external DTDs completely</td>
  </tr>
</table>
