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
                // 用例全部成功了发送, casecount != casesuc
                if (null!=sendCondition&&1 == sendCondition) {
                    if (taskCount[0] == taskCount[1]) {
                        isSend = true;
                    }
                }
                // 用例部分失败了发送
                if (null!=sendCondition&&2 == sendCondition) {
                    if (taskCount[2] > 0) {
                        isSend = true;
                    }
                }
                // 全发
                if (null!=sendCondition&&0 == sendCondition) {
                    isSend = true;
                }
            }
        }
        if (!isSend) {
            LogUtil.APP.info("当前任务不需要发送邮件通知!");
            return;
        }
        String[] addresses = serverOperation.getEmailAddress(taskid);
        Properties properties = SysConfig.getConfiguration();
        if (addresses != null) {
            LogUtil.APP.info("准备将测试结果发送邮件通知！请稍等...");
            //这个类主要是设置邮件
            MailSenderInfo mailInfo = new MailSenderInfo();
            //这个类主要来发送邮件
            SimpleMailSender sms = new SimpleMailSender();
            mailInfo.setMailServerHost(properties.getProperty("mail.smtp.ip"));
            mailInfo.setMailServerPort(properties.getProperty("mail.smtp.port"));
            mailInfo.setSslenable(properties.getProperty("mail.smtp.ssl.enable").equals("true"));
            mailInfo.setValidate(true);
            mailInfo.setUserName(properties.getProperty("mail.smtp.username"));
            //您的邮箱密码
            mailInfo.setPassword(properties.getProperty("mail.smtp.password"));
            mailInfo.setFromAddress(properties.getProperty("mail.smtp.username"));
            //标题
            mailInfo.setSubject(subject);
            //内容
            mailInfo.setContent(content);
            mailInfo.setToAddresses(addresses);
            //sms.sendHtmlMail(mailInfo);

            StringBuilder stringBuilder = new StringBuilder();
            for (String address : addresses) {
                stringBuilder.append(address).append(";");
            }
            String addressesmail = stringBuilder.toString();
            if (sms.sendHtmlMail(mailInfo)) {
                LogUtil.APP.info("给{}的测试结果通知邮件发送完成！",addressesmail);
            } else {
                LogUtil.APP.warn("给{}的测试结果通知邮件发送失败！",addressesmail);
            }
        } else {
            LogUtil.APP.info("当前任务不需要发送邮件通知！");
        }
    }

}
