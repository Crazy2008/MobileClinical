/**
 * AccountDTO.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */
package com.winning.mobileclinical.model.cis;


public class DoctorInfo  {
	 /// <summary>
    /// 医生代码
    /// </summary>
    public String id;
    /// <summary>
    /// 医生名称
    /// </summary>
    public String name;
    /// <summary>
    /// 拼音
    /// </summary>
    public String py;
    /// <summary>
    /// 五笔
    /// </summary>
    public String wb;
    /// <summary>
    /// 性别
    /// </summary>
    public String sex;
    /// <summary>
    /// 科室代码
    /// </summary>
    public String ksdm;
    /// <summary>
    /// 病区代码
    /// </summary>
    public String bqdm;
    /// <summary>
    /// 职工类别 0: 其它 1: 医生 2: 护士
    /// </summary>
    public String zglb;
    /// <summary>
    /// 是否允许登录
    /// </summary>
    public Boolean yxdl;
    /// <summary>
    /// 职称代码
    /// </summary>
    public String zcdm;
    /// <summary>
    /// 职称名称
    /// </summary>
    public String zcmc;
    /// <summary>
    /// 联系电话
    /// </summary>
    public String phone;
    /// <summary>
    /// 记录状态
    /// </summary>
    public int jlzt;
	public String password;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String  hospital_id;
    
	public String  hospital_name;
    
    public String getHospital_id() {
		return hospital_id;
	}
	public void setHospital_id(String hospital_id) {
		this.hospital_id = hospital_id;
	}
	public String getHospital_name() {
		return hospital_name;
	}
	public void setHospital_name(String hospital_name) {
		this.hospital_name = hospital_name;
	}
    
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
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
	public String getZglb() {
		return zglb;
	}
	public void setZglb(String zglb) {
		this.zglb = zglb;
	}
	public Boolean getYxdl() {
		return yxdl;
	}
	public void setYxdl(Boolean yxdl) {
		this.yxdl = yxdl;
	}
	public String getZcdm() {
		return zcdm;
	}
	public void setZcdm(String zcdm) {
		this.zcdm = zcdm;
	}
	public String getZcmc() {
		return zcmc;
	}
	public void setZcmc(String zcmc) {
		this.zcmc = zcmc;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public int getJlzt() {
		return jlzt;
	}
	public void setJlzt(int jlzt) {
		this.jlzt = jlzt;
	}

	@Override
	public String toString() {
		return "DoctorInfo{" +
				"id='" + id + '\'' +
				", name='" + name + '\'' +
				", py='" + py + '\'' +
				", wb='" + wb + '\'' +
				", sex='" + sex + '\'' +
				", ksdm='" + ksdm + '\'' +
				", bqdm='" + bqdm + '\'' +
				", zglb='" + zglb + '\'' +
				", yxdl=" + yxdl +
				", zcdm='" + zcdm + '\'' +
				", zcmc='" + zcmc + '\'' +
				", phone='" + phone + '\'' +
				", jlzt=" + jlzt +
				", password='" + password + '\'' +
				", hospital_id='" + hospital_id + '\'' +
				", hospital_name='" + hospital_name + '\'' +
				'}';
	}
}
