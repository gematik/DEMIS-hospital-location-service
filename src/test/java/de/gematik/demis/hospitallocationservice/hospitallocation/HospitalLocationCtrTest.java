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

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import de.gematik.demis.hospitallocationservice.BaseTest;
import de.gematik.demis.hospitallocationservice.models.HospitalLocationDTO;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class HospitalLocationCtrTest implements BaseTest {

  @Mock private HospitalLocationSrv hospitalLocationSrvMock;

  @InjectMocks private HospitalLocationCtr hospitalLocationCtr;

  @Test
  void shouldCallHospitalLocationServiceAndReturnAnswerInResponseEntity() {

    HospitalLocationDTO hospitalLocationDTO = createDefaultHospitalLocationDTO();
    List<HospitalLocationDTO> hospitalLocationDTOList = singletonList(hospitalLocationDTO);
    when(hospitalLocationSrvMock.findByIk(anyString())).thenReturn(hospitalLocationDTOList);

    ResponseEntity<List<HospitalLocationDTO>> expectedResponseEntity =
        ResponseEntity.ok(hospitalLocationDTOList);

    ResponseEntity<List<HospitalLocationDTO>> actualResponseEntity =
        hospitalLocationCtr.findByIk("1");

    assertThat(actualResponseEntity).isEqualTo(expectedResponseEntity);
  }
}
