package com.accp.execution.dispose.actionkeyword;

import com.accp.driven.SubString;
import com.accp.utils.LogUtil;

/**
 * 动作关键字的处理接口的实现类：使用jsonpath处理json字符串
 *
 *
 *
 *
 *
 *
 */
@Action(name="jsonpath")
public class JsonPathActionParser implements ActionKeyWordParser {


    /**
     * 通过jsonPath表达式获取JSON字符串指定值
     * 仅支持返回值是String类型，不支持List,如果jsonPath表达式返回的是List将抛出异常
     * @param actionParams 动作关键字
     * @param testResult 测试结果
     */
	@Override
    public String parse(String actionParams, String testResult) {
    	LogUtil.APP.info("Action(jsonPath):开始处理jsonPath动作...参数：【{}】   待处理json字符串：【{}】",actionParams,testResult);
    	testResult = SubString.jsonPathGetParams(actionParams, testResult);
        LogUtil.APP.info("Action(jsonPath):处理jsonPath动作完成...处理结果【{}】",testResult);
        return testResult;
    }
}
