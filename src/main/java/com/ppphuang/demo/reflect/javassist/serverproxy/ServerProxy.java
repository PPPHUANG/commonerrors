package com.ppphuang.demo.reflect.javassist.serverproxy;

import javassist.*;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ServerProxy {

    private static Object proxyClass;

    public static Object getProxy(String name, Method[] methods) throws Exception {
        if (proxyClass == null) {
            makeProxy(name, methods);
        }
        return proxyClass;
    }

    //生成代理类
    static void makeProxy(String name, Method[] methods) throws Exception {
        String packName = "com.ppphuang.demo.reflect.javassist.serverproxy";
        final ClassPool pool = new ClassPool();
        pool.appendSystemPath();
        CtClass proxyClass = pool.makeClass(name + "$proxy");
        //实现invoke接口
        CtClass invokeProxy = pool.get(packName + ".InvokeProxy");
        proxyClass.addInterface(invokeProxy);
        //生成存储被代理类的属性
        //手动new 接口实现类 设置到属性
        CtField proxyField = CtField.make("private static " +
                packName + ".TestServiceImpl" +
                " serviceProxy = new " +
                packName + ".TestServiceImpl" +
                "();", proxyClass);

        //使用springcontext获取bean

//        CtField proxyField = CtField.make("private static " +
//                packName + ".TestService" +
//                " serviceProxy = " +
//                "((org.springframework.context.ApplicationContext) com.ppphuang.demo.system.config.Container.getSpringContext()).getBean("
//                + "\"" + "testServiceImpl" +
//                "\");", proxyClass);

        proxyClass.addField(proxyField);
        ArrayList<String> uniqueMethodNameList = new ArrayList<>();
        ArrayList<Method> uniqueMethodList = new ArrayList<>();
        for (int i = 0; i < methods.length; i++) {
            String methodName = methods[i].getName();
            if (!uniqueMethodNameList.contains(methodName)) {
                uniqueMethodNameList.add(methodName);
                uniqueMethodList.add(methods[i]);
            }
        }
        //创建接口方法
        String contextName = Context.class.getName();
        String exceptionName = Exception.class.getName();
        String objectName = Object.class.getName();
        String responseName = Response.class.getName();
        String utilName = Util.class.getName();
        for (Method uniqueMethod : uniqueMethodList) {
            String uniqueMethodName = uniqueMethod.getName();
            if ("equals".equalsIgnoreCase(uniqueMethodName) || "toString".equalsIgnoreCase(uniqueMethodName) || "hashCode".equalsIgnoreCase(uniqueMethodName)) {
                continue;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("public ").append(responseName).append(" ");
            sb.append(uniqueMethodName);
            sb.append("(" + contextName + " context) throws " + exceptionName + " {");
            sb.append(objectName + "[] params = " + "context.getParameters();");
            for (int i = 0; i < methods.length; i++) {
                Method method = methods[i];
                String methodName = method.getName();
                if (!uniqueMethod.getName().equalsIgnoreCase(methodName)) {
                    continue;
                }
                Class<?>[] parameterTypes = method.getParameterTypes();
                Type[] mGenericType = method.getGenericParameterTypes();
                sb.append("if(params.length == ").append(mGenericType.length);
                for (int j = 0; j < mGenericType.length; j++) {
                    String simpleParaName = getSimpleParaName(mGenericType[j], parameterTypes[j]);

                    String paraName = getParaName(mGenericType[j], parameterTypes[j]);

                    sb.append(" && (");
                    sb.append("params[");
                    sb.append(j);
                    sb.append("] == null || ");
                    sb.append("params[");
                    sb.append(j);
                    sb.append("].getClass().getSimpleName().equalsIgnoreCase(\"");
                    sb.append(paraName);
                    sb.append("\")");

                    if (paraName.contains("int")) {
                        sb.append("|| params[");
                        sb.append(j);
                        sb.append("].getClass().getSimpleName().equalsIgnoreCase(\"" + paraName.replaceAll("int", "Integer") + "\")");
                    } else if (paraName.contains("Integer")) {
                        sb.append("|| params[");
                        sb.append(j);
                        sb.append("].getClass().getSimpleName().equalsIgnoreCase(\"" + paraName.replaceAll("Integer", "int") + "\")");
                    } else if (paraName.contains("char")) {
                        sb.append("|| params[");
                        sb.append(j);
                        sb.append("].getClass().getSimpleName().equalsIgnoreCase(\"" + paraName.replaceAll("char", "Character") + "\")");
                    } else if (paraName.contains("Character")) {
                        sb.append("|| params[");
                        sb.append(j);
                        sb.append("].getClass().getSimpleName().equalsIgnoreCase(\"" + paraName.replaceAll("Character", "char") + "\")");
                    }
                    sb.append(")");
                }
                sb.append("){");
                for (int j = 0; j < mGenericType.length; j++) {
                    String paraName = mGenericType[j].toString().replaceFirst("class ", "");
                    if (paraName.startsWith("[")) {
                        paraName = parameterTypes[j].getCanonicalName();
                    }

                    sb.append(paraName.replaceAll("\\<.*\\>", ""));
                    sb.append(" arg" + j);

                    paraName = paraName.replaceAll("java.util.", "").replaceAll("java.lang.", "");

                    if (paraName.equals("long")) {
                        sb.append(" = 0L;");
                    } else if (paraName.equals("float")) {
                        sb.append(" = 0F;");
                    } else if (paraName.equals("double")) {
                        sb.append(" = 0D;");
                    } else if (paraName.equals("int")) {
                        sb.append(" = 0;");
                    } else if (paraName.equals("short")) {
                        sb.append(" = (short)0;");
                    } else if (paraName.equals("byte")) {
                        sb.append(" = (byte)0;");
                    } else if (paraName.equals("boolean")) {
                        sb.append(" = false;");
                    } else if (paraName.equals("char")) {
                        sb.append(" = (char)'\\0';");
                    } else if (paraName.equals("Long")) {
                        sb.append(" = new Long(\"0\");");
                    } else if (paraName.equals("Float")) {
                        sb.append(" = new Float(\"0\");");
                    } else if (paraName.equals("Double")) {
                        sb.append(" = new Double(\"0\");");
                    } else if (paraName.equals("Integer")) {
                        sb.append(" = new Integer(\"0\");");
                    } else if (paraName.equals("Short")) {
                        sb.append(" = new Short(\"0\");");
                    } else if (paraName.equals("Byte")) {
                        sb.append(" = new Byte(\"0\");");
                    } else if (paraName.equals("Boolean")) {
                        sb.append(" = new Boolean(\"false\");");
                    } else if (paraName.equals("Character")) {
                        sb.append(" = new Character((char)'\\0');");
                    } else {
                        sb.append(" = null;");
                    }
                }

                for (int j = 0; j < mGenericType.length; j++) {
                    String paraName = mGenericType[j].toString().replaceFirst("class ", "");

                    if (paraName.startsWith("[")) {
                        paraName = parameterTypes[j].getCanonicalName();
                    }

                    String pn = paraName.replaceAll("java.util.", "").replaceAll("java.lang.", "");
                    if (pn.equalsIgnoreCase("String")
                            || pn.equalsIgnoreCase("int") || pn.equalsIgnoreCase("Integer")
                            || pn.equalsIgnoreCase("long")
                            || pn.equalsIgnoreCase("short")
                            || pn.equalsIgnoreCase("float")
                            || pn.equalsIgnoreCase("boolean")
                            || pn.equalsIgnoreCase("double")
                            || pn.equalsIgnoreCase("char") || pn.equalsIgnoreCase("Character")
                            || pn.equalsIgnoreCase("byte")) {
                        sb.append("arg" + j);
                        sb.append(" = " + utilName + ".convertTo" + pn + "(params[" + j + "]);");
                    } else {
                        sb.append("arg" + j);
                        sb.append(" = (" + paraName.replaceAll("\\<.*\\>", "") + ")" + utilName + ".convertToT(params[" + j + "]), \"" + paraName + "\");");
                    }
                }

                //define returnValue
                Class<?> classReturn = method.getReturnType();
                Type typeReturn = method.getGenericReturnType();
                String returnValueType = typeReturn.getTypeName().replaceFirst("class ", "");
                if (returnValueType.startsWith("[")) {
                    returnValueType = classReturn.getCanonicalName();
                }
                if (!returnValueType.equalsIgnoreCase("void")) {
                    sb.append(returnValueType.replaceAll("\\<.*\\>", "") + " returnValue = ");
                }
                //通过设置的代理类属性调用方法
                sb.append("serviceProxy.");
                sb.append(method.getName());
                sb.append("(");
                //method para
                for (int j = 0; j < mGenericType.length; j++) {
                    sb.append("arg");
                    sb.append(j);
                    if (j != mGenericType.length - 1) {
                        sb.append(", ");
                    }
                }
                sb.append(");");
                if (!returnValueType.equalsIgnoreCase("void")) {
//                    switch (returnValueType) {
//                        case "int":
//                            sb.append("return new Integer(returnValue);");
//                            break;
//                        case "boolean":
//                            sb.append("return new Boolean(returnValue);");
//                            break;
//                        case "long":
//                            sb.append("return new Long(returnValue);");
//                            break;
//                        case "double":
//                            sb.append("return new Double(returnValue);");
//                            break;
//                        case "float":
//                            sb.append("return new Float(returnValue);");
//                            break;
//                        case "short":
//                            sb.append("return new Short(returnValue);");
//                            break;
//                        case "byte":
//                            sb.append("return new Byte(returnValue);");
//                            break;
//                        case "char":
//                            sb.append("return new Character(returnValue);");
//                            break;
//                        default:
//                            sb.append("return returnValue;");
//                    }
                    sb.append("return new " + responseName + "(returnValue);");
                } else {
                    sb.append("return null;");
                }
                sb.append("}");
            }
            sb.append("throw new " + exceptionName + "(\"method:" + proxyClass.getName() + "." + uniqueMethodName + "--msg:not fond method error\");");
            sb.append("}");
            System.out.println("method(" + uniqueMethodName + ") source code:" + sb);
            //生成方法
            CtMethod methodItem = CtMethod.make(sb.toString(), proxyClass);
            //添加到代理类中
            proxyClass.addMethod(methodItem);
        }

        StringBuilder sb = new StringBuilder();
        sb.append("public " + responseName + " invoke(" + contextName + " context) throws " + exceptionName + " {");
        sb.append("String methodName = context.getMethod();");
        for (int i = 0; i < methods.length; i++) {
            Method method = methods[i];
            String methodName = method.getName();
            if ("equals".equalsIgnoreCase(methodName) || "toString".equalsIgnoreCase(methodName) || "hashCode".equalsIgnoreCase(methodName)) {
                continue;
            }
            sb.append("if(methodName.equalsIgnoreCase(\"");
            sb.append(methodName);
            sb.append("\")){return ");
            sb.append(methodName);
            sb.append("(context);}");
        }
        sb.append("throw new " + exceptionName + "(\"method:" + proxyClass.getName() + ".invoke--msg:not found method (\"+methodName+\")\");");
        sb.append("}");
        System.out.println("method(" + "invoke" + ") source code:" + sb);
        CtMethod make = CtMethod.make(sb.toString(), proxyClass);
        proxyClass.addMethod(make);
        //创建invok方法
        ServerProxy.proxyClass = proxyClass.toClass().newInstance();
    }

    private static String getSimpleParaName(Type type, Class<?> clazz) {
        String paraName = type.toString().replaceFirst("class ", "");
        paraName = paraName.replaceAll("java.util.", "").replaceAll("java.lang.", "");
        if (paraName.startsWith("[")) {
            paraName = clazz.getCanonicalName();
        }
        paraName = getSimpleParaName(paraName);
        return paraName;
    }


    private static String getParaName(Type type, Class<?> clazz) {
        String paraName = getSimpleParaName(type, clazz);

        if (paraName.contains("<[") || paraName.contains(";>")) {
            paraName = paraName.replaceAll("\\[L", "").replaceAll("\\[", "");
        }
        //兼容jdk1.6和jdk1.7
        if (paraName.contains("[]>") || paraName.contains("[],")) {
            paraName = paraName.replace("[]>", ";>").replace("[],", ";,");
        }
        return paraName;
    }

    public static String getSimpleParaName(String paraName) {
        paraName = paraName.replaceAll(" ", "");
        if (paraName.indexOf(".") > 0) {
            String[] pnAry = paraName.split("");
            List<String> originalityList = new ArrayList<String>();
            List<String> replaceList = new ArrayList<String>();

            String tempP = "";
            for (int i = 0; i < pnAry.length; i++) {
                if (pnAry[i].equalsIgnoreCase("<")) {
                    originalityList.add(tempP);
                    replaceList.add(tempP.substring(tempP.lastIndexOf(".") + 1));
                    tempP = "";
                } else if (pnAry[i].equalsIgnoreCase(">")) {
                    originalityList.add(tempP);
                    replaceList.add(tempP.substring(tempP.lastIndexOf(".") + 1));
                    tempP = "";
                } else if (pnAry[i].equalsIgnoreCase(",")) {
                    originalityList.add(tempP);
                    replaceList.add(tempP.substring(tempP.lastIndexOf(".") + 1));
                    tempP = "";
                } else if (i == pnAry.length - 1) {
                    originalityList.add(tempP);
                    replaceList.add(tempP.substring(tempP.lastIndexOf(".") + 1));
                    tempP = "";
                } else {
                    if (!pnAry[i].equalsIgnoreCase("[") && !pnAry[i].equalsIgnoreCase("]")) {
                        tempP += pnAry[i];
                    }
                }
            }

            for (int i = 0; i < replaceList.size(); i++) {
                paraName = paraName.replaceAll(originalityList.get(i), replaceList.get(i));
            }
            return paraName;
        } else {
            return paraName;
        }
    }

}
