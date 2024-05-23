package uk.co.bluegecko.marine.shared.error;

import static java.lang.Boolean.TRUE;

import java.util.Map;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.validation.method.MethodValidationResult;
import org.springframework.web.context.request.WebRequest;

@Component
public class MarineErrorAttributes extends DefaultErrorAttributes {

	@Override
	public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
		Map<String, Object> attributes = super.getErrorAttributes(webRequest, options);
		if (getError(webRequest) instanceof MethodValidationResult result) {
			addValidationResultErrorMessage(attributes, result);
		}
		attributes.put("marine", TRUE);
		attributes.remove("status");
		return attributes;
	}

	private void addValidationResultErrorMessage(Map<String, Object> errorAttributes, MethodValidationResult result) {
		errorAttributes.put("errors", result.getAllErrors());
	}

}