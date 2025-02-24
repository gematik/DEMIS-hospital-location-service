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

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@EqualsAndHashCode
public class HospitalLocationDTO {

  private Integer id;
  private String ik;
  private String label;
  private String postalCode;
  private String city;
  private String line;
  private String houseNumber;

  public HospitalLocationDTO(final HospitalLocation hospitalLocation) {
    label = hospitalLocation.getLabel();
    id = hospitalLocation.getId();
    city = hospitalLocation.getAddress().getCity();
    postalCode = hospitalLocation.getAddress().getPostalCode();
    line = hospitalLocation.getAddress().getLine();
    houseNumber = hospitalLocation.getAddress().getHouseNumber();
    ik = hospitalLocation.getReferenceHospital().getIk();
  }

  @Override
  public String toString() {
    return String.format(
        """
        HospitalLocationDTO {
          id=%s,
          ik='%s',
          label='%s',
          postalCode='%s',
          city='%s',
          line='%s',
          houseNumber='%s'
        }
        """,
        id, ik, label, postalCode, city, line, houseNumber);
  }
}
