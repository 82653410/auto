package com.accp.execution.appium.androidex;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.accp.execution.dispose.ActionManageForSteps;
import com.accp.execution.dispose.ChangString;
import com.accp.execution.dispose.ParamsManageForSteps;
import com.accp.execution.httpinterface.TestCaseExecution;
import com.accp.execution.httpinterface.analyticsteps.InterfaceAnalyticCase;
import com.accp.remote.api.serverOperation;
import com.accp.remote.entity.ProjectCase;
import com.accp.remote.entity.ProjectCaseParams;
import com.accp.utils.Constants;
import com.accp.utils.LogUtil;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import com.accp.execution.appium.AppDriverAnalyticCase;
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
public class AndroidCaseExecution{
	static Map<String, String> variable = new HashMap<>();
    private static String casenote = "备注初始化";

	public static void caseExcution(ProjectCase testcase, List<ProjectCaseSteps> steps, String taskid, AndroidDriver<AndroidElement> appium, serverOperation caselog, List<ProjectCaseParams> pcplist) {
		caselog.updateTaskCaseExecuteStatus(taskid, testcase.getCaseId(), 3);
		// 把公共参数加入到MAP中
		for (ProjectCaseParams pcp : pcplist) {
			variable.put(pcp.getParamsName(), pcp.getParamsValue());
		}
		 // 加入全局变量
        variable.putAll(ParamsManageForSteps.GLOBAL_VARIABLE);
	    // 0通过 1失败 2锁定 3执行中 4未执行
	    int setcaseresult = 0;
		for (ProjectCaseSteps step : steps) {
            Map<String, String> params;
            String result;

            // 根据步骤类型来分析步骤参数
            if (3 == step.getStepType()){
            	params = AppDriverAnalyticCase.analyticCaseStep(testcase, step, taskid,caselog,variable);
            }else{
            	params = InterfaceAnalyticCase.analyticCaseStep(testcase, step, taskid, caselog,variable);
            }
			
			if(null != params.get("exception") && params.get("exception").contains("解析异常")){
				setcaseresult = 2;
				break;
			}
			
            // 根据步骤类型来执行步骤
            if (3 == step.getStepType()){
            	result = androidRunStep(params, appium, taskid, testcase.getCaseId(), step.getStepSerialNumber(), caselog);
            }else{
            	TestCaseExecution testCaseExecution=new TestCaseExecution();
            	result = testCaseExecution.runStep(params, taskid, testcase.getCaseSign(), step, caselog);
            }

			String expectedResults = params.get("ExpectedResults");
			expectedResults= ChangString.changparams(expectedResults, variable,"预期结果");
			
            // 判断结果
			int stepresult = judgeResult(testcase, step, params, appium, taskid, expectedResults, result, caselog);
			// 失败，并且不在继续,直接终止
            if (0 != stepresult) {
            	setcaseresult = stepresult;
                if (testcase.getFailcontinue() == 0) {
                    LogUtil.APP.warn("用例【{}】第【{}】步骤执行失败，中断本条用例后续步骤执行，进入到下一条用例执行中......",testcase.getCaseSign(),step.getStepSerialNumber());
                    break;
                } else {
                    LogUtil.APP.warn("用例【{}】第【{}】步骤执行失败，继续本条用例后续步骤执行，进入下个步骤执行中......",testcase.getCaseSign(),step.getStepSerialNumber());
                }
            }
		}

		variable.clear();
		caselog.updateTaskCaseExecuteStatus(taskid, testcase.getCaseId(), setcaseresult);
		if(setcaseresult==0){
			LogUtil.APP.info("用例【{}】全部步骤执行结果成功...",testcase.getCaseSign());
	        caselog.insertTaskCaseLog(taskid, testcase.getCaseId(), "用例全部步骤执行结果成功","info", "ending","");
		}else{
			LogUtil.APP.warn("用例【{}】步骤执行过程中失败或是锁定...请查看具体原因！【{}】",testcase.getCaseSign(),casenote);
	        caselog.insertTaskCaseLog(taskid, testcase.getCaseId(), "用例执行过程中失败或是锁定"+casenote,"error", "ending","");
		}
		//LogOperation.UpdateTastdetail(taskid, 0);
	}

	public static String androidRunStep(Map<String, String> params, AndroidDriver<AndroidElement> appium,String taskid,Integer caseId,int stepno,serverOperation caselog) {
		String result;
		String property;
		String propertyValue;
		String operation;
		String operationValue;

		try {
			property = params.get("property");
			propertyValue = params.get("property_value");
			operation = params.get("operation");
			operationValue = params.get("operation_value");
			
			LogUtil.APP.info("二次解析用例过程完成，等待进行对象操作......");
			caselog.insertTaskCaseLog(taskid, caseId, "对象操作:"+operation+"; 操作值:"+operationValue,"info", String.valueOf(stepno),"");
		} catch (Exception e) {
			LogUtil.APP.error("二次解析用例过程抛出异常！",e);
			return "步骤执行失败：解析用例失败!";
		}

		try {		
			//调用接口用例
			if(null != operationValue && "runcase".equals(operation)){
				String[] temp=operationValue.split(",",-1);
				TestCaseExecution testCaseExecution=new TestCaseExecution();
				String ex = testCaseExecution.oneCaseExecuteForUICase(temp[0], taskid, caselog, appium);
				if(!ex.contains("CallCase调用出错！") && !ex.contains("解析出错啦！") && !ex.contains("失败")){
					return ex;
				}else{
					return "步骤执行失败：调用接口用例过程失败";
				}
			}
			
			AndroidElement ae;
			// 页面元素层
			if (null != property && null != propertyValue) { 
				ae = isElementExist(appium, property, propertyValue);
				// 判断此元素是否存在
				if (null==ae) {
					LogUtil.APP.warn("定位对象失败，isElementExist为null!");
					return "步骤执行失败：isElementExist定位元素过程失败！";
				}

				if (operation.contains("select")) {
					result = AndroidEncapsulateOperation.selectOperation(ae, operation, operationValue);
				} else if (operation.contains("get")){
					result = AndroidEncapsulateOperation.getOperation(ae, operation,operationValue);
				} else {
					result = AndroidEncapsulateOperation.objectOperation(appium, ae, operation, operationValue, property, propertyValue);
				}
				// Driver层操作
			} else if (null==property && null != operation) { 				
				// 处理弹出框事件
				if (operation.contains("alert")){
					result = AndroidEncapsulateOperation.alertOperation(appium, operation);
				}else{
					result = AndroidEncapsulateOperation.driverOperation(appium, operation, operationValue);
				} 				
			}else{
				LogUtil.APP.warn("元素操作过程失败！");
				result =  "步骤执行失败：元素操作过程失败！";
			}
		} catch (Exception e) {
			LogUtil.APP.error("元素定位过程或是操作过程失败或异常！",e);
			return "步骤执行失败：元素定位过程或是操作过程失败或异常！" + e.getMessage();
		}
		caselog.insertTaskCaseLog(taskid, caseId, result,"info", String.valueOf(stepno),"");
		
		if(result.contains("获取到的值是【") && result.contains("】")){
			result = result.substring(result.indexOf("获取到的值是【")+7, result.length()-1);
		}
		return result;

	}
    
	public static AndroidElement isElementExist(AndroidDriver<AndroidElement> appium, String property, String propertyValue) {
		try {
			AndroidElement ae = null;
			property=property.toLowerCase();
			// 处理WebElement对象定位
			switch (property) {
			case "id":
				ae = appium.findElementById(propertyValue);
				break;
			case "name":
				ae = appium.findElementByAndroidUIAutomator("text(\""+propertyValue+"\")");
				break;
			case "androiduiautomator":
				ae = appium.findElementByAndroidUIAutomator(propertyValue);
				break;
			case "xpath":
				ae = appium.findElementByXPath(propertyValue);
				break;
			case "linktext":
				ae = appium.findElementByLinkText(propertyValue);
				break;
			case "tagname":
				ae = appium.findElementByTagName(propertyValue);
				break;
			case "cssselector":
				ae = appium.findElementByCssSelector(propertyValue);
				break;
			case "classname":
				ae = appium.findElementByClassName(propertyValue);
				break;
			case "partiallinktext":
				ae = appium.findElementByPartialLinkText(propertyValue);
				break;
			default:
				break;
			}

			return ae;

		} catch (Exception e) {
			LogUtil.APP.error("当前对象定位失败，出现异常！",e);
			return null;
		}
		
	}

    public static int judgeResult(ProjectCase testcase, ProjectCaseSteps step, Map<String, String> params, AndroidDriver<AndroidElement> appium, String taskid, String expect, String result, serverOperation caselog) {
        int setresult = 0;
        java.text.DateFormat timeformat = new java.text.SimpleDateFormat("MMdd-hhmmss");
		String imagname = timeformat.format(new Date());
        
        result = ActionManageForSteps.actionManage(step.getAction(), result);
        if (null != result && !result.contains("步骤执行失败：")) {
            // 有预期结果
            if (null != expect && !expect.isEmpty()) {
                LogUtil.APP.info("期望结果为【{}】",expect);

                // 赋值传参模式
                if (expect.length() > Constants.ASSIGNMENT_SIGN.length() && expect.startsWith(Constants.ASSIGNMENT_SIGN)) {
                    variable.put(expect.substring(Constants.ASSIGNMENT_SIGN.length()), result);
                    LogUtil.APP.info("用例：{} 第{}步，将测试结果【{}】赋值给变量【{}】",testcase.getCaseSign(),step.getStepSerialNumber(),result,expect.substring(Constants.ASSIGNMENT_SIGN.length()));
                    caselog.insertTaskCaseLog(taskid, testcase.getCaseId(), "将测试结果【" + result + "】赋值给变量【" + expect.substring(Constants.ASSIGNMENT_SIGN.length()) + "】", "info", String.valueOf(step.getStepSerialNumber()), "");
                }
                // 赋值全局变量
                else if (expect.length() > Constants.ASSIGNMENT_GLOBALSIGN.length() && expect.startsWith(Constants.ASSIGNMENT_GLOBALSIGN)) {
                	variable.put(expect.substring(Constants.ASSIGNMENT_GLOBALSIGN.length()), result);
                	ParamsManageForSteps.GLOBAL_VARIABLE.put(expect.substring(Constants.ASSIGNMENT_GLOBALSIGN.length()), result);
                    LogUtil.APP.info("用例：{} 第{}步，将测试结果【{}】赋值给全局变量【{}】",testcase.getCaseSign(),step.getStepSerialNumber(),result,expect.substring(Constants.ASSIGNMENT_GLOBALSIGN.length()));
                    caselog.insertTaskCaseLog(taskid, testcase.getCaseId(), "将测试结果【" + result + "】赋值给全局变量【" + expect.substring(Constants.ASSIGNMENT_GLOBALSIGN.length()) + "】", "info", String.valueOf(step.getStepSerialNumber()), "");
                }
                // 移动端 UI检查模式
                else if (3 == step.getStepType() && params.get("checkproperty") != null && params.get("checkproperty_value") != null) {
                    String checkproperty = params.get("checkproperty");
                    String checkPropertyValue = params.get("checkproperty_value");

                    AndroidElement ae = isElementExist(appium, checkproperty, checkPropertyValue);
                    if (null != ae) {
                        LogUtil.APP.info("用例：{} 第{}步，在当前页面中找到预期结果中对象。当前步骤执行成功！",testcase.getCaseSign(),step.getStepSerialNumber());
                        caselog.insertTaskCaseLog(taskid, testcase.getCaseId(), "在当前页面中找到预期结果中对象。当前步骤执行成功！", "info", String.valueOf(step.getStepSerialNumber()), "");
                    } else {
                        casenote = "第" + step.getStepSerialNumber() + "步，没有在当前页面中找到预期结果中对象。执行失败！";
                        setresult = 1;
                        AndroidBaseAppium.screenShot(appium, imagname);
                        LogUtil.APP.warn("用例：{} 第{}步，没有在当前页面中找到预期结果中对象。当前步骤执行失败！",testcase.getCaseSign(),step.getStepSerialNumber());
                        caselog.insertTaskCaseLog(taskid, testcase.getCaseId(), "在当前页面中没有找到预期结果中对象。当前步骤执行失败！" + "checkproperty【" + checkproperty + "】  checkproperty_value【" + checkPropertyValue + "】", "error", String.valueOf(step.getStepSerialNumber()), imagname);
                    }
                }
                // 其它匹配模式
                else {
                    // 模糊匹配预期结果模式
                    if (expect.length() > Constants.FUZZY_MATCHING_SIGN.length() && expect.startsWith(Constants.FUZZY_MATCHING_SIGN)) {
                        if (result.contains(expect.substring(Constants.FUZZY_MATCHING_SIGN.length()))) {
                            LogUtil.APP.info("用例：{} 第{}步，模糊匹配预期结果成功！执行结果：{}",testcase.getCaseSign(),step.getStepSerialNumber(),result);
                            caselog.insertTaskCaseLog(taskid, testcase.getCaseId(), "模糊匹配预期结果成功！执行结果：" + result, "info", String.valueOf(step.getStepSerialNumber()), "");
                        } else {
                            casenote = "第" + step.getStepSerialNumber() + "步，模糊匹配预期结果失败！";
                            setresult = 1;
                            AndroidBaseAppium.screenShot(appium, imagname);
                            LogUtil.APP.warn("用例：{} 第{}步，模糊匹配预期结果失败！预期结果：{}，测试结果：{}",testcase.getCaseSign(),step.getStepSerialNumber(),expect.substring(Constants.FUZZY_MATCHING_SIGN.length()),result);
                            caselog.insertTaskCaseLog(taskid, testcase.getCaseId(), "模糊匹配预期结果失败！预期结果：" + expect.substring(Constants.FUZZY_MATCHING_SIGN.length()) + "，测试结果：" + result, "error", String.valueOf(step.getStepSerialNumber()), imagname);
                        }
                    }
                    // 正则匹配预期结果模式
                    else if (expect.length() > Constants.REGULAR_MATCHING_SIGN.length() && expect.startsWith(Constants.REGULAR_MATCHING_SIGN)) {
                        Pattern pattern = Pattern.compile(expect.substring(Constants.REGULAR_MATCHING_SIGN.length()));
                        Matcher matcher = pattern.matcher(result);
                        if (matcher.find()) {
                            LogUtil.APP.info("用例：{} 第{}步，正则匹配预期结果成功！执行结果：{}",testcase.getCaseSign(),step.getStepSerialNumber(),result);
                            caselog.insertTaskCaseLog(taskid, testcase.getCaseId(), "正则匹配预期结果成功！", "info", String.valueOf(step.getStepSerialNumber()), "");
                        } else {
                            casenote = "第" + step.getStepSerialNumber() + "步，正则匹配预期结果失败！";
                            setresult = 1;
                            AndroidBaseAppium.screenShot(appium, imagname);
                            LogUtil.APP.warn("用例：{} 第{}步，正则匹配预期结果失败！预期结果：{}，测试结果：{}",testcase.getCaseSign(),step.getStepSerialNumber(),expect.substring(Constants.REGULAR_MATCHING_SIGN.length()),result);
                            caselog.insertTaskCaseLog(taskid, testcase.getCaseId(), "正则匹配预期结果失败！预期结果：" + expect.substring(Constants.REGULAR_MATCHING_SIGN.length()) + "，测试结果：" + result, "error", String.valueOf(step.getStepSerialNumber()), imagname);
                        }
                    }
                    // 精确匹配预期结果模式
                    else {
                        if (expect.equals(result)) {
                            LogUtil.APP.info("用例：{} 第{}步，精确匹配预期结果成功！执行结果：{}",testcase.getCaseSign(),step.getStepSerialNumber(),result);
                            caselog.insertTaskCaseLog(taskid, testcase.getCaseId(), "精确匹配预期结果成功！", "info", String.valueOf(step.getStepSerialNumber()), "");
                        } else {
                            casenote = "第" + step.getStepSerialNumber() + "步，精确匹配预期结果失败！";
                            setresult = 1;
                            AndroidBaseAppium.screenShot(appium, imagname);
                            LogUtil.APP.warn("用例：{} 第{}步，精确匹配预期结果失败！预期结果是：【{}】  执行结果：【{}】",testcase.getCaseSign(),step.getStepSerialNumber(),expect,result);
                            caselog.insertTaskCaseLog(taskid, testcase.getCaseId(), "精确匹配预期结果失败！预期结果是：【"+expect+"】  执行结果：【"+ result+"】", "error", String.valueOf(step.getStepSerialNumber()), imagname);
                        }
                    }
                }
            }
        } else {
            casenote = (null != result) ? result : "";
            setresult = 2;
            AndroidBaseAppium.screenShot(appium, imagname);
            LogUtil.APP.warn("用例：{} 第{}步，执行结果：{}",testcase.getCaseSign(),step.getStepSerialNumber(),casenote);
            caselog.insertTaskCaseLog(taskid, testcase.getCaseId(), "当前步骤在执行过程中解析|定位元素|操作对象失败！" + casenote, "error", String.valueOf(step.getStepSerialNumber()), imagname);
        }
        
        return setresult;
    }

}
