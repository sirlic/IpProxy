package com.demo.task;

import com.demo.spider.ProxyManager;
import com.demo.spider.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
public class ScheduleRunner {

    @Autowired
    public ProxyManager proxyCrawl;
    @Autowired
    private Validator validator;

    @Scheduled(cron = "0 0 0 1/1 * ?")
    public void job1() {
        System.out.println(Thread.currentThread() + ", 开始爬虫@" + LocalTime.now());
        proxyCrawl.start();
    }

    @Scheduled(cron = "0 0 2 1/1 * ?")
    public void validatorIp() {
        System.out.println(Thread.currentThread() + ", 开始检查ip@" + LocalTime.now());
        validator.start();
    }
}
