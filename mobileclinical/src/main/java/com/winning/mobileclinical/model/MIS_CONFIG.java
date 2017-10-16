package com.winning.mobileclinical.model;

import java.io.Serializable;

public class MIS_CONFIG implements Serializable {
	
	    private int xh;		//自增列，无实际意义
	    private String id;                      //关键字
	    private int  moduleid;					//模块标识号：
	    private String modulename;						//模块名称
	    private int functypeid;                       //模块功能类别ID
	    private String functypename;                       //模块功能类别名称
	    private int funcid;					//功能标识号：
	    private String funcname;						//功能名称
	    private String configvalue;						//设置值	
	    private String externalvalue;						//扩展值，用于辅助
	    private String  memo;
		public int getXh() {
			return xh;
		}
		public void setXh(int xh) {
			this.xh = xh;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public int getModuleid() {
			return moduleid;
		}
		public void setModuleid(int moduleid) {
			this.moduleid = moduleid;
		}
		public String getModulename() {
			return modulename;
		}
		public void setModulename(String modulename) {
			this.modulename = modulename;
		}
		public int getFunctypeid() {
			return functypeid;
		}
		public void setFunctypeid(int functypeid) {
			this.functypeid = functypeid;
		}
		public String getFunctypename() {
			return functypename;
		}
		public void setFunctypename(String functypename) {
			this.functypename = functypename;
		}
		public int getFuncid() {
			return funcid;
		}
		public void setFuncid(int funcid) {
			this.funcid = funcid;
		}
		public String getFuncname() {
			return funcname;
		}
		public void setFuncname(String funcname) {
			this.funcname = funcname;
		}
		public String getConfigvalue() {
			return configvalue;
		}
		public void setConfigvalue(String configvalue) {
			this.configvalue = configvalue;
		}
		public String getExternalvalue() {
			return externalvalue;
		}
		public void setExternalvalue(String externalvalue) {
			this.externalvalue = externalvalue;
		}
		public String getMemo() {
			return memo;
		}
		public void setMemo(String memo) {
			this.memo = memo;
		}
}
