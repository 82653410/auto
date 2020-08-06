package springboot.model;

import java.io.Serializable;

/**
 * 
 * 
 * 
 *
 * 
 * 注意对象必须继承Serializable
 *
 */
public class RunTaskEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	/*调度任务名称*/
	private String schedulingName;
	/*任务执行ID*/
    private String taskId;
    /*驱动加载路径*/
    private String loadPath;
    
	public String getSchedulingName() {
		return schedulingName;
	}
	public void setSchedulingName(String schedulingName) {
		this.schedulingName = schedulingName;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getLoadPath() {
		return loadPath;
	}
	public void setLoadPath(String loadPath) {
		this.loadPath = loadPath;
	}

}
