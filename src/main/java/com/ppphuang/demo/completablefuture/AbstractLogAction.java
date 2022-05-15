package com.ppphuang.demo.completablefuture;

import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractLogAction<R> {
    protected final String methodName;
    protected final Object[] args;

    public AbstractLogAction(String methodName, Object... args) {
        this.methodName = methodName;
        this.args = args;
    }

    protected void logResult(R result, Throwable throwable) {
        if (throwable != null) {
//            boolean isBusinessError = throwable instanceof TBase || (throwable.getCause() != null && throwable.getCause() instanceof TBase);
            boolean isBusinessError = true;
            if (isBusinessError) {
                logBusinessError(throwable);
                //这里为内部rpc框架抛出的异常，使用时可以酌情修改
//            } else if (throwable instanceof DegradeException || throwable instanceof DegradeRuntimeException) {
            } else if (throwable instanceof DegradeException) {
//                if (RhinoSwitch.getBoolean("isPrintDegradeLog", false)) {
                if (true) {
                    log.error("{} degrade exception, param:{} , error:{}", methodName, args, throwable);
                }
            } else {
                log.error("{} unknown error, param:{} , error:{}", methodName, args, ExceptionUtils.extractRealException(throwable));
            }
        } else {
            if (isLogResult()) {
                log.info("{} param:{} , result:{}", methodName, args, result);
            } else {
                log.info("{} param:{}", methodName, args);
            }
        }
    }

    private void logBusinessError(Throwable throwable) {
        log.error("{} business error, param:{} , error:{}", methodName, args, throwable.toString(), ExceptionUtils.extractRealException(throwable));
    }

    private boolean isLogResult() {
        //这里是动态配置开关，用于动态控制日志打印，开源动态配置中心可以使用nacos、apollo等，如果项目没有使用配置中心则可以删除
//        return RhinoSwitch.getBoolean(methodName + "_isLogResult", false);
        return true;
    }
}

