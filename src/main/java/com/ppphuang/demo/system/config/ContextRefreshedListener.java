package com.ppphuang.demo.system.config;

import com.ppphuang.demo.reflect.javassist.serverproxy.ServerProxy;
import com.ppphuang.demo.reflect.javassist.serverproxy.TestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
@Slf4j
public class ContextRefreshedListener implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        // 根容器为Spring容器
        if (contextRefreshedEvent.getApplicationContext().getParent() == null) {
            //将SpringContext存放到Container中 已备后续使用
            Container.setSpringContext(contextRefreshedEvent.getApplicationContext());
            //生成服务代理类
            Class<TestService> testServiceClass = TestService.class;
            String serviceName = testServiceClass.getSimpleName();
            Method[] declaredMethods = testServiceClass.getDeclaredMethods();
            Object proxy = null;
            try {
                proxy = ServerProxy.getProxy(serviceName, declaredMethods);
            } catch (Exception e) {
                log.error("初始化服务代理类失败:{}", serviceName);
            }
            Container.setProxyService(serviceName, proxy);
        }
    }
}
