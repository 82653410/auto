package com.accp.utils;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import com.accp.utils.config.DrivenConfig;
import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * 
 * 
 * 
 *
 * 
 * @ClassName: DBToolkit 
 * @Description: 创建与关闭数据库链接
 *
 * 
 * 
 */
public class DbToolkit { 
    /** 
     * 建立数据库链接池
     */ 
	public ComboPooledDataSource cpds;
	private static final String DRIVERCLASS = DrivenConfig.getConfiguration().getProperty("db.ComboPooledDataSource.DriverClass");
	
	public DbToolkit(String urlBase,String usernameBase,String passwordBase){
		cpds=new ComboPooledDataSource();  
        cpds.setUser(usernameBase);  
        cpds.setPassword(passwordBase);  
        cpds.setJdbcUrl(urlBase);  
        try {  
            cpds.setDriverClass(DRIVERCLASS);  
        } catch (PropertyVetoException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
        cpds.setInitialPoolSize(20);  
        cpds.setMaxIdleTime(20);  
        cpds.setMaxPoolSize(30);  
        cpds.setMinPoolSize(1);  	
	}
	
    static { 
    	//注册驱动类 
        try { 
                Class.forName(DRIVERCLASS); 
        } catch (ClassNotFoundException e) { 
                e.printStackTrace();
        } 
} 
    
	public  Connection getBaseConnection() throws SQLException{
		// TODO Auto-generated method stub       
        return cpds.getConnection();
	}

    /** 
     * 在一个数据库连接上执行一个静态SQL语句查询 
     * 
     * @param conn            数据库连接 
     * @param staticSql 静态SQL语句字符串 
     * @return 返回查询结果集ResultSet对象
     */ 
    public static ResultSet executeQuery(Connection conn, String staticSql) throws SQLException { 
    	//创建执行SQL的对象 
            Statement stmt = conn.createStatement(); 
            
          //执行SQL，并获取返回结果 
        // stmt.close();
            return stmt.executeQuery(staticSql);
    } 

    /** 
     * 在一个数据库连接上执行一个静态SQL语句 
     * 
     * @param conn 数据库连接
     * @param staticSql 静态SQL语句字符串
     */ 
    public static int executeSQL(Connection conn, String staticSql) throws SQLException { 
    	//创建执行SQL的对象 
                    Statement stmt = conn.createStatement(); 
                  //执行SQL，并获取返回结果  
                     stmt.execute(staticSql); 
                     return stmt.getUpdateCount();
           
    } 

    /** 
     * 在一个数据库连接上执行一批静态SQL语句 
     * 
     * @param conn        数据库连接 
     * @param sqlList 静态SQL语句字符串集合 
     */ 
    public static void executeBatchSQL(Connection conn, List<String> sqlList) { 
            try { 
            	 //创建执行SQL的对象 
                    Statement stmt = conn.createStatement(); 
                    for (String sql : sqlList) { 
                            stmt.addBatch(sql); 
                    } 
                  //执行SQL，并获取返回结果 
                    stmt.executeBatch(); 
            } catch (SQLException e) { 
                    e.printStackTrace();
            } 
    } 

    public static void closeConnection(Connection conn) { 
            if (conn == null){
            	return;
            } 
            try { 
                if (!conn.isClosed()) { 
                	  //关闭数据库连接 
                        conn.close(); 
                } 
            } catch (SQLException e) { 
                    e.printStackTrace();
            } 
    } 
}

