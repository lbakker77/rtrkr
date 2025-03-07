package de.lbakker77.retracker.core;

import de.lbakker77.retracker.core.interceptor.UserTimeZoneServiceImpl;
import de.lbakker77.retracker.user.UserService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;

import java.lang.reflect.ParameterizedType;
import java.util.Set;

public abstract class BaseUseCaseHandler<Request extends BaseRequest, Response extends BaseResponse> {
    private UserService userService;
    private UserTimeZoneServiceImpl userTimeZoneService;
    private Validator validator;

    @Autowired
    private void setUserService(@NonNull UserService userService) {
        this.userService = userService;
    }

    @Autowired
    private void setValidator(@NonNull Validator validator) {
        this.validator = validator;
    }

    @Autowired
    private void setUserTimeZoneService(@NonNull UserTimeZoneServiceImpl userTimeZoneService) {
        this.userTimeZoneService = userTimeZoneService;
    }

    private final Class<Response> responseClass;

    protected BaseUseCaseHandler() {
        this.responseClass = getResponseClass();
    }

    @SuppressWarnings("unchecked")
    private Class<Response> getResponseClass() {
        var type = getClass().getGenericSuperclass();
        var paramType = (ParameterizedType) type;
        return (Class<Response>) paramType.getActualTypeArguments()[1];
    }

    public Response handle(Request request) {
        var validationResult = validator.validate(request);
        if (!validationResult.isEmpty()) {
            return createErrorResponse(validationResult);
        }
        return handle(request, new CommandContext(userService.getOrCreateUserId(), userTimeZoneService.getUserTimeZone()));
    }

    protected abstract Response handle(Request command, CommandContext commandContext);

    private Response createErrorResponse(@NonNull Set<ConstraintViolation<Request>> validationResult) {
        try {
            var response = responseClass.getDeclaredConstructor().newInstance();
            response.setViolations(validationResult.stream()
                    .map(v -> new Violation(v.getPropertyPath().toString(), v.getMessage()))
                    .toList());
            response.setSuccess(false);
            return response;
        } catch (ReflectiveOperationException e) {
            throw new UnsupportedOperationException("Failed to create error response", e);
        }
    }
}
