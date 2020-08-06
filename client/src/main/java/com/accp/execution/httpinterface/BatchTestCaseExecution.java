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
	 * 创建线程池，多线程执行用例
	 * @param taskid 任务ID
	 * @param batchcase 批量用例字符串，#隔断
	 * @throws Exception 抛异常
	 */
	
	public static void batchCaseExecuteForTast(String taskid, String batchcase) throws Exception{
		int threadcount = GetServerApi.cGetTaskSchedulingByTaskId(Integer.parseInt(taskid)).getExThreadCount();
		ThreadPoolExecutor	threadExecute	= new ThreadPoolExecutor(threadcount, 30, 3, TimeUnit.SECONDS,
				new ArrayBlockingQueue<>(1000),
	            new ThreadPoolExecutor.CallerRunsPolicy());
		//执行全部非成功状态用例
		if(batchcase.contains("ALLFAIL")){
			//初始化写用例结果以及日志模块 
			serverOperation caselog = new serverOperation();
			List<Integer> caseIdList = caselog.getCaseListForUnSucByTaskId(taskid);
			for (Integer integer : caseIdList) {
				ProjectCase testcase = GetServerApi.cGetCaseByCaseId(integer);
				TestControl.THREAD_COUNT++;   //多线程计数++，用于检测线程是否全部执行完
				threadExecute.execute(new ThreadForBatchCase(testcase.getCaseId(), taskid));
			}			
		}else{                                           //批量执行用例
			String[] temp=batchcase.split("#");
			LogUtil.APP.info("当前批量执行任务中共有【{}】条待测试用例...",temp.length);
			for (String s : temp) {
				TestControl.THREAD_COUNT++;   //多线程计数++，用于检测线程是否全部执行完
				threadExecute.execute(new ThreadForBatchCase(Integer.valueOf(s), taskid));
			}
		}
		//多线程计数，用于检测线程是否全部执行完
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
