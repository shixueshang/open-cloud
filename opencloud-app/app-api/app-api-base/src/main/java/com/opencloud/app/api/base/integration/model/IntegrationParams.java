package com.opencloud.app.api.base.integration.model;

import lombok.Data;

import java.util.Map;

/**
 *
 **/
@Data
public class IntegrationParams {

    private String authType;
    private String accountName;
    private Map<String,String[]> authParameters;

    public String getAuthParameter(String paramter){
        String[] values = this.authParameters.get(paramter);
        if(values != null && values.length > 0){
            return values[0];
        }
        return null;
    }
}
