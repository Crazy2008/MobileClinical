package com.winning.mobileclinical.widget;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.widget.EditText;
import android.widget.ImageView;

public class MultiModel {

	private static MultiModel multi = null;
	
	private String xh = "";
	private String photoId = "";
	private String tuyaId = "";
	private String recordId = "";
	private String text = "";
	private String photoPath = "";
	private String tuyaPath = "";
	private String recordPath = "";
	private String title = "";
	private String photoFileName = "";
	private String tuyaFileName = "";
	private String recordFileName = "";
	private String oldTuyaFileName = "";
	private String oldPhotoFileName = "";
	private String oldRecordFileName = "";
	private Bitmap tuYaOld = null; //gutao
	private ArrayList<String> photoPathList = new ArrayList<String>();
	private ArrayList<String> photoFileNameList = new ArrayList<String>();
	
	private ArrayList<String> photoOldPathList = new ArrayList<String>();
	private ArrayList<String> photoOldFileNameList = new ArrayList<String>();
	
	private ArrayList<ImageView> photos = new ArrayList<ImageView>();
	private ArrayList<EditText> photoDes = new ArrayList<EditText>();	 	//ͼƬ��Ӧ˵���½�
	private ArrayList<EditText> photoDesDown = new ArrayList<EditText>();	//ͼƬ��Ӧ˵������
	private MultiModel () {
		
	}

	public static MultiModel getModel() {
		if(multi == null) {
			synchronized(MultiModel.class) {
				multi = new MultiModel();
			}
		}
		return multi;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getPhotoPath() {
		return photoPath;
	}

	public void setPhotoPath(String photoPath) {
		this.photoPath = photoPath;
	}

	public String getTuyaPath() {
		return tuyaPath;
	}

	public void setTuyaPath(String tuyaPath) {
		this.tuyaPath = tuyaPath;
	}

	public String getRecordPath() {
		return recordPath;
	}

	public void setRecordPath(String recordPath) {
		this.recordPath = recordPath;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	
	public String getPhotoFileName() {
		return photoFileName;
	}

	public void setPhotoFileName(String photoFileName) {
		this.photoFileName = photoFileName;
	}

	public String getTuyaFileName() {
		return tuyaFileName;
	}

	public void setTuyaFileName(String tuyaFileName) {
		this.tuyaFileName = tuyaFileName;
	}
	
	public String getRecordFileName() {
		return recordFileName;
	}

	public void setRecordFileName(String recordFileName) {
		this.recordFileName = recordFileName;
	}
	public String getOldTuyaFileName() {
		return oldTuyaFileName;
	}

	public void setOldTuyaFileName(String oldTuyaFileName) {
		this.oldTuyaFileName = oldTuyaFileName;
	}

	public String getOldPhotoFileName() {
		return oldPhotoFileName;
	}

	public void setOldPhotoFileName(String oldPhotoFileName) {
		this.oldPhotoFileName = oldPhotoFileName;
	}

	
	public String getOldRecordFileName() {
		return oldRecordFileName;
	}

	public void setOldRecordFileName(String oldRecordFileName) {
		this.oldRecordFileName = oldRecordFileName;
	}

	
	public String getXh() {
		return xh;
	}

	public void setXh(String xh) {
		this.xh = xh;
	}

	public String getPhotoId() {
		return photoId;
	}

	public void setPhotoId(String photoId) {
		this.photoId = photoId;
	}

	public String getTuyaId() {
		return tuyaId;
	}

	public void setTuyaId(String tuyaId) {
		this.tuyaId = tuyaId;
	}

	public String getRecordId() {
		return recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}
	
	

	public ArrayList<String> getPhotoPathList() {
		return photoPathList;
	}

	public void setPhotoPathList(ArrayList<String> photoPathList) {
		this.photoPathList = photoPathList;
	}

	
	public ArrayList<ImageView> getPhotos() {
		return photos;
	}

	public void setPhotos(ArrayList<ImageView> photos) {
		this.photos = photos;
	}

	
	public ArrayList<String> getPhotoFileNameList() {
		return photoFileNameList;
	}

	public void setPhotoFileNameList(ArrayList<String> photoFileNameList) {
		this.photoFileNameList = photoFileNameList;
	}

	
	public ArrayList<String> getPhotoOldPathList() {
		return photoOldPathList;
	}

	public void setPhotoOldPathList(ArrayList<String> photoOldPathList) {
		this.photoOldPathList = photoOldPathList;
	}

	public ArrayList<String> getPhotoOldFileNameList() {
		return photoOldFileNameList;
	}

	public void setPhotoOldFileNameList(ArrayList<String> photoOldFileNameList) {
		this.photoOldFileNameList = photoOldFileNameList;
	}

	
	public Bitmap getTuYaOld() {
		return tuYaOld;
	}

	public void setTuYaOld(Bitmap tuYaOld) {
		this.tuYaOld = tuYaOld;
	}

	
	public ArrayList<EditText> getPhotoDes() {
		return photoDes;
	}

	public void setPhotoDes(ArrayList<EditText> photoDes) {
		this.photoDes = photoDes;
	}

	public void setPhotoDesDown(ArrayList<EditText> photoDesDown) {
		this.photoDesDown = photoDesDown;
	}

	public ArrayList<EditText> getPhotoDesDown() {
		return photoDesDown;
	}
	
	public void clearData() {
		text = "";
		photoPath = "";
		title = "";
		recordPath = "";
		tuyaPath = "";
		photoFileName = "";
		tuyaFileName = "";
		recordFileName = "";
		oldPhotoFileName = "";
		oldTuyaFileName = "";
		oldRecordFileName = "";
		xh = "";
		recordId = "";
		tuyaId = "";
		photoId = "";
		photoPathList = null;
		photoPathList = new ArrayList<String>();
		photoFileNameList = null;
		photoFileNameList = new ArrayList<String>();
		photos = null;
		photos = new ArrayList<ImageView>();
		photoDes = null;
		photoDes = new ArrayList<EditText>();
		photoDesDown = null;
		photoDesDown = new ArrayList<EditText>();
		tuYaOld = null;
	}

	
}
