package de.gematik.demis.hospitallocationservice.exceptions;

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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpStatus.*;

import jakarta.servlet.http.HttpServletRequest;
import java.nio.file.AccessDeniedException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.mock.http.MockHttpInputMessage;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;

class ExceptionCtrTest {
  private final ExceptionCtr exceptionCtr = new ExceptionCtr();

  @DisplayName("should handle BadRequestException for HttpMessageNotReadableException")
  @Test
  void shouldHandleBadRequestExceptionForHttpMediaTypeNotSupported() {
    final Exception e =
        new HttpMessageNotReadableException(
            "someInternalException", new MockHttpInputMessage("dummy".getBytes()));
    final ResponseEntity<String> resp =
        exceptionCtr.handleBadRequestException(e, new MockHttpServletRequest());
    assertThat(resp.getStatusCode()).isEqualTo(BAD_REQUEST);
    assertThat(resp.getBody()).contains("someInternalException");
  }

  @DisplayName("should handle BadRequestException for AccessDeniedException")
  @Test
  void shouldHandleBadRequestExceptionForAccessDeniedException() {
    final Exception e = new AccessDeniedException("someInternalException");
    final ResponseEntity<String> resp =
        exceptionCtr.handleAccessDeniedException(e, new MockHttpServletRequest());
    assertThat(resp.getStatusCode()).isEqualTo(FORBIDDEN);
    assertThat(resp.getBody()).contains("someInternalException");
  }

  @DisplayName("should handle BadRequestException for HttpRequestMethodNotSupportedException")
  @Test
  void shouldHandleBadRequestExceptionForHttpRequestMethodNotSupportedException() {
    final Exception e = new HttpMediaTypeNotSupportedException("someInternalException");
    final ResponseEntity<String> resp =
        exceptionCtr.handleMethodNotAllowedException(e, new MockHttpServletRequest());
    assertThat(resp.getStatusCode()).isEqualTo(METHOD_NOT_ALLOWED);
    assertThat(resp.getBody()).contains("someInternalException");
  }

  @DisplayName("should handle BadRequestException for HttpMediaTypeNotSupportedException")
  @Test
  void shouldHandleBadRequestExceptionForHttpMediaTypeNotSupportedException() {
    final Exception e = new HttpMediaTypeNotSupportedException("someInternalException");
    final ResponseEntity<String> resp =
        exceptionCtr.handleUnsupportedMediaTypeException(e, new MockHttpServletRequest());
    assertThat(resp.getStatusCode()).isEqualTo(UNSUPPORTED_MEDIA_TYPE);
    assertThat(resp.getBody()).contains("someInternalException");
  }

  @DisplayName("should handle BadRequestException for HttpMediaTypeNotAcceptableException")
  @Test
  void shouldHandleBadRequestExceptionForHttpMediaTypeNotAcceptableException() {
    final Exception e = new HttpMediaTypeNotAcceptableException("someInternalException");
    final ResponseEntity<String> resp =
        exceptionCtr.handleUnsupportedMediaTypeException(e, new MockHttpServletRequest());
    assertThat(resp.getStatusCode()).isEqualTo(UNSUPPORTED_MEDIA_TYPE);
    assertThat(resp.getBody()).contains("someInternalException");
  }

  @DisplayName("should handle BadRequestException for internal server error")
  @Test
  void shouldHandleBadRequestExceptionForInternalServerError() {
    final Exception e = new RuntimeException("someInternalException");
    final ResponseEntity<String> resp =
        exceptionCtr.handleAllOtherExceptions(e, new MockHttpServletRequest());
    assertThat(resp.getStatusCode()).isEqualTo(INTERNAL_SERVER_ERROR);
    assertThat(resp.getBody()).contains("someInternalException");
  }

  @DisplayName("should handle missing accept header")
  @Test
  void shouldHandleMissingAcceptHeaderAndMissingContentTypeHeader() {
    final Exception e = new RuntimeException("someInternalException");
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getHeader(ACCEPT)).thenReturn(null);
    when(request.getHeader(CONTENT_TYPE)).thenReturn(null);
    final ResponseEntity<String> resp = exceptionCtr.handleAllOtherExceptions(e, request);
    assertThat(resp.getStatusCode()).isEqualTo(INTERNAL_SERVER_ERROR);
    assertThat(resp.getBody()).contains("someInternalException");

    assertThat(resp.getHeaders().get(CONTENT_TYPE)).containsExactly("application/json");
  }

  @DisplayName("test accept header handling without content type")
  @ParameterizedTest
  @ValueSource(strings = {" ", "", "*", "application/json"})
  void shouldReturnApplicationJson(String accept) {
    final Exception e = new RuntimeException("someInternalException");
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getHeader(ACCEPT)).thenReturn(accept);
    when(request.getHeader(CONTENT_TYPE)).thenReturn(null);
    final ResponseEntity<String> resp = exceptionCtr.handleAllOtherExceptions(e, request);
    assertThat(resp.getStatusCode()).isEqualTo(INTERNAL_SERVER_ERROR);
    assertThat(resp.getBody()).contains("someInternalException");

    assertThat(resp.getHeaders().get(CONTENT_TYPE)).containsExactly("application/json");
  }

  @DisplayName("test accept header handling with given content type")
  @ParameterizedTest
  @ValueSource(strings = {" ", "", "*", "application/xml"})
  void shouldReturnApplicationXml(String accept) {
    final Exception e = new RuntimeException("someInternalException");
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getHeader(ACCEPT)).thenReturn(accept);
    when(request.getHeader(CONTENT_TYPE)).thenReturn("application/xml");
    final ResponseEntity<String> resp = exceptionCtr.handleAllOtherExceptions(e, request);
    assertThat(resp.getStatusCode()).isEqualTo(INTERNAL_SERVER_ERROR);
    assertThat(resp.getBody()).contains("someInternalException");

    assertThat(resp.getHeaders().get(CONTENT_TYPE)).containsExactly("application/xml");
  }
}
