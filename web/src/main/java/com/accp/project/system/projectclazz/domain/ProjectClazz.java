package com.accp.project.system.projectclazz.domain;

import com.accp.project.system.clazz.domain.Clazz;

public class ProjectClazz {
    private Integer projectId;

    private Integer clazzId;
    
    private Clazz clazz;
    
    

    public Clazz getClazz() {
		return clazz;
	}

	public void setClazz(Clazz clazz) {
		this.clazz = clazz;
	}

	public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Integer getClazzId() {
        return clazzId;
    }

    public void setClazzId(Integer clazzId) {
        this.clazzId = clazzId;
    }
}