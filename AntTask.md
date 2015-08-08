## Introduction
As of version 1.6, it is possible to use the Config Processor in Ant build files. See how below.

## Download
First you need to download and extract the config-processor distribution.

## Task declaration
The provided Ant Task needs some jar files in its classpath. You can declare them like this:

```xml
<taskdef name="config-processor" classname="com.google.code.configprocessor.ant.ConfigProcessorTask">
  <classpath>
    <pathelement location="lib/maven-config-processor-plugin.jar" />
    <pathelement location="lib/xstream.jar" />
    <pathelement location="lib/xercesImpl.jar" />
    <pathelement location="lib/xpp3_min.jar" />
    <pathelement location="lib/commons-lang.jar" />
    <pathelement location="lib/plexus-container-default.jar" />
  </classpath>
</taskdef>
```

These jar files are included in the distribution zip file.

## Task usage
With the Config Processor task declaration in place, we can use it like the following:

```xml
<target name="test">
  <config-processor specificproperties="qa.properties" outputdirectory="target">
    <transformation input="my-config.properties"
                    output="processed/my-config.properties"
                    config="my-config-processing.xml" />
    <transformation input="other-config.properties"
                    output="processed/other-config.properties"
                    config="other-config-processing.xml" />
  </config-processor>
</target>
```

Ant task parameters have the same names and meanings as in the Maven plugin.
