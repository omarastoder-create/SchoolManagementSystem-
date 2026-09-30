package Rastoder.SchoolManagementSystem.exception;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.URI;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class GlobalExceptionHandlerTest extends RuntimeException {

    private final GlobalExceptionHandler underTest = new GlobalExceptionHandler();

    @Test
    void handleEntityNotFound_shouldReturn404ProblemDetail() {
        //Arrange
        EntityNotFoundException ex = new EntityNotFoundException("Student not Found");
        //Act
        ProblemDetail response = underTest.handleEntityNotFound(ex);
        //Assert
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(response.getDetail()).isEqualTo("Student not Found");
        assertThat(response.getTitle()).isEqualTo("Resource Not Found");
        assertThat(response.getType()).isEqualByComparingTo(URI.create(
                "https://school-management.com/errors/not-found"));
    }

    @Test
    void handleBusinessRuleViolation_shouldReturn409ProblemDetaill() {
        // Arrange
        BusinessRuleViolationException ex = new BusinessRuleViolationException("Group capacity of 15 exceeded");

        // Act
        ProblemDetail response = underTest.handleBusinessRuleViolation(ex);

        // Assert
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CONFLICT.value());
        assertThat(response.getTitle()).isEqualTo("Business Rule Violation");
        assertThat(response.getDetail()).isEqualTo("Group capacity of 15 exceeded");
        assertThat(response.getType()).isEqualTo(URI.create(
                "https://school-management.com/errors/business-rule-violation"));
    }

    @Test
    void handleValidationExceptions_shouldReturn400AndFieldErrors() {
        // Arrange
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("request", "email", "must not be blank");

        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

        ProblemDetail response = underTest.handleValidationErrors(ex);

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getProperties()).isNotNull();
        assertThat(response.getProperties().containsKey("error")).isTrue();
    }
}
