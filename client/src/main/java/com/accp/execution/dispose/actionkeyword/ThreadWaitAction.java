package com.accp.execution.dispose.actionkeyword;


import com.accp.utils.LogUtil;
import com.accp.execution.dispose.ChangString;

/**
 * �����ؼ��ֵĴ���ӿڵ�ʵ���ࣺ�̵߳ȴ�ʱ��
 *
 * 
 */
@Action(name="wait")
public class ThreadWaitAction implements ActionKeyWordParser {


    /**
     * �����ؼ���
     * @param actionParams �ؼ��ֲ���
     * @param testResult ��������Խ��
     * @return ���ش������
     */
    @Override
    public String parse(String actionParams, String testResult) {
        if(ChangString.isInteger(actionParams)){
            try {
                // ��ȡ�����ȴ�ʱ��
                int time=Integer.parseInt(actionParams);
                if (time > 0) {
                    LogUtil.APP.info("Action(Wait):�̵߳ȴ���{}����...",time);
                    Thread.sleep(time * 1000);
                }
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }else{
            LogUtil.APP.error("ʹ�õȴ��ؼ��ֵĲ�������������ֱ�������˶��������飡");
        }
        return testResult;
    }
}
