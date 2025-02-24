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

package de.gematik.demis.hospitallocationservice.models;

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
import org.junit.jupiter.api.Test;

class HospitalLocationDTOTest implements BaseTest {

  @Test
  void shouldReturnTrueForEquals() {
    HospitalLocationDTO hlo1 = createDefaultHospitalLocationDTO();

    HospitalLocationDTO hlo2 = createDefaultHospitalLocationDTO();

    boolean equals = hlo1.equals(hlo2);
    int i1 = hlo1.hashCode();
    int i2 = hlo2.hashCode();

    assertThat(i1).isEqualTo(i2);
    assertThat(equals).isTrue();
  }

  @Test
  void shouldReturnFalseForEqualsLabelDiffers() {
    HospitalLocationDTO hlo1 = createDefaultHospitalLocationDTO();

    HospitalLocationDTO hlo2 = createDefaultHospitalLocationDTO().setLabel("nice label");

    boolean equals = hlo1.equals(hlo2);

    assertThat(equals).isFalse();
  }

  @Test
  void shouldReturnFalseForEqualsLocationIdDiffers() {
    HospitalLocationDTO hlo1 = createDefaultHospitalLocationDTO();

    HospitalLocationDTO hlo2 = createDefaultHospitalLocationDTO().setId(77123);

    boolean equals = hlo1.equals(hlo2);

    assertThat(equals).isFalse();
  }

  @Test
  void shouldReturnFalseForEqualsPostalCodeDiffers() {
    HospitalLocationDTO hlo1 = createDefaultHospitalLocationDTO();

    HospitalLocationDTO hlo2 = createDefaultHospitalLocationDTO().setPostalCode("98765");

    boolean equals = hlo1.equals(hlo2);

    assertThat(equals).isFalse();
  }

  @Test
  void shouldReturnFalseForEqualsCityDiffers() {
    HospitalLocationDTO hlo1 = createDefaultHospitalLocationDTO();

    HospitalLocationDTO hlo2 = createDefaultHospitalLocationDTO().setCity("nice city");

    boolean equals = hlo1.equals(hlo2);

    assertThat(equals).isFalse();
  }

  @Test
  void shouldReturnFalseForEqualsLineDiffers() {
    HospitalLocationDTO hlo1 = createDefaultHospitalLocationDTO();

    HospitalLocationDTO hlo2 = createDefaultHospitalLocationDTO().setLine("nice line");

    boolean equals = hlo1.equals(hlo2);

    assertThat(equals).isFalse();
  }

  @Test
  void shouldReturnFalseForEqualsHouseNumberDiffers() {
    HospitalLocationDTO hlo1 = createDefaultHospitalLocationDTO();

    HospitalLocationDTO hlo2 = createDefaultHospitalLocationDTO().setHouseNumber("nice house nr");

    boolean equals = hlo1.equals(hlo2);

    assertThat(equals).isFalse();
  }

  @Test
  void shouldReturnFalseForEqualsIKDiffers() {
    HospitalLocationDTO hlo1 = createDefaultHospitalLocationDTO();

    HospitalLocationDTO hlo2 = createDefaultHospitalLocationDTO().setIk("123456");

    boolean equals = hlo1.equals(hlo2);

    assertThat(equals).isFalse();
  }
}
