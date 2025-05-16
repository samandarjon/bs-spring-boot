# Spring Boot Actuator with OpenTelemetry

This project demonstrates the implementation of Spring Boot Actuator with OpenTelemetry for monitoring and observability.

## Features

### Spring Boot Actuator

Spring Boot Actuator is enabled with the following endpoints:

- `/actuator/health` - Shows application health information
- `/actuator/info` - Displays application information
- `/actuator/metrics` - Shows metrics information
- `/actuator/prometheus` - Exposes metrics in Prometheus format

### Custom Health Indicators

Two custom health indicators have been implemented:

1. **DatabaseHealthIndicator**: Checks database connectivity by executing a simple query.
2. **CustomerServiceHealthIndicator**: Verifies that the customer service is functioning properly by retrieving customers.

### Custom Metrics

Custom metrics have been implemented to track customer operations:

- `app.customer.created` - Counter for customer creation operations
- `app.customer.updated` - Counter for customer update operations
- `app.customer.deleted` - Counter for customer deletion operations
- `app.customer.operation.time` - Timer for measuring the duration of customer operations

These metrics are integrated with the CustomerService implementation and are automatically updated when customer operations are performed.

### OpenTelemetry Tracing

OpenTelemetry tracing has been implemented to provide distributed tracing capabilities:

- A custom TracingAspect that adds spans to methods annotated with @WithSpan
- Integration with the CustomerService implementation to trace all customer operations
- Configuration for OpenTelemetry with proper service name and sampling
- Jaeger UI for visualizing traces and spans

#### Jaeger UI

Jaeger is included in the Docker Compose configuration and provides a UI for visualizing traces:

- Access the Jaeger UI at: http://localhost:16686
- View traces by service, operation, tags, and more
- Analyze trace details, spans, and timing information
- Compare multiple traces side by side

The application is configured to send trace data to Jaeger using the OTLP protocol.

### Prometheus for Service Performance Monitoring

Prometheus is included in the Docker Compose configuration to provide time series database capabilities for service performance monitoring:

- Access the Prometheus UI at: http://localhost:9090
- Query metrics using PromQL (Prometheus Query Language)
- Create graphs and visualizations of service performance metrics
- Set up alerts based on metric thresholds
- Monitor application health, resource usage, and custom business metrics

The Spring Boot application exposes metrics in Prometheus format through the `/actuator/prometheus` endpoint, which Prometheus is configured to scrape every 15 seconds. This allows for real-time monitoring of service performance.

Key metrics available for monitoring include:
- JVM memory usage and garbage collection statistics
- HTTP request counts, durations, and error rates
- System CPU usage and load
- Custom business metrics like customer operations

## Configuration

The Actuator and OpenTelemetry configuration is defined in `application.yaml`:

```yaml
# Actuator Configuration
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,prometheus
      base-path: /actuator
  endpoint:
    health:
      show-details: always
      show-components: always
      probes:
        enabled: true
    prometheus:
      enabled: true
  prometheus:
    metrics:
      export:
        enabled: true
  tracing:
    sampling:
      probability: 1.0
  otlp:
    metrics:
      export:
        enabled: true
        step: 60s
    tracing:
      endpoint: ${OTLP_ENDPOINT:http://localhost:4317}
```

## Usage

### Viewing Health Information

```
GET /actuator/health
```

Example response:

```json
{
  "status": "UP",
  "components": {
    "customerService": {
      "status": "UP",
      "details": {
        "service": "CustomerService",
        "status": "Available",
        "customerCount": 10
      }
    },
    "db": {
      "status": "UP",
      "details": {
        "database": "PostgreSQL",
        "status": "Available"
      }
    },
    "diskSpace": {
      "status": "UP",
      "details": {
        "total": 1000000000,
        "free": 500000000,
        "threshold": 10000000
      }
    }
  }
}
```

### Viewing Metrics

```
GET /actuator/metrics
```

Example response:

```json
{
  "names": [
    "app.customer.created",
    "app.customer.updated",
    "app.customer.deleted",
    "app.customer.operation.time",
    "jvm.memory.used",
    "jvm.memory.max",
    "http.server.requests",
    "system.cpu.usage"
  ]
}
```

To view a specific metric:

```
GET /actuator/metrics/app.customer.created
```

### Prometheus Metrics

```
GET /actuator/prometheus
```

This endpoint exposes metrics in Prometheus format, which can be scraped by Prometheus for monitoring.

## Dependencies

The following dependencies are required for Actuator and OpenTelemetry:

```gradle
// Spring Boot Actuator
implementation 'org.springframework.boot:spring-boot-starter-actuator'

// Micrometer and Prometheus for metrics
implementation 'io.micrometer:micrometer-registry-prometheus'

// OpenTelemetry
implementation 'io.opentelemetry:opentelemetry-api'
implementation 'io.opentelemetry:opentelemetry-sdk'
implementation 'io.opentelemetry:opentelemetry-exporter-otlp'
implementation 'io.opentelemetry.instrumentation:opentelemetry-instrumentation-annotations:1.32.0'

// Spring AOP for tracing aspect
implementation 'org.springframework.boot:spring-boot-starter-aop'
```
