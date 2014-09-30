package com.github.yuruki.camel.runner.examples;

import com.github.yuruki.camel.runner.CamelRunnerMain;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.cache.CacheConstants;
import org.apache.camel.processor.cache.CacheBasedMessageBodyReplacer;
import org.apache.commons.lang.Validate;

import java.util.HashMap;
import java.util.Map;

public class CacheRouteBuilder extends RouteBuilder {

    // Configurable fields
    private String camelRouteId;
    private Integer maximumRedeliveries;
    private Long redeliveryDelay;
    private Double backOffMultiplier;
    private Long maximumRedeliveryDelay;
    private String restUsername;
    private String restPassword;
    private String cache;
    private String cacheKey;

    @Override
    public void configure() throws Exception {
        checkProperties();

        Map<String, String> users = new HashMap<>();
        users.put(restUsername, restPassword);

        // Add a bean to Camel context registry
        CamelRunnerMain.addToRegistry(getContext().getRegistry(), "users", users);

        errorHandler(defaultErrorHandler()
            .maximumRedeliveries(maximumRedeliveries)
            .redeliveryDelay(redeliveryDelay)
            .backOffMultiplier(backOffMultiplier)
            .maximumRedeliveryDelay(maximumRedeliveryDelay));

        from("{{source}}")
            .routeId(camelRouteId + ".cache.in")
            .setHeader(CacheConstants.CACHE_OPERATION, constant(CacheConstants.CACHE_OPERATION_ADD))
            .to(cache);

        from(cache)
            .routeId(camelRouteId + ".cache.out")
            .to("log:{{camelRouteId}}.cache.out");

        from("{{from}}")
            .routeId(camelRouteId)
            .onCompletion()
                .to("direct:processCompletion")
            .end()
            .setHeader(CacheConstants.CACHE_OPERATION, constant(CacheConstants.CACHE_OPERATION_GET))
            .to(cache)
            .to("{{to}}");

        from("direct:processCompletion")
            .routeId(camelRouteId + ".completion")
            .choice()
                .when(simple("${exception} == null"))
                    .log("{{messageOk}}")
                .otherwise()
                    .log(LoggingLevel.ERROR, "{{messageError}}")
            .endChoice();
    }

    public void checkProperties() {
        Validate.notNull(camelRouteId, "camelRouteId property is not set");
        Validate.notNull(maximumRedeliveries, "maximumRedeliveries property is not set");
        Validate.notNull(redeliveryDelay, "redeliveryDelay property is not set");
        Validate.notNull(backOffMultiplier, "backOffMultiplier property is not set");
        Validate.notNull(maximumRedeliveryDelay, "maximumRedeliveryDelay property is not set");
    }
}
