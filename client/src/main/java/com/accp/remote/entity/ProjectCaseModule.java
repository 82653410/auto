package com.accp.remote.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 用例模块实体
 * 
 * 
 * 
 * 
 *
 *
 */
public class ProjectCaseModule extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/** 模块ID */
	private Integer moduleId;
	/** 模块名字 */
	private String moduleName;
	/** 项目ID */
	private Integer projectId;
	/** 父模块id */
	private Integer parentId;
	/** 祖模块列表 */
	private String ancestors;
	/** 显示顺序 */
	private Integer orderNum;
	/** 所属项目名称 */
	private String projectName;
	
	public void setModuleId(Integer moduleId) 
	{
		this.moduleId = moduleId;
	}

	public Integer getModuleId() 
	{
		return moduleId;
	}
	public void setModuleName(String moduleName) 
	{
		this.moduleName = moduleName;
	}

	public String getModuleName() 
	{
		return moduleName;
	}
	public void setProjectId(Integer projectId) 
	{
		this.projectId = projectId;
	}

	public Integer getProjectId() 
	{
		return projectId;
	}
	public void setParentId(Integer parentId) 
	{
		this.parentId = parentId;
	}

	public Integer getParentId() 
	{
		return parentId;
	}
	public void setAncestors(String ancestors) 
	{
		this.ancestors = ancestors;
	}

	public String getAncestors() 
	{
		return ancestors;
	}
	public void setOrderNum(Integer orderNum) 
	{
		this.orderNum = orderNum;
	}

	public Integer getOrderNum() 
	{
		return orderNum;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	@Override
	public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("moduleId", getModuleId())
            .append("moduleName", getModuleName())
            .append("projectId", getProjectId())
            .append("parentId", getParentId())
            .append("ancestors", getAncestors())
            .append("orderNum", getOrderNum())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .append("projectName", getProjectName())
            .toString();
    }
}
