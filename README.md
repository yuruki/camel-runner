camel-runner
============

A standalone Camel runner for command line.

The runner includes a default route which implements a simple from -> to integration pattern with success/failure logging and redelivery.
Everything is configured via properties.

Properties can be loaded from a file and with command line parameters.

Using gradle:

    $ gradle tasks
    Shows all available tasks.

    $ gradle run
    Builds and runs the application with defaults.
    
    $ gradle installApp
    Builds and installs the application in build/install/camel-runner
    
    $ gradle distZip
    Creates a distribution zip in build/distribution/camel-runner.

Example 1, show usage:

    $ build/install/camel-runner/bin/camel-runner -h
    A standalone Camel runner for command line
    
    bin/camel-runner[.bat] [options], or java -cp "lib/*" com.github.yuruki.camel.runner.CamelRunnerMain [options]
    
      -h or -help = Displays the help screen
      -r or -routers <routerBuilderClasses> = Sets the router builder classes which will be loaded while starting the camel context
      -o or -outdir <dot> = Sets the DOT output directory where the visual representations of the routes are generated
      -ad or -aggregate-dot <aggregate-dot> = Aggregates all routes (in addition to individual route generation) into one context to create one monolithic DOT file for visual representations the entire system.
      -d or -duration <duration> = Sets the time duration that the application will run for, by default in milliseconds. You can use '10s' for 10 seconds etc
      -t or -trace = Enables tracing
      -out or -output <filename> = Output all routes to the specified XML file
      -p or -property <propertyValue> = Adds a property value to Camel properties component
      -pf or -propertiesFile <propertiesFile> = Loads a properties file to Camel properties component
      -pp or -propertyPrefix <propertyPrefix> = Sets a property prefix for Camel properties component

Example 2, listen to an ActiveMQ topic and log the messages:

    $ build/install/camel-runner/bin/camel-runner -p "from=amq:topic:camel-runner.test.in?username=admin&password=admin" -p brokerUrl=tcp://localhost:61616
    2014-09-28 17:52:04,880 | .apache.camel.main.MainSupport |  INFO | Apache Camel 2.12.0.redhat-610379 starting
    2014-09-28 17:52:05,223 | camel.impl.DefaultCamelContext |  INFO | Apache Camel 2.12.0.redhat-610379 (CamelContext: camel-runner-2) is starting
    2014-09-28 17:52:05,226 | camel.impl.DefaultCamelContext |  INFO | MDC logging is enabled on CamelContext: camel-runner-2
    2014-09-28 17:52:05,228 | ment.ManagedManagementStrategy |  INFO | JMX is enabled
    2014-09-28 17:52:05,572 | converter.DefaultTypeConverter |  WARN | Overriding type converter from: StaticMethodTypeConverter: public static java.io.InputStream org.apache.camel.component.http.HttpConverter.toInputStream(javax.servlet.http.HttpServletRequest,org.apache.camel.Exchange) throws java.io.IOException to: StaticMethodTypeConverter: public static java.io.InputStream org.apache.camel.component.http4.HttpConverter.toInputStream(javax.servlet.http.HttpServletRequest,org.apache.camel.Exchange) throws java.io.IOException
    2014-09-28 17:52:05,577 | converter.DefaultTypeConverter |  WARN | Overriding type converter from: StaticMethodTypeConverter: public static javax.servlet.http.HttpServletRequest org.apache.camel.component.http.HttpConverter.toServletRequest(org.apache.camel.Message) to: StaticMethodTypeConverter: public static javax.servlet.http.HttpServletRequest org.apache.camel.component.http4.HttpConverter.toServletRequest(org.apache.camel.Message)
    2014-09-28 17:52:05,582 | converter.DefaultTypeConverter |  WARN | Overriding type converter from: StaticMethodTypeConverter: public static javax.servlet.http.HttpServletResponse org.apache.camel.component.http.HttpConverter.toServletResponse(org.apache.camel.Message) to: StaticMethodTypeConverter: public static javax.servlet.http.HttpServletResponse org.apache.camel.component.http4.HttpConverter.toServletResponse(org.apache.camel.Message)
    2014-09-28 17:52:05,618 | converter.DefaultTypeConverter |  INFO | Loaded 209 type converters
    2014-09-28 17:52:06,139 | camel.impl.DefaultCamelContext |  INFO | AllowUseOriginalMessage is enabled. If access to the original message is not needed, then its recommended to turn this option off as it may improve performance.
    2014-09-28 17:52:06,139 | camel.impl.DefaultCamelContext |  INFO | StreamCaching is not in use. If using streams then its recommended to enable stream caching. See more details at http://camel.apache.org/stream-caching.html
    2014-09-28 17:52:06,330 | camel.impl.DefaultCamelContext |  INFO | Route: default-route started and consuming from: Endpoint[amq://topic:camel-runner.test.in?password=xxxxxx&username=admin]
    2014-09-28 17:52:06,350 | camel.impl.DefaultCamelContext |  INFO | Route: default-route.completion started and consuming from: Endpoint[direct://processCompletion]
    2014-09-28 17:52:06,367 | camel.impl.DefaultCamelContext |  INFO | Total 2 routes, of which 2 is started.
    2014-09-28 17:52:06,383 | camel.impl.DefaultCamelContext |  INFO | Apache Camel 2.12.0.redhat-610379 (CamelContext: camel-runner-2) started in 1.144 seconds
    2014-09-28 17:52:13,962 |                  default-route |  INFO | Exchange[ExchangePattern: InOnly, Headers: {breadcrumbId=ID:musta-45532-1411915510530-7:1:1:1:1, JMSCorrelationID=null, JMSDeliveryMode=2, JMSDestination=topic://camel-runner.test.in, JMSExpiration=0, JMSMessageID=ID:musta-45532-1411915510530-7:1:1:1:1, JMSPriority=0, JMSRedelivered=false, JMSReplyTo=null, JMSTimestamp=1411915933892, JMSType=null, JMSXGroupID=null, JMSXUserID=null}, BodyType: String, Body: ]
    2014-09-28 17:52:13,995 |       default-route.completion |  INFO | Success: amq:topic:camel-runner.test.in?username=admin&password=admin -> log:default-route?showHeaders=true
    2014-09-28 17:52:24,134 | .MainSupport$HangupInterceptor |  INFO | Received hang up - stopping the main instance.
    2014-09-28 17:52:24,136 | .apache.camel.main.MainSupport |  INFO | Apache Camel 2.12.0.redhat-610379 stopping
    2014-09-28 17:52:24,136 | camel.impl.DefaultCamelContext |  INFO | Apache Camel 2.12.0.redhat-610379 (CamelContext: camel-runner-2) is shutting down
    2014-09-28 17:52:24,139 | l.impl.DefaultShutdownStrategy |  INFO | Starting to graceful shutdown 2 routes (timeout 300 seconds)
    2014-09-28 17:52:24,143 | l.impl.DefaultShutdownStrategy |  INFO | Route: default-route.completion shutdown complete, was consuming from: Endpoint[direct://processCompletion]
    2014-09-28 17:52:24,991 | l.impl.DefaultShutdownStrategy |  INFO | Route: default-route shutdown complete, was consuming from: Endpoint[amq://topic:camel-runner.test.in?password=xxxxxx&username=admin]
    2014-09-28 17:52:24,992 | l.impl.DefaultShutdownStrategy |  INFO | Graceful shutdown of 2 routes completed in 0 seconds
    2014-09-28 17:52:25,004 | camel.impl.DefaultCamelContext |  INFO | Apache Camel 2.12.0.redhat-610379 (CamelContext: camel-runner-2) uptime 19.782 seconds
    2014-09-28 17:52:25,005 | camel.impl.DefaultCamelContext |  INFO | Apache Camel 2.12.0.redhat-610379 (CamelContext: camel-runner-2) is shutdown in 0.868 seconds

Example 3, set up a REST service at http://localhost:8080/test (also the default) with basic authentication and log the messages:

    $ build/install/camel-runner/bin/camel-runner -pf examples/restletRoute.properties -p restUrl=http://localhost:8080/test
    2014-09-30 18:44:08,028 | .apache.camel.main.MainSupport |  INFO | Apache Camel 2.12.0.redhat-610379 starting
    2014-09-30 18:44:08,297 | i.camel.runner.CamelRunnerMain |  INFO | Adding properties file examples/restletRoute.properties from classpath
    2014-09-30 18:44:08,360 | camel.impl.DefaultCamelContext |  INFO | Apache Camel 2.12.0.redhat-610379 (CamelContext: camel-runner-2) is starting
    2014-09-30 18:44:08,363 | camel.impl.DefaultCamelContext |  INFO | MDC logging is enabled on CamelContext: camel-runner-2
    2014-09-30 18:44:08,364 | ment.ManagedManagementStrategy |  INFO | JMX is enabled
    2014-09-30 18:44:08,732 | converter.DefaultTypeConverter |  WARN | Overriding type converter from: StaticMethodTypeConverter: public static java.io.InputStream org.apache.camel.component.http4.HttpConverter.toInputStream(javax.servlet.http.HttpServletRequest,org.apache.camel.Exchange) throws java.io.IOException to: StaticMethodTypeConverter: public static java.io.InputStream org.apache.camel.component.http.HttpConverter.toInputStream(javax.servlet.http.HttpServletRequest,org.apache.camel.Exchange) throws java.io.IOException
    2014-09-30 18:44:08,737 | converter.DefaultTypeConverter |  WARN | Overriding type converter from: StaticMethodTypeConverter: public static javax.servlet.http.HttpServletRequest org.apache.camel.component.http4.HttpConverter.toServletRequest(org.apache.camel.Message) to: StaticMethodTypeConverter: public static javax.servlet.http.HttpServletRequest org.apache.camel.component.http.HttpConverter.toServletRequest(org.apache.camel.Message)
    2014-09-30 18:44:08,742 | converter.DefaultTypeConverter |  WARN | Overriding type converter from: StaticMethodTypeConverter: public static javax.servlet.http.HttpServletResponse org.apache.camel.component.http4.HttpConverter.toServletResponse(org.apache.camel.Message) to: StaticMethodTypeConverter: public static javax.servlet.http.HttpServletResponse org.apache.camel.component.http.HttpConverter.toServletResponse(org.apache.camel.Message)
    2014-09-30 18:44:08,759 | converter.DefaultTypeConverter |  INFO | Loaded 212 type converters
    2014-09-30 18:44:09,077 | camel.impl.DefaultCamelContext |  INFO | AllowUseOriginalMessage is enabled. If access to the original message is not needed, then its recommended to turn this option off as it may improve performance.
    2014-09-30 18:44:09,078 | camel.impl.DefaultCamelContext |  INFO | StreamCaching is not in use. If using streams then its recommended to enable stream caching. See more details at http://camel.apache.org/stream-caching.html
    Starting the internal [HTTP/1.1] server on port 8080
    2014-09-30 18:44:09,270 | camel.impl.DefaultCamelContext |  INFO | Route: restlet-route started and consuming from: Endpoint[http://localhost:8080/test?restletMethods=POST]
    2014-09-30 18:44:09,276 | camel.impl.DefaultCamelContext |  INFO | Route: restlet-route.completion started and consuming from: Endpoint[direct://processCompletion]
    2014-09-30 18:44:09,291 | camel.impl.DefaultCamelContext |  INFO | Total 2 routes, of which 2 is started.
    2014-09-30 18:44:09,307 | camel.impl.DefaultCamelContext |  INFO | Apache Camel 2.12.0.redhat-610379 (CamelContext: camel-runner-2) started in 0.931 seconds
    2014-09-30 18:44:14,164 |                  restlet-route |  INFO | Exchange[ExchangePattern: InOut, Headers: {breadcrumbId=ID-musta-55215-1412091848128-0-1, CamelHttpMethod=POST, CamelHttpUri=http://localhost:8080/test, CamelRestletRequest=POST http://localhost:8080/test HTTP/1.1, CamelRestletResponse=HTTP/1.1 - OK (200) - The request has succeeded, org.restlet.http.headers=[[Authorization: Basic dGVzdDp0ZXN0], [User-Agent: curl/7.38.0], [Host: localhost:8080], [Accept: */*], [Content-Length: 9], [Content-Type: application/x-www-form-urlencoded]], org.restlet.startTime=1412091854130}, BodyType: String, Body: something]
    2014-09-30      18:44:14        0:0:0:0:0:0:0:1 test    -       8080    POST    /test   -       200     9       9       38      http://localhost:8080   curl/7.38.0     -
    2014-09-30 18:44:14,180 |       restlet-route.completion |  INFO | Success: restlet:http://localhost:8080/test?restletMethods=post&restletRealm=#users -> log:restlet-route?showHeaders=true
    2014-09-30 18:44:18,422 | .MainSupport$HangupInterceptor |  INFO | Received hang up - stopping the main instance.
    2014-09-30 18:44:18,425 | .apache.camel.main.MainSupport |  INFO | Apache Camel 2.12.0.redhat-610379 stopping
    2014-09-30 18:44:18,426 | camel.impl.DefaultCamelContext |  INFO | Apache Camel 2.12.0.redhat-610379 (CamelContext: camel-runner-2) is shutting down
    2014-09-30 18:44:18,428 | l.impl.DefaultShutdownStrategy |  INFO | Starting to graceful shutdown 2 routes (timeout 300 seconds)
    2014-09-30 18:44:18,434 | l.impl.DefaultShutdownStrategy |  INFO | Route: restlet-route.completion shutdown complete, was consuming from: Endpoint[direct://processCompletion]
    2014-09-30 18:44:18,436 | l.impl.DefaultShutdownStrategy |  INFO | Route: restlet-route shutdown complete, was consuming from: Endpoint[http://localhost:8080/test?restletMethods=POST]
    2014-09-30 18:44:18,436 | l.impl.DefaultShutdownStrategy |  INFO | Graceful shutdown of 2 routes completed in 0 seconds
    2014-09-30 18:44:18,450 | camel.impl.DefaultCamelContext |  INFO | Apache Camel 2.12.0.redhat-610379 (CamelContext: camel-runner-2) uptime 10.090 seconds
    2014-09-30 18:44:18,450 | camel.impl.DefaultCamelContext |  INFO | Apache Camel 2.12.0.redhat-610379 (CamelContext: camel-runner-2) is shutdown in 0.024 seconds

Example 4, set up a REST service that accepts data through POST and publishes it through GET:

    $ build/install/camel-runner/bin/camel-runner -pf examples/cacheRoute.properties
    2014-09-30 19:21:45,962 | .apache.camel.main.MainSupport |  INFO | Apache Camel 2.12.0.redhat-610379 starting
    2014-09-30 19:21:46,224 | i.camel.runner.CamelRunnerMain |  INFO | Adding properties file examples/cacheRoute.properties from classpath
    2014-09-30 19:21:46,293 | camel.impl.DefaultCamelContext |  INFO | Apache Camel 2.12.0.redhat-610379 (CamelContext: camel-runner-2) is starting
    2014-09-30 19:21:46,296 | camel.impl.DefaultCamelContext |  INFO | MDC logging is enabled on CamelContext: camel-runner-2
    2014-09-30 19:21:46,297 | ment.ManagedManagementStrategy |  INFO | JMX is enabled
    2014-09-30 19:21:46,676 | converter.DefaultTypeConverter |  WARN | Overriding type converter from: StaticMethodTypeConverter: public static java.io.InputStream org.apache.camel.component.http4.HttpConverter.toInputStream(javax.servlet.http.HttpServletRequest,org.apache.camel.Exchange) throws java.io.IOException to: StaticMethodTypeConverter: public static java.io.InputStream org.apache.camel.component.http.HttpConverter.toInputStream(javax.servlet.http.HttpServletRequest,org.apache.camel.Exchange) throws java.io.IOException
    2014-09-30 19:21:46,681 | converter.DefaultTypeConverter |  WARN | Overriding type converter from: StaticMethodTypeConverter: public static javax.servlet.http.HttpServletRequest org.apache.camel.component.http4.HttpConverter.toServletRequest(org.apache.camel.Message) to: StaticMethodTypeConverter: public static javax.servlet.http.HttpServletRequest org.apache.camel.component.http.HttpConverter.toServletRequest(org.apache.camel.Message)
    2014-09-30 19:21:46,686 | converter.DefaultTypeConverter |  WARN | Overriding type converter from: StaticMethodTypeConverter: public static javax.servlet.http.HttpServletResponse org.apache.camel.component.http4.HttpConverter.toServletResponse(org.apache.camel.Message) to: StaticMethodTypeConverter: public static javax.servlet.http.HttpServletResponse org.apache.camel.component.http.HttpConverter.toServletResponse(org.apache.camel.Message)
    2014-09-30 19:21:46,715 | converter.DefaultTypeConverter |  INFO | Loaded 213 type converters
    2014-09-30 19:21:47,062 | camel.impl.DefaultCamelContext |  INFO | AllowUseOriginalMessage is enabled. If access to the original message is not needed, then its recommended to turn this option off as it may improve performance.
    2014-09-30 19:21:47,067 | camel.impl.DefaultCamelContext |  INFO | StreamCaching is not in use. If using streams then its recommended to enable stream caching. See more details at http://camel.apache.org/stream-caching.html
    2014-09-30 19:21:47,084 | che.DefaultCacheManagerFactory |  INFO | Creating CacheManager using Ehcache defaults
    2014-09-30 19:21:47,115 | he.config.ConfigurationFactory |  WARN | No configuration found. Configuring ehcache from ehcache-failsafe.xml  found in the classpath: jar:file:/home/yuruki/Git/camel-runner/build/install/camel-runner/lib/ehcache-2.7.4.jar!/ehcache-failsafe.xml
    2014-09-30 19:21:47,395 | ache.config.CacheConfiguration |  WARN | Cache 'camel-runner' is set to eternal but also has TTI/TTL set.  To avoid this warning, clean up the config removing conflicting values of eternal, TTI and TTL. Effective configuration for Cache 'camel-runner' will be eternal='true', timeToIdleSeconds='0', timeToLiveSeconds='0'.
    Starting the internal [HTTP/1.1] server on port 8080
    2014-09-30 19:21:47,744 | camel.impl.DefaultCamelContext |  INFO | Route: cache-route.cache.in started and consuming from: Endpoint[http://localhost:8080/data?restletMethods=POST]
    2014-09-30 19:21:47,749 | camel.impl.DefaultCamelContext |  INFO | Route: cache-route.cache.out started and consuming from: Endpoint[cache://camel-runner?eternal=true&key=data]
    2014-09-30 19:21:47,753 | camel.impl.DefaultCamelContext |  INFO | Route: cache-route started and consuming from: Endpoint[http://localhost:8080/data?restletMethods=GET]
    2014-09-30 19:21:47,755 | camel.impl.DefaultCamelContext |  INFO | Route: cache-route.completion started and consuming from: Endpoint[direct://processCompletion]
    2014-09-30 19:21:47,764 | camel.impl.DefaultCamelContext |  INFO | Total 4 routes, of which 4 is started.
    2014-09-30 19:21:47,773 | camel.impl.DefaultCamelContext |  INFO | Apache Camel 2.12.0.redhat-610379 (CamelContext: camel-runner-2) started in 1.472 seconds
    2014-09-30 19:22:00,721 |          cache-route.cache.out |  INFO | Exchange[ExchangePattern: InOnly, BodyType: String, Body: something]
    2014-09-30      19:22:00        0:0:0:0:0:0:0:1 test    -       8080    POST    /data   -       200     9       9       38      http://localhost:8080   curl/7.38.0     -
    2014-09-30 19:22:19,183 |                    cache-route |  INFO | Exchange[ExchangePattern: InOut, Headers: {breadcrumbId=ID-musta-49719-1412094106055-0-5, CamelCacheElementWasFound=true, CamelHttpMethod=GET, CamelHttpUri=http://localhost:8080/data, CamelRestletRequest=GET http://localhost:8080/data HTTP/1.1, CamelRestletResponse=HTTP/1.1 - OK (200) - The request has succeeded, org.restlet.http.headers=[[Authorization: Basic dGVzdDp0ZXN0], [User-Agent: curl/7.38.0], [Host: localhost:8080], [Accept: */*]], org.restlet.startTime=1412094139178}, BodyType: String, Body: something]
    2014-09-30      19:22:19        0:0:0:0:0:0:0:1 test    -       8080    GET     /data   -       200     9       0       8       http://localhost:8080   curl/7.38.0     -
    2014-09-30 19:22:19,202 |         cache-route.completion |  INFO | Success: restlet:http://localhost:8080/data?restletMethods=get&restletRealm=#users -> log:cache-route?showHeaders=true
    2014-09-30 19:22:27,873 | .MainSupport$HangupInterceptor |  INFO | Received hang up - stopping the main instance.
    2014-09-30 19:22:27,874 | .apache.camel.main.MainSupport |  INFO | Apache Camel 2.12.0.redhat-610379 stopping
    2014-09-30 19:22:27,874 | camel.impl.DefaultCamelContext |  INFO | Apache Camel 2.12.0.redhat-610379 (CamelContext: camel-runner-2) is shutting down
    2014-09-30 19:22:27,876 | l.impl.DefaultShutdownStrategy |  INFO | Starting to graceful shutdown 4 routes (timeout 300 seconds)
    2014-09-30 19:22:27,882 | l.impl.DefaultShutdownStrategy |  INFO | Route: cache-route.completion shutdown complete, was consuming from: Endpoint[direct://processCompletion]
    2014-09-30 19:22:27,883 | l.impl.DefaultShutdownStrategy |  INFO | Route: cache-route shutdown complete, was consuming from: Endpoint[http://localhost:8080/data?restletMethods=GET]
    2014-09-30 19:22:27,884 | l.impl.DefaultShutdownStrategy |  INFO | Route: cache-route.cache.out shutdown complete, was consuming from: Endpoint[cache://camel-runner?eternal=true&key=data]
    2014-09-30 19:22:27,885 | l.impl.DefaultShutdownStrategy |  INFO | Route: cache-route.cache.in shutdown complete, was consuming from: Endpoint[http://localhost:8080/data?restletMethods=POST]
    2014-09-30 19:22:27,885 | l.impl.DefaultShutdownStrategy |  INFO | Graceful shutdown of 4 routes completed in 0 seconds
    2014-09-30 19:22:27,926 | camel.impl.DefaultCamelContext |  INFO | Apache Camel 2.12.0.redhat-610379 (CamelContext: camel-runner-2) uptime 41.634 seconds
    2014-09-30 19:22:27,926 | camel.impl.DefaultCamelContext |  INFO | Apache Camel 2.12.0.redhat-610379 (CamelContext: camel-runner-2) is shutdown in 0.052 seconds
    
    From client's perspective:
    
    $ curl -u test:test -d "something" http://localhost:8080/data
    something
    
    $ curl -u test:test http://localhost:8080/data
    something
    $ curl -u test:test http://localhost:8080/data
    something
    
Example 5, listen to an ActiveMQ topic and publish the data via REST service:

    $ build/install/camel-runner/bin/camel-runner -pf examples/cacheRoute.properties -p "source=amq:topic:data?username=admin&password=admin" -p brokerUrl=tcp://localhost:61616
    2014-09-30 19:31:57,735 | .apache.camel.main.MainSupport |  INFO | Apache Camel 2.12.0.redhat-610379 starting
    2014-09-30 19:31:57,999 | i.camel.runner.CamelRunnerMain |  INFO | Adding properties file examples/cacheRoute.properties from classpath
    2014-09-30 19:31:58,068 | camel.impl.DefaultCamelContext |  INFO | Apache Camel 2.12.0.redhat-610379 (CamelContext: camel-runner-2) is starting
    2014-09-30 19:31:58,070 | camel.impl.DefaultCamelContext |  INFO | MDC logging is enabled on CamelContext: camel-runner-2
    2014-09-30 19:31:58,072 | ment.ManagedManagementStrategy |  INFO | JMX is enabled
    2014-09-30 19:31:58,438 | converter.DefaultTypeConverter |  WARN | Overriding type converter from: StaticMethodTypeConverter: public static java.io.InputStream org.apache.camel.component.http.HttpConverter.toInputStream(javax.servlet.http.HttpServletRequest,org.apache.camel.Exchange) throws java.io.IOException to: StaticMethodTypeConverter: public static java.io.InputStream org.apache.camel.component.http4.HttpConverter.toInputStream(javax.servlet.http.HttpServletRequest,org.apache.camel.Exchange) throws java.io.IOException
    2014-09-30 19:31:58,443 | converter.DefaultTypeConverter |  WARN | Overriding type converter from: StaticMethodTypeConverter: public static javax.servlet.http.HttpServletRequest org.apache.camel.component.http.HttpConverter.toServletRequest(org.apache.camel.Message) to: StaticMethodTypeConverter: public static javax.servlet.http.HttpServletRequest org.apache.camel.component.http4.HttpConverter.toServletRequest(org.apache.camel.Message)
    2014-09-30 19:31:58,448 | converter.DefaultTypeConverter |  WARN | Overriding type converter from: StaticMethodTypeConverter: public static javax.servlet.http.HttpServletResponse org.apache.camel.component.http.HttpConverter.toServletResponse(org.apache.camel.Message) to: StaticMethodTypeConverter: public static javax.servlet.http.HttpServletResponse org.apache.camel.component.http4.HttpConverter.toServletResponse(org.apache.camel.Message)
    2014-09-30 19:31:58,488 | converter.DefaultTypeConverter |  INFO | Loaded 213 type converters
    2014-09-30 19:31:59,276 | camel.impl.DefaultCamelContext |  INFO | AllowUseOriginalMessage is enabled. If access to the original message is not needed, then its recommended to turn this option off as it may improve performance.
    2014-09-30 19:31:59,277 | camel.impl.DefaultCamelContext |  INFO | StreamCaching is not in use. If using streams then its recommended to enable stream caching. See more details at http://camel.apache.org/stream-caching.html
    2014-09-30 19:31:59,346 | che.DefaultCacheManagerFactory |  INFO | Creating CacheManager using Ehcache defaults
    2014-09-30 19:31:59,371 | he.config.ConfigurationFactory |  WARN | No configuration found. Configuring ehcache from ehcache-failsafe.xml  found in the classpath: jar:file:/home/yuruki/Git/camel-runner/build/install/camel-runner/lib/ehcache-2.7.4.jar!/ehcache-failsafe.xml
    2014-09-30 19:31:59,681 | ache.config.CacheConfiguration |  WARN | Cache 'camel-runner' is set to eternal but also has TTI/TTL set.  To avoid this warning, clean up the config removing conflicting values of eternal, TTI and TTL. Effective configuration for Cache 'camel-runner' will be eternal='true', timeToIdleSeconds='0', timeToLiveSeconds='0'.
    2014-09-30 19:32:00,035 | camel.impl.DefaultCamelContext |  INFO | Route: cache-route.cache.in started and consuming from: Endpoint[amq://topic:data?password=xxxxxx&username=admin]
    2014-09-30 19:32:00,045 | camel.impl.DefaultCamelContext |  INFO | Route: cache-route.cache.out started and consuming from: Endpoint[cache://camel-runner?eternal=true&key=data]
    Starting the internal [HTTP/1.1] server on port 8080
    2014-09-30 19:32:00,138 | camel.impl.DefaultCamelContext |  INFO | Route: cache-route started and consuming from: Endpoint[http://localhost:8080/data?restletMethods=GET]
    2014-09-30 19:32:00,141 | camel.impl.DefaultCamelContext |  INFO | Route: cache-route.completion started and consuming from: Endpoint[direct://processCompletion]
    2014-09-30 19:32:00,149 | camel.impl.DefaultCamelContext |  INFO | Total 4 routes, of which 4 is started.
    2014-09-30 19:32:00,158 | camel.impl.DefaultCamelContext |  INFO | Apache Camel 2.12.0.redhat-610379 (CamelContext: camel-runner-2) started in 2.081 seconds
    2014-09-30 19:33:05,426 |          cache-route.cache.out |  INFO | Exchange[ExchangePattern: InOnly, BodyType: String, Body: something]
    2014-09-30 19:33:37,697 |                    cache-route |  INFO | Exchange[ExchangePattern: InOut, Headers: {breadcrumbId=ID-musta-45308-1412094717827-0-4, CamelCacheElementWasFound=true, CamelHttpMethod=GET, CamelHttpUri=http://localhost:8080/data, CamelRestletRequest=GET http://localhost:8080/data HTTP/1.1, CamelRestletResponse=HTTP/1.1 - OK (200) - The request has succeeded, org.restlet.http.headers=[[Authorization: Basic dGVzdDp0ZXN0], [User-Agent: curl/7.38.0], [Host: localhost:8080], [Accept: */*]], org.restlet.startTime=1412094817683}, BodyType: String, Body: something]
    2014-09-30      19:33:37        0:0:0:0:0:0:0:1 test    -       8080    GET     /data   -       200     9       0       16      http://localhost:8080   curl/7.38.0     -
    2014-09-30 19:33:37,716 |         cache-route.completion |  INFO | Success: restlet:http://localhost:8080/data?restletMethods=get&restletRealm=#users -> log:cache-route?showHeaders=true
    2014-09-30 19:33:47,123 | .MainSupport$HangupInterceptor |  INFO | Received hang up - stopping the main instance.
    2014-09-30 19:33:47,125 | .apache.camel.main.MainSupport |  INFO | Apache Camel 2.12.0.redhat-610379 stopping
    2014-09-30 19:33:47,126 | camel.impl.DefaultCamelContext |  INFO | Apache Camel 2.12.0.redhat-610379 (CamelContext: camel-runner-2) is shutting down
    2014-09-30 19:33:47,128 | l.impl.DefaultShutdownStrategy |  INFO | Starting to graceful shutdown 4 routes (timeout 300 seconds)
    2014-09-30 19:33:47,134 | l.impl.DefaultShutdownStrategy |  INFO | Route: cache-route.completion shutdown complete, was consuming from: Endpoint[direct://processCompletion]
    2014-09-30 19:33:47,135 | l.impl.DefaultShutdownStrategy |  INFO | Route: cache-route shutdown complete, was consuming from: Endpoint[http://localhost:8080/data?restletMethods=GET]
    2014-09-30 19:33:47,135 | l.impl.DefaultShutdownStrategy |  INFO | Route: cache-route.cache.out shutdown complete, was consuming from: Endpoint[cache://camel-runner?eternal=true&key=data]
    2014-09-30 19:33:47,441 | l.impl.DefaultShutdownStrategy |  INFO | Route: cache-route.cache.in shutdown complete, was consuming from: Endpoint[amq://topic:data?password=xxxxxx&username=admin]
    2014-09-30 19:33:47,443 | l.impl.DefaultShutdownStrategy |  INFO | Graceful shutdown of 4 routes completed in 0 seconds
    2014-09-30 19:33:47,489 | camel.impl.DefaultCamelContext |  INFO | Apache Camel 2.12.0.redhat-610379 (CamelContext: camel-runner-2) uptime 1 minute
    2014-09-30 19:33:47,489 | camel.impl.DefaultCamelContext |  INFO | Apache Camel 2.12.0.redhat-610379 (CamelContext: camel-runner-2) is shutdown in 0.363 seconds
    
    From client's perspective:
    
    $ curl -u test:test http://localhost:8080/data
    something
