package com.ppphuang.demo.annotation;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author ppphuang
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
//因为 Spring 的事务管理也是基于 AOP 的，默认情况下优先级最低也就是会先执行出操作，但是自定义切面 MetricsAspect 也同样是最低优先级，这个时候就可能出现问题：
// 如果出操作先执行捕获了异常，那么 Spring 的事务处理就会因为无法捕获到异常导致无法回滚事务。
// 解决方式是，明确 MetricsAspect 的优先级，可以设置为最高优先级，也就是最先执行入操作最后执行出操作
@Order(Ordered.HIGHEST_PRECEDENCE)
public @interface Metrics {
    /**
     * 在方法成功执行后打点，记录方法的执行时间发送到指标系统，默认开启
     *
     * @return
     */
    boolean recordSuccessMetrics() default true;

    /**
     * 在方法执行失败后打点，记录方法的执行时间发送到指标系统，默认开启
     *
     * @return
     */
    boolean recordFailMetrics() default true;

    /**
     * 通过日志记录参数，默认开启
     *
     * @return
     */
    boolean logParameters() default true;

    /**
     * 通过日志记录返回值，默认开启
     *
     * @return
     */
    boolean logReturn() default true;

    /**
     * 出现异常后通过日志记录异常信息，默认开启
     *
     * @return
     */
    boolean logException() default true;

    /**
     * 出现异常后忽略异常返回默认值，默认关闭
     *
     * @return
     */
    boolean ignoreException() default false;
}
