package com.accp.execution.appium;

import java.util.List;

import com.accp.execution.appium.androidex.AndroidCaseExecution;
import com.accp.remote.api.GetServerApi;
import com.accp.remote.api.serverOperation;
import com.accp.remote.entity.ProjectCase;
import com.accp.remote.entity.ProjectCaseParams;
import com.accp.utils.LogUtil;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
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
	 * �����ƶ�����������
	 * @param appium appium��ʼ������
	 * @param testCaseExternalId �������
	 *
	 *
	 */
	public static void oneCasedebug(AndroidDriver<AndroidElement> appium,String testCaseExternalId){
		 //����¼��־�����ݿ�
		serverOperation.exetype = 1;
		serverOperation caselog = new serverOperation();
		try {
			ProjectCase testcase = GetServerApi.cgetCaseBysign(testCaseExternalId);
			List<ProjectCaseParams> pcplist=GetServerApi.cgetParamsByProjectid(String.valueOf(testcase.getProjectId()));
			LogUtil.APP.info("��ʼִ������:��{}��......",testCaseExternalId);
			List<ProjectCaseSteps> steps=GetServerApi.getStepsbycaseid(testcase.getCaseId());
			AndroidCaseExecution.caseExcution(testcase, steps, "888888", appium, caselog, pcplist);
			LogUtil.APP.info("��ǰ��������{}��ִ�����......������һ��",testcase.getCaseSign());
		} catch (Exception e) {
			LogUtil.APP.error("�û�ִ�й������׳��쳣��", e);
		}
        //�˳�
		appium.quit();
	}
	
	/**
	 * ����ƶ�����������
	 * @param appium appium����
	 * @param projectname ��Ŀ��
	 * @param addtestcase ��������
	 *
	 *
	 */
	public static void moreCaseDebug(AndroidDriver<AndroidElement> appium,String projectname,List<String> addtestcase){
		System.out.println("��ǰ���������ܹ���"+addtestcase.size());
		for(String testCaseExternalId:addtestcase) {
		    try{
		    LogUtil.APP.info("��ʼ���÷�������Ŀ��:{}���������:{}",projectname,testCaseExternalId); 
		    oneCasedebug(appium,testCaseExternalId);
		    }catch(Exception e){
				LogUtil.APP.info("�������� �����쳣���������:{}",testCaseExternalId);
			}
		}
	}

}
