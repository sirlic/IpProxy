package com.demo.spider;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class IPageProcessor implements PageProcessor {

    /**
     * 判断是否处理page
     * @param page
     * @return
     */
    public abstract boolean isMatch(Page page);

    /**
     * 启动url
     * @return
     */
    public abstract String getStartUrl();

    @Override
    public Site getSite() {
        return null;
    }



    /** * 判断是否为合法IP * @return the ip */
    public  boolean isboolIp(String ipAddress) {
        String ip = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
        Pattern pattern = Pattern.compile(ip);
        Matcher matcher = pattern.matcher(ipAddress);
        return matcher.matches();
    }

}
