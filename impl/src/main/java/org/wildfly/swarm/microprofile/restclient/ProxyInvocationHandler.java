package org.wildfly.swarm.microprofile.restclient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.ws.rs.client.ResponseProcessingException;

/**
 * Created by hbraun on 22.01.18.
 */
class ProxyInvocationHandler implements InvocationHandler {

    private Object target;

    public ProxyInvocationHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        try {
           return  method.invoke(target,args);
        } catch (InvocationTargetException e) {

            if(e.getCause() instanceof ResponseProcessingException) {
                ResponseProcessingException rpe = (ResponseProcessingException) e.getCause();
                Throwable cause = rpe.getCause();
                if(cause instanceof RuntimeException) {
                    throw (RuntimeException)cause;
                }
            }

            throw e;
        }
    }
}
