package com.demo.spider.crawl.xicidaili;

import com.demo.model.Proxy;
import com.demo.spider.IPageProcessor;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Selectable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public abstract class Proxy_xicidaili_base extends IPageProcessor {

    @Override
    public void process(Page page) {
        ArrayList<Proxy> proxys = null;
        Selectable tbody = page.getHtml().xpath("//*[@id=\"ip_list\"]/tbody/");
        List<Selectable> tr = tbody.nodes();
        for (Selectable selectable : tr) {
            try {
                List<Selectable> nodes = selectable.xpath("td").nodes();
                if (isboolIp(nodes.get(1).xpath("td/text()").toString())) {
                    if (proxys == null) {
                        proxys = new ArrayList<>();
                    }
                    Proxy e = new Proxy();
                    e.setIp(nodes.get(1).xpath("td/text()").toString());
                    e.setPort(Integer.parseInt(nodes.get(2).xpath("td/text()").toString()));
                    e.setArea(nodes.get(3).xpath("td/a/text()").toString());
                    e.setCreatetime(new Date());
                    e.setUpdatetime(new Date());
                    proxys.add(e);

                }
            } catch (Exception e){}

        }
        if (proxys != null) {

            page.putField("proxys",proxys);
        }

    }

}
