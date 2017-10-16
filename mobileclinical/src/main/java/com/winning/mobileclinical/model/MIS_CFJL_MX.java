package com.winning.mobileclinical.model;

public class MIS_CFJL_MX {
	private String  cfxh;//查房序号MIS_CFJL.XH
	private String  syxh;	 //首页序号
	private int  flag;	 //明细类别 0 图片  1 音频  2 视频
	private String  cjrq;    //创建日期
	public String getCfxh() {
		return cfxh;
	}
	public void setCfxh(String cfxh) {
		this.cfxh = cfxh;
	}
	public String getSyxh() {
		return syxh;
	}
	public void setSyxh(String syxh) {
		this.syxh = syxh;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public String getCjrq() {
		return cjrq;
	}
	public void setCjrq(String cjrq) {
		this.cjrq = cjrq;
	}
	
	
}
