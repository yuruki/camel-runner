camel-runner
============

A standalone Camel runner for command line

Example 1, show usage:

    # java -jar camel-runner-1.1-SNAPSHOT.jar -h
    A standalone Camel runner for command line
    
    java -jar camel-runner-<version>.jar [options]
    
      -h or -help = Displays the help screen
      -r or -routers <routerBuilderClasses> = Sets the router builder classes which will be loaded while starting the camel context
      -o or -outdir <dot> = Sets the DOT output directory where the visual representations of the routes are generated
      -ad or -aggregate-dot <aggregate-dot> = Aggregates all routes (in addition to individual route generation) into one context to create one monolithic DOT file for visual representations the entire system.
      -d or -duration <duration> = Sets the time duration that the application will run for, by default in milliseconds. You can use '10s' for 10 seconds etc
      -t or -trace = Enables tracing
      -out or -output <filename> = Output all routes to the specified XML file
      -p or -property <propertyValue> = Adds a property value to Camel properties component
      -pf or -propertiesFile <propertyFile> = Loads a properties file to Camel properties component
      -pp or -propertyPrefix <propertyPrefix> = Sets a property prefix for Camel properties component

Example 2, listen to ActiveMQ topic and log the messages:

    # java -jar target\camel-runner-1.1-SNAPSHOT.jar -p "from=amqpool:topic:camel-runner.test.in?username=admin&password=xxxxxx" -p poolBrokerUrl=tcp://localhost:61616
    [                          main] CamelRunnerMain                INFO  Added property from = amqpool:topic:camel-runner.test.in?username=admin&password=xxxxxx
    [                          main] CamelRunnerMain                INFO  Added property poolBrokerUrl = tcp://localhost:61616
    [                          main] MainSupport                    INFO  Apache Camel 2.12.0.redhat-610379 starting
    [                          main] DefaultCamelContext            INFO  Apache Camel 2.12.0.redhat-610379 (CamelContext: camel-runner-2) is starting
    [                          main] DefaultCamelContext            INFO  MDC logging is enabled on CamelContext: camel-runner-2
    [                          main] ManagedManagementStrategy      INFO  JMX is enabled
    [                          main] DefaultTypeConverter           INFO  Loaded 188 type converters
    [                          main] DefaultCamelContext            INFO  AllowUseOriginalMessage is enabled. If access to the original message is not needed, then its recommended to turn this option off as it may improve performance.
    [                          main] DefaultCamelContext            INFO  StreamCaching is not in use. If using streams then its recommended to enable stream caching. See more details at http://camel.apache.org/stream-caching.html
    [                          main] DefaultCamelContext            INFO  Route: simple-route started and consuming from: Endpoint[amqpool://topic:camel-runner.test.in?password=xxxxxx&username=admin]
    [                          main] DefaultCamelContext            INFO  Route: simple-route.completion started and consuming from: Endpoint[direct://processCompletion]
    [                          main] DefaultCamelContext            INFO  Total 2 routes, of which 2 is started.
    [                          main] DefaultCamelContext            INFO  Apache Camel 2.12.0.redhat-610379 (CamelContext: camel-runner-2) started in 0.654 seconds
    [Consumer[camel-runner.test.in]] bar                            INFO  Exchange[ExchangePattern: InOnly, BodyType: String, Body: Test]
    [er-2) thread #2 - OnCompletion] completion                     INFO  Success: amqpool:topic:camel-runner.test.in?username=admin&password=xxxxxx -> log:bar
    [                      Thread-0] MainSupport$HangupInterceptor  INFO  Received hang up - stopping the main instance.
    [                      Thread-0] MainSupport                    INFO  Apache Camel 2.12.0.redhat-610379 stopping
    [                      Thread-0] DefaultCamelContext            INFO  Apache Camel 2.12.0.redhat-610379 (CamelContext: camel-runner-2) is shutting down
    [                      Thread-0] DefaultShutdownStrategy        INFO  Starting to graceful shutdown 2 routes (timeout 300 seconds)
    [er-2) thread #3 - ShutdownTask] DefaultShutdownStrategy        INFO  Route: simple-route.completion shutdown complete, was consuming from: Endpoint[direct://processCompletion]
    [er-2) thread #3 - ShutdownTask] DefaultShutdownStrategy        INFO  Route: simple-route shutdown complete, was consuming from: Endpoint[amqpool://topic:camel-runner.test.in?password=xxxxxx&username=admin]
    [                      Thread-0] DefaultShutdownStrategy        INFO  Graceful shutdown of 2 routes completed in 0 seconds
    [                      Thread-0] DefaultCamelContext            INFO  Apache Camel 2.12.0.redhat-610379 (CamelContext: camel-runner-2) uptime 13.582 seconds
    [                      Thread-0] DefaultCamelContext            INFO  Apache Camel 2.12.0.redhat-610379 (CamelContext: camel-runner-2) is shutdown in 0.388 seconds