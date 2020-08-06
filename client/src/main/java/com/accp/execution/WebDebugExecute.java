package com.accp.execution;

import java.io.File;

import com.accp.execution.httpinterface.TestControl;
import com.accp.execution.httpinterface.WebTestCaseDebug;
import com.accp.utils.LogUtil;
import org.apache.log4j.PropertyConfigurator;

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
public class WebDebugExecute extends TestControl {

	public static void main(String[] args) {
		try {
			PropertyConfigurator.configure(System.getProperty("user.dir") + File.separator + "log4j.conf");
	 		String caseIdStr = args[0];
	 		String userIdStr = args[1];
	 		WebTestCaseDebug.oneCaseDebug(caseIdStr, userIdStr);
		} catch (Exception e) {
			LogUtil.APP.error("启动用例调试主函数出现异常，请检查！",e);
		} finally{
			System.exit(0);
		}
	}
}
