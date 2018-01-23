package org.wildfly.swarm.microprofile.restclient.deployment;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.rest.client.ext.ResponseExceptionMapper;

/**
 * Created by hbraun on 22.01.18.
 */
class ExceptionMapping implements ClientResponseFilter {

    ExceptionMapping(Set<Object> instances) {

        this.instances = instances;
    }

    @Override
    public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext) throws IOException {

        Response response = new PartialResponse(responseContext);

        Map<ResponseExceptionMapper, Integer> mappers = new HashMap<>();
        for (Object o : instances) {
            if(o instanceof ResponseExceptionMapper) {
                ResponseExceptionMapper candiate = (ResponseExceptionMapper) o;
                if (candiate.handles(response.getStatus(), response.getHeaders())) {
                    mappers.put(candiate, candiate.getPriority());
                }
            }
        }

        if(mappers.size()>0) {
            Map<Optional<Throwable>, Integer> errors = new HashMap<>();

            mappers.forEach( (m, i) -> {
                Optional<Throwable> t = Optional.ofNullable(m.toThrowable(response));
                errors.put(t, i);
            });

            Optional<Throwable> prioritised = Optional.empty();
            for (Optional<Throwable> throwable : errors.keySet()) {
                if(throwable.isPresent()) {
                    if(!prioritised.isPresent())
                        prioritised = throwable;
                    else if(errors.get(throwable)<errors.get(prioritised))
                        prioritised = throwable;

                }
            }

            if(prioritised.isPresent()) // strange rule from the spec
                throw (WebApplicationException) prioritised.get();
        }

    }

    private Set<Object> instances;
}
