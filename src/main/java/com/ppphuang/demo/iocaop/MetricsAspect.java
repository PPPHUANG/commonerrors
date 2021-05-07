package com.ppphuang.demo.iocaop;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ppphuang.demo.annotation.Metrics;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Array;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

@Aspect
@Component
@Slf4j
public class MetricsAspect {
    //让Spring帮我们注入ObjectMapper，以方便通过JSON序列化来记录方法入参和出参

    @Autowired
    private ObjectMapper objectMapper;

    //实现一个返回Java基本类型默认值的工具。其实，你也可以逐一写很多if-else判断类型，然后手动设置其默认值。这里为了减少代码量用了一个小技巧，即通过初始化一个具有1个元素的数组，然后通过获取这个数组的值来获取基本类型默认值
    private static final Map<Class<?>, Object> DEFAULT_VALUES = Stream.of(boolean.class, byte.class, double.class, float.class, int.class, long.class, short.class)
            .collect(toMap(clazz -> (Class<?>) clazz, clazz -> Array.get(Array.newInstance(clazz,1), 0)));

    public static <T> T getDefaultValue(Class<?> clazz) {
        return (T) DEFAULT_VALUES.get(clazz);
    }

    //@annotation指示器实现对标记了Metrics注解的方法进行匹配
    @Pointcut("within(@com.ppphuang.demo.annotation.Metrics *)")
    public void withMetricsAnnotation() {

    }

    //within指示器实现了匹配那些类型上标记了@RestController注解的方法
    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void controllerBean() {

    }

    @Around("controllerBean() || withMetricsAnnotation())")
    public Object metrics(ProceedingJoinPoint pjp) throws Throwable {
        //通过连接点获取方法签名和方法上Metrics注解，并根据方法签名生成日志中要输出的方法定义描述
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Metrics metrics = signature.getMethod().getAnnotation(Metrics.class);
        //切入的连接点是方法，注解定义在类上是无法直接从方法上获取到注解的。
        //优先从方法获取，如果获取不到再从类获取，如果还是获取不到再使用默认的注解
        if (metrics == null) {
            metrics = signature.getMethod().getDeclaringClass().getAnnotation(Metrics.class);
        }
        String name = String.format("【%s】【%s】", signature.getDeclaringType().toString(), signature.toLongString());
        //因为需要默认对所有@RestController标记的Web控制器实现@Metrics注解的功能，在这种情况下方法上必然是没有@Metrics注解的，我们需要获取一个默认注解。
        //虽然可以手动实例化一个@Metrics注解的实例出来，但为了节省代码行数，我们通过在一个内部类上定义@Metrics注解方式，然后通过反射获取注解的小技巧，来获得一个默认的@Metrics注解的实例
        if (metrics == null) {
            @Metrics
            final class c {}
            metrics = c.class.getAnnotation(Metrics.class);
        }
        //尝试从请求上下文（如果有的话）获得请求URL，以方便定位问题
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            if (request != null) {
                name += String.format("【%s】", request.getRequestURL().toString());
            }
        }
        //实现的是入参的日志输出
        if (metrics.logParameters()) {
            log.info(String.format("【入参日志】调用 %s 的参数是：【%s】", name, objectMapper.writeValueAsString(pjp.getArgs())));
        }
        //实现连接点方法的执行，以及成功失败的打点，出现异常的时候还会记录日志
        Object returnValue;
        Instant start = Instant.now();
        try {
            returnValue = pjp.proceed();
            if (metrics.recordSuccessMetrics())
                //在生产级代码中，我们应考虑使用类似Micrometer的指标框架，把打点信息记录到时间序列数据库中，实现通过图表来查看方法的调用次数和执行时间，在设计篇我们会重点介绍
            {
                log.info(String.format("【成功打点】调用 %s 成功，耗时：%d ms", name, Duration.between(start, Instant.now()).toMillis()));
            }
        } catch (Exception ex) {
            if (metrics.recordFailMetrics()) {
                log.info(String.format("【失败打点】调用 %s 失败，耗时：%d ms", name, Duration.between(start, Instant.now()).toMillis()));
            }
            if (metrics.logException()) {
                log.error(String.format("【异常日志】调用 %s 出现异常！", name), ex);
            }

            //忽略异常的时候，使用一开始定义的getDefaultValue方法，来获取基本类型的默认值
            if (metrics.ignoreException()) {
                returnValue = getDefaultValue(signature.getReturnType());
            } else {
                throw ex;
            }
        }
        //实现了返回值的日志输出
        if (metrics.logReturn()) {
            log.info(String.format("【出参日志】调用 %s 的返回是：【%s】", name, returnValue));
        }
        return returnValue;
    }
}
