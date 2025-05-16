package live.akbarov.bsspringboot.actuator.tracing;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * Aspect for adding OpenTelemetry tracing to methods annotated with @WithSpan.
 */
@Aspect
@Component
public class TracingAspect {

    private final Tracer tracer;

    @Autowired
    public TracingAspect(OpenTelemetry openTelemetry) {
        this.tracer = openTelemetry.getTracer("live.akbarov.bsspringboot");
    }

    /**
     * Adds a span around methods annotated with @WithSpan.
     *
     * @param joinPoint The join point for the method call
     * @return The result of the method call
     * @throws Throwable If an error occurs during the method call
     */
    @Around("@annotation(io.opentelemetry.instrumentation.annotations.WithSpan)")
    public Object traceMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        
        WithSpan withSpan = method.getAnnotation(WithSpan.class);
        String spanName = withSpan.value().isEmpty() 
                ? method.getDeclaringClass().getSimpleName() + "." + method.getName() 
                : withSpan.value();
        
        Span span = tracer.spanBuilder(spanName).startSpan();
        
        try {
            // Add method parameters as span attributes
            Object[] args = joinPoint.getArgs();
            String[] paramNames = signature.getParameterNames();
            for (int i = 0; i < args.length; i++) {
                if (args[i] != null && isPrimitiveOrString(args[i].getClass())) {
                    span.setAttribute(paramNames[i], args[i].toString());
                }
            }
            
            // Execute the method
            return joinPoint.proceed();
        } catch (Throwable t) {
            // Record the exception
            span.recordException(t);
            throw t;
        } finally {
            // End the span
            span.end();
        }
    }
    
    /**
     * Checks if a class is a primitive type or a String.
     *
     * @param clazz The class to check
     * @return True if the class is a primitive type or a String, false otherwise
     */
    private boolean isPrimitiveOrString(Class<?> clazz) {
        return clazz.isPrimitive() 
                || clazz == String.class 
                || clazz == Boolean.class 
                || clazz == Character.class 
                || Number.class.isAssignableFrom(clazz);
    }
}