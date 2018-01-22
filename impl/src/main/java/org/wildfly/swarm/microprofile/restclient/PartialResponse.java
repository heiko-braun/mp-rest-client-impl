package org.wildfly.swarm.microprofile.restclient;

import java.lang.annotation.Annotation;
import java.net.URI;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.specimpl.MultivaluedMapImpl;


/**
 * Created by hbraun on 22.01.18.
 */
public class PartialResponse extends Response {
    public PartialResponse(ClientResponseContext responseContext) {

        this.responseContext = responseContext;
    }

    @Override
    public int getStatus() {
        return responseContext.getStatus();
    }

    @Override
    public StatusType getStatusInfo() {
        return responseContext.getStatusInfo();
    }

    @Override
    public Object getEntity() {
        throw notSupported();
    }

    private RuntimeException notSupported() {
        RuntimeException ex = new RuntimeException("method call not supported");
        ex.printStackTrace();
        return ex;
    }


    @Override
    public <T> T readEntity(Class<T> entityType) {
        throw notSupported();
    }

    @Override
    public <T> T readEntity(GenericType<T> entityType) {
        throw notSupported();
    }

    @Override
    public <T> T readEntity(Class<T> entityType, Annotation[] annotations) {
        throw notSupported();
    }

    @Override
    public <T> T readEntity(GenericType<T> entityType, Annotation[] annotations) {
        throw notSupported();
    }

    @Override
    public boolean hasEntity() {
        return responseContext.hasEntity();
    }

    @Override
    public boolean bufferEntity() {
        throw new RuntimeException("method call not supported");
    }

    @Override
    public void close() {

    }

    @Override
    public MediaType getMediaType() {
        return responseContext.getMediaType();
    }

    @Override
    public Locale getLanguage() {
        return responseContext.getLanguage();
    }

    @Override
    public int getLength() {
        return responseContext.getLength();
    }

    @Override
    public Set<String> getAllowedMethods() {
        return responseContext.getAllowedMethods();
    }

    @Override
    public Map<String, NewCookie> getCookies() {
        return responseContext.getCookies();
    }

    @Override
    public EntityTag getEntityTag() {
        return responseContext.getEntityTag();
    }

    @Override
    public Date getDate() {
        return responseContext.getDate();
    }

    @Override
    public Date getLastModified() {
        return responseContext.getLastModified();
    }

    @Override
    public URI getLocation() {
        return responseContext.getLocation();
    }

    @Override
    public Set<Link> getLinks() {
        return responseContext.getLinks();
    }

    @Override
    public boolean hasLink(String relation) {
        return responseContext.hasLink(relation);
    }

    @Override
    public Link getLink(String relation) {
        return responseContext.getLink(relation);
    }

    @Override
    public Link.Builder getLinkBuilder(String relation) {
        throw new RuntimeException("method call not supported");
    }

    @Override
    public MultivaluedMap<String, Object> getMetadata() {
        MultivaluedMap<String, Object> metaData = new MultivaluedMapImpl<String, Object>();
        // TODO
        return metaData;
    }

    @Override
    public MultivaluedMap<String, String> getStringHeaders() {
        return responseContext.getHeaders();
    }

    @Override
    public String getHeaderString(String name) {
        return responseContext.getHeaderString(name);
    }

    private final ClientResponseContext responseContext;
}
