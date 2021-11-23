package com.ppphuang.demo.controller;

import com.ppphuang.demo.reflect.javassist.serverproxy.Context;
import com.ppphuang.demo.reflect.javassist.serverproxy.InvokeProxy;
import com.ppphuang.demo.reflect.javassist.serverproxy.TestService;
import com.ppphuang.demo.system.config.Container;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@RestController
public class ProxyServiceController {

    @GetMapping(value = "proxyService")
    public Object proxyService() throws Exception {

        //生成客户端代理类
        TestService testService = (TestService) Proxy.newProxyInstance(TestService.class.getClassLoader(), new Class[]{TestService.class}, new MyInvocationHandler(TestService.class));
        String ppphuang = testService.sayHello("ppphuang", 18);
        return ppphuang;
    }
}

/**
 * 自定义InvocationHandler
 */
class MyInvocationHandler implements InvocationHandler {
    private Class<?> clz;

    public MyInvocationHandler(Class<?> clz) {
        this.clz = clz;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String methodName = method.getName();
        //生成服务端代理类
        InvokeProxy invokeProxy = (InvokeProxy) Container.getProxyService(clz.getSimpleName());
        Context context = new Context();
        context.setMethod(methodName);
        context.setParameters(args);
        context.setParametersTypes(method.getParameterTypes());
        context.setServiceName(clz.getSimpleName());
        return invokeProxy.invoke(context);
    }
}