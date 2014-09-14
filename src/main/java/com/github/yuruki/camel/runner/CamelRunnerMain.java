package com.github.yuruki.camel.runner;

import org.apache.camel.CamelContext;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.main.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public class CamelRunnerMain extends Main {

    private Logger log = LoggerFactory.getLogger(this.getClass());
    private String propertyPrefix = "";

    private File propertiesFile;
    private Properties properties = new Properties();

    public CamelRunnerMain() {
        addOption(new ParameterOption("p", "property", "Adds a property value to Camel properties component", "propertyValue") {
            @Override
            protected void doProcess(String arg, String parameter, LinkedList<String> remainingArgs) {
                String[] p = parameter.split("=");
                log.info(String.format("Added property %s = %s", p[0], p[1]));
                properties.put(p[0], p[1]);
            }
        });
        addOption(new ParameterOption("pf", "propertiesFile", "Loads a properties file to Camel properties component", "propertyFile") {
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

    @Override
    public void showOptionsHeader() {
        System.out.println("A standalone Camel runner for command line");
        System.out.println();
        System.out.println("java -jar camel-runner-<version>.jar [options]");
        System.out.println();
    }

    @Override
    protected CamelContext createContext() {
        CamelContext answer = super.createContext();

        // Set up properties
        answer = setupPropertiesComponent(answer);

        // Set up default routes through properties component
        if (null == getRouteBuilderClasses()) {
            try {
                setRouteBuilderClasses(answer.resolvePropertyPlaceholders("{{defaultRouteBuilderClasses}}"));
            } catch (Exception e) {
                log.warn("Couldn't set default routes", e);
            }
        }
        return answer;
    }

    private CamelContext setupPropertiesComponent(CamelContext camelContext) {
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

        return camelContext;
    }
}
