package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ErrorHandlerTest {

    private final ErrorHandler errorHandler = new ErrorHandler();

    @Test
    void handleNotFound_shouldReturnErrorMap() {
        RuntimeException exception = new NotFoundException("Test error");

        Map<String, String> result = errorHandler.handleNotFound(exception);

        assertNotNull(result);
        assertEquals("Test error", result.get("error"));
    }

    @Test
    void handleConflict_shouldReturnErrorMap() {
        RuntimeException exception = new ConflictException("Test conflict");

        Map<String, String> result = errorHandler.handleConflict(exception);

        assertNotNull(result);
        assertEquals("Test conflict", result.get("error"));
    }

    @Test
    void handleIllegalArgument_shouldReturnErrorMap() {
        IllegalArgumentException exception = new IllegalArgumentException("Test illegal argument");

        Map<String, String> result = errorHandler.handleIllegalArgument(exception);

        assertNotNull(result);
        assertEquals("Test illegal argument", result.get("error"));
    }

    @Test
    void handleValidationErrors_shouldReturnErrorMap() throws Exception {
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("objectName", "fieldName", "defaultMessage");

        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(Collections.singletonList(fieldError));

        Map<String, String> result = errorHandler.handleValidationErrors(exception);

        assertNotNull(result);
        assertTrue(result.containsKey("error"));
    }

    @Test
    void handleNotFound_withDifferentMessage_shouldReturnCorrectMessage() {
        RuntimeException exception = new NotFoundException("User not found");

        Map<String, String> result = errorHandler.handleNotFound(exception);

        assertEquals("User not found", result.get("error"));
    }

    @Test
    void handleConflict_withDifferentMessage_shouldReturnCorrectMessage() {
        RuntimeException exception = new ConflictException("Email already exists");

        Map<String, String> result = errorHandler.handleConflict(exception);

        assertEquals("Email already exists", result.get("error"));
    }

    @Test
    void handleIllegalArgument_withDifferentMessage_shouldReturnCorrectMessage() {
        IllegalArgumentException exception = new IllegalArgumentException("Invalid state");

        Map<String, String> result = errorHandler.handleIllegalArgument(exception);

        assertEquals("Invalid state", result.get("error"));
    }

}