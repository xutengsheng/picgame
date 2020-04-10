package com.xts.picgame.model.bean;


import android.graphics.Picture;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class DataBean {
    /**
     * imageid : 127
     * typeid : 15
     * mname : ic_floor_006
     * url : http://47.110.151.50:8080/images/15-花/ic_floor_006.jpg
     * imgtime : 2020-04-08 00:41:14
     * tname : 花
     * music : null
     */

    @Id
    private String url;
    private int imageid;
    private int typeid;
    private String mname;
    private String imgtime;
    private String tname;
    private String music;
    private boolean identify = true;//识别图片
    private boolean expressive = true;//认知考核
    private boolean match = true;//完成匹配
    private boolean similar = true;//相似匹配
    private boolean sort = true;//图片分类
    private boolean receptive = true;//分类辨识
    @Generated(hash = 409069177)
    public DataBean(String url, int imageid, int typeid, String mname,
            String imgtime, String tname, String music, boolean identify,
            boolean expressive, boolean match, boolean similar, boolean sort,
            boolean receptive) {
        this.url = url;
        this.imageid = imageid;
        this.typeid = typeid;
        this.mname = mname;
        this.imgtime = imgtime;
        this.tname = tname;
        this.music = music;
        this.identify = identify;
        this.expressive = expressive;
        this.match = match;
        this.similar = similar;
        this.sort = sort;
        this.receptive = receptive;
    }
    @Generated(hash = 908697775)
    public DataBean() {
    }
    public String getUrl() {
        return this.url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public int getImageid() {
        return this.imageid;
    }
    public void setImageid(int imageid) {
        this.imageid = imageid;
    }
    public int getTypeid() {
        return this.typeid;
    }
    public void setTypeid(int typeid) {
        this.typeid = typeid;
    }
    public String getMname() {
        return this.mname;
    }
    public void setMname(String mname) {
        this.mname = mname;
    }
    public String getImgtime() {
        return this.imgtime;
    }
    public void setImgtime(String imgtime) {
        this.imgtime = imgtime;
    }
    public String getTname() {
        return this.tname;
    }
    public void setTname(String tname) {
        this.tname = tname;
    }
    public String getMusic() {
        return this.music;
    }
    public void setMusic(String music) {
        this.music = music;
    }
    public boolean getIdentify() {
        return this.identify;
    }
    public void setIdentify(boolean identify) {
        this.identify = identify;
    }
    public boolean getExpressive() {
        return this.expressive;
    }
    public void setExpressive(boolean expressive) {
        this.expressive = expressive;
    }
    public boolean getMatch() {
        return this.match;
    }
    public void setMatch(boolean match) {
        this.match = match;
    }
    public boolean getSimilar() {
        return this.similar;
    }
    public void setSimilar(boolean similar) {
        this.similar = similar;
    }
    public boolean getSort() {
        return this.sort;
    }
    public void setSort(boolean sort) {
        this.sort = sort;
    }
    public boolean getReceptive() {
        return this.receptive;
    }
    public void setReceptive(boolean receptive) {
        this.receptive = receptive;
    }

    @Override
    public String toString() {
        return "DataBean{" +
                "url='" + url + '\'' +
                ", imageid=" + imageid +
                ", typeid=" + typeid +
                ", mname='" + mname + '\'' +
                ", imgtime='" + imgtime + '\'' +
                ", tname='" + tname + '\'' +
                ", music='" + music + '\'' +
                ", identify=" + identify +
                ", expressive=" + expressive +
                ", match=" + match +
                ", similar=" + similar +
                ", sort=" + sort +
                ", receptive=" + receptive +
                '}';
    }
}