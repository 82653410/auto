package com.accp.execution.appium.iosex;

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
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSElement;
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
 */
public class IosOneCaseExecute {

	public static void oneCaseExecuteForTast(Integer caseId, String taskid)
			throws InterruptedException {
		// ��¼��־�����ݿ�
		serverOperation.exetype = 0;
		TestControl.TASKID = taskid;
		IOSDriver<IOSElement> iosd = null;
		AppiumService as=null;
		try {
			Properties properties = AppiumConfig.getConfiguration();
			//���������Զ�����Appiume����
			if(Boolean.parseBoolean(properties.getProperty("autoRunAppiumService"))){
				as =new AppiumService();
				as.start();
				Thread.sleep(10000);
			}
			
			iosd = AppiumInitialization.setIosAppium(properties);
		} catch (IOException e1) {
			LogUtil.APP.error("��ʼ��IOSDriver����", e1);
		}
		serverOperation caselog = new serverOperation();
		ProjectCase testcase = GetServerApi.cGetCaseByCaseId(caseId);
		// ɾ���ɵ���־
		serverOperation.deleteTaskCaseLog(testcase.getCaseId(), taskid);

		List<ProjectCaseParams> pcplist = GetServerApi.cgetParamsByProjectid(String.valueOf(testcase.getProjectId()));
		LogUtil.APP.info("��ʼִ����������{}��......",testcase.getCaseSign());
		try {
			List<ProjectCaseSteps> steps = GetServerApi.getStepsbycaseid(testcase.getCaseId());
			IosCaseExecution.caseExcution(testcase, steps, taskid, iosd, caselog, pcplist);
			LogUtil.APP.info("��ǰ��������{}��ִ�����......������һ��",testcase.getCaseSign());
		} catch (Exception e) {
			LogUtil.APP.error("�û�ִ�й������׳��쳣��", e);
		}
		serverOperation.updateTaskExecuteData(taskid, 0,2);
		assert iosd != null;
		iosd.closeApp();
		//�ر�Appium������߳�
		if(as!=null){
			as.interrupt();
		}
	}

}
