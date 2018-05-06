package com.demo.spider;

import com.demo.dao.ProxysRepository;
import com.demo.model.Proxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.List;

@Component
public class ProxyPipeline implements Pipeline {

    @Autowired
    private ProxysRepository repository;

    @Override
    public void process(ResultItems resultItems, Task task) {
        List<Proxy> proxys = resultItems.get("proxys");
        if (proxys == null) {
            return;
        }
        try {
            for (Proxy proxy:proxys) {
                if (repository.findProxyByIpAndPort(proxy.getIp(),proxy.getPort()) == null) {
                    proxy.setScore(0);
                    repository.save(proxy);
                } else {
                    repository.updateByIpAndPort(proxy.getIp(),proxy.getPort(),proxy.getArea(),proxy.getCountry(),proxy.getUpdatetime());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
