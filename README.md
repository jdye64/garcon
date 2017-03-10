# nifi-device-registry

## Building and Running Device Registry
Device Registry is built using the popular Dropwizard Java microframework and the standard gamut of Java development tools EX: Maven. To build the application
simple run ```mvn clean install package``` from the projects root folder. This will generate a single jar that will be located in the target directory.

Dropwizard is really neat in that it produces a single jar with a standard java main method that will accept differeing arguments depending on how you would
like to run the microservice. For example the standard way this application will be ran is as a "server". This means that Dropwizard will start up a Jetty
server and serve both the UI and the REST API components of the application from the single jar. To command to run in this mode is.

```java -jar ./target/nifi-device-registry-1.2.0-SNAPSHOT.jar server DeviceRegistry.yml```

The "DeviceRegistry.yml" file is a single location where you can adjust the configurations for the Device Registry application.

Device Registry also supports other run modes including some CLI ultities all bundled within the same jar. Those list of commands are laid out in the below table ....

//TODO: Include the table of commands once done.

## Device Registry UI
The Device Registry UI is a single page HTML5 application that is meant to allow the end user visibility to the Apache NiFi components sprawling over
the entire organization. Often there are several thousand instances of Apache NiFi, MiNifi, and MiNiFi-C++ running so this allows the user a convenient
single location to better understand the state of NiFi as well as basic informaiton about the devices NiFi is running on.

The UI can be accessed at: ```http://IP_OF_SERVER:8080/assets/index.html```