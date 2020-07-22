package com.accp.common.constant;

/**
 * 调度任务常量信息
 * 
 * 
 * 
 * 
 *
 *
 */
public class JobConstants {
	
    /** 调度任务在JOB中相关参数定义 */
    public final static String JOB_GROUPNAME_FOR_TASKSCHEDULING = "自动化任务调度";
    public final static String JOB_JOBNAME_FOR_TASKSCHEDULING = "runAutomationTestTask";
    public final static String JOB_METHODNAME_FOR_TASKSCHEDULING = "runTask";
    
    /** 客户端监听在JOB中相关参数定义 */
    public final static String JOB_GROUPNAME_FOR_CLIENTHEART = "客户端心跳监听";
    public final static String JOB_JOBNAME_FOR_CLIENTHEART = "clientHeart";
    public final static String JOB_METHODNAME_FOR_CLIENTHEART = "heartTask";

    /** JOB状态 */
    public final static String JOB_STATUS_FOR_RUN = "0";
    public final static String JOB_STATUS_FOR_PAUSE = "1";
}
