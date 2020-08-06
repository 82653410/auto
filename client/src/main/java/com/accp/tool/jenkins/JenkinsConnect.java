package com.accp.tool.jenkins;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

import com.accp.utils.LogUtil;
import com.accp.utils.config.SysConfig;
import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.client.JenkinsHttpClient;

/**
 * Jenkins链接
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
	 * 如果有些 API 该Jar工具包未提供，可以用此Http客户端操作远程接口，执行命令
	 * @return 返回jenkins客户端对象
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
	 * Jenkins API链接
	 * @return 返回jenkins服务对象
	 *
	 * 
	 */
	public JenkinsServer connection() {
		JenkinsServer jenkinsServer = null;
		try {
			LogUtil.APP.info("准备连接Jenkins...URL:{}  用户名:{}  密码:{}",JENKINS_URL,JENKINS_USERNAME,JENKINS_PASSWORD);
			jenkinsServer = new JenkinsServer(new URI(JENKINS_URL), JENKINS_USERNAME, JENKINS_PASSWORD);
			LogUtil.APP.info("连接Jenkins成功！");
			LogUtil.APP.info("Jenkins版本:{}",jenkinsServer.getVersion());
		} catch (URISyntaxException e) {
			LogUtil.APP.error("连接Jenkins出现异常",e);
		}
		return jenkinsServer;
	}

}