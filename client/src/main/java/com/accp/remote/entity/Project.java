package com.accp.remote.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 项目实体
 * 
 * 
 * 
 * 
 *
 *
 */
public class Project extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/** 项目ID */
	private Integer projectId;
	/** 项目名称 */
	private String projectName;
	/** 归属部门 */
	private Integer deptId;
	/** 项目标识 */
	private String projectSign;
	/** 项目标记 */
	private boolean flag = false;
	
	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public void setProjectId(Integer projectId) 
	{
		this.projectId = projectId;
	}

	public Integer getProjectId() 
	{
		return projectId;
	}
	public void setProjectName(String projectName) 
	{
		this.projectName = projectName;
	}

	public String getProjectName() 
	{
		return projectName;
	}
	public void setDeptId(Integer deptId) 
	{
		this.deptId = deptId;
	}

	public Integer getDeptId() 
	{
		return deptId;
	}
	public void setProjectSign(String projectSign) 
	{
		this.projectSign = projectSign;
	}

	public String getProjectSign() 
	{
		return projectSign;
	}

	@Override
	public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("projectId", getProjectId())
            .append("projectName", getProjectName())
            .append("deptId", getDeptId())
            .append("projectSign", getProjectSign())
            .toString();
    }
}
