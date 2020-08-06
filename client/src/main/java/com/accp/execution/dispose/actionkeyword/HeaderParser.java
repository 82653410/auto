package com.accp.execution.dispose.actionkeyword;


import com.accp.utils.LogUtil;
import com.alibaba.fastjson.JSONObject;

/**
 * �����ؼ��ֵĴ���ӿڵ�ʵ���ࣺ����Ӧheader��ȡ��ĳ��headerֵ
 *
 *
 */
@Action(name="header")
public class HeaderParser implements ActionKeyWordParser {


    /**
     * �����ؼ���
     * @param actionParams �ؼ��ֲ���
     * @param testResult ������Ĳ��Խ��
     * @return ���ش�����
     */
    @Override
    public String parse(String actionParams, String testResult) {
        String pre = "RESPONSE_HEAD:��";
        String headerStr = testResult.substring(testResult.indexOf(pre) + pre.length(), testResult.indexOf("�� RESPONSE_CODE"));
        String getHeader = JSONObject.parseObject(headerStr).getJSONArray(actionParams).getString(0);
        LogUtil.APP.info("Action(header):����Ӧheader��ȡ��ָ��headerֵ��:{}",getHeader);
        return getHeader;

    }
}
