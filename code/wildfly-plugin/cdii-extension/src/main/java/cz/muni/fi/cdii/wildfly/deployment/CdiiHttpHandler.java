package cz.muni.fi.cdii.wildfly.deployment;

import javax.enterprise.inject.spi.BeanManager;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;

/**
 * It handles http request of request path matching "<context_root>/cdii/?", other requests are 
 * passed to next handler.
 * Response to cdii requests is json encoded data model.
 */
public class CdiiHttpHandler implements HttpHandler {
    
    private final BeanManager beanManager;
    private final HttpHandler nextHandler;
    
    public CdiiHttpHandler(BeanManager beanManager, HttpHandler nextHandler) {
        this.beanManager = beanManager;
        this.nextHandler = nextHandler;
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        if (isCdiiStateRequest(exchange)) {
            this.handleCdiiStateRequest(exchange);
        } else if (isCdiiDataRequest(exchange)) {
            this.handleCdiiDataRequest(exchange);
        } else {
            nextHandler.handleRequest(exchange);
        }

    }

    private void handleCdiiStateRequest(final HttpServerExchange exchange) {
        exchange.getResponseHeaders().add(new HttpString("Content-Type"), 
                "application/json");
        exchange.getResponseSender().send("{\"status\": \"enabled\"}");
    }

    private void handleCdiiDataRequest(final HttpServerExchange exchange) {
        exchange.getResponseHeaders().add(new HttpString("Content-Type"), 
                "application/json");
        exchange.getResponseSender().send("cdii demo: " + 
                (beanManager == null ? "null" : beanManager));
    }
    
    private static boolean isCdiiStateRequest(final HttpServerExchange exchange) {
        return applicationPathsEqual(exchange) 
                && getPathSuffix(exchange).matches("/cdii/status/?"); 
    }

    private static boolean isCdiiDataRequest(final HttpServerExchange exchange) {
        return applicationPathsEqual(exchange)
                   && getPathSuffix(exchange).matches("/cdii/?");
    }
    
    private static String getPathSuffix(HttpServerExchange exchange) {
        final String resolvedPath = exchange.getResolvedPath();
        final String requestPath = exchange.getRequestPath();
        return requestPath.substring(resolvedPath.length(), requestPath.length());
    }

    private static boolean applicationPathsEqual(final HttpServerExchange exchange) {
        final String resolvedPath = exchange.getResolvedPath();
        final String requestPath = exchange.getRequestPath();
        return requestPath.substring(0, resolvedPath.length()).equals(resolvedPath);
    }

}
