package com.accp.execution.httpinterface;

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
public class ThreadForBatchCase extends Thread{

	private Integer caseId;
	private String taskid;
	
	public ThreadForBatchCase(Integer caseId,String taskid){
		this.caseId = caseId;
		this.taskid = taskid;
	}
	
	@Override
	public void run(){
		TestCaseExecution testCaseExecution=new TestCaseExecution();
		testCaseExecution.oneCaseExecuteForTask(caseId, taskid);
		TestControl.THREAD_COUNT--;        //多线程计数--，用于检测线程是否全部执行完
	}

}
