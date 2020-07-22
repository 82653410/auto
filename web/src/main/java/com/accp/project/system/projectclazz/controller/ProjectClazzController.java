package com.accp.project.system.projectclazz.controller;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.accp.project.system.clazz.service.IClazzService;
import com.accp.project.system.projectclazz.domain.ProjectClazz;
import com.accp.project.system.projectclazz.service.IProjectClazzService;

/**
 * 项目绑定多班级
 * @author tang
 *
 */
@Controller
@RequestMapping("/system/projectClazz")
public class ProjectClazzController {
	
	@Autowired
	private IProjectClazzService projectClazzService;
	
	

	/**
	 * 加载班级树
	 * @param projectId
	 * @param mmap
	 * @return
	 */
	@RequiresPermissions("system:project:list")
	@GetMapping("/list/{id}")
	@ResponseBody
	public List<ProjectClazz> list(@PathVariable("id") Integer projectId)
	{
		return projectClazzService.selectByProjectId(projectId);
	}
	
	/**
	 * 加载班级树
	 * @param projectId
	 * @param mmap
	 * @return
	 */
	@GetMapping("/selectClazzTree/{projectId}")
    public String selectClazzTree(@PathVariable("projectId") Integer projectId, ModelMap mmap)
    {
        mmap.put("projectId", projectId);
        
        //根据查询到的已绑定的班级list ,clazzId查询clazz的详细信息
        
        return "system/project/tree";
    }
	

	/**
	 * 加载班级树
	 * @param projectId
	 * @param mmap
	 * @return
	 */
	@GetMapping("/selectClazzTree")
    public String selectClazzTree(ModelMap mmap)
    {
        mmap.put("projectId", 0);
        
        //根据查询到的已绑定的班级list ,clazzId查询clazz的详细信息
        
        return "system/project/tree";
    }
}
