package com.accp.execution.dispose.actionkeyword;

/**
 * �����ؼ��ֵĴ���ӿ�
 *
 *
 */
public interface ActionKeyWordParser {

	/**
	 * ��Թؼ��ֵĳ��󷽷�
	 * @param actionParams �ؼ���
	 * @param testResult ������Ĳ��Խ��
	 * @return ���ش�����
	 *
	 *
	 */
    String parse(String actionParams, String testResult);
}
