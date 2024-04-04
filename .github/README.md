<img align="right" width="250" height="47" src="../media/Gematik_Logo_Flag.png"/> <br/>

# Hospital-Location-Service

<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
       <ul>
        <li><a href="#quality-gate">Quality Gate</a></li>
        <li><a href="#release-notes">Release Notes</a></li>
      </ul>
	</li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#test">Test</a></li>
        <li><a href="#docker">Docker</a></li>
        <li><a href="#local">Local</a></li>
      </ul>
    </li>
    <li><a href="#endpoints">Endpoints</a></li>
    <li><a href="#data-origin">Data origin</a></li>
    <li><a href="#usage">Usage</a></li>
    <li><a href="#cucumber-integrationtests">Cucumber Integrationtests</a></li>
    <li><a href="#security-policy">Security Policy</a></li>
    <li><a href="#contributing">Contributing</a></li>
    <li><a href="#license">License</a></li>
    <li><a href="#contact">Contact</a></li>
  </ol>
</details>

## About the Project

This service provides information for hospital location data. For any valid IK-Number it provides a list of allowed/registered/known hospitals and the addresses of those.

### Quality Gate
[![Quality Gate Status](https://sonar.prod.ccs.gematik.solutions/api/project_badges/measure?project=de.gematik.demis%3Ahospital-location-service&metric=alert_status&token=a0e957b0913694adb1395cafd1a1183f561e8eb5)](https://sonar.prod.ccs.gematik.solutions/dashboard?id=de.gematik.demis%3Ahospital-location-service)|[![Vulnerabilities](https://sonar.prod.ccs.gematik.solutions/api/project_badges/measure?project=de.gematik.demis%3Ahospital-location-service&metric=vulnerabilities&token=a0e957b0913694adb1395cafd1a1183f561e8eb5)](https://sonar.prod.ccs.gematik.solutions/dashboard?id=de.gematik.demis%3Ahospital-location-service)|[![Bugs](https://sonar.prod.ccs.gematik.solutions/api/project_badges/measure?project=de.gematik.demis%3Ahospital-location-service&metric=bugs&token=a0e957b0913694adb1395cafd1a1183f561e8eb5)](https://sonar.prod.ccs.gematik.solutions/dashboard?id=de.gematik.demis%3Ahospital-location-service)|[![Code Smells](https://sonar.prod.ccs.gematik.solutions/api/project_badges/measure?project=de.gematik.demis%3Ahospital-location-service&metric=code_smells&token=a0e957b0913694adb1395cafd1a1183f561e8eb5)](https://sonar.prod.ccs.gematik.solutions/dashboard?id=de.gematik.demis%3Ahospital-location-service)|[![Lines of Code](https://sonar.prod.ccs.gematik.solutions/api/project_badges/measure?project=de.gematik.demis%3Ahospital-location-service&metric=ncloc&token=a0e957b0913694adb1395cafd1a1183f561e8eb5)](https://sonar.prod.ccs.gematik.solutions/dashboard?id=de.gematik.demis%3Ahospital-location-service)|[![Coverage](https://sonar.prod.ccs.gematik.solutions/api/project_badges/measure?project=de.gematik.demis%3Ahospital-location-service&metric=coverage&token=a0e957b0913694adb1395cafd1a1183f561e8eb5)](https://sonar.prod.ccs.gematik.solutions/dashboard?id=de.gematik.demis%3Ahospital-location-service)

[![Quality gate](https://sonar.prod.ccs.gematik.solutions/api/project_badges/quality_gate?project=de.gematik.demis%3Ahospital-location-service&token=a0e957b0913694adb1395cafd1a1183f561e8eb5)](https://sonar.prod.ccs.gematik.solutions/dashboard?id=de.gematik.demis%3Ahospital-location-service)

### Release Notes

See [ReleaseNotes](../ReleaseNotes.md) for all information regarding the (newest) releases.


## Getting Started

### Test
Hint:  
the test-locations.json contains specific test data.  
The test locations belong to the ik numbers `987654321 & 098765432`.  
```
mvn clean verify
```

### Docker

build image with

```docker 
docker build -t europe-west3-docker.pkg.dev/gematik-all-infra-prod/demis-dev/hospital-location-service:latest .
```
the image can alternatively also be built with maven:
```docker
mvn clean install -Pdocker
```

run image as container `docker run -p 8083:8083 -dt --name hls-container europe-west3-docker.pkg.dev/gematik-all-infra-prod/demis-dev/hospital-location-service:latest`

### Local
in IntelliJ as SpringBoot Application
![image](../media/SpringBootApplicationHLS.png)

## Endpoints

`/status` get Endpunkt für Statusmeldung (aktuell minimal implementiert)

`/hospital-locations?ik=` Get Endpunkt für die Abfrage von Krankhausstandorten

`/actuator/health/` Standardenpunkt vom Actuator

`/actuator/health/liveness` Standardenpunkt vom Actuator

`/actuator/health/readiness` Standardenpunkt vom Actuator


## Data origin

Usualy the origin of the InEK data is <https://krankenhausstandorte.de/xml/latest>.

This file is currently stored in `src/main/resources` and it must be registered in the `src/main/resources/application.properties` file.

## Usage

### Intellij/cmd

Start the spring boot server with: `mvn clean spring-boot:run`
Check the server with: `curl -v localhost:8082/status`

## Cucumber Integrationtests
Execute with 'mvn verify' or in Intellij with start button
Report will be generated and saved under target/cucumber

## Security Policy

If you want to see the security policy, please check our [SECURITY.md](SECURITY.md).

## Contributing

If you want to contribute, please check our [CONTRIBUTING.md](CONTRIBUTING.md).

## License

EUROPEAN UNION PUBLIC LICENCE v. 1.2

EUPL © the European Union 2007, 2016

Copyright (c) 2023 gematik GmbH

See [LICENSE](../LICENSE.md).

## Contact

E-Mail to [DEMIS Entwicklung](mailto:demis-entwicklung@gematik.de?subject=[GitHub]%20Validation-Service)

