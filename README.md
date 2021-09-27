# XML Extra Plugins for Pentaho KETTLE

[![CI](https://github.com/nationalarchives/kettle-xml-extra-plugins/workflows/CI/badge.svg)](https://github.com/nationalarchives/kettle-xml-extra-plugins/actions?query=workflow%3ACI)
[![Java 8](https://img.shields.io/badge/java-8+-blue.svg)](https://adoptopenjdk.net/)
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](https://opensource.org/licenses/MIT)
[![Coverage Status](https://coveralls.io/repos/github/nationalarchives/kettle-xml-extra-plugins/badge.svg?branch=main)](https://coveralls.io/github/nationalarchives/kettle-xml-extra-plugins?branch=main)

This project contains plugins for [Pentaho Data Integration](https://github.com/pentaho/pentaho-kettle) (or KETTLE as it
is commonly known), that add additional functionality for working with XML.

The plugins provided are:

* Transform to Canonical XML

    <img alt="Create Jena Model Icon" src="https://raw.githubusercontent.com/nationalarchives/kettle-xml-extra-plugins/main/src/main/resources/CanonicalStep.svg" width="32"/>
    This transform plugin can be used to convert XML held as a java.util.String value in field to [C14N Canonical XML](https://www.w3.org/TR/xml-exc-c14n/).

This project was developed by [Evolved Binary](https://evolvedbinary.com) and [DeveXe](https://devexe.co.uk) as part of
Project OMEGA for the [National Archives](https://nationalarchives.gov.uk).

## Getting the Plugins

You can either download the plugins from our GitHub releases
page: https://github.com/nationalarchives/kettle-xml-extra-plugins/releases/, or you can build them from source.

## Building from Source Code

The plugins can be built from Source code by installing the pre-requisites and following the steps described below.

### Pre-requisites for building the project:

* [Apache Maven](https://maven.apache.org/), version 3+
* [Java JDK](https://adoptopenjdk.net/) 1.8
* [Git](https://git-scm.com)

### Build steps:

1. Clone the Git repository
    ```
    $ git clone https://github.com/nationalarchives/kettle-xml-extra-plugins.git
    ```

2. Compile a package
    ```
    $ cd kettle-xml-extra-plugins
    $ mvn clean package
    ```

3. The plugins directory is then available
   at `target/kettle-extra-xml-plugins-1.0.0-SNAPSHOT-kettle-plugin/kettle-xml-extra-plugins`

## Installing the plugins

* Tested with Pentaho Data Integration - Community Edition - version: 9.1.0.0-324

You need to copy the plugins directory `kettle-xml-extra-plugins` (from building above) into the `plugins` sub-directory
of your KETTLE installation.

This can be done by either running:

```
  $ mvn -Pdeploy-pdi-local -Dpentaho-kettle.plugins.dir=/opt/data-integration/plugins antrun:run@deploy-to-pdi
```

or, you can do so manually, e.g.:

```
  $ cp -r target/kettle-xml-extra-plugins-1.0.0-SNAPSHOT-kettle-plugin/kettle-xml-extra-plugins /opt/data-integration/plugins/
```