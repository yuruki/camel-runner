package com.github.yuruki.camel.runner;

import org.apache.camel.builder.RouteBuilder;

public class SimpleRouteBuilder extends RouteBuilder {

    public void configure() {

        from("timer:foo?period=5000")
                .setBody(constant("kukkuu"))
                .to("log:bar");
    }

}
