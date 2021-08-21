package com.ppphuang.demo.system.config.interceptor;

import com.ppphuang.demo.annotation.UGCContent;
import com.ppphuang.demo.tools.EmojiTransfer;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Intercepts({
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})
})
@Slf4j
public class EmojiTransferInterceptior implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        if (invocation.getTarget() instanceof Executor && invocation.getArgs().length == 2) {
            final Executor executor = (Executor) invocation.getTarget();
            // 获取第一个参数
            final MappedStatement ms = (MappedStatement) invocation.getArgs()[0];
            final Object paramObj = invocation.getArgs()[1];
            if (ms.getSqlCommandType() == SqlCommandType.INSERT) {
                return this.executeInsert(executor, ms, paramObj);
            } else if (ms.getSqlCommandType() == SqlCommandType.UPDATE) {
                return this.executeUpdate(executor, ms, paramObj);
            }
        } else if (invocation.getTarget() instanceof Executor && invocation.getArgs().length == 4) {
            Object result = invocation.proceed();
            // 获取第一个参数
            final MappedStatement ms = (MappedStatement) invocation.getArgs()[0];
            if (ms.getSqlCommandType() == SqlCommandType.SELECT) {
                return handleResults(result);
            }
        }
        return invocation.proceed();
    }

    private Object handleResults(Object input) {
        if (null == input) {
            return input;
        }
        Map<Class<?>, List<Field>> targets;
        if (input instanceof Collection && !CollectionUtils.isEmpty((Collection) input)) {
            targets = batchFindTargets(((Collection) input).iterator().next().getClass());
        } else if (input instanceof Map && !CollectionUtils.isEmpty((Map<?, ?>) input)) {
            targets = batchFindTargets(((Map) input).values().iterator().next().getClass());
        } else {
            targets = batchFindTargets(input.getClass());
        }

        if (CollectionUtils.isEmpty(targets)) {
            // 如果没有要转的类，直接返回了
            return input;
        }
        List<String> strTargets = batchGetUGC(targets, input);
        if (CollectionUtils.isEmpty(strTargets)) {
            return input;
        }
        Map<String, String> transferTargets = batchTransfer(strTargets);
        batchFillTransferUGC(targets, input, transferTargets);
        return input;
    }

    private void batchFillTransferUGC(Map<Class<?>, List<Field>> targets, Object object, Map<String, String> ugcs) {
        if (null == object) {
            // 啥也不做
            return;
        } else if (object instanceof Collection) {
            ((Collection<?>) object).forEach(obj -> batchFillTransferUGC(targets, obj, ugcs));
        } else if (object instanceof Map) {
            ((Map<?, ?>) object).values().forEach(obj -> batchFillTransferUGC(targets, obj, ugcs));
        } else {
            // 填充自己
            if (targets.containsKey(object.getClass())) {
                targets.get(object.getClass()).forEach(field -> {
                    try {
                        field.setAccessible(true);
                        field.set(object, ugcs.getOrDefault((String) field.get(object), (String) field.get(object)));
                    } catch (IllegalAccessException e) {
                        // ignore
                    }
                });
            }
            Arrays.stream(object.getClass().getDeclaredFields()).forEach(field -> {
                field.setAccessible(true);
                try {
                    if (field.get(object) != null && (needStepIn(field) || Collection.class.isAssignableFrom(field.getType()) || Map.class.isAssignableFrom(field.getType()))) {
                        batchFillTransferUGC(targets, field.get(object), ugcs);
                    }
                } catch (IllegalAccessException e) {
                    // ignore
                }
            });

        }
    }

    private Map<String, String> batchTransfer(List<String> input) {
        int MAX_SEND = 100;
        List<List<String>> splitList = Stream.iterate(0, n -> n + 1)
                .limit(input.size() / MAX_SEND + 1).parallel()
                .map(a -> input.stream().skip(a * MAX_SEND).limit(MAX_SEND).parallel().collect(Collectors.toList())).collect(Collectors.toList());
        return splitList.parallelStream()
                .flatMap(list -> EmojiTransfer.multiUnicodeEmoji(list)
                        .entrySet().stream())
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private List<String> batchGetUGC(Map<Class<?>, List<Field>> targets, Object object) {
        if (null == object) {
            return new ArrayList<>();
        } else if (object instanceof Collection) {
            return ((Collection<?>) object).stream().flatMap(obj -> {
                List<String> childResult = batchGetUGC(targets, obj);
                return childResult.stream();
            }).collect(Collectors.toList());
        } else if (object instanceof Map) {
            return ((Map<?, ?>) object).values().stream().flatMap(obj -> {
                List<String> childResult = batchGetUGC(targets, obj);
                return childResult.stream();
            }).collect(Collectors.toList());
        } else {
            List<String> ugcs = new ArrayList<>();
            Arrays.stream(object.getClass().getDeclaredFields()).forEach(field -> {
                field.setAccessible(true);
                try {
                    if (Collection.class.isAssignableFrom(field.getType()) || Map.class.isAssignableFrom(field.getType())) {
                        ugcs.addAll(batchGetUGC(targets, field.get(object)));
                    } else if (field.get(object) != null && needStepIn(field)) {
                        ugcs.addAll(batchGetUGC(targets, field.get(object)));
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            });
            if (targets.containsKey(object.getClass())) {
                ugcs.addAll(targets.get(object.getClass()).stream().map(field -> {
                    try {
                        field.setAccessible(true);
                        return (String) field.get(object);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        return "";
                    }
                }).filter(number -> !StringUtils.isEmpty(number))
                        .collect(Collectors.toSet()));
            }
            return ugcs;
        }
    }

    private Map<Class<?>, List<Field>> batchFindTargets(Class<?> clazz) {
        Map<Class<?>, List<Field>> results = new HashMap<>();
        Arrays.stream(clazz.getDeclaredFields()).filter(field -> !field.getType().isPrimitive()).forEach(field -> {
            results.computeIfAbsent(clazz, k -> new ArrayList<>());
            if (String.class.equals(field.getType()) && field.getAnnotation(UGCContent.class) != null) {
                results.get(clazz).add(field);
            } else if (Collection.class.isAssignableFrom(field.getType()) || Map.class.isAssignableFrom(field.getType())) {
                Objects.requireNonNull(getRawClass(field)).forEach(fieldClazz -> results.putAll(batchFindTargets(fieldClazz)));
            } else if (!results.containsKey(field.getType()) && needStepIn(field)) {
                results.putAll(batchFindTargets(field.getType()));
            } else {
                // ignore 非model内类型不处理
            }
        });
        return results;
    }

    private boolean needStepIn(Field field) {
        return field.getType().getPackage() != null && field.getType().getPackage().getName().contains("com.ppphuang.demo.dao");
    }

    private List<Class<?>> getRawClass(Field collectionField) {
        ParameterizedType listGenericType = (ParameterizedType) collectionField.getGenericType();
        Type[] listActualTypeArguments = listGenericType.getActualTypeArguments();
        if (null == listActualTypeArguments || listActualTypeArguments.length == 0) {
            return new ArrayList<>();
        } else {
            return Arrays.stream(listActualTypeArguments).map(type -> {
                try {
                    return Class.forName(type.getTypeName());
                } catch (ClassNotFoundException e) {
                    return null;
                }
            }).filter(Objects::nonNull).collect(Collectors.toList());
        }
    }

    private Object handleRequest(Object input) {
        Map<Class<?>, List<Field>> targets = new HashMap<>();

        if (input instanceof Map) {
            //多参数需要遍历
            HashMap<Object, Object> inputMap = (HashMap<Object, Object>) input;
            for (Map.Entry<Object, Object> v : inputMap.entrySet()) {
                System.out.println(v.getValue().getClass());
                targets.putAll(batchFindTargets(v.getValue().getClass()));
            }
        } else {
            targets = batchFindTargets(input.getClass());
        }
        targets = targets.entrySet().stream().filter(entry -> !CollectionUtils.isEmpty(entry.getValue())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        if (CollectionUtils.isEmpty(targets)) {
            // 如果没有要转的类，直接返回了就
            return input;
        }
        List<String> strTargets = batchGetUGC(targets, input);
        if (CollectionUtils.isEmpty(strTargets)) {
            return input;
        }
        Map<String, String> transferTargets = EmojiTransfer.multiTransferEmoji(strTargets);
        batchFillTransferUGC(targets, input, transferTargets);
        return input;
    }

    private Object executeUpdate(Executor executor, MappedStatement ms, Object paramObj) throws Exception {
        return executor.update(ms, handleRequest(paramObj));
    }

    private Object executeInsert(Executor executor, MappedStatement ms, Object paramObj) throws Exception {
        return executor.update(ms, handleRequest(paramObj));
    }

    @Override
    public Object plugin(Object obj) {
        return Plugin.wrap(obj, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }
}
