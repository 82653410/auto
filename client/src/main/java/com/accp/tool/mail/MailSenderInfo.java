package com.accp.tool.mail;

import java.security.GeneralSecurityException;
import java.util.Properties;

import com.accp.utils.LogUtil;
import com.sun.mail.util.MailSSLSocketFactory;

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
public class MailSenderInfo {
    /**
     * �����ʼ��ķ�������IP�Ͷ˿�
     */
    private String mailServerHost;
    private String mailServerPort = "25";
    /**
     * �ʼ������ߵĵ�ַ
     */
    private String fromAddress;
    /**
     * �ʼ������ߵĵ�ַ
     */
    private String toAddress;
    /**
     * ����ʼ������ߵĵ�ַ
     */
    private String[] toAddresses;
    /**
     * ��½�ʼ����ͷ��������û���������
     */
    private String userName;
    private String password;
    /**
     * �Ƿ�ʹ��SSL����
     */
    private boolean sslenable = true;
    /**
     * �Ƿ���Ҫ������֤
     */
    private boolean validate = false;

    /**
     * �ʼ�����
     */
    private String subject;
    /**
     * �ʼ����ı�����
     */
    private String content;
    /**
     * �ʼ��������ļ���
     */
    private String[] attachFileNames;

    /**
     * ����ʼ��Ự����
     */
    public Properties getProperties() {
        Properties p = new Properties();
        try {
            p.put("mail.smtp.host", this.mailServerHost);
            p.put("mail.smtp.port", this.mailServerPort);
            p.put("mail.smtp.auth", validate ? "true" : "false");
            if (this.sslenable) {
                p.put("mail.smtp.ssl.enable", "true");
                MailSSLSocketFactory sf = new MailSSLSocketFactory();
                sf.setTrustAllHosts(true);
                p.put("mail.smtp.ssl.socketFactory", sf);
            }
        } catch (GeneralSecurityException e) {
            LogUtil.APP.error("����ʼ��Ự����ʧ�ܻ��쳣: ",e);
            return null;
        }
        return p;
    }

    public String getMailServerHost() {
        return mailServerHost;
    }

    public void setMailServerHost(String mailServerHost) {
        this.mailServerHost = mailServerHost;
    }

    public String getMailServerPort() {
        return mailServerPort;
    }

    public void setMailServerPort(String mailServerPort) {
        this.mailServerPort = mailServerPort;
    }

    public boolean isSslenable() {
        return this.sslenable;
    }

    public void setSslenable(boolean sslenable) {
        this.sslenable = sslenable;
    }

    public boolean isValidate() {
        return validate;
    }

    public void setValidate(boolean validate) {
        this.validate = validate;
    }

    public String[] getAttachFileNames() {
        return attachFileNames;
    }

    public void setAttachFileNames(String[] fileNames) {
        this.attachFileNames = fileNames;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public String[] getToAddresses() {
        return toAddresses;
    }

    public void setToAddresses(String[] toAddresses) {
        this.toAddresses = toAddresses;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String textContent) {
        this.content = textContent;
    }


}