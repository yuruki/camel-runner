package com.github.yuruki.camel.runner;

import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.apache.camel.Endpoint;

import java.util.Map;

@SuppressWarnings("unused")
public class PooledAmqComponent extends ActiveMQComponent {

    private PooledConnectionFactory pooledConnectionFactory = null;

    public PooledAmqComponent() {
        super();
        setConcurrentConsumers(1);
        setTestConnectionOnStartup(true);
    }

    @Override
    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        try {
            String amqBrokerUrl = getCamelContext().resolvePropertyPlaceholders("{{amqBrokerUrl}}");
            setConnectionFactory(getPooledConnectionFactory(amqBrokerUrl));
        } catch (Exception e) {
            throw new IllegalArgumentException("Component amq requires amqBrokerUrl property to be set.", e);
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
