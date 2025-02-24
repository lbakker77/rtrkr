package de.lbakker77.retracker.core.usercase;

import de.lbakker77.retracker.core.interceptor.UserTimeZoneService;
import de.lbakker77.retracker.user.UserService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;

import java.lang.reflect.ParameterizedType;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class BaseUseCaseHandler<Request extends BaseRequest, Response extends BaseResponse> {
    private UserService userService;
    private UserTimeZoneService userTimeZoneService;
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
    private void setUserTimeZoneService(@NonNull UserTimeZoneService userTimeZoneService) {
        this.userTimeZoneService = userTimeZoneService;
    }

    private final Class<Response> responseClass;

    protected BaseUseCaseHandler() {
        this.responseClass = getResponseClass();
    }

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
        return handle(request, new CommandContext(userService.getUserIdOrCreateIfNew(), userTimeZoneService.getUserTimeZone()));
    }

    protected abstract Response handle(Request command, CommandContext commandContext);

    private Response createErrorResponse(@NonNull Set<ConstraintViolation<Request>> validationResult) {
        try {
            var response = responseClass.getDeclaredConstructor().newInstance();
            response.setViolations(validationResult.stream()
                    .map(v -> new Violation(v.getPropertyPath().toString(), v.getMessage()))
                    .collect(Collectors.toList()));
            response.setSuccess(false);
            return response;
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Failed to create error response", e);
        }
    }
}
