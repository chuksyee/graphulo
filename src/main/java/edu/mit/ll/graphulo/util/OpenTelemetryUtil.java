package edu.mit.ll.graphulo.util;

import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.SimpleSpanProcessor;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.api.GlobalOpenTelemetry;
public class OpenTelemetryUtil {

    public static OpenTelemetry openTelemetry() {
        return GlobalOpenTelemetry.get();
    }

    public static Tracer getTracer(String name) {
        Tracer tracer = openTelemetry().getTracer(name);
        return tracer;
    }
}
