package com.accp.remote.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * ���Լƻ�����ʵ��
 *
 *
 *
 *
 *
 *
 */
public class ProjectPlanCase extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/** �ƻ�����ID */
	private Integer planCaseId;
	/** ����ID */
	private Integer caseId;
	/** ���Լƻ�ID */
	private Integer planId;
	/** �������ȼ� ����ԽС�����ȼ�Խ�� */
	private Integer priority;

	public void setPlanCaseId(Integer planCaseId) 
	{
		this.planCaseId = planCaseId;
	}

	public Integer getPlanCaseId() 
	{
		return planCaseId;
	}
	public void setCaseId(Integer caseId) 
	{
		this.caseId = caseId;
	}

	public Integer getCaseId() 
	{
		return caseId;
	}
	public void setPlanId(Integer planId) 
	{
		this.planId = planId;
	}

	public Integer getPlanId() 
	{
		return planId;
	}
	public void setPriority(Integer priority) 
	{
		this.priority = priority;
	}

	public Integer getPriority() 
	{
		return priority;
	}

	@Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("planCaseId", getPlanCaseId())
            .append("caseId", getCaseId())
            .append("planId", getPlanId())
            .append("priority", getPriority())
            .toString();
    }
}
