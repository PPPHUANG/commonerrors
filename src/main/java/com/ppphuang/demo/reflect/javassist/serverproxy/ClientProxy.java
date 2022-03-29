package com.ppphuang.demo.reflect.javassist.serverproxy;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ClientProxy {
    public static void main(String[] args) {
        //生成客户端代理类
        TestService testService = (TestService) Proxy.newProxyInstance(TestService.class.getClassLoader(), new Class[]{TestService.class}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Method[] declaredMethods = proxy.getClass().getDeclaredMethods();
                String methodName = method.getName();
                //生成服务端代理类
                InvokeProxy invokeProxy = (InvokeProxy) ServerProxy.getProxy(methodName, declaredMethods);
                Context context = new Context();
                context.setMethod(methodName);
                context.setParameters(args);
                context.setParametersTypes(method.getParameterTypes());
                context.setServiceName(proxy.getClass().getSimpleName());
                return invokeProxy.invoke(context);
            }
        });
        System.out.println(testService.sayHello("ppphuang", 18));
        System.out.println(testService.sayHello("ppphuang", null));
        System.out.println(testService.sayHello(null, 18));
        System.out.println(testService.sayHello(null, null));
        System.out.println(testService.sayHelloInt(18));
        System.out.println(testService.sayHelloInt(18, 180));
    }

}
