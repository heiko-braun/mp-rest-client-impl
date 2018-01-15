package org.wildfly.swarm.microprofile.restclient;

import java.net.URL;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Created by hbraun on 15.01.18.
 */
public class SimpleTest {

    @Test
    public void abstractResponse() throws Exception {
        RemoteApi remoteApi = RestClientBuilder.newBuilder()
                .baseUrl(new URL("http://echo.jsontest.com/key/value"))
                .build(RemoteApi.class);
        Response response = remoteApi.getResponse();
        Assert.assertEquals(200, response.getStatus());
        String payload = response.readEntity(String.class);
        System.out.println(payload);
        Assert.assertTrue(payload.contains("value"));
    }

    @Test
    @Ignore
    public void typedResponse() throws Exception {
        RemoteApi remoteApi = RestClientBuilder.newBuilder()
                .baseUrl(new URL("http://echo.jsontest.com/key/value"))
                .build(RemoteApi.class);

        Tuple tuple = remoteApi.get();
        Assert.assertEquals("value", tuple.value);
    }

    @Path("/")
    interface RemoteApi {

        @GET
        Tuple get();

        @GET
        Response getResponse();
    }


    class Tuple {
        public String key;
        public String value;

    }
}
