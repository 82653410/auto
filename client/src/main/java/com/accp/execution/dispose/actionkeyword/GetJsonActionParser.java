package com.accp.execution.dispose.actionkeyword;

import com.accp.driven.SubString;
import com.accp.utils.LogUtil;

/**
 * 动作关键字的处理接口的实现类：获取JSON字符串指定Key的值的
 *
 *
 */
@Action(name="getjv")
public class GetJsonActionParser implements ActionKeyWordParser {


    /**
     * 获取JSON字符串指定Key的值是
     * @param actionParams 动作关键字
     * @param testResult 测试结果
     */
    @Override
    public String parse(String actionParams, String testResult) {
        String key;
        String index="1";
        if(actionParams.endsWith("]")&&actionParams.contains("[")){
            key=actionParams.substring(0,actionParams.indexOf("["));
            index=actionParams.substring(actionParams.indexOf("[")+1, actionParams.lastIndexOf("]"));
        }else{
            key=actionParams;
        }
        testResult= SubString.getJsonValue(testResult, key, index);
        LogUtil.APP.info("Action(getJV):获取JSON字符串指定Key的值是:{}",testResult);
        return testResult;
    }
}
