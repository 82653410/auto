package com.accp.tool.shell;

import com.accp.remote.api.serverOperation;
import com.accp.utils.LogUtil;

/**
 * 
 * 
 * 
 *
 * 
 * 
 *
 * 
 * 
 */
public class RestartServerInitialization {

	public static String restartServerRun(String tastid){
		String result;
		try{
			String[] command = serverOperation.getRestartComm(tastid);
			if(command!=null){
				LogUtil.APP.info("׼������ָ����TOMCAT�����Եȡ�������������:{}",command.length);
				if(command.length==5){
					LogUtil.APP.info("��ʼ��������TOMCAT��������������0:{} ����1:{} ����2:{} ����3:{} ����4:{}",command[0],command[1],command[2],command[3],command[4]);
					result = RmtShellExecutor.sshShell(command[0], command[1], command[2], Integer.parseInt(command[3]), command[4]);
				}else{
					LogUtil.APP.warn("����TOMCAT�����в��������쳣������������Ϣ��");
					result = "����TOMCAT�����в��������쳣������������Ϣ��";
				}				
			}else{
				result = "Status:true"+" ��ǰ����û���ҵ���Ҫ������TOMCAT���";
				LogUtil.APP.info("��ǰ����û��ָ����Ҫ����TOMCAT��");
			}
		}catch(Throwable e){
			LogUtil.APP.error("����TOMCAT�����г����쳣",e);
			result = "����TOMCAT�����г����쳣";
			return result;
		}
		return result;

	}

}
