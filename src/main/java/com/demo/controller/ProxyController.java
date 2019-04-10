package com.demo.controller;

import com.demo.bean.BaseResponse;
import com.demo.bean.IpResult;
import com.demo.dao.ProxysRepository;
import com.demo.model.Proxy;
import com.demo.spider.ProxyManager;
import com.demo.spider.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/proxy")
public class ProxyController {
    @Autowired
    private ProxysRepository repository;

    @Autowired
    public ProxyManager proxyCrawl;
    @Autowired
    private Validator validator;

    @RequestMapping(path = "/gets",produces = "application/json; charset=utf-8")
    @ResponseBody
    public BaseResponse<List<IpResult>> getProxys() {
        Iterable<Proxy> all = repository.findProxiesByScoreGreaterThanOrderByScoreDesc(0);
        ArrayList<IpResult> ipResults = new ArrayList<>();
        for (Proxy proxy : all) {
            ipResults.add(new IpResult(proxy.getIp(),proxy.getPort(),proxy.getProtocol(),proxy.getScore()));
        }
        return new BaseResponse<List<IpResult>>(ipResults);
    }

    @RequestMapping(path = "/get",produces = "application/json; charset=utf-8")
    @ResponseBody
    public BaseResponse<IpResult> getProxy() {
        System.out.println(Thread.currentThread()+"gerproxy");
        Proxy proxy = repository.findFirstByAllocedFalseAndScoreGreaterThan(0);
        if (proxy == null) {
            Iterable<Proxy> all = repository.findAll();
            for (Proxy p : all) {
                p.setAlloced(false);
            }
            repository.save(all);
            proxy = repository.findFirstByAllocedFalseAndScoreGreaterThan(0);
        }

        if (proxy != null) {
            proxy.setAlloced(true);
            repository.save(proxy);
        }
        return new BaseResponse<IpResult>(proxy == null ? null : new IpResult(proxy.getIp(),proxy.getPort(),proxy.getProtocol(),proxy.getScore()));
    }

    @RequestMapping(path = "/startValidater",produces = "application/json; charset=utf-8")
    @ResponseBody
    @Async
    public BaseResponse startValidater() {
        validator.start();
        return new BaseResponse(null);
    }

    @RequestMapping(path = "/startCrawel",produces = "application/json; charset=utf-8")
    @ResponseBody
    @Async
    public BaseResponse startCrawel() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                proxyCrawl.start();
            }
        }).start();
        return new BaseResponse(null);
    }
}
