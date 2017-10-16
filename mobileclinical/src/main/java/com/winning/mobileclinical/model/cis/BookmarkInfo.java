package com.winning.mobileclinical.model.cis;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2016/12/2.
 * 涂鸦的bean
 */

public class BookmarkInfo {
    private BigDecimal id;
    private BigDecimal syxh;
    private BigDecimal yexh;
    private String emr_id;
    private String doctor;
    private String create_time;
    private String file;
        public BookmarkInfo(){}
    public BookmarkInfo(BigDecimal id, BigDecimal syxh, BigDecimal yexh, String emr_id, String doctor, String create_time, String file) {
        this.id = id;
        this.syxh = syxh;
        this.yexh = yexh;
        this.emr_id = emr_id;
        this.doctor = doctor;
        this.create_time = create_time;
        this.file = file;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public BigDecimal getSyxh() {
        return syxh;
    }

    public void setSyxh(BigDecimal syxh) {
        this.syxh = syxh;
    }

    public BigDecimal getYexh() {
        return yexh;
    }

    public void setYexh(BigDecimal yexh) {
        this.yexh = yexh;
    }

    public String getEmr_id() {
        return emr_id;
    }

    public void setEmr_id(String emr_id) {
        this.emr_id = emr_id;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
}
