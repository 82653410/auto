package com.accp.tool.mail;

import java.util.Properties;

import com.accp.remote.api.serverOperation;
import com.accp.utils.LogUtil;
import com.accp.utils.config.SysConfig;
import com.accp.remote.entity.TaskScheduling;

/**
 * 
 * 
 * 
 *
 * 
 *
 *
 * 
 */
public class MailSendInitialization {

    public static void sendMailInitialization(String subject, String content, String taskid, TaskScheduling taskScheduling, int[] taskCount) {
        boolean isSend = false;
        if (null == taskCount) {
            isSend = true;
        } else {
            if (taskCount.length == 5 && null != taskScheduling) {
                Integer sendCondition = taskScheduling.getEmailSendCondition();
                // ����ȫ���ɹ��˷���, casecount != casesuc
                if (null!=sendCondition&&1 == sendCondition) {
                    if (taskCount[0] == taskCount[1]) {
                        isSend = true;
                    }
                }
                // ��������ʧ���˷���
                if (null!=sendCondition&&2 == sendCondition) {
                    if (taskCount[2] > 0) {
                        isSend = true;
                    }
                }
                // ȫ��
                if (null!=sendCondition&&0 == sendCondition) {
                    isSend = true;
                }
            }
        }
        if (!isSend) {
            LogUtil.APP.info("��ǰ������Ҫ�����ʼ�֪ͨ!");
            return;
        }
        String[] addresses = serverOperation.getEmailAddress(taskid);
        Properties properties = SysConfig.getConfiguration();
        if (addresses != null) {
            LogUtil.APP.info("׼�������Խ�������ʼ�֪ͨ�����Ե�...");
            //�������Ҫ�������ʼ�
            MailSenderInfo mailInfo = new MailSenderInfo();
            //�������Ҫ�������ʼ�
            SimpleMailSender sms = new SimpleMailSender();
            mailInfo.setMailServerHost(properties.getProperty("mail.smtp.ip"));
            mailInfo.setMailServerPort(properties.getProperty("mail.smtp.port"));
            mailInfo.setSslenable(properties.getProperty("mail.smtp.ssl.enable").equals("true"));
            mailInfo.setValidate(true);
            mailInfo.setUserName(properties.getProperty("mail.smtp.username"));
            //������������
            mailInfo.setPassword(properties.getProperty("mail.smtp.password"));
            mailInfo.setFromAddress(properties.getProperty("mail.smtp.username"));
            //����
            mailInfo.setSubject(subject);
            //����
            mailInfo.setContent(content);
            mailInfo.setToAddresses(addresses);
            //sms.sendHtmlMail(mailInfo);

            StringBuilder stringBuilder = new StringBuilder();
            for (String address : addresses) {
                stringBuilder.append(address).append(";");
            }
            String addressesmail = stringBuilder.toString();
            if (sms.sendHtmlMail(mailInfo)) {
                LogUtil.APP.info("��{}�Ĳ��Խ��֪ͨ�ʼ�������ɣ�",addressesmail);
            } else {
                LogUtil.APP.warn("��{}�Ĳ��Խ��֪ͨ�ʼ�����ʧ�ܣ�",addressesmail);
            }
        } else {
            LogUtil.APP.info("��ǰ������Ҫ�����ʼ�֪ͨ��");
        }
    }

}
