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
 * 
 * 
 */

public class RunBatchCaseEntity implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String projectname;
    private String taskid;
    private String batchcase;
    private String loadpath;
    
	public String getLoadpath() {
		return loadpath;
	}
	public void setLoadpath(String loadpath) {
		this.loadpath = loadpath;
	}
	public String getProjectname() {
		return projectname;
	}
	public void setProjectname(String projectname) {
		this.projectname = projectname;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getBatchcase() {
		return batchcase;
	}
	public void setBatchcase(String batchcase) {
		this.batchcase = batchcase;
	}

}
