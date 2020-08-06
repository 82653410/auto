package com.accp.remote.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * ���Լƻ�ʵ��
 * 
 * 
 * 
 * 
 *
 *
 */
public class ProjectPlan extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/** ���Լƻ�ID */
	private Integer planId;
	/** ���Լƻ����� */
	private String planName;
	/** �ƻ����������� */
	private Integer planCaseCount;
	/** ��ĿID */
	private Integer projectId;
	/** ������Ŀʵ�� */
	private Project project;

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public void setPlanId(Integer planId) 
	{
		this.planId = planId;
	}

	public Integer getPlanId() 
	{
		return planId;
	}
	public void setPlanName(String planName) 
	{
		this.planName = planName;
	}

	public String getPlanName() 
	{
		return planName;
	}
	public void setPlanCaseCount(Integer planCaseCount) 
	{
		this.planCaseCount = planCaseCount;
	}

	public Integer getPlanCaseCount() 
	{
		return planCaseCount;
	}
	public void setProjectId(Integer projectId) 
	{
		this.projectId = projectId;
	}

	public Integer getProjectId() 
	{
		return projectId;
	}

	@Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("planId", getPlanId())
            .append("planName", getPlanName())
            .append("planCaseCount", getPlanCaseCount())
            .append("projectId", getProjectId())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .append("project", getProject())
            .toString();
    }
}
