package de.lbakker77.retracker.core.config;

import de.lbakker77.retracker.core.BaseResponse;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class BaseResponseAdvice implements ResponseBodyAdvice<BaseResponse> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return BaseResponse.class.isAssignableFrom(returnType.getParameterType());
    }

    @Override
    public BaseResponse beforeBodyWrite(BaseResponse body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (body != null && !body.isSuccess()) {
            log.info("Request not successful: {}", request.getURI());
            response.setStatusCode(HttpStatus.BAD_REQUEST);
        }
        return body;
    }


}