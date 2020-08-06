package com.accp.utils;

/**
 * 通用常量定义
 * 
 * 
 * 
 * 
 *
 * 
 */
public class Constants {	
	/******************预期结果常量定义*************************
	 * 预期结果赋值符---赋值单条用例作用域变量
	 */	
    public static final String ASSIGNMENT_SIGN = "$=";
	/**
	 * 预期结果赋值符---赋值测试任务作用域变量
	 */	
    public static final String ASSIGNMENT_GLOBALSIGN = "$A=";
	/**
	 * 预期结果匹配符---模糊匹配
	 */	
    public static final String FUZZY_MATCHING_SIGN = "%=";
	/**
	 * 预期结果匹配符---正则匹配
	 */	
    public static final String REGULAR_MATCHING_SIGN = "~=";
	/**
	 * 预期结果匹配符---json匹配
	 */	
    public static final String JSONPATH_SIGN = "$JP#";

    
	/************************HTTP 返回结果头域 响应码分隔符定义***************************
	 * HTTP测试返回结果 是否接收头域
	 */	
    public static final String RESPONSE_HEAD = "RESPONSE_HEAD:【";
	/**
	 * HTTP测试返回结果 是否接收响应码
	 */	
    public static final String RESPONSE_CODE = "RESPONSE_CODE:【";    
	/**
	 * HTTP测试返回结果 尾部链接符
	 */	
    public static final String RESPONSE_END = "】 ";
}
