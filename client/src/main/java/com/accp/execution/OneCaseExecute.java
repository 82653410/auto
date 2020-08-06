package com.accp.execution;

import java.io.File;
import java.util.Properties;

import com.accp.execution.appium.androidex.AndroidOneCaseExecute;
import com.accp.execution.appium.iosex.IosOneCaseExecute;
import com.accp.execution.httpinterface.TestCaseExecution;
import com.accp.execution.httpinterface.TestControl;
import com.accp.execution.webdriver.ex.WebOneCaseExecute;
import com.accp.remote.api.GetServerApi;
import com.accp.remote.entity.TaskExecute;
import com.accp.utils.LogUtil;
import com.accp.utils.config.AppiumConfig;
import org.apache.log4j.PropertyConfigurator;

import com.accp.remote.entity.TaskScheduling;

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
public class OneCaseExecute extends TestControl {

	public static void main(String[] args) {
		try{
			PropertyConfigurator.configure(System.getProperty("user.dir")+ File.separator +"log4j.conf");
			String taskId = args[0];
			String caseId = args[1];
			TaskExecute task = GetServerApi.cgetTaskbyid(Integer.parseInt(taskId));
			TaskScheduling taskScheduling = GetServerApi.cGetTaskSchedulingByTaskId(Integer.parseInt(taskId));
			if (taskScheduling.getTaskType() == 0) {
					// 接口测试
				    TestCaseExecution testCaseExecution=new TestCaseExecution();
				    testCaseExecution.oneCaseExecuteForTask(Integer.valueOf(caseId), String.valueOf(task.getTaskId()));

			} else if (taskScheduling.getTaskType() == 1) {
					WebOneCaseExecute.oneCaseExecuteForTast(Integer.valueOf(caseId),
							String.valueOf(task.getTaskId()));

			} else if (taskScheduling.getTaskType() == 2) {
				Properties properties = AppiumConfig.getConfiguration();

				if ("Android".equals(properties.getProperty("platformName"))) {
					AndroidOneCaseExecute.oneCaseExecuteForTast(Integer.valueOf(caseId),
							String.valueOf(task.getTaskId()));
				} else if ("IOS".equals(properties.getProperty("platformName"))) {
					IosOneCaseExecute.oneCaseExecuteForTast(Integer.valueOf(caseId),
							String.valueOf(task.getTaskId()));
				}

			}
		}catch(Exception e){
			LogUtil.APP.error("启动单个用例运行主函数出现异常，请检查！",e);
		} finally{
			System.exit(0);
		}
	}
}
