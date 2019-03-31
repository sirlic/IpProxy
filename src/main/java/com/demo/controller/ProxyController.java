package com.demo.controller;

import com.demo.bean.BaseResponse;
import com.demo.bean.IpResult;
import com.demo.dao.ProxysRepository;
import com.demo.model.Proxy;
import org.springframework.beans.factory.annotation.Autowired;
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
        Proxy proxy = repository.findFirstByAllocedFalseAndScoreGreaterThan(0);
        if (proxy == null) {
            Iterable<Proxy> all = repository.findAll();
            for (Proxy p : all) {
                p.setAlloced(false);
                repository.save(p);
            }
        }
        proxy = repository.findFirstByAllocedFalseAndScoreGreaterThan(0);
        if (proxy != null) {
            proxy.setAlloced(true);
            repository.save(proxy);
        }
        return new BaseResponse<IpResult>(new IpResult(proxy.getIp(),proxy.getPort(),proxy.getProtocol(),proxy.getScore()));
    }

}
