package com.github.yuruki.camel.runner;

import org.apache.camel.impl.SimpleRegistry;

public class SelfReferencingRegistry extends SimpleRegistry {

    public static final String SELF;
    static {
        SELF = java.util.UUID.randomUUID().toString();
    }

    public SelfReferencingRegistry() {
        super();
        put(SELF, this);
    }
}
