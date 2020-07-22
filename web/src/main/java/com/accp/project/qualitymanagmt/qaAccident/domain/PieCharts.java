package com.accp.project.qualitymanagmt.qaAccident.domain;

import java.io.Serializable;

/**
 * 
 * 
 * 
 * 
 * 
 *
 *
 */
public class PieCharts implements Serializable{

	private static final long serialVersionUID = 1L;
	/*图形分类名称*/
	private String name;
	/*图形分类值*/
	private double value;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
}
