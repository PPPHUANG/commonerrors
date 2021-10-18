package com.ppphuang.demo.reflect.javassist;

import javassist.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

public class Proxy {
    // 动态生成代理类的前缀
    private static final String PROXY_CLASSNAME_PREFIX = "$Proxy_";
    // 缓存容器，防止生成同一个Class文件在同一个ClassLoader加载崩溃的问题
    private static final Map<String, Class<?>> proxyClassCache = new HashMap<>();

    /**
     * 缓存已经生成的代理类的Class，key值根据 classLoader 和 targetClass 共同决定
     */
    private static void saveProxyClassCache(ClassLoader classLoader, Class<?> targetClass, Class<?> proxyClass) {
        String key = classLoader.toString() + "_" + targetClass.getName();
        proxyClassCache.put(key, proxyClass);
    }

    /**
     * 从缓存中取得代理类的Class，如果没有则返回 null
     */
    private static Class<?> getProxyClassCache(ClassLoader classLoader, Class<?> targetClass) {
        String key = classLoader.toString() + "_" + targetClass.getName();
        return proxyClassCache.get(key);
    }

    /**
     * 返回一个动态创建的代理类，此类继承自 targetClass
     *
     * @param classLoader       从哪一个ClassLoader加载Class
     * @param invocationHandler 代理类中每一个方法调用时的回调接口
     * @param targetClass       被代理对象
     * @param targetConstructor 被代理对象的某一个构造器，用于决定代理对象实例化时采用哪一个构造器
     * @param targetParam       被代理对象的某一个构造器的参数，用于实例化构造器
     * @return
     */
    public static Object newProxyInstance(ClassLoader classLoader,
                                          InvocationHandler invocationHandler,
                                          Class<?> targetClass,
                                          Constructor<?> targetConstructor,
                                          Object... targetParam) {
        if (classLoader == null || targetClass == null || invocationHandler == null) {
            throw new IllegalArgumentException("argument is null");
        }
        try {
            // 查看是否有缓存
            Class<?> proxyClass = getProxyClassCache(classLoader, targetClass);
            if (proxyClass != null) {
                // 实例化代理对象
                return newInstance(proxyClass, invocationHandler, targetConstructor, targetParam);
            }

            ClassPool pool = new ClassPool(true);
            pool.importPackage(InvocationHandler.class.getName());
            pool.importPackage(Method.class.getName());
            // 被代理类
            CtClass targetCtClass = pool.get(targetClass.getName());
            // 检查被代理类是否是 final的
            if ((targetCtClass.getModifiers() & Modifier.FINAL) == Modifier.FINAL) {
                throw new IllegalArgumentException("class is final");
            }
            // 新建代理类
            CtClass proxyCtClass = pool.makeClass(generateProxyClassName(targetClass));
            // 设置描述符
            proxyCtClass.setModifiers(Modifier.PUBLIC | Modifier.FINAL);
            // 设置继承关系
            proxyCtClass.setSuperclass(targetCtClass);
            // 添加构造器
            addConstructor(pool, proxyCtClass, targetCtClass);
            // 添加方法
            addMethod(proxyCtClass, targetCtClass, targetClass);
            // 从指定ClassLoader加载Class
            proxyClass = proxyCtClass.toClass(classLoader, null);
            // 缓存
            saveProxyClassCache(classLoader, targetClass, proxyClass);
            // 输出到文件保存，用于debug调试
//            File outputFile = new File("/Users/jm/Downloads/Demo");
//            proxyCtClass.writeFile(outputFile.getAbsolutePath());
            proxyCtClass.defrost();
            // 实例化代理对象
            return newInstance(proxyClass, invocationHandler, targetConstructor, targetParam);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 生成代理类的类名生成规则
     */
    private static String generateProxyClassName(Class<?> targetClass) {
        return targetClass.getPackage().getName() + "." + PROXY_CLASSNAME_PREFIX + targetClass.getSimpleName();
    }

    /**
     * 根据被代理类的构造器，构造代理类对象。代理类的所有构造器都是被代理类构造器前添加一个invocationHandler 参数
     *
     * @param proxyClass        代理类
     * @param invocationHandler 代理类所有构造器的第一个参数
     * @param targetConstructor 被代理类的构造器
     * @param targetParam       被代理类的构造器的参数
     * @return
     * @throws Exception
     */
    private static Object newInstance(Class<?> proxyClass,
                                      InvocationHandler invocationHandler,
                                      Constructor<?> targetConstructor,
                                      Object... targetParam) throws Exception {
        Class[] parameterTypes = new Class[targetConstructor.getParameterTypes().length + 1];
        merge(parameterTypes, InvocationHandler.class, targetConstructor.getParameterTypes());
        Constructor<?> constructor = proxyClass.getConstructor(parameterTypes);
        Object[] paramter = new Object[targetParam.length + 1];
        merge(paramter, invocationHandler, targetParam);
        return constructor.newInstance(paramter);
    }

    /**
     * 代理类添加构造器，基于被代理类的构造器，在所有参数开头添加一个 {@link InvocationHandler} 参数
     *
     * @param pool
     * @param proxyClass
     * @param targetClass
     * @throws Exception
     */
    private static void addConstructor(ClassPool pool, CtClass proxyClass, CtClass targetClass) throws Exception {
        // 添加 invocationHandler 字段
        CtField field = CtField.make("private InvocationHandler invocationHandler = null;", proxyClass);
        proxyClass.addField(field);
        CtConstructor[] constructors = targetClass.getConstructors();
        for (CtConstructor constructor : constructors) {
            CtClass[] parameterTypes = new CtClass[constructor.getParameterTypes().length + 1];
            merge(parameterTypes, pool.get(InvocationHandler.class.getName()), constructor.getParameterTypes());
            CtConstructor newConstructor = new CtConstructor(parameterTypes, proxyClass);
            // 因为第一个参数指定为 InvocationHandler，所以super()的参数是从2开始的
            StringBuilder sb = new StringBuilder();
            for (int i = 1; i <= constructor.getParameterTypes().length; i++) {
                sb.append("$").append(i + 1).append(",");
            }
            if (sb.length() > 0) {
                sb.setLength(sb.length() - 1);
            }
            // 添加构造器方法
            String code = String.format("{super(%s);this.invocationHandler = $1;}", sb.toString());
            newConstructor.setBody(code);
            proxyClass.addConstructor(newConstructor);
        }
    }

    /**
     * 先添加 Public 的方法，然后添加 Project 和 default 的方法
     *
     * @param proxyCtClass  代理类
     * @param targetCtClass 被代理类
     * @param targetClass   被代理类
     * @throws Exception
     */
    private static void addMethod(CtClass proxyCtClass, CtClass targetCtClass, Class<?> targetClass) throws Exception {
        int methodNameIndex = 0;
        methodNameIndex = addMethod(proxyCtClass, targetCtClass, targetClass.getMethods(), targetCtClass.getMethods(), true, methodNameIndex);
        addMethod(proxyCtClass, targetCtClass, targetClass.getDeclaredMethods(), targetCtClass.getDeclaredMethods(), false, methodNameIndex);
    }

    /**
     * 代理类添加方法，基于被代理类的共有方法。因为{@link CtClass#getMethods()} 和 {@link Class#getMethods()}返回的列表顺序不一样，所以需要做一次匹配
     *
     * @param proxyCtClass    代理类
     * @param targetCtClass   被代理类
     * @param methods         被代理类的方法数组
     * @param ctMethods       被代理类的方法数组
     * @param isPublic        是否是共有方法，是：只包含public方法；否：包含projected和default方法
     * @param methodNameIndex 新建方法的命名下标
     * @return
     * @throws Exception
     */
    private static int addMethod(CtClass proxyCtClass, CtClass targetCtClass, Method[] methods, CtMethod[] ctMethods, boolean isPublic, int methodNameIndex) throws Exception {
        for (int i = 0; i < ctMethods.length; i++) {
            CtMethod ctMethod = ctMethods[i];
            // final和static修饰的方法不能被继承
            if ((ctMethod.getModifiers() & Modifier.FINAL) == Modifier.FINAL
                    || (ctMethod.getModifiers() & Modifier.STATIC) == Modifier.STATIC) {
                continue;
            }
            // 满足指定的修饰符
            int modifyFlag = -1;
            if (isPublic) {
                // public 方法
                if ((ctMethod.getModifiers() & Modifier.PUBLIC) == Modifier.PUBLIC) {
                    modifyFlag = Modifier.PUBLIC;
                }
            } else {
                // protected 方法
                if ((ctMethod.getModifiers() & Modifier.PROTECTED) == Modifier.PROTECTED) {
                    modifyFlag = Modifier.PROTECTED;
                } else if ((ctMethod.getModifiers() & Modifier.PUBLIC) == 0
                        && (ctMethod.getModifiers() & Modifier.PROTECTED) == 0
                        && (ctMethod.getModifiers() & Modifier.PRIVATE) == 0) {
                    modifyFlag = 0;
                }
            }
            if (modifyFlag == -1) {
                continue;
            }

            // 匹配对应的方法
            int methodIndex = findSomeMethod(methods, ctMethod);
            if (methodIndex == -1) {
                continue;
            }
            // 将这个方法作为字段保存，便于新增的方法能够访问原来的方法
            String code = null;
            if (isPublic) {
                code = String.format("private static Method method%d = Class.forName(\"%s\").getMethods()[%d];",
                        methodNameIndex, targetCtClass.getName(), methodIndex);
            } else {
                code = String.format("private static Method method%d = Class.forName(\"%s\").getDeclaredMethods()[%d];",
                        methodNameIndex, targetCtClass.getName(), methodIndex);
            }
            CtField field = CtField.make(code, proxyCtClass);
            proxyCtClass.addField(field);

            CtMethod newCtMethod = new CtMethod(ctMethod.getReturnType(), ctMethod.getName(), ctMethod.getParameterTypes(), proxyCtClass);
            // 区分静态与非静态，主要就是对象是否传null。注意这里必须用($r)转换类型，否则会发生类型转换失败的问题
            if ((ctMethod.getModifiers() & Modifier.STATIC) == Modifier.STATIC) {
                code = String.format("return ($r)invocationHandler.invoke(null, method%d, $args);", methodNameIndex);
            } else {
                code = String.format("return ($r)invocationHandler.invoke(this, method%d, $args);", methodNameIndex);
            }
            newCtMethod.setBody(code);
            newCtMethod.setModifiers(modifyFlag);
            proxyCtClass.addMethod(newCtMethod);
            methodNameIndex++;
        }
        return methodNameIndex;
    }

    /**
     * 从 methods 找到等于 ctMethod 的下标索引并返回。找不到则返回 -1
     */
    private static int findSomeMethod(Method[] methods, CtMethod ctMethod) {
        for (int i = 0; i < methods.length; i++) {
            if (equalsMethod(methods[i], ctMethod)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 判断{@link Method} 和 {@link CtMethod} 是否相等。主要从方法名、返回值类型、参数类型三个维度判断
     */
    private static boolean equalsMethod(Method method, CtMethod ctMethod) {
        if (method == null && ctMethod == null) {
            return true;
        }
        if (method == null || ctMethod == null) {
            return false;
        }
        try {
            if (method.getName().equals(ctMethod.getName())
                    && method.getReturnType().getName().equals(ctMethod.getReturnType().getName())) {
                Class<?>[] parameterTypes = method.getParameterTypes();
                CtClass[] parameterTypesCt = ctMethod.getParameterTypes();
                if (parameterTypes.length != parameterTypesCt.length) {
                    return false;
                }
                boolean equals = true;
                for (int i = 0; i < parameterTypes.length; i++) {
                    if (!parameterTypes[i].getName().equals(parameterTypesCt[i].getName())) {
                        equals = false;
                        break;
                    }
                }
                return equals;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 将 item 和 array 合并到 merge
     */
    private static <T> T[] merge(T[] merge, T item, T[] array) {
        List<T> list = new ArrayList<>();
        list.add(item);
        Collections.addAll(list, array);
        list.toArray(merge);
        return merge;
    }
}
