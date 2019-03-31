package com.demo.spider.crawl;

import com.demo.model.Proxy;
import com.demo.spider.IPageProcessor;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Selectable;

import java.util.*;

@Component
public class Proxy_89ip extends IPageProcessor {

    public String host = "http://www.89ip.cn/";
    public  String url = host + "index_1.html";
    public  int i = 0;

    private List<String> mPages = new ArrayList<>();
    @Override
    public void process(Page page) {

        Selectable pages = page.getHtml().xpath("//*[@id=\"layui-laypage-1\"]/");
        List<Selectable> nexts = pages.nodes();
        for (Selectable n : nexts) {

            String e = n.xpath("a/@href").toString();
            if (!mPages.contains(e)) {
                mPages.add(e);
            }
        }

        Collections.sort(mPages, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                String s1 = o1.substring(6, o1.length() - 5);
                String s2 = o1.substring(6, o1.length() - 5);
                return Integer.valueOf(s2)-Integer.valueOf(s1);
            }
        });


        ArrayList<Proxy> proxys = null;
        Selectable tbody = page.getHtml().xpath("/html/body//div[3]/div[1]/div/div[1]/table/tbody/");
        List<Selectable> tr = tbody.nodes();
        for (Selectable selectable : tr) {
            List<Selectable> nodes = selectable.xpath("td").nodes();
            if (isboolIp(nodes.get(0).xpath("td/text()").toString().trim())) {
                if (proxys == null) {
                    proxys = new ArrayList<>();
                }
                Proxy e = new Proxy();
                e.setIp(nodes.get(0).xpath("td/text()").toString().trim());
                e.setPort(Integer.parseInt(nodes.get(1).xpath("td/text()").toString().trim()));
                e.setArea(nodes.get(2).xpath("td/text()").toString().trim());
                e.setCreatetime(new Date());
                e.setUpdatetime(new Date());
                proxys.add(e);

            }
        }
        if (proxys != null) {
            page.putField("proxys",proxys);
        }

        if (i < mPages.size()) {
            i++;
            page.addTargetRequest(host+mPages.get(i));
        }

    }

    @Override
    public boolean isMatch(Page page) {
        return page.getRequest().getUrl().startsWith(host);
    }

    @Override
    public String getStartUrl() {
        return String.format(url,i);
    }
}
