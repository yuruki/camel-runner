package com.github.yuruki.camel.runner;

import org.apache.activemq.pool.PooledConnectionFactory;
import org.apache.camel.Endpoint;
import org.apache.camel.component.jms.JmsComponent;

import java.util.Map;

public class PooledAmqComponent extends JmsComponent {

    private PooledConnectionFactory pooledConnectionFactory = null;

    public PooledAmqComponent() {
        super();
        setConcurrentConsumers(1);
        setTestConnectionOnStartup(true);
    }

    @Override
    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        try {
            String poolBrokerUrl = getCamelContext().resolvePropertyPlaceholders("{{poolBrokerUrl}}");
            setConnectionFactory(getPooledConnectionFactory(poolBrokerUrl));
        } catch (Exception e) {
            throw new IllegalArgumentException("Component amqpool requires poolBrokerUrl property to be set.", e);
        }
        return super.createEndpoint(uri, remaining, parameters);
    }

    private PooledConnectionFactory getPooledConnectionFactory(String brokerUrl) {
        if (null != pooledConnectionFactory) {
            return pooledConnectionFactory;
        } else {
            PooledConnectionFactory connectionFactory = new PooledConnectionFactory(brokerUrl);
            connectionFactory.setMaxConnections(8);
            connectionFactory.start();
            this.pooledConnectionFactory = connectionFactory;
            return pooledConnectionFactory;
        }
    }
}
