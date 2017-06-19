# Garcon

## Garcon Capabilities
* **Visualize data exchange** – View interactions between a multitude of NiFi and MiNiFi nodes in a single location
* **Auto scaling** – using Docker scale NiFi cluster sizes up and down as flow demand increases or decreases
* **Device Health** – Spot and troubleshoot slow nodes in a cluster or across clusters.
* **Flow optimization** – Understand non optimized portions of your flow to increase performance.
* **Meet SLAs** – Ensure SLAs are meet down to the individual flowfile level intra node/cluster or across multiple clusters.
..* React to missed SLAs by auto scaling to increase flow throughput
* **Examine Global Processor state** – Visualize all stopped, disabled, invalid, and running processors across your entire Garcon managed domain in a single location.
* **Spot global “backpressured” connections** – Maintain a list of “backpressure” occurrences to understand periods of increased load for capacity planning or auto scaling.
* **Chargeback** – Understand the amount of resources that each user in a multi-tenant environment is using


## Building and Running Garcon
Garcon is built using the popular Dropwizard Java microframework and the standard gamut of Java development tools EX: Maven. To build the application
simple run ```mvn clean install package``` from the projects root folder. This will generate a single jar that will be located in the target directory.

Dropwizard is really neat in that it produces a single jar with a standard java main method that will accept differeing arguments depending on how you would
like to run the microservice. For example the standard way this application will be ran is as a "server". This means that Dropwizard will start up a Jetty
server and serve both the UI and the REST API components of the application from the single jar. To command to run in this mode is.

```java -jar ./web/target/nifi-device-registry-1.2.0-SNAPSHOT.jar server ./web/Garcon.yml```

The "Garcon.yml" file is a single location where you can adjust the configurations for the Garcon application.

## Garcon UI
The Garcon UI is a single page HTML5 application that is meant to allow the end user visibility to the Apache NiFi components sprawling over
the entire organization. Often there are several thousand instances of Apache NiFi, MiNifi, and MiNiFi-C++ running so this allows the user a convenient
single location to better understand the state of NiFi as well as basic informaiton about the devices NiFi is running on.

The UI can be accessed at: ```http://IP_OF_SERVER:8888/assets/index.html```


## Running via Docker
Hands down the easiest way to start using Garcon is via Docker. There are several dependencies and to ease the initial run configuration
I have offered a single fat Docker image that contains all of the required dependencies. If you don't wish to run via Docker I still recommend looking at the
Dockerfile as it will give the most up to date way to manually install Garcon on your system. You can find more information on running via Docker
 at [Running with Docker](./docker/README.md)