package com.accp.execution.dispose.actionkeyword;

import com.accp.driven.SubString;
import com.accp.utils.LogUtil;

/**
 * �����ؼ��ֵĴ���ӿڵ�ʵ���ࣺ��ȡJSON�ַ���ָ��Key��ֵ��
 *
 *
 */
@Action(name="getjv")
public class GetJsonActionParser implements ActionKeyWordParser {


    /**
     * ��ȡJSON�ַ���ָ��Key��ֵ��
     * @param actionParams �����ؼ���
     * @param testResult ���Խ��
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
        LogUtil.APP.info("Action(getJV):��ȡJSON�ַ���ָ��Key��ֵ��:{}",testResult);
        return testResult;
    }
}
