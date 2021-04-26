package com.accp.remote.api;

import com.accp.remote.entity.ProjectCaseDebug;
import com.accp.remote.entity.TaskCaseLog;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.accp.remote.entity.TaskCaseExecute;
import com.accp.utils.httputils.HttpRequest;


/**
 * 
 *
 *
 *
 *
 *
 *
 */
public class PostServerApi {
	
	private static final String PREFIX = "/openPostApi";

	/**
	 * put web��������ݵ������
	 * @param userId �û�ID
	 * @param caseId ����ID
	 * @param logLevel ��־����
	 * @param logDetail ��־��ϸ
	 * @param debugIsend ������־
	 */
	public static void cPostDebugLog(Integer userId, Integer caseId, String logLevel, String logDetail,Integer debugIsend){
		ProjectCaseDebug projectCaseDebug = new ProjectCaseDebug();
		projectCaseDebug.setCaseId(caseId);
		projectCaseDebug.setUserId(userId);
		projectCaseDebug.setLogLevel(logLevel);
		projectCaseDebug.setLogDetail(logDetail);
		projectCaseDebug.setDebugIsend(debugIsend);
		
		HttpRequest.httpClientPostJson(PREFIX+"/clientPostCaseDebugLog", JSONObject.toJSONString(projectCaseDebug));
	}

	/**
	 * ��������ִ����ϸ�����ݿ�
	 * @param taskId ����ID
	 * @param projectId ��ĿID
	 * @param caseId ����ID
	 * @param caseSign �������
	 * @param caseName ��������
	 * @param caseStatus ����״̬
	 *
	 *
	 */
	public static void clientPostInsertTaskCaseExecute(Integer taskId, Integer projectId, Integer caseId, String caseSign, String caseName, Integer caseStatus){
		TaskCaseExecute taskCaseExecute = new TaskCaseExecute();
		taskCaseExecute.setTaskId(taskId);
		taskCaseExecute.setProjectId(projectId);
		taskCaseExecute.setCaseId(caseId);
		taskCaseExecute.setCaseSign(caseSign);
		taskCaseExecute.setCaseName(caseName);
		taskCaseExecute.setCaseStatus(caseStatus);
		
		HttpRequest.httpClientPostJson(PREFIX+"/clientPostTaskCaseExecute", JSONObject.toJSONString(taskCaseExecute));
	}
	
	/**
	 * �޸�����ִ��״̬
	 * @param taskId ����ID
	 * @param caseId ����ID
	 * @param caseStatus ����״̬
	 *
	 *
	 */
	public static void clientUpdateTaskCaseExecuteStatus(Integer taskId, Integer caseId, Integer caseStatus){
		TaskCaseExecute taskCaseExecute = new TaskCaseExecute();
		taskCaseExecute.setTaskId(taskId);
		taskCaseExecute.setCaseId(caseId);
		taskCaseExecute.setCaseStatus(caseStatus);
		
		HttpRequest.httpClientPostJson(PREFIX+"/clientUpdateTaskCaseExecuteStatus", JSONObject.toJSONString(taskCaseExecute));
	}
	
	/**
	 * ��������ִ����ϸ�����ݿ�
	 * @param taskId ����ID
	 * @param caseId ����ID
	 * @param logDetail ��־��ϸ
	 * @param logGrade ��־�ȼ�
	 * @param logStep ��־��Ӧ����
	 * @param imgname ��ͼ����
	 *
	 *
	 */
	public static void clientPostInsertTaskCaseLog(Integer taskId, Integer caseId, String logDetail, String logGrade, String logStep,
			String imgname){
		TaskCaseLog taskCaseLog = new TaskCaseLog();
		taskCaseLog.setTaskId(taskId);
		taskCaseLog.setCaseId(caseId);
		taskCaseLog.setLogDetail(logDetail);
		taskCaseLog.setLogGrade(logGrade);
		taskCaseLog.setLogStep(logStep);
		taskCaseLog.setImgname(imgname);
		
		HttpRequest.httpClientPostJson(PREFIX+"/clientPostTaskCaseLog", JSONObject.toJSONString(taskCaseLog));
	}

	/**
	 * ��������ִ������
	 * @param taskId ����ID
	 * @param caseCount ��������
	 * @param taskStatus ����״̬
	 * @return ���½��
	 */
	public static String clientUpdateTaskExecuteData(Integer taskId, Integer caseCount, Integer taskStatus){
		String str = "{\"taskId\":"+taskId+",\"caseCount\":"+caseCount+",\"taskStatus\":"+taskStatus+"}";
		JSONObject jsonObject = JSON.parseObject(str);
		return HttpRequest.httpClientPostJson(PREFIX+"/clientUpdateTaskExecuteData", jsonObject.toJSONString());
	}

	/**
	 * ɾ������ִ����־
	 * @param taskId ����ID
	 * @param caseId ����ID
	 */
	public static void clientDeleteTaskCaseLog(Integer taskId, Integer caseId){
		String str = "{\"taskId\":"+taskId+",\"caseId\":"+caseId+"}";
		JSONObject jsonObject = JSON.parseObject(str);
		HttpRequest.httpClientPostJson(PREFIX + "/clientDeleteTaskCaseLog", jsonObject.toJSONString());
	}

	/**
	 * ��ȡ������������ϸ��־�Լ����
	 * @param taskName ��������
	 * @param caseSign �������
	 * @return ��ȡ���
	 */
	public static String getLogDetailResult(String taskName, String caseSign){
		String str = "{\"taskName\":\""+taskName+"\",\"caseSign\":\""+caseSign+"\"}";
		JSONObject jsonObject = JSON.parseObject(str);
		return HttpRequest.httpClientPostJson(PREFIX+"/getLogDetailResult", jsonObject.toJSONString());
	}

}