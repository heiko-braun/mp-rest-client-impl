package org.wildfly.swarm.microprofile.restclient;

import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.eclipse.microprofile.rest.client.spi.RestClientBuilderResolver;

/**
 * Created by hbraun on 15.01.18.
 */
public class RestClientBuilderResolverImpl extends RestClientBuilderResolver {
    @Override
    public RestClientBuilder newBuilder() {
        return new RestEasyClientBuilder();
    }
}
