package com.github.yuruki.camel.runner;

import org.apache.camel.builder.RouteBuilder;

public class SimpleRouteBuilder extends RouteBuilder {

    public void configure() {

        from("{{from}}")
                .setBody(constant("foobar"))
                .to("{{to}}");
    }

}
