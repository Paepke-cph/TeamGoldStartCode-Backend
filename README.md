# Backend Startcode
This code sample / Project, is to act as a codebase on which a backend REST-API could be built on.

## Basic setup
1. Clone this repository, with a desired name git clone <github-link> <name-of-project>
2. Rename Project name, in the following places:
	2.1 The file ./BaseStartcode.iml -> renamed to: <Your-project-name>.iml
	2.2 In ./Pom.xml Change <artifactId>BaseStartcode</artifactId> to your project name, or choose a better production name.
	2.3 In ./Pom.xml Change <name>BaseStartCode</name> to your project name.
3. Rename database and test database in the following places (if you want to):
	3.1 In ./src/main/resources/config.properties, change line 17 and 21 to the following:
		db.database=<your-database-name>
		dbtest.database=<your-test-database-name>
4. Change the context path in the file ./src/main/webapp/context.xml to that of your artifactId

## Basic usage
1. Place all entity in the package entity.
1.2 Make sure that all entity also are listed in the persistence.xml file, within a <class> tag.
2. Place all database facing facades in facades.
3. Place all Rest Endpoints in rest.
3.2 Make sure athat all endpoints and providers are listed in the ApplicationConfig.java file, like this: 
resources.add(<class-name-here>.class);
