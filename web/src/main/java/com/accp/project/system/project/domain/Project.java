package com.accp.project.system.project.domain;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.accp.framework.web.domain.BaseEntity;
import com.accp.project.system.clazz.domain.Clazz;
import com.accp.project.system.projectclazz.domain.ProjectClazz;

/**
 * 测试项目管理表 sys_project
 * 
 * uckyframe
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
	/** 归属部门实体 */
	
	/** 项目标记 */
	
	private List<Clazz> clazzs;
	
	
	
	public List<Clazz> getClazzs() {
		return clazzs;
	}

	public void setClazzs(List<Clazz> clazzs) {
		this.clazzs = clazzs;
	}





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

	



	public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("projectId", getProjectId())
            .append("projectName", getProjectName())
            //.append("deptId", getDeptId())
            .append("projectSign", getProjectSign())
            .toString();
    }
}
