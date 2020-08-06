package com.accp.execution.appium;

import java.util.List;

import com.accp.execution.appium.androidex.AndroidCaseExecution;
import com.accp.remote.api.GetServerApi;
import com.accp.remote.api.serverOperation;
import com.accp.remote.entity.ProjectCase;
import com.accp.remote.entity.ProjectCaseParams;
import com.accp.utils.LogUtil;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
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
	 * 单个移动端用例调试
	 * @param appium appium初始化对象
	 * @param testCaseExternalId 用例编号
	 *
	 *
	 */
	public static void oneCasedebug(AndroidDriver<AndroidElement> appium,String testCaseExternalId){
		 //不记录日志到数据库
		serverOperation.exetype = 1;
		serverOperation caselog = new serverOperation();
		try {
			ProjectCase testcase = GetServerApi.cgetCaseBysign(testCaseExternalId);
			List<ProjectCaseParams> pcplist=GetServerApi.cgetParamsByProjectid(String.valueOf(testcase.getProjectId()));
			LogUtil.APP.info("开始执行用例:【{}】......",testCaseExternalId);
			List<ProjectCaseSteps> steps=GetServerApi.getStepsbycaseid(testcase.getCaseId());
			AndroidCaseExecution.caseExcution(testcase, steps, "888888", appium, caselog, pcplist);
			LogUtil.APP.info("当前用例：【{}】执行完成......进入下一条",testcase.getCaseSign());
		} catch (Exception e) {
			LogUtil.APP.error("用户执行过程中抛出异常！", e);
		}
        //退出
		appium.quit();
	}
	
	/**
	 * 多个移动端用例调试
	 * @param appium appium对象
	 * @param projectname 项目名
	 * @param addtestcase 用例对象集
	 *
	 *
	 */
	public static void moreCaseDebug(AndroidDriver<AndroidElement> appium,String projectname,List<String> addtestcase){
		System.out.println("当前调试用例总共："+addtestcase.size());
		for(String testCaseExternalId:addtestcase) {
		    try{
		    LogUtil.APP.info("开始调用方法，项目名:{}，用例编号:{}",projectname,testCaseExternalId); 
		    oneCasedebug(appium,testCaseExternalId);
		    }catch(Exception e){
				LogUtil.APP.info("运行用例 出现异常，用例编号:{}",testCaseExternalId);
			}
		}
	}

}
