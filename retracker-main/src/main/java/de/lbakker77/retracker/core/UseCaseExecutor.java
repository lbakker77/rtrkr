package de.lbakker77.retracker.core;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UseCaseExecutor {
    private final Map<Class<?>, BaseUseCaseHandler<?,?>>  useCaseHandlerMap;

    @Autowired
    public UseCaseExecutor(List<BaseUseCaseHandler<?,?>> useCaseHandlers) {
        useCaseHandlerMap = useCaseHandlers.stream().collect(Collectors.toMap(handler -> (Class<?>)((ParameterizedType)handler.getClass().getGenericSuperclass()).getActualTypeArguments()[0], handler -> handler));
    }

    @Transactional
    public <Response extends BaseResponse, Request extends BaseRequest> Response execute(Request request) {
        Assert.notNull(request, "Request must not be null");
        @SuppressWarnings("unchecked")
        BaseUseCaseHandler<Request, Response> useCaseHandler = (BaseUseCaseHandler<Request, Response>) useCaseHandlerMap.get(request.getClass());
        return useCaseHandler.handle(request);
    }

    @Transactional
    public <Response extends BaseResponse, Request extends BaseRequest> Response execute(Request request, Class<Response> ignoredC) {
        Assert.notNull(request, "Request must not be null");
        @SuppressWarnings("unchecked")
        BaseUseCaseHandler<Request, Response> useCaseHandler = (BaseUseCaseHandler<Request, Response>) useCaseHandlerMap.get(request.getClass());
        return useCaseHandler.handle(request);
    }
}
