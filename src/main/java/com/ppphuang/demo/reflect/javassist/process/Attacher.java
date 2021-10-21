package com.ppphuang.demo.reflect.javassist.process;

import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;

import java.io.IOException;

public class Attacher {
    public static void main(String[] args) throws
            AttachNotSupportedException, IOException,
            AgentLoadException, AgentInitializationException {
        // 传入目标 JVM pid
        VirtualMachine vm = VirtualMachine.attach("10864");
        vm.loadAgent("/Users/ppphuang/Documents/code/java/target/demo-0.0.1-SNAPSHOT.jar");
    }
}
