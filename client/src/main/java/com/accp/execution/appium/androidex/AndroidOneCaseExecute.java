package com.accp.execution.appium.androidex;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import com.accp.execution.httpinterface.TestControl;
import com.accp.remote.api.GetServerApi;
import com.accp.remote.api.serverOperation;
import com.accp.remote.entity.ProjectCase;
import com.accp.remote.entity.ProjectCaseParams;
import com.accp.utils.LogUtil;
import com.accp.utils.config.AppiumConfig;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import com.accp.execution.appium.AppiumInitialization;
import com.accp.execution.appium.AppiumService;
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
public class AndroidOneCaseExecute {

	public static void oneCaseExecuteForTast(Integer caseId, String taskid)
			throws InterruptedException {
		// ��¼��־�����ݿ�
		serverOperation.exetype = 0;
		TestControl.TASKID = taskid;
		AndroidDriver<AndroidElement> ad = null;
		AppiumService as=null;
		try {
			Properties properties = AppiumConfig.getConfiguration();
			//���������Զ�����Appiume����
			if(Boolean.parseBoolean(properties.getProperty("autoRunAppiumService"))){
				as =new AppiumService();
				as.start();
				Thread.sleep(10000);
			}
			
			ad = AppiumInitialization.setAndroidAppium(properties);
		} catch (IOException e1) {
			LogUtil.APP.error("��ʼ��AndroidDriver����", e1);
		}
		serverOperation caselog = new serverOperation();
		// ɾ���ɵ���־
		ProjectCase testcase = GetServerApi.cGetCaseByCaseId(caseId);
		serverOperation.deleteTaskCaseLog(testcase.getCaseId(), taskid);
		List<ProjectCaseParams> pcplist = GetServerApi.cgetParamsByProjectid(String.valueOf(testcase.getProjectId()));
		LogUtil.APP.info("��ʼִ����������{}��......",testcase.getCaseSign());
		try {
			List<ProjectCaseSteps> steps = GetServerApi.getStepsbycaseid(testcase.getCaseId());
			AndroidCaseExecution.caseExcution(testcase, steps, taskid, ad, caselog, pcplist);
			LogUtil.APP.info("��ǰ��������{}��ִ�����......������һ��",testcase.getCaseSign());
		} catch (Exception e) {
			LogUtil.APP.error("�û�ִ�й������׳��쳣��", e);
		}
		serverOperation.updateTaskExecuteData(taskid, 0,2);
		assert ad != null;
		ad.closeApp();
		//�ر�Appium������߳�
		if(as!=null){
			as.interrupt();
		}
	}

}
