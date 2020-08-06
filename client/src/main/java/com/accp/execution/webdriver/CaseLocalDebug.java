package com.accp.execution.webdriver;

import java.util.List;

import com.accp.execution.webdriver.ex.WebCaseExecution;
import com.accp.remote.api.GetServerApi;
import com.accp.remote.api.serverOperation;
import com.accp.remote.entity.ProjectCase;
import com.accp.remote.entity.ProjectCaseParams;
import com.accp.utils.LogUtil;
import org.openqa.selenium.WebDriver;

import com.accp.remote.entity.ProjectCaseSteps;

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
public class CaseLocalDebug{

	/**
	 * ��������Web UI����
	 * @param wd ����
	 * @param testCaseExternalId �������
	 *
	 *
	 */
	public static void oneCasedebug(WebDriver wd,String testCaseExternalId){
		 //����¼��־�����ݿ�
		serverOperation.exetype = 1;
		serverOperation caselog = new serverOperation();
		try {
			ProjectCase testcase = GetServerApi.cgetCaseBysign(testCaseExternalId);
			List<ProjectCaseParams> pcplist=GetServerApi.cgetParamsByProjectid(String.valueOf(testcase.getProjectId()));
			LogUtil.APP.info("��ʼִ������:��{}��......",testCaseExternalId);
			List<ProjectCaseSteps> steps=GetServerApi.getStepsbycaseid(testcase.getCaseId());
			WebCaseExecution.caseExcution(testcase,steps, "888888",wd,caselog,pcplist);
			LogUtil.APP.info("��ǰ��������{}��ִ�����......������һ��",testcase.getCaseSign());
		} catch (Exception e) {
			LogUtil.APP.error("�û�ִ�й������׳��쳣��", e);
		}
        //�ر������
        wd.quit();
	}
	
	/**
	 * �����������Web UI����
	 * @param wd ����
	 * @param projectname ��Ŀ����
	 * @param addtestcase ������
	 *
	 *
	 */
	public static void moreCaseDebug(WebDriver wd,String projectname,List<String> addtestcase){
		System.out.println("��ǰ���������ܹ���"+addtestcase.size());
		for(String testCaseExternalId:addtestcase) {
		    try{
		    LogUtil.APP.info("��ʼ���÷�������Ŀ��:{}���������:{}",projectname,testCaseExternalId); 
		    oneCasedebug(wd,testCaseExternalId);
		    }catch(Exception e){
				LogUtil.APP.error("�������г����쳣���������:{}",testCaseExternalId);
			}
		}
	}

}
