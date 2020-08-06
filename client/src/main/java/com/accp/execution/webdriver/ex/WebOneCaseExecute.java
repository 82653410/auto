package com.accp.execution.webdriver.ex;

import java.io.IOException;
import java.util.List;

import com.accp.execution.httpinterface.TestControl;
import com.accp.remote.api.GetServerApi;
import com.accp.remote.api.serverOperation;
import com.accp.remote.entity.ProjectCase;
import com.accp.remote.entity.ProjectCaseParams;
import com.accp.utils.LogUtil;
import org.openqa.selenium.WebDriver;

import com.accp.execution.webdriver.WebDriverInitialization;
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
public class WebOneCaseExecute{
	
	public static void oneCaseExecuteForTast(Integer caseId, String taskid){
		//记录日志到数据库
		serverOperation.exetype = 0;
		TestControl.TASKID = taskid;
		int drivertype = serverOperation.querydrivertype(taskid);
		WebDriver wd = null;
		try {
			wd = WebDriverInitialization.setWebDriverForTask(drivertype);
		} catch (IOException e1) {
			LogUtil.APP.error("初始化WebDriver出错！", e1);
		}
		serverOperation caselog = new serverOperation();
		ProjectCase testcase = GetServerApi.cGetCaseByCaseId(caseId);
		//删除旧的日志
		serverOperation.deleteTaskCaseLog(testcase.getCaseId(), taskid);    

		List<ProjectCaseParams> pcplist=GetServerApi.cgetParamsByProjectid(String.valueOf(testcase.getProjectId()));
		LogUtil.APP.info("开始执行用例:【{}】......",testcase.getCaseSign());
		try {
			List<ProjectCaseSteps> steps=GetServerApi.getStepsbycaseid(testcase.getCaseId());
			WebCaseExecution.caseExcution(testcase, steps, taskid,wd,caselog,pcplist);
			LogUtil.APP.info("当前用例:【{}】执行完成......进入下一条",testcase.getCaseSign());
		} catch (Exception e) {
			LogUtil.APP.error("用户执行过程中抛出异常！", e);
		}
		serverOperation.updateTaskExecuteData(taskid, 0,2);
        //关闭浏览器
		assert wd != null;
		wd.quit();
	}

}
