package com.demo.dao;

import com.demo.model.Course;
import com.demo.model.Proxy;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Repository
public interface ProxysRepository extends CrudRepository<Proxy,Long> {
    @Override
    Iterable<Proxy> findAll();


    @Transactional
    @Modifying
    @Query("update Proxy p set p.protocol = :protocol , p.types = :types , p.speed = :speed  , p.updatetime = :updatetime where p.id = :id ")
    int updateById(@Param(value = "id") long id, @Param(value = "protocol") int protocol, @Param(value = "types") int types,
                   @Param(value = "speed") long speed, @Param(value = "updatetime") Date updatetime);

    @Transactional
    @Modifying
    @Query("update Proxy p set p.area = :area , p.country = :country , p.updatetime = :updatetime where p.ip = :ip and p.port = :port")
    int updateByIpAndPort(@Param("ip") String ip, @Param("port") int port,@Param(value = "area") String area,
                   @Param(value = "country") String country, @Param(value = "updatetime") Date updatetime);
    @Transactional
    @Modifying
    @Query("update Proxy p set p.score = score+1 where p.id = :id ")
    int updateScoreIncByid(@Param(value = "id") long id);

    @Transactional
    @Modifying
    @Query("update Proxy p set p.score = score-1 where p.id = :id ")
    int updateScoreDecByid(@Param(value = "id") long id);

    Proxy findProxyByIpAndPort(String ip,Integer port);
}
