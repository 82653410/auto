package com.accp.execution;

import java.io.File;

import com.accp.execution.appium.AppTestControl;
import com.accp.execution.httpinterface.TestControl;
import com.accp.execution.webdriver.WebTestControl;
import com.accp.remote.api.GetServerApi;
import com.accp.remote.entity.TaskExecute;
import com.accp.utils.LogUtil;
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
public class RunAutomationTest extends TestControl {
	public static void main(String[] args) {
		try {
			PropertyConfigurator.configure(System.getProperty("user.dir") + File.separator + "log4j.conf");
			String taskid = args[0];
			TaskExecute task = GetServerApi.cgetTaskbyid(Integer.parseInt(taskid));
			TaskScheduling taskScheduling = GetServerApi.cGetTaskSchedulingByTaskId(Integer.parseInt(taskid));
			if (taskScheduling.getTaskType() == 0) {
				// 接口测试
				TestControl.taskExecutionPlan(task);
			} else if (taskScheduling.getTaskType() == 1) {
				// UI测试
				WebTestControl.taskExecutionPlan(task);
			} else if (taskScheduling.getTaskType() == 2) {
				AppTestControl.taskExecutionPlan(task);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LogUtil.APP.error("启动测试任务运行主函数出现异常，请检查！",e);
		} finally{
			System.exit(0);
		}
	}
}
