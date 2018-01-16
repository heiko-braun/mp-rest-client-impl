package org.wildfly.swarm.microprofile.restclient;


import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.AfterDeploymentValidation;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.enterprise.inject.spi.WithAnnotations;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by hbraun on 15.01.18.
 */
public class RestClientExtension implements Extension {

    private static Set<Class<?>> proxyTypes = new LinkedHashSet<>();

    private static Set<Throwable> errors = new LinkedHashSet<>();

    public void registerClient(@Observes @WithAnnotations({RegisterRestClient.class}) ProcessAnnotatedType<?> pat) {
        Class<?> typeDef = pat.getAnnotatedType().getJavaClass();
        if(typeDef.isInterface()) {
            proxyTypes.add(typeDef);
            pat.veto();
        } else {
            errors.add(new IllegalArgumentException("Rest client needs to be interface: " + typeDef));
        }
    }

    public void createProxy(@Observes AfterBeanDiscovery afterBeanDiscovery, BeanManager beanManager) {
        for (Class<?> proxyType : proxyTypes) {
            afterBeanDiscovery.addBean(new RestClientDelegateBean(proxyType, beanManager));
        }
    }

    public void reportErrors(@Observes AfterDeploymentValidation afterDeploymentValidation) {
        for (Throwable error : errors) {
            afterDeploymentValidation.addDeploymentProblem(error);
        }
    }
}
