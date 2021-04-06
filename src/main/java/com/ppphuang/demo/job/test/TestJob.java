package com.ppphuang.demo.job.test;

import com.ppphuang.demo.job.BaseJob;

public class TestJob extends BaseJob {
    @Override
    public void run(String[] args) {
        System.out.println("Exec TestJob ing");
    }
}
