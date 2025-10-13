package cloud.cholewa.reporter.infrastructure.error;

import cloud.cholewa.reporter.infrastructure.error.processor.DefaultExceptionProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Objects;

@Slf4j
@Order(-2)
public class GlobalErrorWebExceptionHandler extends AbstractErrorWebExceptionHandler {

    private final Map<Class<? extends Exception>, ExceptionProcessor> exceptionProcessors;
    private final DefaultExceptionProcessor defaultExceptionProcessor = new DefaultExceptionProcessor();

    public GlobalErrorWebExceptionHandler(
        final ErrorAttributes errorAttributes,
        final WebProperties.Resources resources,
        final ApplicationContext applicationContext,
        final ServerCodecConfigurer serverCodecConfigurer
    ) {
        super(errorAttributes, resources, applicationContext);
        this.setMessageWriters(serverCodecConfigurer.getWriters());

        exceptionProcessors = Map.ofEntries();
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(final ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    /*
    * Method overrode to not have a log for the parent method.
    * Only logging from default processor is required
     */
    @Override
    protected void logError(final ServerRequest request, final ServerResponse response, final Throwable throwable) {
        //log.error("Error processing request [{}]: {}", request.uri(), throwable.getLocalizedMessage());
    }

    Mono<ServerResponse> renderErrorResponse(final ServerRequest request) {
        final ExceptionMessage exceptionMessage = getExceptionMessage(getError(request));

        return ServerResponse
            .status(HttpStatus.valueOf(Objects.requireNonNull(exceptionMessage).getErrorCode()))
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(Objects.requireNonNull(getExceptionMessage(getError(request)))));
    }

    private ExceptionMessage getExceptionMessage(final Throwable throwable) {
        if (throwable instanceof WebClientResponseException) {
            return null;
        }

        return exceptionProcessors.getOrDefault(throwable.getClass(), defaultExceptionProcessor)
            .process(throwable);
    }
}
