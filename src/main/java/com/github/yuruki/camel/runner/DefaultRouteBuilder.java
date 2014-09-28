package com.github.yuruki.camel.runner;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.commons.lang.Validate;

import java.util.Map;

public class DefaultRouteBuilder extends RouteBuilder {

    // Configurable fields
    private String camelRouteId;
    private Integer maximumRedeliveries;
    private Long redeliveryDelay;
    private Double backOffMultiplier;
    private Long maximumRedeliveryDelay;

    @Override
    public void configure() throws Exception {
        checkProperties();

        // Get a handle on Camel context registry in case you want to add beans here
        Map<String, Object> registry = (Map) getContext().getRegistry().lookupByName(SelfReferencingRegistry.SELF);

        errorHandler(defaultErrorHandler()
                .maximumRedeliveries(maximumRedeliveries)
                .redeliveryDelay(redeliveryDelay)
                .backOffMultiplier(backOffMultiplier)
                .maximumRedeliveryDelay(maximumRedeliveryDelay));

        from("{{from}}")
            .routeId(camelRouteId)
            .onCompletion()
                .to("direct:processCompletion")
            .end()
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
        Validate.notNull(maximumRedeliveries, "maximumRedeliveries property is not set");
        Validate.notNull(redeliveryDelay, "redeliveryDelay property is not set");
        Validate.notNull(backOffMultiplier, "backOffMultiplier property is not set");
        Validate.notNull(maximumRedeliveryDelay, "maximumRedeliveryDelay property is not set");
        Validate.notNull(camelRouteId, "camelRouteId property is not set");
    }
}
