package com.ppphuang.demo.iocaop;

import com.ppphuang.demo.annotation.Metrics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Metrics(logReturn = false)
@Slf4j
public class TestService {
    public void test() {
        log.info("TestService test method");
    }
}
