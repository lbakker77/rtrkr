package de.lbakker77.retracker.main.architecture;

import de.lbakker77.retracker.core.BaseRequest;
import de.lbakker77.retracker.core.BaseResponse;
import de.lbakker77.retracker.core.BaseUseCaseHandler;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.reflections.ReflectionUtils.*;

import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

import static org.reflections.scanners.Scanners.SubTypes;

class RequestResponseUseCaseTests {

    @Test
    void everyResponseHasADefaultConstructor() {
        // Arrange
        Reflections reflections = new Reflections("de.lbakker77.retracker");
        Set<Class<?>> subTypes =
                reflections.get(SubTypes.of(BaseResponse.class).asClass());
        subTypes.add(BaseResponse.class);
        assertThat(subTypes).isNotEmpty();
        for (Class<?> requestClass : subTypes) {
            var constructors = get(Constructors.of(requestClass));
            assertThat(constructors.stream().anyMatch(c -> c.getParameterCount() == 0 && c.getModifiers() == Modifier.PUBLIC))
                    .withFailMessage("Response class %s should have a default constructor", requestClass.getSimpleName())
                    .isTrue();
        }
    }

    @Test
    void everyRequestAdheresToTheNamingConvention() {
        // Arrange
        Reflections reflections = new Reflections("de.lbakker77.retracker");
        Set<Class<?>> subTypes =
                reflections.get(SubTypes.of(BaseRequest.class).asClass());
        
        assertThat(subTypes).isNotEmpty();
        for (Class<?> requestClass : subTypes) {
            assertThat(requestClass.getSimpleName())
                    .withFailMessage("Request class %s should end with 'Request'", requestClass.getSimpleName())
                    .endsWith("Request");
        }
    }

    @Test
    void everyResponseAdheresToTheNamingConvention() {
        // Arrange
        Reflections reflections = new Reflections("de.lbakker77.retracker");
        Set<Class<?>> subTypes =
                reflections.get(SubTypes.of(BaseResponse.class).asClass());

        assertThat(subTypes).isNotEmpty();
        for (Class<?> responseClass : subTypes) {
            assertThat(responseClass.getSimpleName())
                    .withFailMessage("Request class %s should end with 'Response'", responseClass.getSimpleName())
                    .endsWith("Response");
        }
    }

    @Test
    void everyUseCaseClassAdheresToTheNamingConvention() {
        // Arrange
        Reflections reflections = new Reflections("de.lbakker77.retracker");
        Set<Class<?>> subTypes =
                reflections.get(SubTypes.of(BaseUseCaseHandler.class).asClass());

        assertThat(subTypes).isNotEmpty();
        for (Class<?> useCaseClass : subTypes) {
            assertThat(useCaseClass.getSimpleName())
                    .withFailMessage("UseCase class %s should end with 'UseCase'", useCaseClass.getSimpleName())
                    .endsWith("UseCase");
        }
    }

    @Test
    void thereIsAlwaysOnlyOneUseCaseImplementationPerRequest() {
        // Arrange
        Reflections reflections = new Reflections("de.lbakker77.retracker");
        Set<Class<?>> subTypes =
                reflections.get(SubTypes.of(BaseUseCaseHandler.class).asClass());

        assertThat(subTypes).isNotEmpty();
        Set<Type> requestTypes = new HashSet<>();
        for (Class<?> useCaseClass : subTypes) {
            var requestType = ((ParameterizedType)useCaseClass.getGenericSuperclass()).getActualTypeArguments()[0];
            assertThat(requestTypes).withFailMessage("UseCase %s uses request %s that is already processed", useCaseClass, requestType).doesNotContain(requestType);
            requestTypes.add(requestType);
        }
    }
}
