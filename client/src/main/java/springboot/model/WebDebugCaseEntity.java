package springboot.model;

import java.io.Serializable;

/**
 * 用例调试实体
 * 
 * 
 * 
 * 
 *
 * 
 */
public class WebDebugCaseEntity implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer caseId;
    private Integer userId;
    private String loadpath;
    
	public Integer getCaseId() {
		return caseId;
	}
	public void setCaseId(Integer caseId) {
		this.caseId = caseId;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getLoadpath() {
		return loadpath;
	}
	public void setLoadpath(String loadpath) {
		this.loadpath = loadpath;
	}

}
