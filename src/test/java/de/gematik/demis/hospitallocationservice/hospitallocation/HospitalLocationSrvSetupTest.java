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

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;

@SpringBootTest
class HospitalLocationSrvSetupTest {

  @Autowired ObjectMapper objectMapper;

  @Test
  void shouldFailDueToDuplicateIk() {
    final HospitalLocationSrv underTest =
        createService("verzeichnisabruf-test.xml", "invalid-ik.json");
    Assertions.assertThatThrownBy(underTest::setUpData)
        .isInstanceOf(BeanInitializationException.class)
        .hasMessageContaining("261101015");
  }

  @Test
  void successfullyInitialized(
      @Value("${data.uri}") String data, @Value("${data.test.uri}") String testData) {
    final HospitalLocationSrv underTest = createService(data, testData);
    Assertions.assertThatNoException().isThrownBy(underTest::setUpData);
  }

  private HospitalLocationSrv createService(final String data, final String testData) {
    final HospitalLocationSrv underTest = new HospitalLocationSrv(objectMapper);
    underTest.resource = new ClassPathResource(data);
    underTest.testResource = new ClassPathResource(testData);
    return underTest;
  }
}
