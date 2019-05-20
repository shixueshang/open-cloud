package com.opencloud.app.api.base.integration.model;

/**
 **/
public class IntegrationAuthenticationContext {

    private static ThreadLocal<IntegrationParams> holder = new ThreadLocal<>();

    public static void set(IntegrationParams integrationAuthentication){
        holder.set(integrationAuthentication);
    }

    public static IntegrationParams get(){
        return holder.get();
    }

    public static void clear(){
        holder.remove();
    }
}
