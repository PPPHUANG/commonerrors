package com.ppphuang.demo.reflect;

import com.ppphuang.demo.annotation.MyAnnotation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotatedElementUtils;

@MyAnnotation(value = "Class")
@Slf4j
class AnnoParent {
    @MyAnnotation(value = "Method")
    public void foo() {

    }
}

@Slf4j
@MyAnnotation(value = "Class1")
class Child extends AnnoParent {
    @Override
    public void foo() {

    }

    private static String getAnnotationValue(MyAnnotation annotation) {
        if (annotation == null) {
            return "";
        }
        return annotation.value();
    }

    public static void wrong() throws NoSuchMethodException {
        AnnoParent annoParent = new AnnoParent();
        log.info("ParentClass:{}", getAnnotationValue(annoParent.getClass().getAnnotation(MyAnnotation.class)));
        log.info("ParentMethod:{}", getAnnotationValue(annoParent.getClass().getMethod("foo").getAnnotation(MyAnnotation.class)));

        Child child = new Child();
        log.info("ChildClass:{}",getAnnotationValue(child.getClass().getAnnotation(MyAnnotation.class)));
        log.info("ChildMethod:{}",getAnnotationValue(child.getClass().getMethod("foo").getAnnotation(MyAnnotation.class)));
//        可以看到，父类的类和方法上的注解都可以正确获得，但是子类的类和方法却不能。这说明，子类以及子类的方法，无法自动继承父类和父类方法上的注解。
    }

    public static void right () throws NoSuchMethodException {
        Child child = new Child();
        log.info("ChildClass:{}",getAnnotationValue(AnnotatedElementUtils.findMergedAnnotation(child.getClass(), MyAnnotation.class)));
        log.info("ChildMethod:{}",getAnnotationValue(AnnotatedElementUtils.findMergedAnnotation(child.getClass().getMethod("foo"), MyAnnotation.class)));
    }
    public static void main(String[] args) throws NoSuchMethodException {
        wrong();
        right();
    }
}
