package com.ppphuang.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
public class ThreadPoolController {

    @GetMapping("/get1")
    public Object get1() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new HashMap<String, Object>(3) {{
            put("id", 1);
            put("name", "name1");
        }};
    }

    @GetMapping("/get2")
    public Object get2() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new HashMap<String, Object>(3) {{
            put("id", 2);
            put("name", "name2");
        }};
    }

    @GetMapping("/get3")
    public Object get3() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new HashMap<String, Object>(3) {{
            put("id", 3);
            put("name", "name3");
        }};
    }
}
