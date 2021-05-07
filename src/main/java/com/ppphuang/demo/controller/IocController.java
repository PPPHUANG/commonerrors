package com.ppphuang.demo.controller;

import com.ppphuang.demo.iocaop.SayService;
import com.ppphuang.demo.iocaop.TestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class IocController {
    @Autowired
    List<SayService> sayServiceList;

    @Autowired
    TestService testService;

    @GetMapping("ioctest")
    public void test() {
        log.info("====================");
        sayServiceList.forEach(SayService::say);
    }

    @GetMapping("aoptest")
    public String aoptest(String name) {
        testService.test();
        return "success";
    }

}
