package com.accp.project.system.projectclazz.service;

import java.util.List;

import com.accp.project.system.projectclazz.domain.ProjectClazz;
public interface IProjectClazzService {
	
	/**
	 * 查询指定的idList
	 * @param projectclazz
	 * @return
	 */ 
	List<ProjectClazz> selectByProjectId(Integer projectId);
	
	//int updateProjectClazz(Integer projectId,String clazzId);
}
