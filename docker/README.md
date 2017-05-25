## Docker - Apache NiFi Device Registry

Running Apache NiFi Device Registry via Docker is extremely straightforward.

```mvn clean install package -Pdocker```

Then after the image has successfully built run.

```docker run -d -p 8888:8888 -p 8080:8080 jdye64/nifideviceregistry:1.2.0-SNAPSHOT```