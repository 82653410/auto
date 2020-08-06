package com.accp.execution;

import java.io.File;
import java.util.Properties;

import com.accp.execution.appium.androidex.AndroidBatchExecute;
import com.accp.execution.appium.iosex.IosBatchExecute;
import com.accp.execution.httpinterface.BatchTestCaseExecution;
import com.accp.execution.httpinterface.TestControl;
import com.accp.execution.webdriver.ex.WebBatchExecute;
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
public class BatchCaseExecute extends TestControl {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			PropertyConfigurator.configure(System.getProperty("user.dir") + File.separator + "log4j.conf");
			String taskid = args[0];
			String batchcase = args[1];
			TaskExecute task = GetServerApi.cgetTaskbyid(Integer.parseInt(taskid));
			TaskScheduling taskScheduling = GetServerApi.cGetTaskSchedulingByTaskId(Integer.parseInt(taskid));
			if (taskScheduling.getTaskType() == 0) {
					BatchTestCaseExecution.batchCaseExecuteForTast(
                            String.valueOf(task.getTaskId()), batchcase);
			} else if (taskScheduling.getTaskType() == 1) {
					// UI测试
					WebBatchExecute.batchCaseExecuteForTast(
							String.valueOf(task.getTaskId()), batchcase);

			} else if (taskScheduling.getTaskType() == 2) {
				Properties properties = AppiumConfig.getConfiguration();

				if ("Android".equals(properties.getProperty("platformName"))) {
					AndroidBatchExecute.batchCaseExecuteForTast(
                            String.valueOf(task.getTaskId()), batchcase);
				} else if ("IOS".equals(properties.getProperty("platformName"))) {
					IosBatchExecute.batchCaseExecuteForTast(
                            String.valueOf(task.getTaskId()), batchcase);
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LogUtil.APP.error("启动批量运行用例主函数出现异常，请检查！",e);
		} finally{
			System.exit(0);
		}
	}

}
