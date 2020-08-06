package com.accp.execution.httpinterface;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.accp.remote.api.GetServerApi;
import com.accp.remote.api.serverOperation;
import com.accp.remote.entity.ProjectCase;
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
public class BatchTestCaseExecution {

	/**
	 * �����̳߳أ����߳�ִ������
	 * @param taskid ����ID
	 * @param batchcase ���������ַ�����#����
	 * @throws Exception ���쳣
	 */
	
	public static void batchCaseExecuteForTast(String taskid, String batchcase) throws Exception{
		int threadcount = GetServerApi.cGetTaskSchedulingByTaskId(Integer.parseInt(taskid)).getExThreadCount();
		ThreadPoolExecutor	threadExecute	= new ThreadPoolExecutor(threadcount, 30, 3, TimeUnit.SECONDS,
				new ArrayBlockingQueue<>(1000),
	            new ThreadPoolExecutor.CallerRunsPolicy());
		//ִ��ȫ���ǳɹ�״̬����
		if(batchcase.contains("ALLFAIL")){
			//��ʼ��д��������Լ���־ģ�� 
			serverOperation caselog = new serverOperation();
			List<Integer> caseIdList = caselog.getCaseListForUnSucByTaskId(taskid);
			for (Integer integer : caseIdList) {
				ProjectCase testcase = GetServerApi.cGetCaseByCaseId(integer);
				TestControl.THREAD_COUNT++;   //���̼߳���++�����ڼ���߳��Ƿ�ȫ��ִ����
				threadExecute.execute(new ThreadForBatchCase(testcase.getCaseId(), taskid));
			}			
		}else{                                           //����ִ������
			String[] temp=batchcase.split("#");
			LogUtil.APP.info("��ǰ����ִ�������й��С�{}��������������...",temp.length);
			for (String s : temp) {
				TestControl.THREAD_COUNT++;   //���̼߳���++�����ڼ���߳��Ƿ�ȫ��ִ����
				threadExecute.execute(new ThreadForBatchCase(Integer.valueOf(s), taskid));
			}
		}
		//���̼߳��������ڼ���߳��Ƿ�ȫ��ִ����
		int i=0;
		while(TestControl.THREAD_COUNT!=0){
			i++;
			if(i>600){
				break;
			}
			Thread.sleep(6000);
		}
		threadExecute.shutdown();
	}

}
