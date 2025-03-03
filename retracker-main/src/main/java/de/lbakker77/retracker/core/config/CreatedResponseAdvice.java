package de.lbakker77.retracker.core.config;

import de.lbakker77.retracker.core.CreatedResponse;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import org.springframework.lang.NonNullApi;

@ControllerAdvice
@Hidden
public class CreatedResponseAdvice implements ResponseBodyAdvice<CreatedResponse> {

    @Override
    public boolean supports(MethodParameter returnType,  Class<? extends HttpMessageConverter<?>> converterType) {
        return CreatedResponse.class.isAssignableFrom(returnType.getParameterType());
    }

    @Override
    public CreatedResponse beforeBodyWrite(CreatedResponse body,  MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (body != null && body.isSuccess()) {
            response.setStatusCode(HttpStatus.CREATED);
        }
        return body;
    }
}
