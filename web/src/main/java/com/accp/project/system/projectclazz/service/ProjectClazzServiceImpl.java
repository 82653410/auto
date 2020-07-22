package com.accp.project.system.projectclazz.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//import com.accp.common.constant.UserConstants;
//import com.accp.project.system.clazz.domain.Clazz;
import com.accp.project.system.projectclazz.domain.ProjectClazz;
import com.accp.project.system.projectclazz.domain.ProjectClazzExample;
import com.accp.project.system.projectclazz.mapper.ProjectClazzMapper;

@Service
public class ProjectClazzServiceImpl implements IProjectClazzService {

	@Autowired
	private ProjectClazzMapper projectClazzMapper;
	
//
//    @Autowired
//    private ClazzMapper clazzMapper;
	/**
	 * 根据项目id查询绑定的部门
	 */
	@Override
	public List<ProjectClazz> selectByProjectId(Integer projectId) {
		ProjectClazzExample example = new ProjectClazzExample();
		example.createCriteria().andProjectIdEqualTo(projectId);

		List<ProjectClazz> list = projectClazzMapper.selectByExample(example);
		//ArrayList<Long> list2=;

//		Long[] Longs = new Long[list2.size()];
//
//		Long[] clazzId =list.toArray(Longs);
//		clazzMapper.selectClazzByIds(clazzId);
		return list;
	}

}
