package com.accp.driven;

import java.util.Properties;

import com.accp.utils.DbOperation;
import com.accp.utils.config.DrivenConfig;

/**
 * �ṩ���ݿ��ѯ������Ĭ�ϲ�������
 * 
 * 
 * 
 * 
 *
 *
 */
public class DbDriven {

	/**
	 * ִ��SQL���
	 * @param sql ִ��SQL���
	 * @return ����ִ�н�������ż���ʾ
	 */
	public String executeSql(String sql) {
		Properties properties = DrivenConfig.getConfiguration();
		String url = properties.getProperty("db.url");
		String username = properties.getProperty("db.username");
		String password = properties.getProperty("db.password");
		DbOperation db=new DbOperation(url,username,password);
		return db.executeSql(sql);
	}

	/**
	 * ��ѯSQL���
	 * @param sql ��ѯSQL
	 * @return ���ز�ѯ���
	 * @throws Exception �쳣��Ϣ
	 */
	public String executeQuery(String sql) throws Exception{
		Properties properties = DrivenConfig.getConfiguration();
		String url = properties.getProperty("db.url");
		String username = properties.getProperty("db.username");
		String password = properties.getProperty("db.password");
		DbOperation db=new DbOperation(url,username,password);
		return db.executeQuery(sql);
	}

}
