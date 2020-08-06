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
 * @Description: ������ر����ݿ�����
 *
 * 
 * 
 */
public class DbToolkit { 
    /** 
     * �������ݿ����ӳ�
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
    	//ע�������� 
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
     * ��һ�����ݿ�������ִ��һ����̬SQL����ѯ 
     * 
     * @param conn            ���ݿ����� 
     * @param staticSql ��̬SQL����ַ��� 
     * @return ���ز�ѯ�����ResultSet����
     */ 
    public static ResultSet executeQuery(Connection conn, String staticSql) throws SQLException { 
    	//����ִ��SQL�Ķ��� 
            Statement stmt = conn.createStatement(); 
            
          //ִ��SQL������ȡ���ؽ�� 
        // stmt.close();
            return stmt.executeQuery(staticSql);
    } 

    /** 
     * ��һ�����ݿ�������ִ��һ����̬SQL��� 
     * 
     * @param conn ���ݿ�����
     * @param staticSql ��̬SQL����ַ���
     */ 
    public static int executeSQL(Connection conn, String staticSql) throws SQLException { 
    	//����ִ��SQL�Ķ��� 
                    Statement stmt = conn.createStatement(); 
                  //ִ��SQL������ȡ���ؽ��  
                     stmt.execute(staticSql); 
                     return stmt.getUpdateCount();
           
    } 

    /** 
     * ��һ�����ݿ�������ִ��һ����̬SQL��� 
     * 
     * @param conn        ���ݿ����� 
     * @param sqlList ��̬SQL����ַ������� 
     */ 
    public static void executeBatchSQL(Connection conn, List<String> sqlList) { 
            try { 
            	 //����ִ��SQL�Ķ��� 
                    Statement stmt = conn.createStatement(); 
                    for (String sql : sqlList) { 
                            stmt.addBatch(sql); 
                    } 
                  //ִ��SQL������ȡ���ؽ�� 
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
                	  //�ر����ݿ����� 
                        conn.close(); 
                } 
            } catch (SQLException e) { 
                    e.printStackTrace();
            } 
    } 
}

