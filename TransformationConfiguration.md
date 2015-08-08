## Introduction

Each file is processed using a set of rules defined by the file configured in the fields config or rules of the plugin declaration.

## File Format
There are five basic operations that can be performed on target files: add, remove, modify, comment and uncomment:

* Add operations append property values and XML nodes at absolute positions first and last and relative positions like before, after and inside. You can specify only one position for each add operation.
* Remove operations delete property values and XML nodes matching the filter expression in name.
* Modify operations change property values and XML nodes matching the filter expression in name or execute find/replace operations in property values and comments or XML node values.
* Comment and uncomment operations are pretty self explanatory and are supported only for properties files processing.

The transformation configuration file defines these operations:

```xml
<processor>
    <add>
        <name></name>
        <value></value>
        <before></before> <!-- OR --> <after></after> <!-- OR --> <inside></inside>
        <!-- Only for properties file processing -->
        <first /> <!-- OR --> <last />
    </add>

    <modify>
        <name></name>
        <value></value>
        <!-- OR -->
        <find></find>
        <replace></replace>
        <flags></flags>
    </modify>

    <remove>
        <name></name>
    </remove>

    <!-- Only for properties file processing -->
    <comment>
        <name></name>
    </comment>

    <uncomment>
        <name></name>
    </uncomment>
</processor>
```

It is possible to configure as many tags as necessary. There is a work-in-progress schema file available at [http://config-processor-maven-plugin.appspot.com/schema/transformation-2.2.xsd](http://config-processor-maven-plugin.appspot.com/schema/transformation-2.2.xsd)

## Properties File Processing
Here is an example of a configuration file to process properties files.

```xml
<processor>
    <!-- Add non-existing property -->
    <add>
        <name>test-property</name>
        <value>${project.build.directory}</value>
        <before>property1.value</before>
    </add>

    <!-- Modify a property value -->
    <modify>
        <name>property1.value</name>
        <value>${test-project.property1}</value>
    </modify>

    <!-- Remove a property -->
    <remove>
        <name>property3.value</name>
    </remove>

    <!-- Commenting a property -->
    <comment>
        <name>property4.value</name>
    </comment>

    <!-- Appending a file -->
    <add>
        <file>jar:file:///C:/projects/java/test.jar!/META-INF/test.properties</file>
        <last />
    <add>
</processor>
```

This file instructs the plugin to add the property test-property before the property property1.value and its value must be ${project.build.directory} which by default would be replaced by the project build output directory (target by default).

In addition the plugin will modify the value of the property property1.value to ${test-project.property1} which should be defined in your command-line, pom.xml or specific properties file (in the plugin configuration - specificProperties element).

The property property3.value will not appear in the generated file since it is configured to be removed.

The property property4.value will be commented out in the generated file.

The file test.properties that is inside test.jar will be appended to the resulting file. You can add it before everything with <first /> instead of <last />. Or add just a new property instead of a whole file.

## XML File Processing
XML file processing uses the same format as properties files. But it is a bit more complex because of XML features such as nodes, attributes and namespaces.

The following is an example of configuration file to process Spring bean files.

```xml
<processor>
    <add>
        <after>/beans/bean[@id='testBean']</after>
        <value>
            <![CDATA[
                <bean id="testBean2" class="com.foo.TestBean">
                </bean>
                ]]>
        </value>
    </add>

    <add>
        <name>/beans/bean[@id='testBean']</name>
        <value>
            <![CDATA[
                scope="prototype" parent="testBeanParent"
            ]]>
        </value>
    </add>

    <add>
        <file>my-other-config.xml</file>
        <inside>/beans/bean[@id='environmentBean']</inside>
        <actions>
            <modifiy>
                <name>/beans/bean[@class='x.y.z']/@parent</name>
                <value>thatBean</value>
            </modify>
        </actions>
    </add>

    <remove>
        <name>/beans/bean[@id='mock']</name>
    </remove>

    <remove>
        <name>/beans/bean[@id='service']/@scope</name>
    </remove>

    <modify>
        <name>/beans/bean[@id='testBean']</name>
        <value>
            <![CDATA[
                <bean id="testBean" class="${test.bean.class}">
                </bean>
            ]]>
        </value>
    </modify>

    <modify>
        <name>/beans/bean[@id='dataSource']/@scope</name>
        <value>singleton</value>
    </modify>

    <modify>
        <name>/beans/bean[@id='mappings']/property[@name='properties']/value/text()</name>
        <value>new-value=new-text</value>
    </modify>
</processor>
```

All the name elements must have XPath expressions that resolve to single nodes or node attributes.

As you can see, we can add completely new XML nodes specifying after or before which node we want to add.

It is possible to add node attributes as well, but you should use the name attribute instead of after/before attributes because node attribute ordering is not supported.

It is also possible to append other XML files and add nodes inside other nodes instead of after or before. Here we are appending the file my-other-config.xml inside the bean with id="environmentBean". You can set "ignore-root" to false to insert the root element with its children like the following:

```xml
<add>
    <file ignore-root="false">my-other-config.xml</file>
    <inside>/beans/bean[@id='environmentBean']</inside>
    <strict>false</strict>
</add>
```

"strict=false" allows you to ignore parser errors so you can append invalid XML fragments if you need to.

It is possible to specify actions to be processed only on the file being included, the tag /add/actions shows an example. Warning: previous version of the plugin (< 1.9) apply all top-level actions to files being included. This behaviour generated some performance and configuration problems, so you will need to move your top-level actions intended to process only the files being included to sub-actions.

You can remove entire nodes and nodesets or just some attributes using XPath expressions to select the attributes such as /beans/bean[@id='service']/@scope, this expression will remove the scope of the bean with id="service". To remove all nodes matching an XPath expression, add <nodeSetPolicy>all</nodeSetPolicy> to <remove>, you can also specify first to remove only the first element and last to remove the last node in the nodeset.

Also, it is possible to replace nodes with entirely new nodes. Modifying attributes is specially nice because the value attribute can hold only the new value while the name attribute holds the XPath query.

## XML Namespaces

If your XML file uses XML Schema namespaces, you can still use XPath queries. The following example shows how:

First, the target XML:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://www.springframework.org/schema/beans
    http://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="testBean" class="TestClass">
    </bean>
</beans>
```

Note that the beans element is under the namespace http://www.springframework.org/schema/beans. Thus, XPath queries in the form:

/beans/bean[@id='testBean']

will not select testBean. The XPath should be:

//s:bean[@id='testBean']

See the "s:" preceding "bean"? It is a prefix that we can map to the same http://www.springframework.org/schema/beans namespace used in the spring file. Now we have to tell the parser which namespace "s" is in. We do this in the plugin declaration in the pom.xml file as follows:

```xml
<plugin>
    <groupId>com.google.code.maven-config-processor-plugin</groupId>
    <artifactId>maven-config-processor-plugin</artifactId>
    <configuration>
        ...
        <transformations>
            ...
        </transformations>

        <!-- Here is the mapping -->
        <namespaceContexts>
            <s>http://www.springframework.org/schema/beans</s>
        </namespaceContexts>
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

Now the XPath expression will work as expected. Notice that expressions must specify namespace prefixes at all node levels so the expression /beans/bean/property1? should be /s:beans/s:bean/s:property1? if you want to query namespaced XML.
