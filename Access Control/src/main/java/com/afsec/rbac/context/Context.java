package com.afsec.rbac.context;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class Context {
    private final String correlationId;
    private final String clientIp;
    private final Instant timestamp;
    private final String environment;

    public Context(String clientIp, String environment) {
        this.correlationId = UUID.randomUUID().toString();
        this.clientIp = clientIp != null ? clientIp : "unknown";
        this.timestamp = Instant.now();
        this.environment = environment != null ? environment : "production";
    }

    public static Context empty() {
        return new Context("unknown", "production");
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public String getClientIp() {
        return clientIp;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public String getEnvironment() {
        return environment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Context context = (Context) o;
        return correlationId.equals(context.correlationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(correlationId);
    }

    @Override
    public String toString() {
        return String.format("Context[id=%s, ip=%s, env=%s, time=%s]", correlationId, clientIp, environment, timestamp);
    }
}