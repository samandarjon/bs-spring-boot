package live.akbarov.bsspringboot.actuator.health;

import live.akbarov.bsspringboot.service.CustomerService;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

/**
 * Custom health indicator that checks if the customer service is functioning properly.
 * This will be exposed via the /actuator/health endpoint.
 */
@Component
public class CustomerServiceHealthIndicator implements HealthIndicator {

    private final CustomerService customerService;

    public CustomerServiceHealthIndicator(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Override
    public Health health() {
        try {
            // Try to retrieve the first page of customers with just one item
            // This is a lightweight operation to check if the service is working
            long customerCount = customerService.getCustomers(PageRequest.of(0, 1)).getTotalElements();
            
            return Health.up()
                    .withDetail("service", "CustomerService")
                    .withDetail("status", "Available")
                    .withDetail("customerCount", customerCount)
                    .build();
        } catch (Exception e) {
            return Health.down()
                    .withDetail("service", "CustomerService")
                    .withDetail("status", "Unavailable")
                    .withDetail("error", e.getMessage())
                    .build();
        }
    }
}