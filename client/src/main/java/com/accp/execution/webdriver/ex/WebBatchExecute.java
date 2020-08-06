package com.accp.execution.webdriver.ex;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import com.accp.execution.httpinterface.TestControl;
import com.accp.remote.api.GetServerApi;
import com.accp.remote.api.serverOperation;
import com.accp.remote.entity.ProjectCase;
import com.accp.remote.entity.ProjectCaseParams;
import com.accp.remote.entity.TaskExecute;
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
public class WebBatchExecute{
	
	public static void batchCaseExecuteForTast(String taskid, String batchcase) throws IOException{
		//记录日志到数据库
		serverOperation.exetype = 0;
		TestControl.TASKID = taskid;
		int drivertype = serverOperation.querydrivertype(taskid);
		WebDriver wd = null;
		try {
			wd = WebDriverInitialization.setWebDriverForTask(drivertype);
		} catch (MalformedURLException e1) {
			LogUtil.APP.error("初始化WebDriver出现异常！", e1);
		}
		serverOperation caselog = new serverOperation();
		TaskExecute task= GetServerApi.cgetTaskbyid(Integer.parseInt(taskid));
		List<ProjectCaseParams> pcplist=GetServerApi.cgetParamsByProjectid(task.getProjectId().toString());
		 //执行全部非成功状态用例
		if(batchcase.contains("ALLFAIL")){
			List<Integer> caseIdList = caselog.getCaseListForUnSucByTaskId(taskid);
			for (Integer integer : caseIdList) {
				ProjectCase testcase = GetServerApi.cGetCaseByCaseId(integer);
				List<ProjectCaseSteps> steps = GetServerApi.getStepsbycaseid(testcase.getCaseId());
				//删除旧的日志
				serverOperation.deleteTaskCaseLog(testcase.getCaseId(), taskid);
				try {
					WebCaseExecution.caseExcution(testcase, steps, taskid, wd, caselog, pcplist);
				} catch (Exception e) {
					LogUtil.APP.error("用户执行过程中抛出异常！", e);
				}
			}			
		}else{                                           //批量执行用例
			String[] temp=batchcase.split("#");
			for (String s : temp) {
				ProjectCase testcase = GetServerApi.cGetCaseByCaseId(Integer.valueOf(s));
				List<ProjectCaseSteps> steps = GetServerApi.getStepsbycaseid(testcase.getCaseId());
				//删除旧的日志
				serverOperation.deleteTaskCaseLog(testcase.getCaseId(), taskid);
				try {
					WebCaseExecution.caseExcution(testcase, steps, taskid, wd, caselog, pcplist);
				} catch (Exception e) {
					LogUtil.APP.error("用户执行过程中抛出异常！", e);
				}
			}
		}
		serverOperation.updateTaskExecuteData(taskid, 0,2);
        //关闭浏览器
		assert wd != null;
		wd.quit();
	}
	
}
