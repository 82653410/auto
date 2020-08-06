package com.accp.tool.jenkins;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

import com.accp.utils.LogUtil;
import com.accp.utils.config.SysConfig;
import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.client.JenkinsHttpClient;

/**
 * Jenkins����
 * 
 * 
 * 
 * 
 *
 * 
 */
public class JenkinsConnect {

	private String JENKINS_URL;
	private String JENKINS_USERNAME;
	private String JENKINS_PASSWORD;

	JenkinsConnect() {
		Properties properties = SysConfig.getConfiguration();
		this.JENKINS_URL=properties.getProperty("jenkins.url");
		this.JENKINS_USERNAME=properties.getProperty("jenkins.username");
		this.JENKINS_PASSWORD=properties.getProperty("jenkins.password");
	}

	/**
	 * �����Щ API ��Jar���߰�δ�ṩ�������ô�Http�ͻ��˲���Զ�̽ӿڣ�ִ������
	 * @return ����jenkins�ͻ��˶���
	 */
	public JenkinsHttpClient getClient() {
		JenkinsHttpClient jenkinsHttpClient = null;
		try {
			jenkinsHttpClient = new JenkinsHttpClient(new URI(JENKINS_URL), JENKINS_USERNAME, JENKINS_PASSWORD);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return jenkinsHttpClient;
	}

	/**
	 * Jenkins API����
	 * @return ����jenkins�������
	 *
	 * 
	 */
	public JenkinsServer connection() {
		JenkinsServer jenkinsServer = null;
		try {
			LogUtil.APP.info("׼������Jenkins...URL:{}  �û���:{}  ����:{}",JENKINS_URL,JENKINS_USERNAME,JENKINS_PASSWORD);
			jenkinsServer = new JenkinsServer(new URI(JENKINS_URL), JENKINS_USERNAME, JENKINS_PASSWORD);
			LogUtil.APP.info("����Jenkins�ɹ���");
			LogUtil.APP.info("Jenkins�汾:{}",jenkinsServer.getVersion());
		} catch (URISyntaxException e) {
			LogUtil.APP.error("����Jenkins�����쳣",e);
		}
		return jenkinsServer;
	}

}