package org.wildfly.swarm.microprofile.restclient;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;

import javax.ws.rs.core.Configuration;

import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.eclipse.microprofile.rest.client.RestClientDefinitionException;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;

/**
 * Created by hbraun on 15.01.18.
 */
class RestEasyClientBuilder implements RestClientBuilder {

    public RestEasyClientBuilder() {
        this.builderDelegate = new ResteasyClientBuilder();
    }

    @Override
    public RestClientBuilder baseUrl(URL url) {
        try {
            this.baseURI = url.toURI();
            return this;
        } catch (URISyntaxException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public <T> T build(Class<T> aClass) throws IllegalStateException, RestClientDefinitionException {
        ResteasyClient client = this.builderDelegate.build();
        return client.target(this.baseURI).proxy(aClass);
    }

    @Override
    public Configuration getConfiguration() {
        return null;
    }

    @Override
    public RestClientBuilder property(String s, Object o) {
        return null;
    }

    @Override
    public RestClientBuilder register(Class<?> aClass) {
        return null;
    }

    @Override
    public RestClientBuilder register(Class<?> aClass, int i) {
        return null;
    }

    @Override
    public RestClientBuilder register(Class<?> aClass, Class<?>[] classes) {
        return null;
    }

    @Override
    public RestClientBuilder register(Class<?> aClass, Map<Class<?>, Integer> map) {
        return null;
    }

    @Override
    public RestClientBuilder register(Object o) {
        return null;
    }

    @Override
    public RestClientBuilder register(Object o, int i) {
        return null;
    }

    @Override
    public RestClientBuilder register(Object o, Class<?>[] classes) {
        return null;
    }

    @Override
    public RestClientBuilder register(Object o, Map<Class<?>, Integer> map) {
        return null;
    }

    private final ResteasyClientBuilder builderDelegate;

    private URI baseURI;
}
