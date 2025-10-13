package cloud.cholewa.reporter.infrastructure.error;

@FunctionalInterface
public interface ExceptionProcessor {

    ExceptionMessage process(final Throwable throwable);
}
