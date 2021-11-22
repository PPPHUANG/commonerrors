package com.ppphuang.demo.system.config;

import java.util.concurrent.ConcurrentHashMap;

public class Container {

    private static Object SpringContext = null;

    private static ConcurrentHashMap<String, Object> proxyServiceMap = new ConcurrentHashMap<>();

    public static Object getSpringContext() {
        return SpringContext;
    }

    public static void setSpringContext(Object springContext) {
        SpringContext = springContext;
    }

    public static void setProxyService(String serviceName, Object service) {
        proxyServiceMap.put(serviceName, service);
    }

    public static Object getProxyService(String serviceName) {
        return proxyServiceMap.get(serviceName);
    }
}
