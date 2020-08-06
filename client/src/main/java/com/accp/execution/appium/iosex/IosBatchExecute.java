package com.accp.execution.appium.iosex;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Properties;

import com.accp.execution.appium.AppiumService;
import com.accp.execution.httpinterface.TestControl;
import com.accp.remote.api.GetServerApi;
import com.accp.remote.api.serverOperation;
import com.accp.remote.entity.ProjectCase;
import com.accp.remote.entity.ProjectCaseParams;
import com.accp.remote.entity.TaskExecute;
import com.accp.utils.LogUtil;
import com.accp.utils.config.AppiumConfig;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSElement;
import com.accp.execution.appium.AppiumInitialization;
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
public class IosBatchExecute {

	public static void batchCaseExecuteForTast(String taskid, String batchcase) throws IOException, InterruptedException {
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
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			LogUtil.APP.error("���������Զ�����Appiume�������׳��쳣��", e);
		}
		serverOperation caselog = new serverOperation();
		TaskExecute task = GetServerApi.cgetTaskbyid(Integer.parseInt(taskid));
		List<ProjectCaseParams> pcplist = GetServerApi
				.cgetParamsByProjectid(task.getProjectId().toString());
		// ִ��ȫ���ǳɹ�״̬����
		if (batchcase.contains("ALLFAIL")) {
			List<Integer> caseIdList = caselog.getCaseListForUnSucByTaskId(taskid);
			for (Integer integer : caseIdList) {
				ProjectCase testcase = GetServerApi.cGetCaseByCaseId(integer);
				List<ProjectCaseSteps> steps = GetServerApi.getStepsbycaseid(testcase.getCaseId());
				// ɾ���ɵ���־
				serverOperation.deleteTaskCaseLog(testcase.getCaseId(), taskid);
				try {
					IosCaseExecution.caseExcution(testcase, steps, taskid, iosd, caselog, pcplist);
				} catch (Exception e) {
					LogUtil.APP.error("�û�ִ�й������׳��쳣��", e);
				}
			}
		} else { // ����ִ������
			String[] temp = batchcase.split("#");
			for (String s : temp) {
				ProjectCase testcase = GetServerApi.cGetCaseByCaseId(Integer.valueOf(s));
				List<ProjectCaseSteps> steps = GetServerApi.getStepsbycaseid(testcase.getCaseId());
				// ɾ���ɵ���־
				serverOperation.deleteTaskCaseLog(testcase.getCaseId(), taskid);
				try {
					IosCaseExecution.caseExcution(testcase, steps, taskid, iosd, caselog, pcplist);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					LogUtil.APP.error("�û�ִ�й������׳��쳣��", e);
				}
			}
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
