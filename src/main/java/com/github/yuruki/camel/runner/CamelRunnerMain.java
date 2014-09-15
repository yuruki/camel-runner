package com.github.yuruki.camel.runner;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.DefaultCamelContextNameStrategy;
import org.apache.camel.main.Main;
import org.apache.camel.util.ReflectionHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public class CamelRunnerMain extends Main {

    private Logger log = LoggerFactory.getLogger(this.getClass());
    private String propertyPrefix = "";
    private File propertiesFile;
    private Properties properties = new Properties();

    // Configurable fields
    private String camelContextId;

    public CamelRunnerMain() {
        addOption(new ParameterOption("p", "property", "Adds a property value to Camel properties component", "propertyValue") {
            @Override
            protected void doProcess(String arg, String parameter, LinkedList<String> remainingArgs) {
                String[] p = parameter.split("=", 2);
                log.info(String.format("Added property %s = %s", p[0], p[1]));
                properties.put(p[0], p[1]);
            }
        });
        addOption(new ParameterOption("pf", "propertiesFile", "Loads a properties file to Camel properties component", "propertyFile") {
            @Override
            protected void doProcess(String arg, String parameter, LinkedList<String> remainingArgs) {
                log.info("Setting properties file: " + parameter);
                propertiesFile = new File(parameter);
            }
        });
        addOption(new ParameterOption("pp", "propertyPrefix", "Sets a property prefix for Camel properties component", "propertyPrefix") {
            @Override
            protected void doProcess(String arg, String parameter, LinkedList<String> remainingArgs) {
                log.info("Setting property prefix: " + parameter);
                propertyPrefix = parameter;
            }
        });
    }

    public static void main(String... args) throws Exception {
        Main main = new CamelRunnerMain();
        main.enableHangupSupport();
        main.run(args);
    }

    @Override
    public void showOptionsHeader() {
        System.out.println("A standalone Camel runner for command line");
        System.out.println();
        System.out.println("java -jar camel-runner-<version>.jar [options]");
        System.out.println();
    }

    @Override
    protected CamelContext createContext() {
        CamelContext camelContext = new DefaultCamelContext();

        // Set up properties
        setupPropertiesComponent(camelContext);

        // Configure fields from properties
        configure(camelContext, this, log);

        // Set up context
        try {
            camelContext.setNameStrategy(new DefaultCamelContextNameStrategy(camelContextId));
            camelContext.setUseMDCLogging(true);
            camelContext.setUseBreadcrumb(true);
        } catch (Exception e) {
            log.warn("Couldn't set Camel context name", e);
        }

        // Set up default routes through properties component
        if (null == getRouteBuilderClasses()) {
            try {
                setRouteBuilderClasses(camelContext.resolvePropertyPlaceholders("{{defaultRouteBuilderClasses}}"));
            } catch (Exception e) {
                log.warn("Couldn't set default routes", e);
            }
        }
        return camelContext;
    }

    @Override
    protected void postProcessCamelContext(CamelContext camelContext) throws Exception {
        // try to load the route builders from the routeBuilderClasses
        loadRouteBuilders(camelContext);
        for (RouteBuilder routeBuilder : routeBuilders) {
            // Configure route fields from properties
            configure(camelContext, routeBuilder, log);
            camelContext.addRoutes(routeBuilder);
        }
    }

    private void setupPropertiesComponent(CamelContext camelContext) {
        PropertiesComponent pc = new PropertiesComponent();
        if (camelContext.getComponentNames().contains("properties")) {
            pc = camelContext.getComponent("properties", PropertiesComponent.class);
        } else {
            camelContext.addComponent("properties", pc);
        }

        if (!propertyPrefix.isEmpty()) {
            pc.setPropertyPrefix(propertyPrefix);
        }

        // Overlay properties (classpath -> file -> command line)
        List<String> locations = new ArrayList<>();
        locations.add("default.properties");
        if (null != propertiesFile && propertiesFile.exists()) {
            log.info("Adding properties file " + propertiesFile.getPath());
            locations.add("file:" + propertiesFile.getPath());
        } else if (null != propertiesFile) {
            log.warn("Properties file not found (" + propertiesFile.getPath() + ")");
        }
        pc.setLocations(locations.toArray(new String[locations.size()]));
        pc.setOverrideProperties(properties);
    }

    public static <T> void configure(CamelContext context, T target, Logger log) {
        Class clazz = target.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            try {
                String propertyValue = context.resolvePropertyPlaceholders("{{" + field.getName() + "}}");
                if (!propertyValue.isEmpty()) {
                    // Try to convert the value and set the field
                    Object convertedValue = convertValue(propertyValue, field.getGenericType());
                    ReflectionHelper.setField(field, target, convertedValue);
                    log.debug("Set field " + field.getName() + " with value " + propertyValue);
                }
            } catch (Exception e) {
                log.debug("Field " + field.getName() + " skipped", e);
            }
        }
    }

    public static Object convertValue(String value, Type type) throws Exception {
        Class<?> clazz = null;
        if (type instanceof ParameterizedType) {
            clazz = (Class<?>) ((ParameterizedType) type).getRawType();
        } else if (type instanceof Class) {
            clazz = (Class) type;
        }
        if (null != value) {
            if (clazz.isInstance(value)) {
                return value;
            } else if (clazz == String.class) {
                return value;
            } else if (clazz == Boolean.class || clazz == boolean.class) {
                return Boolean.parseBoolean(value);
            } else if (clazz == Integer.class || clazz == int.class) {
                return Integer.parseInt(value);
            } else if (clazz == Long.class || clazz == long.class) {
                return Long.parseLong(value);
            } else if (clazz == Double.class || clazz == double.class) {
                return Double.parseDouble(value);
            } else if (clazz == File.class) {
                return new File(value);
            } else if (clazz == URI.class) {
                return new URI(value);
            } else if (clazz == URL.class) {
                return new URL(value);
            } else {
                throw new IllegalArgumentException("Unknown type: "+ (clazz != null ? clazz.getName() : "null"));
            }
        } else {
            return null;
        }
    }
}
