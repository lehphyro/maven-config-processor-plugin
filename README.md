This is a Maven2 plugin and Ant Task to process configuration files using a set of rules and generate modified files. It is useful when you need to generate different files for heterogeneous environments and don't want to keep a copy for each one. This plugin aims to do the same things that XConf does but it is fully integrated with Maven and utilizes standard technologies like XPath.

## Supported Features

* Add, remove, modify, comment and uncomment single values of .properties files keeping comments
* Add, remove and modify elements of XML files keeping comments
* Ant task with the same capabilities of the maven plugin

## Getting Started

Add this plugin configuration to you pom.xml file:

```xml
<plugin>
    <groupId>com.google.code.maven-config-processor-plugin</groupId>
    <artifactId>config-processor-maven-plugin</artifactId>
    <version>2.6</version>
    <configuration>
        <specificProperties>src/assembly/qa.properties</specificProperties>
        <transformations>
            <transformation>
                <input>src/main/resources/my-config.properties</input>
                <output>processed-files/my-config.properties</output>
                <config>src/assembly/qa/my-config-processing.xml</config>
            </transformation>
        </transformations>
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

Go to [PluginConfiguration](PluginConfiguration.md) to see all available options and [TransformationConfiguration](TransformationConfiguration.md) to see all available transformation rules.

## Getting Help

You can subscribe to our [users mailing list](http://groups.google.com/group/maven-config-processor-users).

If you think you've found a bug or would like to request a new feature or enhancement, feel free to fork this project and submit patches.
