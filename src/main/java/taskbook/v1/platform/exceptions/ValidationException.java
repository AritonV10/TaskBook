package taskbook.v1.platform.exceptions;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import taskbook.v1.platform.utility.JSONR;

@Provider
public class ValidationException implements ExceptionMapper<ConstraintViolationException>{
	
	@Inject
	private JSONR jsonResponse;
	
	@Override
	public Response toResponse(ConstraintViolationException exception) {
		return
				jsonResponse.onError(toList(exception.getConstraintViolations()));
	}
	
	private List<String> toList(final Set<ConstraintViolation<?>> set) {
		return set
			.stream()
			.map(ConstraintViolation::getMessage)
			.collect(Collectors.toList());
	}

}
