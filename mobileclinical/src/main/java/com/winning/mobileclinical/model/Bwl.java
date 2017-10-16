package com.winning.mobileclinical.model;

import java.io.Serializable;
import java.text.ParseException;

import org.json.JSONException;
import org.json.JSONObject;

import com.winning.mobileclinical.web.TimeTool;


public class Bwl implements Comparable<Bwl>,Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6390637371938997280L;
	private String xh;
	private String title;
	private String contents;
	private String type;
	private String gxsj;
	private String cjsj;			
	private String cjhsid;
	private String cjhsname;
	private String jlzt;
	private String bq_id;
	private String bq_mc;
	private int save_type;			
	private String txsj;		
	private String glbr;			
	private String syxh;
	private String yexh;
	
	
	public Bwl(JSONObject bwl) {
		// TODO Auto-generated constructor stub
		try {
			this.xh = bwl.getString("xh");
			this.title = bwl.getString("title");
			this.contents = bwl.getString("contents");
			this.type = bwl.getString("type");
			this.gxsj = bwl.getString("gxsj");
			this.cjsj = bwl.getString("cjsj");
			this.cjhsid = bwl.getString("cjhsid");
			this.cjhsname = bwl.getString("cjhsname");
			this.jlzt = bwl.getString("jlzt");
			this.bq_id = bwl.getString("bq_id");
			this.bq_mc = bwl.getString("bq_mc");
			this.save_type = bwl.getInt("save_type");
			this.txsj = bwl.getString("txsj");
			this.glbr = bwl.getString("glbr");
			this.syxh = bwl.getString("syxh");
			this.yexh = bwl.getString("yexh");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Bwl() {
		// TODO Auto-generated constructor stub
	}


	public String getXh() {
		return xh;
	}


	public Bwl setXh(String xh) {
		this.xh = xh;
		return this;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getContents() {
		return contents;
	}


	public void setContents(String contents) {
		this.contents = contents;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getGxsj() {
		return gxsj;
	}


	public void setGxsj(String gxsj) {
		this.gxsj = gxsj;
	}


	public String getCjsj() {
		return cjsj;
	}


	public void setCjsj(String cjsj) {
		this.cjsj = cjsj;
	}


	public String getCjhsid() {
		return cjhsid;
	}


	public void setCjhsid(String cjhsid) {
		this.cjhsid = cjhsid;
	}


	public String getCjhsname() {
		return cjhsname;
	}


	public void setCjhsname(String cjhsname) {
		this.cjhsname = cjhsname;
	}


	public String getJlzt() {
		return jlzt;
	}


	public void setJlzt(String jlzt) {
		this.jlzt = jlzt;
	}


	public String getBq_id() {
		return bq_id;
	}


	public void setBq_id(String bq_id) {
		this.bq_id = bq_id;
	}


	public String getBq_mc() {
		return bq_mc;
	}


	public void setBq_mc(String bq_mc) {
		this.bq_mc = bq_mc;
	}


	public int getSave_type() {
		return save_type;
	}


	public Bwl setSave_type(int save_type) {
		this.save_type = save_type;
		return this;
	}

	public String getTxsj() {
		return txsj;
	}


	public void setTxsj(String txsj) {
		this.txsj = txsj;
	}


	public String getGlbr() {
		return glbr;
	}


	public void setGlbr(String glbr) {
		this.glbr = glbr;
	}


	public String getSyxh() {
		return syxh;
	}

	public void setSyxh(String syxh) {
		this.syxh = syxh;
	}

	public String getYexh() {
		return yexh;
	}

	public void setYexh(String yexh) {
		this.yexh = yexh;
	}

	@Override
	public int compareTo(Bwl bwl) {
		
		String createTime=bwl.getCjsj();
		if(createTime == null || this.cjsj == null)
			return 1;
		long time1=0;
		long time2=0;
		try {
			time1=TimeTool.formateTime(createTime, "yyyy-MM-dd").getTime();
			time2=TimeTool.formateTime(this.cjsj, "yyyy-MM-dd").getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return time2>time1?-1:1;
	}
	
}
