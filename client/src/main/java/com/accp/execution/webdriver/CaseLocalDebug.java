package com.accp.execution.webdriver;

import java.util.List;

import com.accp.execution.webdriver.ex.WebCaseExecution;
import com.accp.remote.api.GetServerApi;
import com.accp.remote.api.serverOperation;
import com.accp.remote.entity.ProjectCase;
import com.accp.remote.entity.ProjectCaseParams;
import com.accp.utils.LogUtil;
import org.openqa.selenium.WebDriver;

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
	 * 单个用例Web UI调试
	 * @param wd 驱动
	 * @param testCaseExternalId 用例编号
	 *
	 *
	 */
	public static void oneCasedebug(WebDriver wd,String testCaseExternalId){
		 //不记录日志到数据库
		serverOperation.exetype = 1;
		serverOperation caselog = new serverOperation();
		try {
			ProjectCase testcase = GetServerApi.cgetCaseBysign(testCaseExternalId);
			List<ProjectCaseParams> pcplist=GetServerApi.cgetParamsByProjectid(String.valueOf(testcase.getProjectId()));
			LogUtil.APP.info("开始执行用例:【{}】......",testCaseExternalId);
			List<ProjectCaseSteps> steps=GetServerApi.getStepsbycaseid(testcase.getCaseId());
			WebCaseExecution.caseExcution(testcase,steps, "888888",wd,caselog,pcplist);
			LogUtil.APP.info("当前用例：【{}】执行完成......进入下一条",testcase.getCaseSign());
		} catch (Exception e) {
			LogUtil.APP.error("用户执行过程中抛出异常！", e);
		}
        //关闭浏览器
        wd.quit();
	}
	
	/**
	 * 多个用例串行Web UI调试
	 * @param wd 驱动
	 * @param projectname 项目名称
	 * @param addtestcase 用例集
	 *
	 *
	 */
	public static void moreCaseDebug(WebDriver wd,String projectname,List<String> addtestcase){
		System.out.println("当前调试用例总共："+addtestcase.size());
		for(String testCaseExternalId:addtestcase) {
		    try{
		    LogUtil.APP.info("开始调用方法，项目名:{}，用例编号:{}",projectname,testCaseExternalId); 
		    oneCasedebug(wd,testCaseExternalId);
		    }catch(Exception e){
				LogUtil.APP.error("用例运行出现异常，用例编号:{}",testCaseExternalId);
			}
		}
	}

}
