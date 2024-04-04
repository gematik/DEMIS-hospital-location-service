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

package de.gematik.demis.hospitallocationservice.exceptions;

import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;
import static org.springframework.http.HttpStatus.UNSUPPORTED_MEDIA_TYPE;

import jakarta.servlet.http.HttpServletRequest;
import java.nio.file.AccessDeniedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class ExceptionCtr {

  @ResponseStatus(BAD_REQUEST)
  @ExceptionHandler(
      value = {MissingRequestHeaderException.class, HttpMessageNotReadableException.class})
  public ResponseEntity<String> handleBadRequestException(
      final Exception exception, final HttpServletRequest request) {
    return createResponseEntity(exception, BAD_REQUEST, request);
  }

  @ResponseStatus(FORBIDDEN)
  @ExceptionHandler(value = AccessDeniedException.class)
  public ResponseEntity<String> handleAccessDeniedException(
      final Exception exception, final HttpServletRequest request) {

    return createResponseEntity(exception, FORBIDDEN, request);
  }

  @ResponseStatus(METHOD_NOT_ALLOWED)
  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ResponseEntity<String> handleMethodNotAllowedException(
      final Exception exception, final HttpServletRequest request) {

    return createResponseEntity(exception, METHOD_NOT_ALLOWED, request);
  }

  @ResponseStatus(UNSUPPORTED_MEDIA_TYPE)
  @ExceptionHandler({
    HttpMediaTypeNotSupportedException.class,
    HttpMediaTypeNotAcceptableException.class
  })
  public ResponseEntity<String> handleUnsupportedMediaTypeException(
      final Exception exception, final HttpServletRequest request) {

    return createResponseEntity(exception, UNSUPPORTED_MEDIA_TYPE, request);
  }

  @ResponseStatus(INTERNAL_SERVER_ERROR)
  @ExceptionHandler(value = {Exception.class})
  public ResponseEntity<String> handleAllOtherExceptions(
      final Exception exception, final HttpServletRequest request) {

    return createResponseEntity(exception, INTERNAL_SERVER_ERROR, request);
  }

  private ResponseEntity<String> createResponseEntity(
      final Exception exception, final HttpStatus httpStatus, final HttpServletRequest request) {

    log.error("Error processing request for {}", request.getPathInfo(), exception);

    final MediaType mediaType = getMediaType(request);
    return ResponseEntity.status(httpStatus)
        .contentType(mediaType)
        .body(exception.getLocalizedMessage());
  }

  private MediaType getMediaType(HttpServletRequest request) {
    String accept = request.getHeader(ACCEPT);
    if (StringUtils.isBlank(accept) || accept.contains("*")) {
      accept = request.getHeader(CONTENT_TYPE);
    }
    return accept == null ? MediaType.APPLICATION_JSON : MediaType.parseMediaType(accept);
  }
}
