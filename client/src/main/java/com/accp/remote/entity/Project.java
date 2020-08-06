package com.accp.remote.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * ��Ŀʵ��
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
	
	/** ��ĿID */
	private Integer projectId;
	/** ��Ŀ���� */
	private String projectName;
	/** �������� */
	private Integer deptId;
	/** ��Ŀ��ʶ */
	private String projectSign;
	/** ��Ŀ��� */
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
