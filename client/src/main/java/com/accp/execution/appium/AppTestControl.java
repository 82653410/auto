package com.accp.execution.appium;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.accp.execution.appium.androidex.AndroidCaseExecution;
import com.accp.execution.appium.iosex.IosCaseExecution;
import com.accp.execution.httpinterface.TestControl;
import com.accp.remote.api.GetServerApi;
import com.accp.remote.api.serverOperation;
import com.accp.remote.entity.ProjectCase;
import com.accp.remote.entity.ProjectCaseParams;
import com.accp.remote.entity.TaskExecute;
import com.accp.tool.jenkins.BuildingInitialization;
import com.accp.tool.mail.HtmlMail;
import com.accp.tool.mail.MailSendInitialization;
import com.accp.tool.shell.RestartServerInitialization;
import com.accp.utils.LogUtil;
import com.accp.utils.config.AppiumConfig;
import com.offbytwo.jenkins.model.BuildResult;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSElement;
import com.accp.remote.entity.ProjectCaseSteps;
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
public class AppTestControl {

	/**
	 * 控制台模式调度计划执行用例
	 * @param planname 测试计划名称
	 */
	public static void manualExecutionPlan(String planname) {
		// 不记日志到数据库
		serverOperation.exetype = 1;
		String taskid = "888888";
		AndroidDriver<AndroidElement> androiddriver = null;
		IOSDriver<IOSElement> iosdriver = null;
		Properties properties = AppiumConfig.getConfiguration();
		try {
			if ("Android".equals(properties.getProperty("platformName"))) {
				androiddriver = AppiumInitialization.setAndroidAppium(properties);
			} else if ("IOS".equals(properties.getProperty("platformName"))) {
				iosdriver = AppiumInitialization.setIosAppium(properties);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			LogUtil.APP.error("控制台模式初始化Appium Driver异常！", e);
		}
		serverOperation caselog = new serverOperation();
		List<ProjectCase> testCases = GetServerApi.getCasesbyplanname(planname);
		List<ProjectCaseParams> pcplist = new ArrayList<>();
		if (testCases.size() != 0) {
			pcplist = GetServerApi.cgetParamsByProjectid(String.valueOf(testCases.get(0).getProjectId()));
		}
		LogUtil.APP.info("当前计划中读取到用例共{}个",testCases.size());
		int i = 0;
		for (ProjectCase testcase : testCases) {
			List<ProjectCaseSteps> steps = GetServerApi.getStepsbycaseid(testcase.getCaseId());
			if (steps.size() == 0) {
				continue;
			}
			i++;
			LogUtil.APP.info("开始执行计划中的第{}条用例：【{}】......",i,testcase.getCaseSign());
			try {
				if ("Android".equals(properties.getProperty("platformName"))) {
					AndroidCaseExecution.caseExcution(testcase, steps, taskid, androiddriver, caselog, pcplist);
				} else if ("IOS".equals(properties.getProperty("platformName"))) {
					IosCaseExecution.caseExcution(testcase, steps, taskid, iosdriver, caselog, pcplist);
				}
			} catch (Exception e) {
				LogUtil.APP.error("用户执行过程中抛出Exception异常！", e);
			}
			LogUtil.APP.info("当前用例：【{}】执行完成......进入下一条",testcase.getCaseSign());
		}
		LogUtil.APP.info("当前项目测试计划中的用例已经全部执行完成...");
		// 关闭APP以及appium会话
		if ("Android".equals(properties.getProperty("platformName"))) {
			assert androiddriver != null;
			androiddriver.closeApp();
		} else if ("IOS".equals(properties.getProperty("platformName"))) {
			assert iosdriver != null;
			iosdriver.closeApp();
		}
	}

	public static void taskExecutionPlan(TaskExecute task) throws InterruptedException {
		// 记录日志到数据库
		String taskId=task.getTaskId().toString();
		serverOperation.exetype = 0;
		TestControl.TASKID = taskId;
		AndroidDriver<AndroidElement> androiddriver = null;
		IOSDriver<IOSElement> iosdriver = null;
		Properties properties = AppiumConfig.getConfiguration();
		AppiumService as=null;
		//根据配置自动启动Appiume服务
		if(Boolean.parseBoolean(properties.getProperty("autoRunAppiumService"))){
			as =new AppiumService();
			as.start();
			Thread.sleep(10000);
		}
		TaskScheduling taskScheduling = GetServerApi.cGetTaskSchedulingByTaskId(task.getTaskId());
		String restartstatus = RestartServerInitialization.restartServerRun(taskId);
		BuildResult buildResult = BuildingInitialization.buildingRun(taskId);
		List<ProjectCaseParams> pcplist = GetServerApi
				.cgetParamsByProjectid(task.getProjectId().toString());
		String projectname = task.getProject().getProjectName();
		String jobname = GetServerApi.cGetTaskSchedulingByTaskId(task.getTaskId()).getSchedulingName();
        int[] tastcount;
		// 判断是否要自动重启TOMCAT
		if (restartstatus.contains("Status:true")) {
			// 判断是否构建是否成功
			if (BuildResult.SUCCESS.equals(buildResult)) {
				try {
					if ("Android".equals(properties.getProperty("platformName"))) {
						androiddriver = AppiumInitialization.setAndroidAppium(properties);
						LogUtil.APP.info("完成AndroidDriver初始化动作...APPIUM Server【http://{}/wd/hub】",properties.getProperty("appiumsever"));
					} else if ("IOS".equals(properties.getProperty("platformName"))) {
						iosdriver = AppiumInitialization.setIosAppium(properties);
						LogUtil.APP.info("完成IOSDriver初始化动作...APPIUM Server【http://{}/wd/hub】",properties.getProperty("appiumsever"));
					}
				} catch (Exception e) {
					LogUtil.APP.error("初始化AppiumDriver出错 ！APPIUM Server【http://{}/wd/hub】",properties.getProperty("appiumsever"), e);
				}
				serverOperation caselog = new serverOperation();
				List<ProjectCase> cases = GetServerApi.getCasesbyplanId(taskScheduling.getPlanId());
				LogUtil.APP.info("当前计划【{}】中共有【{}】条待测试用例...",task.getTaskName(),cases.size());
				serverOperation.updateTaskExecuteStatusIng(taskId, cases.size());
				int i = 0;
				for (ProjectCase testcase : cases) {
					i++;
					LogUtil.APP.info("开始执行当前测试任务 {} 的第【{}】条测试用例:【{}】......",task.getTaskName(),i,testcase.getCaseSign());
					List<ProjectCaseSteps> steps = GetServerApi.getStepsbycaseid(testcase.getCaseId());
					if (steps.size() == 0) {
						continue;
					}
					try {
						//插入开始执行的用例
						caselog.insertTaskCaseExecute(taskId, taskScheduling.getProjectId(),testcase.getCaseId(),testcase.getCaseSign(), testcase.getCaseName(), 4);
						if ("Android".equals(properties.getProperty("platformName"))) {
							AndroidCaseExecution.caseExcution(testcase, steps, taskId, androiddriver, caselog, pcplist);
						} else if ("IOS".equals(properties.getProperty("platformName"))) {
							IosCaseExecution.caseExcution(testcase, steps, taskId, iosdriver, caselog, pcplist);
						}
					} catch (Exception e) {
						LogUtil.APP.error("用户执行过程中抛出异常！", e);
					}
					LogUtil.APP.info("当前用例：【{}】执行完成......进入下一条",testcase.getCaseSign());
				}
				tastcount = serverOperation.updateTaskExecuteData(taskId, cases.size(),2);
				String testtime = serverOperation.getTestTime(taskId);
				LogUtil.APP.info("当前项目【{}】测试计划中的用例已经全部执行完成...",projectname);
				MailSendInitialization.sendMailInitialization(HtmlMail.htmlSubjectFormat(jobname),
						HtmlMail.htmlContentFormat(tastcount, taskId, buildResult.toString(), restartstatus, testtime, jobname),
						taskId, taskScheduling, tastcount);
				// 关闭APP以及appium会话
				if ("Android".equals(properties.getProperty("platformName"))) {
					assert androiddriver != null;
					androiddriver.closeApp();
				} else if ("IOS".equals(properties.getProperty("platformName"))) {
					assert iosdriver != null;
					iosdriver.closeApp();
				}
			} else {
				LogUtil.APP.warn("项目构建失败，自动化测试自动退出！请前往JENKINS中检查项目构建情况。");
				MailSendInitialization.sendMailInitialization(jobname, "构建项目过程中失败，自动化测试自动退出！请前去JENKINS查看构建情况！", taskId, taskScheduling, null);
			}
		} else {
			LogUtil.APP.warn("项目TOMCAT重启失败，自动化测试自动退出！请检查项目TOMCAT运行情况。");
			MailSendInitialization.sendMailInitialization(jobname, "项目TOMCAT重启失败，自动化测试自动退出！请检查项目TOMCAT运行情况！", taskId, taskScheduling, null);
		}
		//关闭Appium服务的线程
		if(as!=null){
			as.interrupt();
		}
	}

}
