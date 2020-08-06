package com.accp.execution.dispose.actionkeyword;

import com.accp.driven.SubString;
import com.accp.utils.LogUtil;

/**
 * �����ؼ��ֵĴ���ӿڵ�ʵ���ࣺʹ��jsonpath����json�ַ���
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
     * ͨ��jsonPath���ʽ��ȡJSON�ַ���ָ��ֵ
     * ��֧�ַ���ֵ��String���ͣ���֧��List,���jsonPath���ʽ���ص���List���׳��쳣
     * @param actionParams �����ؼ���
     * @param testResult ���Խ��
     */
	@Override
    public String parse(String actionParams, String testResult) {
    	LogUtil.APP.info("Action(jsonPath):��ʼ����jsonPath����...��������{}��   ������json�ַ�������{}��",actionParams,testResult);
    	testResult = SubString.jsonPathGetParams(actionParams, testResult);
        LogUtil.APP.info("Action(jsonPath):����jsonPath�������...��������{}��",testResult);
        return testResult;
    }
}
