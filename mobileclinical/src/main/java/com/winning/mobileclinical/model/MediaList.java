package com.winning.mobileclinical.model;

public class MediaList {
	private int id;
	private int  xh;  //序号
	private String  name;	 //首页序号
	private int  flag;	 //明细类别 0 图片  1 音频  2 视频
	private String  url;    //创建日期
	private String cfsj;   //查房时间

	public int getXh() {
		return xh;
	}
	public void setXh(int xh) {
		this.xh = xh;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getCfsj() {
		return cfsj;
	}
	public void setCfsj(String cfsj) {
		this.cfsj = cfsj;
	}
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}


}
