package com.ppphuang.demo.job;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Launcher {
    public static void main(String[] args) {
        if (args == null || args.length == 0) {
            outPrint();
            System.exit(-1);
        }
        String execClassName = args[0];
        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("springJob.xml");
        classPathXmlApplicationContext.setValidating(false);
        BaseJob job = (BaseJob) classPathXmlApplicationContext.getBean(execClassName);
        job.run(args);
    }

    private static void outPrint() {
        System.out.println("Usage  java -jar jarName.jar className");
    }
}



//    public static void main(String[] args) {
//
//        //args = new String[]{"fetchChatPhoneUserJob"};
//        //args = new String[]{"handleConsultantInfoUpdate"};
//        args = new String[]{"handleOrderInfoUpdate"};
//
//
//        if (null == args || args.length == 0) {
//            outPrint();
//            System.exit(-1);
//        }
//        String className = args[0];
//        ApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
//        BaseJob job = (BaseJob) context.getBean(className);
//        job.run(args);
//    }
//
//    private static void outPrint() {
//        System.out.println("Usage  java -jar jarName.jar className");
//    }