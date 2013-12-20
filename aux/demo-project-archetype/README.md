Web application demo archetype
==============================

About
-----

Archetype for creating simple web cdi applications.


Usage
-----

1. insert archetype in local repository  
```mvn install```
2. update catalog of local archetypes  
```mvn archetype:update-local-catalog```
3. create a project from archetype  
```mvn archetype:generate -DarchetypeGroupId=cz.muni.fi.cdii -DarchetypeArtifactId=webapp-demo-archetype -DarchetypeVersion=1.0-SNAPSHOT```
