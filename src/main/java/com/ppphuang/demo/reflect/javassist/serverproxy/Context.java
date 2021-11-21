package com.ppphuang.demo.reflect.javassist.serverproxy;

public class Context {
    private String serviceName;

    private String method;

    private Class<?>[] parametersTypes;

    private Object[] parameters;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Class<?>[] getParametersTypes() {
        return parametersTypes;
    }

    public void setParametersTypes(Class<?>[] parametersTypes) {
        this.parametersTypes = parametersTypes;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }
}
