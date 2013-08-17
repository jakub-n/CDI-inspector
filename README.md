CDI-inspector
=============

A set of tools for CDI visualization and inspection.

## Eclipse update site

`https://raw.github.com/jakub-n/cdi-inspector-repo/master/repos/cdii_composite`

Update site contains all necessary dependencies.

## Local build

### Requirements

* Maven 3
* Eclipse 4.3 Kepler ([download page](http://www.eclipse.org/downloads))
* JBoss Tools :: JavaEE Tools v4.1 ([update site](http://download.jboss.org/jbosstools/updates/development/kepler/))
* Zest 2 ([git repo](http://git.eclipse.org/c/gef/org.eclipse.zest.git))

### Installation

1. Install dependencies.
  1. Install JBoss Tools into your Eclipse installation.
  2. Download, compile and install Zest 2 into your Eclipse installation.
2. Run `mvn install` in *code/cz.muni.fi.cdii.main* directory.  
Note: Maven requires Zest 2 artifacts from step 1.2 to be in your local maven repository.
3. Add *code/cz.muni.fi.cdii.repository/target/repository* directory as a new local update site in your Eclipse installation.
4. Install all features from this update site.

## Usage

1. Right click on Java EE project in Package Explorer view.
2. Select 'Inspect CDI beans'.

