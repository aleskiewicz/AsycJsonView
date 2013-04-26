package pl.aleskiewicz.jaxrs;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.client.InvocationCallback;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleResourceTestJersey extends JerseyTest {
    @Before
    public void beforeMethod() {
        // populate dao with 1 entry
        SimpleService.INSTANCE.notifyListeners("standard", "detailed");

    }

    @Override
    protected ResourceConfig configure() {
        // mvn test
        // -DargLine="-Djersey.config.test.containerFactory=org.glassfish.jersey.test.inmemory.InMemoryTestContainerFactory"
        // mvn test
        // -DargLine="-Djersey.config.test.containerFactory=org.glassfish.jersey.test.grizzly.GrizzlyTestContainerFactory"

        enable(TestProperties.LOG_TRAFFIC);
        enable(TestProperties.DUMP_ENTITY);
        return new SimpleApplication();
    }
    private static final Logger _logger = LoggerFactory.getLogger(SimpleResourceTestJersey.class.getName());


    @Test()
    public void getAsync() {
        final WebTarget resourceTarget = target().path(
                UriBuilder.fromResource(SimpleResource.class)
                .path(SimpleResource.class, "async").build()
                .getPath()).queryParam("offset", 100);
        final CountDownLatch requestLatch = new CountDownLatch(1);
        final String[] serverResponse = { "" };
        _logger.debug("make call {}", resourceTarget.getUri());
        resourceTarget.request(MediaType.APPLICATION_JSON).async().get(new InvocationCallback<String>() {

            @Override
            public void completed(String response) {
                _logger.debug("resourceTarget call completed: {}", response);
                serverResponse[0] = response;
                requestLatch.countDown();
            }

            @Override
            public void failed(Throwable throwable) {
                _logger.error("callback problem: ", throwable);
                requestLatch.countDown();
            }
        });
        try {
            Thread.sleep(500);
            SimpleService.INSTANCE.notifyListeners("new", "some details");
            if (!requestLatch.await(5, TimeUnit.SECONDS)) {
                _logger.error("Waiting for all GET requests to complete has timed out.");
            }
            Assert.assertTrue("Actual result: " + serverResponse,
                    serverResponse[0].contains("\"detailedInfo\":\"some details\""));
            Assert.assertFalse("Actual result: " + serverResponse,
                    serverResponse[0].contains("\"standardInfo\":\"new\""));
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test()
    public void getPreviousAsync() {
        final WebTarget resourceTarget = target().path(
                UriBuilder.fromResource(SimpleResource.class)
                .path(SimpleResource.class, "async").build()
                .getPath()).queryParam("offset", 0);

        _logger.debug("make call {}", resourceTarget.getUri());
        String serverResponse = resourceTarget.request(MediaType.APPLICATION_JSON).get(String.class);
        Assert.assertTrue("Actual result: " + serverResponse, serverResponse.contains("\"detailedInfo\""));
        Assert.assertFalse("Actual result: " + serverResponse, serverResponse.contains("\"standardInfo\""));
        _logger.debug("result {}", serverResponse);
    }

    @Test()
    public void getSync() {
        final WebTarget resourceTarget = target().path(
                UriBuilder.fromResource(SimpleResource.class)
                .path(SimpleResource.class, "sync").build()
                .getPath());

        _logger.debug("make call {}", resourceTarget.getUri());
        String serverResponse = resourceTarget.request(MediaType.APPLICATION_JSON).get(String.class);
        _logger.debug("result {}", serverResponse);
        Assert.assertFalse("Actual result: " + serverResponse, serverResponse.contains("\"detailedInfo\""));
        Assert.assertTrue("Actual result: " + serverResponse, serverResponse.contains("\"standardInfo\""));
    }
}