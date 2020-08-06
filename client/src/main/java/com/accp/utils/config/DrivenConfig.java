package com.accp.utils.config;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import com.accp.utils.LogUtil;

/**
 * ��ʼ�����ݿ���������
 * 
 * 
 * 
 * 
 *
 * 
 */
public class DrivenConfig {
	private static final Properties SYS_CONFIG = new Properties();
	private static final String SYS_CONFIG_FILE = "/TestDriven/driven_config.properties";
	static{
		try {
		    InputStream in = new BufferedInputStream(DrivenConfig.class.getResourceAsStream(SYS_CONFIG_FILE));
			SYS_CONFIG.load(new InputStreamReader(in, StandardCharsets.UTF_8));
		} catch (IOException e) {
			LogUtil.APP.error("��ȡ��������driven_config.properties�����ļ������쳣�����飡", e);
		}
	}
	private DrivenConfig(){}
	public static Properties getConfiguration(){
		return SYS_CONFIG;
	}
}
