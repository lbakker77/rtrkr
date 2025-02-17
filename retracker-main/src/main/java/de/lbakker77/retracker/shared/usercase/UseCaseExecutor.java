package de.lbakker77.retracker.shared.usercase;


import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UseCaseExecutor {
    private final Map<Class<?>, BaseUseCaseHandler<?,?>>  useCaseHandlerMap;
    private final Validator validator;

    @Autowired
    public UseCaseExecutor(List<BaseUseCaseHandler<?,?>> useCaseHandlers, Validator validator) {
        this.validator = validator;
        useCaseHandlerMap = useCaseHandlers.stream().collect(Collectors.toMap(handler -> (Class<?>)((ParameterizedType)handler.getClass().getGenericSuperclass()).getActualTypeArguments()[0], handler -> handler));
    }

    public <Response extends BaseResponse, Request extends BaseRequest> Response execute(Request request) {
        @SuppressWarnings("unchecked")

        BaseUseCaseHandler<Request, Response> usecaseHandler = (BaseUseCaseHandler<Request, Response>) useCaseHandlerMap.get(request.getClass());
        return usecaseHandler.handle(request);

    }

    public <Response extends BaseResponse, Request extends BaseRequest> Response execute(Request request, Class<Response> c) {
        return execute(request);
    }
}
