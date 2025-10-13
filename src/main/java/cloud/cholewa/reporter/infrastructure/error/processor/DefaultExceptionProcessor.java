package cloud.cholewa.reporter.infrastructure.error.processor;

import cloud.cholewa.reporter.infrastructure.error.ExceptionMessage;
import cloud.cholewa.reporter.infrastructure.error.ExceptionProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
public class DefaultExceptionProcessor implements ExceptionProcessor {
    @Override
    public ExceptionMessage process(final Throwable throwable) {
        return ExceptionMessage.builder()
            .code(HttpStatus.INTERNAL_SERVER_ERROR.name())
            .title("Generic processing exception")
            .detail(throwable.getLocalizedMessage())
            .build();
    }
}
