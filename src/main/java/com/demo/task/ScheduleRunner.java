package com.demo.task;

import com.demo.spider.ProxyCrawl;
import com.demo.spider.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
public class ScheduleRunner {

    @Autowired
    public ProxyCrawl proxyCrawl;
    @Autowired
    private Validator validator;

    @Scheduled(initialDelay = 1000,fixedDelay = 999999999)
    public void job1() {
        System.out.println(Thread.currentThread() + ", 开始爬虫@" + LocalTime.now());
        proxyCrawl.start();
    }

    @Scheduled(initialDelay = 2000,fixedDelay = 999999999)
    public void validatorIp() {
        System.out.println(Thread.currentThread() + ", 开始检查ip@" + LocalTime.now());
        validator.start();
    }
}