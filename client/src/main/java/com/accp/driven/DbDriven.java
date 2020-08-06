package com.accp.driven;

import java.util.Properties;

import com.accp.utils.DbOperation;
import com.accp.utils.config.DrivenConfig;

/**
 * 提供数据库查询操作的默认测试驱动
 * 
 * 
 * 
 * 
 *
 *
 */
public class DbDriven {

	/**
	 * 执行SQL语句
	 * @param sql 执行SQL语句
	 * @return 返回执行结果条数才及提示
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
	 * 查询SQL语句
	 * @param sql 查询SQL
	 * @return 返回查询结果
	 * @throws Exception 异常信息
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
