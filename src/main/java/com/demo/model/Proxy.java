package com.demo.model;


import org.hibernate.annotations.ColumnDefault;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Generated;
import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "proxys" )
public class Proxy {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String ip;
    private Integer  port;
    private Integer types;
    private Integer protocol;
    private String country;

    @Column(length = 500)
    private String area;
    private Date updatetime;
    private Date createtime;
    private Long speed;

    private Integer score;

    //表示正在使用
    private boolean alloced;

    //表示已经校验
    private boolean valided = false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Integer getTypes() {
        return types;
    }

    public void setTypes(Integer types) {
        this.types = types;
    }

    public Integer getProtocol() {
        return protocol;
    }

    public void setProtocol(Integer protocol) {
        this.protocol = protocol;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Long getSpeed() {
        return speed;
    }

    public void setSpeed(Long speed) {
        this.speed = speed;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public boolean isAlloced() {
        return alloced;
    }

    public void setAlloced(boolean alloced) {
        this.alloced = alloced;
    }

    public boolean isValided() {
        return valided;
    }

    public void setValided(boolean valided) {
        this.valided = valided;
    }

    @Override
    public String toString() {
        return "Proxy{" +
                "id=" + id +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                ", types=" + types +
                ", protocol=" + protocol +
                ", country='" + country + '\'' +
                ", area='" + area + '\'' +
                ", updatetime=" + updatetime +
                ", createtime=" + createtime +
                ", speed=" + speed +
                ", score=" + score +
                ", alloced=" + alloced +
                ", valided=" + valided +
                '}';
    }
}
