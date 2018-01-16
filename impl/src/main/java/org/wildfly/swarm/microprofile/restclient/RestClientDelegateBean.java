package org.wildfly.swarm.microprofile.restclient;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.Dependent;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.inject.spi.PassivationCapable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class RestClientDelegateBean implements Bean<Object>, PassivationCapable{
    public static final String REST_URL_FORMAT = "%s/mp-rest/url";
    public static final String REST_SCOPE_FORMAT = "%s/mp-rest/scope";
    private final Class<?> clientInterface;
    private final Class<? extends Annotation> scope;
    private final BeanManager beanManager;
    //private final Config config;

    RestClientDelegateBean(Class<?> clientInterface, BeanManager beanManager) {
        this.clientInterface = clientInterface;
        this.beanManager = beanManager;
        //this.config = ConfigProvider.getConfig();
        this.scope = this.readScope();
    }
    @Override
    public String getId() {
        return clientInterface.getName();
    }

    @Override
    public Class<?> getBeanClass() {
        return clientInterface;
    }

    @Override
    public Set<InjectionPoint> getInjectionPoints() {
        return Collections.emptySet();
    }

    @Override
    public boolean isNullable() {
        return false;
    }

    @Override
    public Object create(CreationalContext<Object> creationalContext) {
        RestEasyClientBuilder builder = new RestEasyClientBuilder();
        String baseUrl = getBaseUrl();
        try {
            return builder.baseUrl(new URL(baseUrl)).build(clientInterface);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("The value of URL was invalid "+baseUrl);
        }
    }

    @Override
    public void destroy(Object instance, CreationalContext<Object> creationalContext) {

    }

    @Override
    public Set<Type> getTypes() {
        return Collections.singleton(clientInterface);
    }

    @Override
    public Set<Annotation> getQualifiers() {
        return Collections.singleton(RestClient.LITERAL);
    }

    @Override
    public Class<? extends Annotation> getScope() {
        return scope;
    }

    @Override
    public String getName() {
        return clientInterface.getName();
    }

    @Override
    public Set<Class<? extends Annotation>> getStereotypes() {
        return Collections.emptySet();
    }

    @Override
    public boolean isAlternative() {
        return false;
    }

    private String getBaseUrl() {
        /*String property = String.format(REST_URL_FORMAT, clientInterface.getName());
        return config.getValue(property, String.class);*/
        return "http://foobar";
    }

    private Class<? extends Annotation> readScope() {
        // first check to see if the value is set
        String property = String.format(REST_SCOPE_FORMAT, clientInterface.getName());
        String configuredScope = null;//config.getOptionalValue(property, String.class).orElse(null);
        if(configuredScope != null) {
            try {
                return (Class<? extends Annotation>)Class.forName(configuredScope);
            } catch (Exception e) {
                throw new IllegalArgumentException("The scope "+configuredScope+" is invalid",e);
            }
        }

        List<Annotation> possibleScopes = new ArrayList<>();
        Annotation[] annotations = clientInterface.getDeclaredAnnotations();
        for(Annotation annotation : annotations) {
            if(beanManager.isScope(annotation.annotationType())) {
                possibleScopes.add(annotation);
            }
        }
        if(possibleScopes.isEmpty()) {
            return Dependent.class;
        }
        else if(possibleScopes.size() == 1) {
            return possibleScopes.get(0).annotationType();
        }
        else {
            throw new IllegalArgumentException("The client interface "+clientInterface+" has multiple scopes defined "+possibleScopes);
        }
    }
}
