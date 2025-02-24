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

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.gematik.demis.hospitallocationservice.BaseTest;
import de.gematik.demis.hospitallocationservice.models.HospitalLocationDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc()
@SpringBootTest
@ActiveProfiles("test")
class HospitalLocationCtrIT implements BaseTest {

  @Autowired ObjectMapper jsonMapper;
  @Autowired private MockMvc mockMvc;

  @DisplayName("standard happy case test")
  @Test
  void shouldReturnListForExistingIK() throws Exception {

    HospitalLocationDTO expectedReturnElement = createDefaultHospitalLocationDTO();

    HttpHeaders headers = new HttpHeaders();
    mockMvc
        .perform(get("/hospital-locations").param("ik", "260200570").headers(headers))
        .andExpect(status().isOk())
        .andExpect(
            header().string("Content-Type", containsString(MediaType.APPLICATION_JSON_VALUE)))
        .andExpect(
            content().string(jsonMapper.writeValueAsString(singletonList(expectedReturnElement))));
  }

  @DisplayName("")
  @Test
  void shouldReturnEmptyBodyForNoExistingIK() throws Exception {
    HttpHeaders headers = new HttpHeaders();
    mockMvc
        .perform(get("/hospital-locations").param("ik", "111111111").headers(headers))
        .andExpect(status().isOk())
        .andExpect(header().exists("Content-Type"))
        .andExpect(content().string(jsonMapper.writeValueAsString(emptyList())));
  }
}
