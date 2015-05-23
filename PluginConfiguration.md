# Configuration options #

Below is a plugin configuration with all the available options set:

```
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

|Name|Type|Required|Default Value|Description|
|:---|:---|:-------|:------------|:----------|
|outputDirectory|file|false   |${project.build.directory}|Output directory of processed files, all output will be relative to this directory unless _useOutputDirectory_ is set to false|
|useOutputDirectory|boolean|false   |true         |Specify if you want to output all files in directories relative to _outputDirectory_. Ignore _outputDirectory_ completely if false|
|encoding|string|false   |UTF-8        |Encoding to use when generating files resulting of the processing|
|lineWidth|integer|false   |80           |Line width to use when formatting XML output files|
|indentSize|integer|false   |4            |Number of whitespaces to use when formatting XML output files|
|failOnMissingXpath|boolean|false   |true         |Indicates whether to fail when XML elements are not found|
|specificProperties|file|false   |empty        |File to load additional properties from when replacing placeholders of output files. Useful to keep properties for different environments in files specific to each one. Maven/Ant environment properties always take precedence when resolving placeholders. _Not supported by Maven 3.x or later, you have to use the properties section of your pom.xml file_|
|skip|boolean|false   |false        |Disables plugin execution|
|transformations|list|true    |none         |List of files to process with one transformation per file|
|type|string|false   |auto-detected|Type of the file being processed. Possible values are: xml, properties. The plugin will try to auto-detect the file type looking for known file name extensions such as .properties for properties files and .xml for XML files|
|replacePlaceholders|boolean|false   |true         |Specify if you want the plugin to replace text fragments in the form ${} on transformation configuration files using maven/ant environment and specific properties|
|input|file(s)|true    |none         |Input file(s) to process. It can be a file relative to the project, a URL of a protocol supported by maven like http, jar or a file in the classpath of the plugin made available declaring its jar as a dependency of the plugin. Since version 2.1, it may also be an ant-style pattern to specify a set of files, example: src/main/resources/`*`.properties|
|output|file|false   |none         |Output file to generate when processing. If not set, input file will be overwritten|
|config|file|false   |empty        |Rules configuration file to use when processing the input file. It can be a file relative to the project, a URL supported by maven like HTTP or jar or a file in the classpath of the plugin made available declaring its jar as a dependency of the plugin|
|rules|string|false   |empty        |Transformation rules can be defined here so you don't have to create a new file to configure them|
|namespaceContexts|map |false   |empty        |Mapping in the form: identifier => URL of XML namespace. Used when querying namespaced XML files such as spring bean configuration files using XPath|
|parserFeatures|list|false   |empty        |List of XML parser features to activate/deactivate. The example configures the parser to ignore external DTDs completely|