package com.winning.mobileclinical.model.cis;


import java.math.BigDecimal;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;



public class OrderInfo {/// <summary>
    /// 首页序号
    /// </summary>
    public BigDecimal syxh;
    /// <summary>
    /// 婴儿序号
    /// </summary>
    public BigDecimal yexh;
    /// <summary>
    /// 开嘱科室代码
    /// </summary>
    public String ksdm;
    /// <summary>
    /// 开嘱科室名称
    /// </summary>
    public String ksmc;
    /// <summary>
    /// 开嘱病区代码
    /// </summary>
    public String bqdm;
    /// <summary>
    /// 开嘱病区名称
    /// </summary>
    public String bqmc;
    /// <summary>
    /// 开嘱医生
    /// </summary>
    public String ysdm;
    /// <summary>
    /// 开嘱医生名称
    /// </summary>
    public String ysmc;
    /// <summary>
    /// 录入日期
    /// </summary>
    public String lrrq;
    /// <summary>
    /// 开始日期
    /// </summary>
    public String kssj;
    /// <summary>
    /// 医嘱序号
    /// </summary>
    public BigDecimal xh;
    /// <summary>
    /// 分组序号
    /// </summary>
    public BigDecimal fzxh;
    /// <summary>
    /// 医嘱标志1:临时 0 长期
    /// </summary>
    public int yzbz;
    /// <summary>
    /// 上级序号(申请单)
    /// </summary>
    public BigDecimal sjxh;
    /// <summary>
    /// 药品idm
    /// </summary>
    public BigDecimal idm;
    /// <summary>
    /// 药品代码
    /// </summary>
    public String ypdm;
    /// <summary>
    /// 临床项目代码
    /// </summary>
    public String lcxmdm;
    /// <summary>
    /// 药品代码
    /// </summary>
    public String ypmc;
    /// <summary>
    /// 药品规格
    /// </summary>
    public String ypgg;
    /// <summary>
    /// 收费大项目代码
    /// </summary>
    public String dxmdm;
    /// <summary>
    /// 医嘱类别 医嘱类别(0药品，1治疗，2手术，3膳食，4输血，5护理，6检查，7检验，8输液，9停止医嘱，10转科，11出院，12出院带药，13转床,14术后医嘱，15产后医嘱)
    /// </summary>
    public int yzlb;
    /// <summary>
    /// 频次代码
    /// </summary>
    public String pcdm;
    /// <summary>
    /// 频次名称
    /// </summary>
    public String pcmc;
    /// <summary>
    /// 频次执行时间
    /// </summary>
    public String pczxsj;
    /// <summary>
    /// 药品用法
    /// </summary>
    public String ypyf;
    /// <summary>
    /// 药品用法名称
    /// </summary>
    public String ypyfmc;
    /// <summary>
    /// 药品剂量
    /// </summary>
    public float ypjl;
    /// <summary>
    /// 剂量单位
    /// </summary>
    public String jldw;
    /// <summary>
    /// 剂量单位名称
    /// </summary>
    public int dwlb;
    /// <summary>
    /// 药品数量
    /// </summary>
    public float ypsl;
    /// <summary>
    /// 执行单位
    /// </summary>
    public String zxdw;
    /// <summary>
    /// 医嘱内容
    /// </summary>
    public String yznr;
    /// <summary>
    /// 执行科室/药房
    /// </summary>
    public String zxksdm;
    /// <summary>
    /// 执行科室/药房名称
    /// </summary>
    public String zxksmc;
    /// <summary>
    /// 审核操作员
    /// </summary>
    public String shczyh;
    /// <summary>
    /// 审核操作员名称
    /// </summary>
    public String shczymc;
    /// <summary>
    /// 审核时间
    /// </summary>
    public String shsj;
    /// <summary>
    /// 执行操作员
    /// </summary>
    public String zxczyh;
    /// <summary>
    /// 执行操作员名称
    /// </summary>
    public String zxczymc;
    /// <summary>
    /// 执行操作员签名(base64图片)
    /// </summary>
    public String zxczyqm;
    /// <summary>
    /// 执行时间
    /// </summary>
    public String zxsj;
    /// <summary>
    /// 停止时间
    /// </summary>
    public String tzsj;
    /// <summary>
    /// 停止操作员
    /// </summary>
    public String tzczyh;
    /// <summary>
    /// 停止操作员名称
    /// </summary>
    public String tzczymc;
    /// <summary>
    /// DC操作员
    /// </summary>
    public String dcczyh;
    /// <summary>
    /// DC操作员名称
    /// </summary>
    public String dcczymc;
    /// <summary>
    /// DC时间
    /// </summary>
    public String dcsj;
    /// <summary>
    /// 开嘱医生签名(base64图片)
    /// </summary>
    public String ysqm;
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
	public String getKsmc() {
		return ksmc;
	}
	public void setKsmc(String ksmc) {
		this.ksmc = ksmc;
	}
	public String getBqdm() {
		return bqdm;
	}
	public void setBqdm(String bqdm) {
		this.bqdm = bqdm;
	}
	public String getBqmc() {
		return bqmc;
	}
	public void setBqmc(String bqmc) {
		this.bqmc = bqmc;
	}
	public String getYsdm() {
		return ysdm;
	}
	public void setYsdm(String ysdm) {
		this.ysdm = ysdm;
	}
	public String getYsmc() {
		return ysmc;
	}
	public void setYsmc(String ysmc) {
		this.ysmc = ysmc;
	}
	public String getLrrq() {
		return lrrq;
	}
	public void setLrrq(String lrrq) {
		this.lrrq = lrrq;
	}
	public String getKssj() {
		return kssj;
	}
	public void setKssj(String kssj) {
		this.kssj = kssj;
	}
	public BigDecimal getXh() {
		return xh;
	}
	public void setXh(BigDecimal xh) {
		this.xh = xh;
	}
	public BigDecimal getFzxh() {
		return fzxh;
	}
	public void setFzxh(BigDecimal fzxh) {
		this.fzxh = fzxh;
	}
	public int getYzbz() {
		return yzbz;
	}
	public void setYzbz(int yzbz) {
		this.yzbz = yzbz;
	}
	public BigDecimal getSjxh() {
		return sjxh;
	}
	public void setSjxh(BigDecimal sjxh) {
		this.sjxh = sjxh;
	}
	public BigDecimal getIdm() {
		return idm;
	}
	public void setIdm(BigDecimal idm) {
		this.idm = idm;
	}
	public String getYpdm() {
		return ypdm;
	}
	public void setYpdm(String ypdm) {
		this.ypdm = ypdm;
	}
	public String getLcxmdm() {
		return lcxmdm;
	}
	public void setLcxmdm(String lcxmdm) {
		this.lcxmdm = lcxmdm;
	}
	public String getYpmc() {
		return ypmc;
	}
	public void setYpmc(String ypmc) {
		this.ypmc = ypmc;
	}
	public String getYpgg() {
		return ypgg;
	}
	public void setYpgg(String ypgg) {
		this.ypgg = ypgg;
	}
	public String getDxmdm() {
		return dxmdm;
	}
	public void setDxmdm(String dxmdm) {
		this.dxmdm = dxmdm;
	}
	public int getYzlb() {
		return yzlb;
	}
	public void setYzlb(int yzlb) {
		this.yzlb = yzlb;
	}
	public String getPcdm() {
		return pcdm;
	}
	public void setPcdm(String pcdm) {
		this.pcdm = pcdm;
	}
	public String getPcmc() {
		return pcmc;
	}
	public void setPcmc(String pcmc) {
		this.pcmc = pcmc;
	}
	public String getPczxsj() {
		return pczxsj;
	}
	public void setPczxsj(String pczxsj) {
		this.pczxsj = pczxsj;
	}
	public String getYpyf() {
		return ypyf;
	}
	public void setYpyf(String ypyf) {
		this.ypyf = ypyf;
	}
	public String getYpyfmc() {
		return ypyfmc;
	}
	public void setYpyfmc(String ypyfmc) {
		this.ypyfmc = ypyfmc;
	}
	public float getYpjl() {
		return ypjl;
	}
	public void setYpjl(float ypjl) {
		this.ypjl = ypjl;
	}
	public String getJldw() {
		return jldw;
	}
	public void setJldw(String jldw) {
		this.jldw = jldw;
	}
	public int getDwlb() {
		return dwlb;
	}
	public void setDwlb(int dwlb) {
		this.dwlb = dwlb;
	}
	public float getYpsl() {
		return ypsl;
	}
	public void setYpsl(float ypsl) {
		this.ypsl = ypsl;
	}
	public String getZxdw() {
		return zxdw;
	}
	public void setZxdw(String zxdw) {
		this.zxdw = zxdw;
	}
	public String getYznr() {
		return yznr;
	}
	public void setYznr(String yznr) {
		this.yznr = yznr;
	}
	public String getZxksdm() {
		return zxksdm;
	}
	public void setZxksdm(String zxksdm) {
		this.zxksdm = zxksdm;
	}
	public String getZxksmc() {
		return zxksmc;
	}
	public void setZxksmc(String zxksmc) {
		this.zxksmc = zxksmc;
	}
	public String getShczyh() {
		return shczyh;
	}
	public void setShczyh(String shczyh) {
		this.shczyh = shczyh;
	}
	public String getShczymc() {
		return shczymc;
	}
	public void setShczymc(String shczymc) {
		this.shczymc = shczymc;
	}
	public String getShsj() {
		return shsj;
	}
	public void setShsj(String shsj) {
		this.shsj = shsj;
	}
	public String getZxczyh() {
		return zxczyh;
	}
	public void setZxczyh(String zxczyh) {
		this.zxczyh = zxczyh;
	}
	public String getZxczymc() {
		return zxczymc;
	}
	public void setZxczymc(String zxczymc) {
		this.zxczymc = zxczymc;
	}
	public String getZxczyqm() {
		return zxczyqm;
	}
	public void setZxczyqm(String zxczyqm) {
		this.zxczyqm = zxczyqm;
	}
	public String getZxsj() {
		return zxsj;
	}
	public void setZxsj(String zxsj) {
		this.zxsj = zxsj;
	}
	public String getTzsj() {
		return tzsj;
	}
	public void setTzsj(String tzsj) {
		this.tzsj = tzsj;
	}
	public String getTzczyh() {
		return tzczyh;
	}
	public void setTzczyh(String tzczyh) {
		this.tzczyh = tzczyh;
	}
	public String getTzczymc() {
		return tzczymc;
	}
	public void setTzczymc(String tzczymc) {
		this.tzczymc = tzczymc;
	}
	public String getDcczyh() {
		return dcczyh;
	}
	public void setDcczyh(String dcczyh) {
		this.dcczyh = dcczyh;
	}
	public String getDcczymc() {
		return dcczymc;
	}
	public void setDcczymc(String dcczymc) {
		this.dcczymc = dcczymc;
	}
	public String getDcsj() {
		return dcsj;
	}
	public void setDcsj(String dcsj) {
		this.dcsj = dcsj;
	}
	public String getYsqm() {
		return ysqm;
	}
	public void setYsqm(String ysqm) {
		this.ysqm = ysqm;
	}
    
}
