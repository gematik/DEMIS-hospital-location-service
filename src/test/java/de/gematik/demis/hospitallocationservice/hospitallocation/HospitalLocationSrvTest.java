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

import static org.assertj.core.api.Assertions.assertThat;

import de.gematik.demis.hospitallocationservice.BaseTest;
import de.gematik.demis.hospitallocationservice.models.HospitalLocationDTO;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class HospitalLocationSrvTest implements BaseTest {

  @Autowired private HospitalLocationSrv hospitalLocationSrv;

  @Test
  void shouldCreateMapAndReturnLocationsForIK() {
    List<HospitalLocationDTO> dataForKey = hospitalLocationSrv.findByIk("261101015");

    assertThat(dataForKey).isNotEmpty();
  }

  @Test
  void shouldReturnCompleteListForIK() {
    assertThat(hospitalLocationSrv.findByIk("261101015")).hasSize(7);
    assertThat(hospitalLocationSrv.findByIk("260200570")).hasSize(1);
  }

  @Test
  void shouldReturnEmptyListForNoFindings() {
    assertThat(hospitalLocationSrv.findByIk("0")).isEmpty();
  }

  @Test
  void shouldReturnListOfHospitalLocationOutput() {
    List<HospitalLocationDTO> dataForKey = hospitalLocationSrv.findByIk("260200570");

    assertThat(dataForKey).hasOnlyElementsOfType(HospitalLocationDTO.class);
  }

  @Test
  void shouldCreateHospitalLocationOutputFromInput() {

    List<HospitalLocationDTO> dataForKey = hospitalLocationSrv.findByIk("260200570");
    HospitalLocationDTO hospitalLocationDTO = createDefaultHospitalLocationDTO();
    assertThat(dataForKey).containsExactlyInAnyOrder(hospitalLocationDTO);
  }

  @Test
  void shouldRemoveExpiredLocations() {
    List<HospitalLocationDTO> dataForKey = hospitalLocationSrv.findByIk("999999999");
    assertThat(dataForKey).isEmpty();
  }

  @Test
  void shouldReturnLocationsForLeadingZeroIkNumber() {
    List<HospitalLocationDTO> locations = hospitalLocationSrv.findByIk("098765432");

    assertThat(locations)
        .isNotEmpty()
        .hasSize(1)
        .element(0)
        .hasNoNullFieldsOrProperties()
        .hasFieldOrPropertyWithValue("id", 987653)
        .hasFieldOrPropertyWithValue("ik", "098765432")
        .hasFieldOrPropertyWithValue("label", "Testkrankenhaus - gematik GmbH")
        .hasFieldOrPropertyWithValue("postalCode", "10117")
        .hasFieldOrPropertyWithValue("city", "Berlin")
        .hasFieldOrPropertyWithValue("line", "Friedrichstraße")
        .hasFieldOrPropertyWithValue("houseNumber", "136");
  }
}
