package com.winning.mobileclinical.model.cis;


import java.math.BigDecimal;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;



public class PatientInfo {/// <summary>
    /// 病历首页序号
    /// </summary>
    public BigDecimal emrxh;
    /// <summary>
    /// his首页序号
    /// </summary>
    public BigDecimal syxh;
    /// <summary>
    /// 婴儿序号
    /// </summary>
    public BigDecimal yexh;
    /// <summary>
    /// 病历号
    /// </summary>
    public String blh;
    /// <summary>
    /// 患者信息
    /// </summary>
    public String name;
    /// <summary>
    /// 性别
    /// </summary>
    public String sex;
    /// <summary>
    /// 显示年龄
    /// </summary>
    public String age;
    /// <summary>
    /// 生日
    /// </summary>
    public String birth;
    /// <summary>
    /// 拼音
    /// </summary>
    public String py;
    /// <summary>
    /// 五笔
    /// </summary>
    public String wb;
    /// <summary>
    /// 身份证号
    /// </summary>
    public String sfzh;
    /// <summary>
    /// 病人状态(	0：入院登记	1：病区分床 2：病区出院 3：病人出院 4：取消结算 5：进入ICU 6：进入产房 7：转科状态 8：数据转出 9：作废记录
    /// </summary>
    public int brzt;
    /// <summary>
    /// 科室代码
    /// </summary>
    public String ksdm;
    /// <summary>
    /// 科室名称
    /// </summary>
    public String ksmc;
    /// <summary>
    /// 病区代码
    /// </summary>
    public String bqdm;
    /// <summary>
    /// 病区名称
    /// </summary>
    public String bqmc;
    /// <summary>
    /// 床位代码
    /// </summary>
    public String cwdm;
    /// <summary>
    /// 护理代码
    /// </summary>
    public String hldm;
    /// <summary>
    /// 护理名称
    /// </summary>
    public String hlmc;
    /// <summary>
    /// 入院日期
    /// </summary>
    public String ryrq;
    /// <summary>
    /// 入区日期
    /// </summary>
    public String rqrq;
    /// <summary>
    /// 出区日期
    /// </summary>
    public String cqrq;
    /// <summary>
    /// 出院日期
    /// </summary>
    public String cyrq;
    /// <summary>
    /// 危重级别
    /// </summary>
    public String wzjb;
    /// <summary>
    /// 入院诊断代码
    /// </summary>
    public String zddm;
    /// <summary>
    /// 入院诊断名称
    /// </summary>
    public String zdmc;
    /// <summary>
    /// 出院方式
    /// </summary>
    public String cyfs;
    /// <summary>
    /// 急观标志((0：住院1：在观2：出观))
    /// </summary>
    public int jgbz;
    /// <summary>
    /// 医保代码
    /// </summary>
    public String ybdm;
    /// <summary>
    /// 医保说明
    /// </summary>
    public String ybsm;
    /// <summary>
    /// 凭证类型
    /// </summary>
    public int pzlx;
    /// <summary>
    /// 病人类型
    /// </summary>
    public String brlx;
    /// <summary>
    /// 凭证号
    /// </summary>
    public String pzh;
    /// <summary>
    /// 卡号
    /// </summary>
    public String cardno;
    /// <summary>
    /// 卡类型
    /// </summary>
    public String cardtype;
    /// <summary>
    /// 出院诊断代码
    /// </summary>
    public String cyzddm;
    /// <summary>
    /// 出院诊断名称
    /// </summary>
    public String cyzdmc;
    /// <summary>
    /// 临床路径标志
    /// </summary>
    public int lcljbz;
    /// <summary>
    /// 病历归档标志
    /// </summary>
    public int blgdbz;
    /// <summary>
    /// 主治医生代码
    /// </summary>
    public String zzysdm;
    /// <summary>
    /// 主治医生名称
    /// </summary>
    public String zzysmc;
	public BigDecimal getEmrxh() {
		return emrxh;
	}
	public void setEmrxh(BigDecimal emrxh) {
		this.emrxh = emrxh;
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
	public String getBlh() {
		return blh;
	}
	public void setBlh(String blh) {
		this.blh = blh;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getBirth() {
		return birth;
	}
	public void setBirth(String birth) {
		this.birth = birth;
	}
	public String getPy() {
		return py;
	}
	public void setPy(String py) {
		this.py = py;
	}
	public String getWb() {
		return wb;
	}
	public void setWb(String wb) {
		this.wb = wb;
	}
	public String getSfzh() {
		return sfzh;
	}
	public void setSfzh(String sfzh) {
		this.sfzh = sfzh;
	}
	public int getBrzt() {
		return brzt;
	}
	public void setBrzt(int brzt) {
		this.brzt = brzt;
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
	public String getCwdm() {
		return cwdm;
	}
	public void setCwdm(String cwdm) {
		this.cwdm = cwdm;
	}
	public String getHldm() {
		return hldm;
	}
	public void setHldm(String hldm) {
		this.hldm = hldm;
	}
	public String getHlmc() {
		return hlmc;
	}
	public void setHlmc(String hlmc) {
		this.hlmc = hlmc;
	}
	public String getRyrq() {
		return ryrq;
	}
	public void setRyrq(String ryrq) {
		this.ryrq = ryrq;
	}
	public String getRqrq() {
		return rqrq;
	}
	public void setRqrq(String rqrq) {
		this.rqrq = rqrq;
	}
	public String getCqrq() {
		return cqrq;
	}
	public void setCqrq(String cqrq) {
		this.cqrq = cqrq;
	}
	public String getCyrq() {
		return cyrq;
	}
	public void setCyrq(String cyrq) {
		this.cyrq = cyrq;
	}
	public String getWzjb() {
		return wzjb;
	}
	public void setWzjb(String wzjb) {
		this.wzjb = wzjb;
	}
	public String getZddm() {
		return zddm;
	}
	public void setZddm(String zddm) {
		this.zddm = zddm;
	}
	public String getZdmc() {
		return zdmc;
	}
	public void setZdmc(String zdmc) {
		this.zdmc = zdmc;
	}
	public String getCyfs() {
		return cyfs;
	}
	public void setCyfs(String cyfs) {
		this.cyfs = cyfs;
	}
	public int getJgbz() {
		return jgbz;
	}
	public void setJgbz(int jgbz) {
		this.jgbz = jgbz;
	}
	public String getYbdm() {
		return ybdm;
	}
	public void setYbdm(String ybdm) {
		this.ybdm = ybdm;
	}
	public String getYbsm() {
		return ybsm;
	}
	public void setYbsm(String ybsm) {
		this.ybsm = ybsm;
	}
	public int getPzlx() {
		return pzlx;
	}
	public void setPzlx(int pzlx) {
		this.pzlx = pzlx;
	}
	public String getBrlx() {
		return brlx;
	}
	public void setBrlx(String brlx) {
		this.brlx = brlx;
	}
	public String getPzh() {
		return pzh;
	}
	public void setPzh(String pzh) {
		this.pzh = pzh;
	}
	public String getCardno() {
		return cardno;
	}
	public void setCardno(String cardno) {
		this.cardno = cardno;
	}
	public String getCardtype() {
		return cardtype;
	}
	public void setCardtype(String cardtype) {
		this.cardtype = cardtype;
	}
	public String getCyzddm() {
		return cyzddm;
	}
	public void setCyzddm(String cyzddm) {
		this.cyzddm = cyzddm;
	}
	public String getCyzdmc() {
		return cyzdmc;
	}
	public void setCyzdmc(String cyzdmc) {
		this.cyzdmc = cyzdmc;
	}
	public int getLcljbz() {
		return lcljbz;
	}
	public void setLcljbz(int lcljbz) {
		this.lcljbz = lcljbz;
	}
	public int getBlgdbz() {
		return blgdbz;
	}
	public void setBlgdbz(int blgdbz) {
		this.blgdbz = blgdbz;
	}
	public String getZzysdm() {
		return zzysdm;
	}
	public void setZzysdm(String zzysdm) {
		this.zzysdm = zzysdm;
	}
	public String getZzysmc() {
		return zzysmc;
	}
	public void setZzysmc(String zzysmc) {
		this.zzysmc = zzysmc;
	}
    
}
