package com.bst.bean;

import java.io.Serializable;
import java.util.Date;

public class CheckingBean implements Serializable{

	private String id;
	private String uuid;
	private String userId;
	private String checking;
	private String checkingtime;
	private String photo;
	private String code;
	private String executor;
	private String precision;
	private String dimensionality;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getChecking() {
		return checking;
	}
	public void setChecking(String checking) {
		this.checking = checking;
	}

	public String getCheckingtime() {
		return checkingtime;
	}

	public void setCheckingtime(String checkingtime) {
		this.checkingtime = checkingtime;
	}

	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getExecutor() {
		return executor;
	}
	public void setExecutor(String executor) {
		this.executor = executor;
	}
	public String getPrecision() {
		return precision;
	}
	public void setPrecision(String precision) {
		this.precision = precision;
	}
	public String getDimensionality() {
		return dimensionality;
	}
	public void setDimensionality(String dimensionality) {
		this.dimensionality = dimensionality;
	}
	
	
	
}
