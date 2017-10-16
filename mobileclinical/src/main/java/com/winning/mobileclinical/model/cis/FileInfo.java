package com.winning.mobileclinical.model.cis;

import java.math.BigDecimal;

public class FileInfo {
	/// <summary>
    /// 首页序号
    /// </summary>
    public BigDecimal syxh;
    /// <summary>
    /// 婴儿序号
    /// </summary>
    public BigDecimal yexh;
    /// <summary>
    /// 科室代码
    /// </summary>
    public String ksdm;
    /// <summary>
    /// 病区代码
    /// </summary>
    public String bqdm;
    /// <summary>
    /// 关键字
    /// </summary>
    public String key;
    /// <summary>
    /// 名称
    /// </summary>
    public String res_id;
    /// <summary>
    /// 源
    /// </summary>
    public String src;
    
	public String getRes_id() {
		return res_id;
	}
	public void setRes_id(String res_id) {
		this.res_id = res_id;
	}
	public String getSrc() {
		return src;
	}
	public void setSrc(String src) {
		this.src = src;
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
	public String getKsdm() {
		return ksdm;
	}
	public void setKsdm(String ksdm) {
		this.ksdm = ksdm;
	}
	public String getBqdm() {
		return bqdm;
	}
	public void setBqdm(String bqdm) {
		this.bqdm = bqdm;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	
    }
