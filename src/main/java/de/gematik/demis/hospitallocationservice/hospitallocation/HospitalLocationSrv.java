/*
 * Copyright [2024], gematik GmbH
 *
 * Licensed under the EUPL, Version 1.2 or - as soon they will be approved by the
 * European Commission – subsequent versions of the EUPL (the "Licence").
 * You may not use this work except in compliance with the Licence.
 *
 * You find a copy of the Licence in the "Licence" file or at
 * https://joinup.ec.europa.eu/collection/eupl/eupl-text-eupl-12
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either expressed or implied.
 * In case of changes by gematik find details in the "Readme" file.
 *
 * See the Licence for the specific language governing permissions and limitations under the Licence.
 */

package de.gematik.demis.hospitallocationservice.hospitallocation;

/*-
 * #%L
 * hospital-location-service
 * %%
 * Copyright (C) 2025 gematik GmbH
 * %%
 * Licensed under the EUPL, Version 1.2 or - as soon they will be approved by the
 * European Commission – subsequent versions of the EUPL (the "Licence").
 * You may not use this work except in compliance with the Licence.
 *
 * You find a copy of the Licence in the "Licence" file or at
 * https://joinup.ec.europa.eu/collection/eupl/eupl-text-eupl-12
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either expressed or implied.
 * In case of changes by gematik find details in the "Readme" file.
 *
 * See the Licence for the specific language governing permissions and limitations under the Licence.
 * #L%
 */

import static java.util.stream.Collectors.groupingBy;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import de.gematik.demis.hospitallocationservice.models.HospitalLocation;
import de.gematik.demis.hospitallocationservice.models.HospitalLocationDTO;
import io.micrometer.observation.annotation.Observed;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class HospitalLocationSrv {

  private static final String STANDORT = "Standort";

  private final ObjectMapper jsonMapper;

  private Map<String, List<HospitalLocationDTO>> hospitalLocationsMap;

  @Value("classpath:${data.uri}")
  Resource resource;

  @Value("classpath:${data.test.uri}")
  Resource testResource;

  @Observed(
      name = "locations",
      contextualName = "locations",
      lowCardinalityKeyValues = {"locations", "fhir"})
  public List<HospitalLocationDTO> findByIk(String ik) {
    final List<HospitalLocationDTO> locations =
        hospitalLocationsMap.getOrDefault(ik, Collections.emptyList());
    log.debug("Received Request for ik {}. Found {} location(s)", ik, locations.size());
    return locations;
  }

  @PostConstruct
  void setUpData() throws IOException {
    hospitalLocationsMap = new HashMap<>();
    XmlMapper xmlMapper = new XmlMapper();
    JsonNode jsonNode = xmlMapper.readTree(resource.getInputStream()).get(STANDORT);
    List<HospitalLocation> locations = jsonMapper.convertValue(jsonNode, new TypeReference<>() {});
    log.info("All real locations loaded: {}", locations.size());

    processDataAndFillMap(locations);
    addTestLocations();
  }

  private void processDataAndFillMap(List<HospitalLocation> locations) {
    hospitalLocationsMap =
        locations.stream()
            .filter(this::isActiveLocation)
            .map(HospitalLocationDTO::new)
            .collect(groupingBy(HospitalLocationDTO::getIk));
    log.info("active ik(s): {}", hospitalLocationsMap.size());
  }

  private void addTestLocations() throws IOException {
    final List<HospitalLocationDTO> testLocations =
        jsonMapper.readValue(testResource.getInputStream(), new TypeReference<>() {});
    log.info("test locations size: {}", testLocations.size());

    final Map<String, List<HospitalLocationDTO>> testMap =
        testLocations.stream().collect(groupingBy(HospitalLocationDTO::getIk));

    assertNoDuplicateIks(testMap.keySet(), hospitalLocationsMap.keySet());

    hospitalLocationsMap.putAll(testMap);
    log.info("active ik(s) inclusive test ik(s): {}", hospitalLocationsMap.size());
  }

  private static void assertNoDuplicateIks(final Set<String> testIks, final Set<String> iks) {
    final var duplicates = new HashSet<>(testIks);
    duplicates.retainAll(iks);
    if (!duplicates.isEmpty()) {
      log.error("Invalid Test IKs: {}", duplicates);
      throw new BeanInitializationException(
          "invalid test-locations. Iks already in use: " + duplicates);
    }
  }

  private boolean isActiveLocation(HospitalLocation location) {
    return location.getExpired() == null || location.getExpired().isAfter(LocalDate.now());
  }
}
