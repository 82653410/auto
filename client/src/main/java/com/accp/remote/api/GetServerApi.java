package com.accp.remote.api;

import com.accp.remote.entity.*;
import com.alibaba.fastjson.JSONObject;
import com.accp.execution.dispose.ParamsManageForSteps;
import com.accp.utils.httputils.HttpRequest;

import java.util.List;


/**
 * 
 * 
 * 
 *
 * 
 * 
 *
 *
 * 
 */
public class GetServerApi {
	
	private static final String PREFIX = "/openGetApi";

	/**
	 * ͨ���ƻ�ID��ȡ������������
	 * @param planId ���Լƻ�ID
	 * @return ��������List
	 */
	public static List<ProjectCase> getCasesbyplanId(int planId) {
		String result = HttpRequest.loadJSON(PREFIX+"/clientGetCaseListByPlanId.do?planId=" + planId);
		return JSONObject.parseArray(result, ProjectCase.class);
	}

	/**
	 * ͨ���ƻ����ƻ�ȡ������������
	 * @param name ���Լƻ�����
	 * @return ��������List
	 */
	public static List<ProjectCase> getCasesbyplanname(String name) {
		String result = HttpRequest.loadJSON(PREFIX+"/clientGetCaseListByPlanName.do?planName=" + name);
		return JSONObject.parseArray(result, ProjectCase.class);
	}

	/**
	 * ͨ������ID��ȡ����Ĳ������
	 * @param caseid ����ID
	 * @return ������������List
	 */
	public static List<ProjectCaseSteps> getStepsbycaseid(Integer caseid) {
		String result = HttpRequest.loadJSON(PREFIX+"/clientGetStepListByCaseId.do?caseId=" + caseid);
		return JSONObject.parseArray(result, ProjectCaseSteps.class);
	}

	/**
	 * ͨ��taskid��ȡ����
	 * @param taskid ��������ID
	 * @return ���ز����������
	 */
	public static TaskExecute cgetTaskbyid(int taskid) {
		String result = HttpRequest.loadJSON(PREFIX+"/clientGetTaskByTaskId.do?taskId=" + taskid);
		return JSONObject.parseObject(result, TaskExecute.class);
	}
	
	/**
	 * ͨ��taskid��ȡ���ȶ���
	 * @param taskid ��������ID
	 * @return ���ص��ȶ���
	 */
	public static TaskScheduling cGetTaskSchedulingByTaskId(int taskid) {
		String result = HttpRequest.loadJSON(PREFIX+"/clientGetTaskSchedulingByTaskId.do?taskId=" + taskid);
		return JSONObject.parseObject(result, TaskScheduling.class);
	}

	/**
	 * ͨ��������Ż�ȡ����
	 * @param sign �������
	 * @return ��������
	 */
	public static ProjectCase cgetCaseBysign(String sign) {
		String result = HttpRequest.loadJSON(PREFIX+"/clientGetCaseByCaseSign.do?caseSign=" + sign);
		return JSONObject.parseObject(result, ProjectCase.class);
	}

	/**
	 * ͨ������ID��ȡ����
	 * @param caseId ����ID
	 * @return ��������
	 */
	public static ProjectCase cGetCaseByCaseId(Integer caseId) {
		String result = HttpRequest.loadJSON(PREFIX+"/clientGetCaseByCaseId.do?caseId=" + caseId);
		return JSONObject.parseObject(result, ProjectCase.class);
	}
	
	/**
	 * ��ȡ��Ŀ�µ����й�������
	 * @param projectid ��ĿID
	 * @return ������������
	 */
	public static List<ProjectCaseParams> cgetParamsByProjectid(String projectid) {
		String result = HttpRequest.loadJSON(PREFIX+"/clientGetParamsByProjectId.do?projectId="+projectid);
		List<ProjectCaseParams> paramsList = JSONObject.parseArray(result, ProjectCaseParams.class);
		//�����������������ú���ʱ���Ƚ�������ת��
		for(ProjectCaseParams pcp:paramsList){
			pcp.setParamsValue(ParamsManageForSteps.paramsManage(pcp.getParamsValue()));
		}
		return paramsList;
	}

	/**
	 * ͨ���ƻ�ID��ȡ������������
	 * @param taskId ��������ID
	 * @return ��������ID����
	 */
	public static List<Integer> clientGetCaseListForUnSucByTaskId(Integer taskId) {
		String result = HttpRequest.loadJSON(PREFIX+"/clientGetCaseListForUnSucByTaskId.do?taskId=" + taskId);
		return JSONObject.parseArray(result, Integer.class);
	}
	
	/**
	 * ͨ��templateId��ȡʵ��
	 * @param templateId ģ��ID
	 * @return Э��ģ�����
	 *
	 *
	 */
	public static ProjectProtocolTemplate clientGetProjectProtocolTemplateByTemplateId(Integer templateId) {
		String result = HttpRequest.loadJSON(PREFIX+"/clientGetProjectProtocolTemplateByTemplateId.do?templateId=" + templateId);
		return JSONObject.parseObject(result, ProjectProtocolTemplate.class);
	}
	
	/**
	 * ͨ��ģ��ID��ȡ�����б�
	 * @param templateId ģ��ID
	 * @return ��������
	 *
	 *
	 */
	public static List<ProjectTemplateParams> clientGetProjectTemplateParamsListByTemplateId(Integer templateId) {
		String result = HttpRequest.loadJSON(PREFIX+"/clientGetProjectTemplateParamsListByTemplateId.do?templateId=" + templateId);
		return JSONObject.parseArray(result, ProjectTemplateParams.class);
	}

}
