package com.meancat.jac;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.glassfish.jersey.apache.connector.ApacheClientProperties;
import org.glassfish.jersey.apache.connector.ApacheConnectorProvider;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Hello world!
 *
 */
public class App 
{
    private static final Logger LOG = LoggerFactory.getLogger(App.class);

    private static Client client;

    public static class AppArgs {
        @Option(required=true, name="--url", aliases={"-u"})
        public String url;

        @Option(required=false, name="--iterations", aliases={"-i"})
        public int iterations= 50;
    }

    public static void main( String[] args ) throws InterruptedException {
        AppArgs appArgs = new AppArgs();
        CmdLineParser p = new CmdLineParser(appArgs);
        try {
            p.parseArgument(args);
        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            return;
        }

        ClientConfig config = new ClientConfig();

        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        // set intentionally low for testing pool access
        connectionManager.setMaxTotal(5);
        connectionManager.setDefaultMaxPerRoute(10);

        config.property(ApacheClientProperties.CONNECTION_MANAGER, connectionManager);
        config.property(ClientProperties.READ_TIMEOUT, 30_000);
        config.property(ClientProperties.CONNECT_TIMEOUT, 30_000);

        final ObjectMapper clientObjMapper = new ObjectMapper();
        config.register(new BodyConverter(clientObjMapper));
        config.connectorProvider(new ApacheConnectorProvider());

        client = ClientBuilder.newClient(config);

        // {"storefront":"test_store","wgid":0,"language":"en","additional_data":{},"country":"US","profiles":{}}
        SomeRequest request = new SomeRequest();

        LOG.debug("Run for {} iterations", appArgs.iterations);
        for (int i=0; i < appArgs.iterations; i++) {
            postString(appArgs.url, request);
//            postComplexObject(appArgs.url, request);
            Thread.sleep(100);
        }
    }

    private static void postString(String url, SomeRequest request) {
        String response = client.target(url)
                .request()
                .post(Entity.entity(request, MediaType.APPLICATION_JSON_TYPE), new GenericType<String>() {
                });

        LOG.debug("String response: {}", response);
    }

    private static void postComplexObject(String url, SomeRequest request) {
        SomeResponse response = client.target(url)
                .request()
                .post(Entity.entity(request, MediaType.APPLICATION_JSON_TYPE), new GenericType<SomeResponse>() {
                });

        LOG.debug("String response: {}", response);
    }
}
