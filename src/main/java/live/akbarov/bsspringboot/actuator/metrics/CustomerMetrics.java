package live.akbarov.bsspringboot.actuator.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * Custom metrics for tracking customer operations.
 * These metrics will be exposed via the /actuator/prometheus endpoint.
 */
@Component
public class CustomerMetrics {

    private final Counter customerCreatedCounter;
    private final Counter customerUpdatedCounter;
    private final Counter customerDeletedCounter;
    private final Timer customerOperationTimer;

    public CustomerMetrics(MeterRegistry meterRegistry) {
        // Create counters for customer operations
        this.customerCreatedCounter = Counter.builder("app.customer.created")
                .description("Number of customers created")
                .register(meterRegistry);
        
        this.customerUpdatedCounter = Counter.builder("app.customer.updated")
                .description("Number of customers updated")
                .register(meterRegistry);
        
        this.customerDeletedCounter = Counter.builder("app.customer.deleted")
                .description("Number of customers deleted")
                .register(meterRegistry);
        
        // Create a timer for measuring customer operation duration
        this.customerOperationTimer = Timer.builder("app.customer.operation.time")
                .description("Time taken to perform customer operations")
                .register(meterRegistry);
    }

    /**
     * Increment the counter for customer creation.
     */
    public void incrementCustomerCreated() {
        customerCreatedCounter.increment();
    }

    /**
     * Increment the counter for customer updates.
     */
    public void incrementCustomerUpdated() {
        customerUpdatedCounter.increment();
    }

    /**
     * Increment the counter for customer deletions.
     */
    public void incrementCustomerDeleted() {
        customerDeletedCounter.increment();
    }

    /**
     * Record the time taken to perform a customer operation.
     *
     * @param operation The operation to time
     * @param <T> The return type of the operation
     * @return The result of the operation
     */
    public <T> T timeOperation(Supplier<T> operation) {
        return customerOperationTimer.record(operation);
    }

    /**
     * Record the time taken to perform a customer operation that doesn't return a value.
     *
     * @param operation The operation to time
     */
    public void timeOperation(Runnable operation) {
        customerOperationTimer.record(operation);
    }
}