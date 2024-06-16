package uk.co.bluegecko.marine.shared.error;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.error.ErrorAttributeOptions.Include;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.method.MethodValidationException;
import org.springframework.validation.method.MethodValidationResult;
import org.springframework.validation.method.ParameterErrors;
import org.springframework.validation.method.ParameterValidationResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class MarineExceptionHandler extends ResponseEntityExceptionHandler {

	private final ErrorAttributes errorAttributes;

	@Override
	protected ResponseEntity<Object> handleHandlerMethodValidationException(
			@NonNull HandlerMethodValidationException ex, @NonNull HttpHeaders headers, @NonNull HttpStatusCode status,
			@NonNull WebRequest request) {
		ProblemDetail problemDetail = createProblemDetail(ex, status, getValidationMessage(ex),
				ex.getDetailMessageCode(), ex.getDetailMessageArguments(), request);
		return handleMethodValidationResult(ex, problemDetail, headers, status, request);
	}

	@Override
	protected ResponseEntity<Object> handleMethodValidationException(
			@NonNull MethodValidationException ex, @NonNull HttpHeaders headers, @NonNull HttpStatus status,
			@NonNull WebRequest request) {
		ProblemDetail problemDetail = createProblemDetail(ex, status, getValidationMessage(ex),
				null, null, request);
		return handleMethodValidationResult(ex, problemDetail, headers, status, request);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
			@NonNull MethodArgumentNotValidException ex, @NonNull HttpHeaders headers, @NonNull HttpStatusCode status,
			@NonNull WebRequest request) {
		ProblemDetail problemDetail = createProblemDetail(ex, status, ex.getMessage(),
				ex.getDetailMessageCode(), ex.getDetailMessageArguments(), request);
		return handleMethodValidationResult(ex, problemDetail, headers, status, request);
	}

	private ResponseEntity<Object> handleMethodValidationResult(Exception ex,
			ProblemDetail problemDetail, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		Map<String, Object> properties = errorAttributes.getErrorAttributes(request, getOptions());
		if (problemDetail.getProperties() != null) {
			properties.putAll(problemDetail.getProperties());
		}
		if (ex instanceof ResponseStatusException exception) {
			problemDetail.setProperty("error", exception.getReason());
		} else {
			problemDetail.setProperty("error", "Validation Failure");
		}
		problemDetail.setProperties(properties);
		return handleExceptionInternal(ex, problemDetail, headers, status, request);
	}

	private String getValidationMessage(MethodValidationResult result) {
		return Stream.concat(result.getValueResults().stream().map(this::format),
						result.getBeanResults().stream().map(this::format))
				.collect(Collectors.joining(", "));
	}

	private String format(ParameterValidationResult r) {
		return String.format("[%s='%s'] %s", r.getMethodParameter().getParameter().getName(), r.getArgument(),
				r.getResolvableErrors().stream().map(MessageSourceResolvable::getDefaultMessage)
						.collect(Collectors.joining(", ")));
	}

	private String format(ParameterErrors r) {
		return String.format("[%s='%s'] %s", r.getMethodParameter().getParameter().getName(), r.getArgument(),
				r.getResolvableErrors().stream().map(MessageSourceResolvable::getDefaultMessage)
						.collect(Collectors.joining(", ")));
	}

	private ErrorAttributeOptions getOptions() {
		return ErrorAttributeOptions.of(Include.BINDING_ERRORS, Include.MESSAGE, Include.EXCEPTION);
	}

}