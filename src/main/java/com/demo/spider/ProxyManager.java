package com.demo.spider;

import com.demo.spider.crawl.Proxy_66ip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.ArrayList;
import java.util.List;


@Component
public class ProxyManager implements PageProcessor {

    @Autowired
    private ProxyPipeline proxyPipeline;

    private List<IPageProcessor> mList = new ArrayList<>();
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000).setTimeOut(10000);

    public ProxyManager() {
        mList.add(new Proxy_66ip());
    }


    @Override
    public void process(Page page) {
        for (IPageProcessor pageProcessor : mList) {
            if (pageProcessor.isMatch(page)) {
                pageProcessor.process(page);
            }
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    public void start() {
        Spider spider = Spider.create(this)
                .addPipeline(proxyPipeline).thread(1);
        for (IPageProcessor pageProcessor : mList) {
            spider.addUrl(pageProcessor.getStartUrl());
        }
        spider.run();
    }
}
