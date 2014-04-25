CDI-inspector
=============

A set of tools for CDI visualization and inspection.

## Binaries

### Eclipse update site

`https://raw.github.com/jakub-n/cdi-inspector-repo/master/repos/cdii`

Update site contains all necessary dependencies. 
Built with Eclipse 4.3.1

### WildFly extension

[`module` directory](https://github.com/jakub-n/cdi-inspector-repo/tree/master/wildfly-binary/module) 
Copy content of `module` directory to your WildFly module directory.

## Local build

### Requirements

* Maven 3

### Eclipse plugin

#### Build

* `cd <reporoot>/code/common/model`
* `mvn clean install`
* `cd <reporoot>/code/eclipse-plugin`
* `mvn clean install`

#### Installation

1. Add *&lt;reporoot>/code/eclipse-plugin/repository/target/repository/* directory as a new local update site in your Eclipse installation.
2. Install all features from this update site.

### WildFly extension

#### Build

* `cd <reporoot>/code/wildfly-plugin/`
* `mvn clean install`

#### Instalation

Copy content of *&lt;reporoot>/code/wildfly-plugin/cdii-extension/target/module* into module directory of your WildFly installation.

## Usage

### Inspecting local projects

1. Right click on Java EE project with CDI Project Facet enabled in Package Explorer view.
1.1 JBoss Tools CDI support can be enabled in project *Properties* > *Project Facets*
2. Select *Inspect CDI beans*.

### Inspecting running applications

1. Show CDI Inspector window in Eclipse: *Main menu* > *Window* > *Show view* > "Other ...", Select *CDI Inspector*
2. Click *Inspect running application* icon in view toolbar and select eclipse managed application or directly enter context URL of your running application. 
e.g. `http://localhost:8080/your-cool-app`

Extracted data in JSON format can be viewed at `http://localhost:8080/your-cool-app/cdii`

## Licence

[GNU General Public License version 3](https://www.gnu.org/copyleft/gpl.html)
