package com.accp.utils.config;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import com.accp.utils.LogUtil;

/**
 * 初始化数据库驱动配置
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
			LogUtil.APP.error("读取测试驱动driven_config.properties配置文件出现异常，请检查！", e);
		}
	}
	private DrivenConfig(){}
	public static Properties getConfiguration(){
		return SYS_CONFIG;
	}
}
