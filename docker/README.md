## Docker - Apache NiFi Device Registry

Running Apache NiFi Device Registry via Docker is extremely straightforward.

```mvn clean install package -Pdocker```

Then after the image has successfully built run.

```docker run -itd jdye64/nifideviceregistry:1.2.0-SNAPSHOT```