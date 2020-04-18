# Backend Startcode
This code sample / Project, is to act as a codebase on which a backend REST-API could be built on.

## Basic Setup
If you want to deploy this project to your droplet make sure 
to register the following *new* value as system variables on Tomcat:
* **SECRET** (this should be a 32 byte long string)

## Databases
DB: **startcode**
<br>
Test DB: **startcode_test**


## Tests requiring login
There are currently tests, which require a login to test another functionality.
<br>Currently you wil need to make a properties file **testing.properties**
and store the usernames and passwords in there. This file **MUST NOT** be committed to GitHub
```properties
{
   user1_username=<username>
   user1_password=<password>
   ...
}