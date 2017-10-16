package com.winning.mobileclinical.model.cis;

public class Bedinfo { 
    /// <summary>
    /// 床位代码
    /// </summary>
    public String cwdm;
    /// <summary>
    /// 病区代码
    /// </summary>
    public String bqdm;
    /// <summary>
    /// 病区名称
    /// </summary>
    public String bqmc;
    /// <summary>
    /// 房间号
    /// </summary>
    public String room;
    /// <summary>
    /// 科室代码
    /// </summary>
    public String ksdm;
    /// <summary>
    /// 科室名称
    /// </summary>
    public String ksmc;
    /// <summary>
    /// 床位费代码
    /// </summary>
    public String cwfdm;
    /// <summary>
    /// 公费床位费代码
    /// </summary>
    public String gfcwfdm;
    /// <summary>
    /// 住院医师代码
    /// </summary>
    public String zyysdm;
    /// <summary>
    /// 主治医师代码
    /// </summary>
    public String zzysdm;
    /// <summary>
    /// 主任医师代码
    /// </summary>
    public String zrysdm;
    /// <summary>
    /// 床位类型(0：男，1：女，2：混)
    /// </summary>
    public int cwlx;
    /// <summary>
    /// 编制类型(0：在编，1：非编，2：加床)
    /// </summary>
    public int bzlx;
    /// <summary>
    /// 占床标志(0空床，1占床，2包床)
    /// </summary>
    public int zcbz;
    /// <summary>
    /// 特需标志(0普通，1特需)
    /// </summary>
    public int txbz;

    /// <summary>
    /// 
    /// </summary>
    public int qyid;
    /// <summary>
    /// 
    /// </summary>
    public String qymc;
	public String getCwdm() {
		return cwdm;
	}
	public void setCwdm(String cwdm) {
		this.cwdm = cwdm;
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
	public String getRoom() {
		return room;
	}
	public void setRoom(String room) {
		this.room = room;
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
	public String getCwfdm() {
		return cwfdm;
	}
	public void setCwfdm(String cwfdm) {
		this.cwfdm = cwfdm;
	}
	public String getGfcwfdm() {
		return gfcwfdm;
	}
	public void setGfcwfdm(String gfcwfdm) {
		this.gfcwfdm = gfcwfdm;
	}
	public String getZyysdm() {
		return zyysdm;
	}
	public void setZyysdm(String zyysdm) {
		this.zyysdm = zyysdm;
	}
	public String getZzysdm() {
		return zzysdm;
	}
	public void setZzysdm(String zzysdm) {
		this.zzysdm = zzysdm;
	}
	public String getZrysdm() {
		return zrysdm;
	}
	public void setZrysdm(String zrysdm) {
		this.zrysdm = zrysdm;
	}
	public int getCwlx() {
		return cwlx;
	}
	public void setCwlx(int cwlx) {
		this.cwlx = cwlx;
	}
	public int getBzlx() {
		return bzlx;
	}
	public void setBzlx(int bzlx) {
		this.bzlx = bzlx;
	}
	public int getZcbz() {
		return zcbz;
	}
	public void setZcbz(int zcbz) {
		this.zcbz = zcbz;
	}
	public int getTxbz() {
		return txbz;
	}
	public void setTxbz(int txbz) {
		this.txbz = txbz;
	}
	public int getQyid() {
		return qyid;
	}
	public void setQyid(int qyid) {
		this.qyid = qyid;
	}
	public String getQymc() {
		return qymc;
	}
	public void setQymc(String qymc) {
		this.qymc = qymc;
	}
}
