package com.winning.mobileclinical.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DrInfo implements Serializable {
	private String xh;// 序号
	private String ysdm;// 查房医生代码
	private String syxh; // 首页序号
	private String yexh; // 婴儿序号
	private String memo; // 查房文本内容
	private String cfsj; // 创建日期
	private List<MediaList> mediaList = new ArrayList<MediaList>();

	public String getXh() {
		return xh;
	}

	public void setXh(String xh) {
		this.xh = xh;
	}

	public String getSyxh() {
		return syxh;
	}

	public void setSyxh(String syxh) {
		this.syxh = syxh;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getYsdm() {
		return ysdm;
	}

	public void setYsdm(String ysdm) {
		this.ysdm = ysdm;
	}

	public String getCfsj() {
		return cfsj;
	}

	public void setCfsj(String cfsj) {
		this.cfsj = cfsj;
	}

	public String getYexh() {
		return yexh;
	}

	public void setYexh(String yexh) {
		this.yexh = yexh;
	}

	public List<MediaList> getMediaList() {
		return mediaList;
	}

	public void setMediaList(List<MediaList> mediaList) {
		this.mediaList = mediaList;
	}

}
