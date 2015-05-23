This is a Maven2 plugin and Ant Task to process configuration files using a set of rules and generate modified files. It is useful when you need to generate different files for heterogeneous environments and don't want to keep a copy for each one.
This plugin aims to do the same things that [XConf](http://xconf.sourceforge.net/) does but it is fully integrated with Maven and utilizes standard technologies like XPath.

## Supported Features ##

  * Add, remove, modify, comment and uncomment single values of .properties files **keeping** comments
  * Add, remove and modify elements of XML files **keeping** comments
  * Ant task with the same capabilities of the maven plugin

## Getting Started ##

Please, see the HowTo wiki page or the [maven generated site](http://config-processor-maven-plugin.appspot.com/) for more details.

Details on plugin configuration are available at PluginConfiguration and AntTask wiki pages.

## Getting Help ##

You can subscribe to our users mailing list, see the [mailing lists section](http://config-processor-maven-plugin.appspot.com/mail-lists.html).

If you think you've found a bug or would like to request a new feature or enhancement, feel free to submit issues on the google code [issue tracker](http://code.google.com/p/maven-config-processor-plugin/issues/list). Even better, you can submit patches and be part of the project!

## News ##

### February 1st, 2015 ###

Version 2.7 is available with an enhancement submitted in issue ~~[48](https://code.google.com/p/maven-config-processor-plugin/issues/detail?id=48)~~.

### March 11th, 2014 ###

Version 2.6 is available with a few enhancements requested in issues ~~[46](https://code.google.com/p/maven-config-processor-plugin/issues/detail?id=46)~~ and ~~[47](https://code.google.com/p/maven-config-processor-plugin/issues/detail?id=47)~~.

### February 22th, 2013 ###

Version 2.5 has been released with a new feature requested in [issue 44](https://code.google.com/p/maven-config-processor-plugin/issues/detail?id=44). Now it's possible to remove a set of XML nodes specifying `<nodeSetPolicy>all</nodeSetPolicy>` in transformation configuration.

### December 21th, 2012 ###

Released version 2.4 just before the end of the world :) The list of issues closed in this release is available [here](http://code.google.com/p/maven-config-processor-plugin/issues/list?can=1&q=label%3ARelease-2.4).

**The artifactId has been changed to config-processor-maven-plugin, the format maven-name-plugin has been reserved for plugins in the groupId org.apache.maven.plugins**

### October 2nd, 2012 ###

Version 2.3 has been released with a few bug fixes. Properties files are now interpreted more like what the java.util.Properties class does thanks to a new contributor. The list of issues closed in this release is available [here](http://code.google.com/p/maven-config-processor-plugin/issues/list?can=1&q=label%3ARelease-2.3).

### May 1st, 2012 ###

Version 2.2 is out with support for latest releases of Ant and Maven, the _specificProperties_ configuration is deprecated as result of changes in Maven 3.x. An XML schema file is available to help write transformation config files ([issue 29](https://code.google.com/p/maven-config-processor-plugin/issues/detail?id=29)) and transformation rules can now be defined inside the pom.xml file ([issue 34](https://code.google.com/p/maven-config-processor-plugin/issues/detail?id=34)). [Here](http://code.google.com/p/maven-config-processor-plugin/issues/list?can=1&q=label%3ARelease-2.2) is the list of all issues related to this release.

### February 15th, 2012 ###

Because the plugin is pretty much feature complete, there hasn't been news for a while. Today we released version 2.1 as a result of new code contributions ([issue 28](https://code.google.com/p/maven-config-processor-plugin/issues/detail?id=28)) with support for ant-style pattern matching of input files, now it's possible to apply the same transformations to a set of files, a new plugin configuration (failOnMissingXpath) has been introduced to ignore failures when an XPath expression doesn't find anything.

### November 9th, 2010 ###

Version 2.0 has been released with support for absolute property placement in property files. See first and last directives in TransformationConfiguration wiki page.

### February 8th, 2010 ###

Version 1.9 has been released! This version has even more features contributed by users, these are some of them: output parameter is optional, XML parser is customizable and better support for adding files with nested transformations. You can see a complete list of resolved issues [here](http://code.google.com/p/maven-config-processor-plugin/issues/list?can=1&q=label%3ARelease-1.8+status%3AFixed&colspec=ID+Type+Status+Priority+Milestone+Owner+Summary&cells=tiles).

### August 14th, 2009 ###

Version 1.8 has been released! This new version includes more bug fixes and adds better support for multi-module projects.

### July 9th, 2009 ###

Version 1.6 uploaded to the maven central repository! Thanks to [Sonatype OSS Repository Hosting](https://docs.sonatype.org/display/Repository/Sonatype+OSS+Maven+Repository+Usage+Guide) we can deploy our next releases very quickly to the central repo.