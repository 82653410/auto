package com.accp.project.system.project.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.accp.common.exception.BusinessException;
import com.accp.common.utils.poi.ExcelUtil;
import com.accp.common.utils.security.PermissionUtils;
import com.accp.framework.aspectj.lang.annotation.Log;
import com.accp.framework.aspectj.lang.enums.BusinessType;
import com.accp.framework.web.controller.BaseController;
import com.accp.framework.web.domain.AjaxResult;
import com.accp.framework.web.page.TableDataInfo;
import com.accp.project.system.clazz.domain.Clazz;
import com.accp.project.system.project.domain.Project;
import com.accp.project.system.projectclazz.domain.ProjectClazz;
import com.accp.project.system.project.service.IProjectService;

/**
 * 测试项目管理 信息操作处理
 * 
 * uckyframe
 *
 */
@Controller
@RequestMapping("/system/project")
public class ProjectController extends BaseController
{
	@Autowired
	private IProjectService projectService;
	
	
	
	@RequiresPermissions("system:project:view")
	@GetMapping()
	public String project()
	{
	    return "system/project/project";
	}
	
	/**
	 * 查询测试项目管理列表
	 */
	@RequiresPermissions("system:project:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(Project project)
	{
		startPage();
        List<Project> list = projectService.selectProjectList(project);
		return getDataTable(list);
	}
	
	
	/**
	 * 导出测试项目管理列表
	 */
	@RequiresPermissions("system:project:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(Project project)
    {
    	List<Project> list = projectService.selectProjectList(project);
        ExcelUtil<Project> util = new ExcelUtil<>(Project.class);
        return util.exportExcel(list, "project");
    }
	
	/**
	 * 新增测试项目管理
	 */
	@GetMapping("/add")
	public String add()
	{
	    return "system/project/add";
	}
	
	/**
	 * 新增保存测试项目管理
	 */
	@RequiresPermissions("system:project:add")
	@Log(title = "测试项目管理", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(Project project,String clazzId)
	{		
		return toAjax(projectService.insertProject(project,clazzId));
	}

	/**
	 * 修改测试项目管理
	 */
	@GetMapping("/edit/{projectId}")
	public String edit(@PathVariable("projectId") Integer projectId, ModelMap mmap)
	{
		Project project = projectService.selectProjectById(projectId);
		mmap.put("project", project);
	    return "system/project/edit";
	}
	
	/**
	 * 修改保存测试项目管理
	 */
	@RequiresPermissions("system:project:edit")
	@Log(title = "测试项目管理", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(Project project,String clazzId,String clazzName)
	{	
		if(clazzId==null) {
			return error("参数不能为空");
		}
		if(!PermissionUtils.isProjectPermsPassByProjectId(project.getProjectId())){
			return error("没有此项目保存权限");
		}
		if(!projectService.checkClazzIdPresence(clazzId)) {
			return error("参数错误，班级不存在");
		} 
		
		return toAjax(projectService.updateProject(project,clazzId));
	}
	
	/**
	 * 删除测试项目管理
	 */
	@RequiresPermissions("system:project:remove")
	@Log(title = "测试项目管理", businessType = BusinessType.DELETE)
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(String ids)
	{
        try
        {
    		return toAjax(projectService.deleteProjectByIds(ids));
        }
        catch (BusinessException e)
        {
            return error(e.getMessage());
        }
	}
	
    /**
     * 校验项目名称唯一性
     */
    @PostMapping("/checkProjectNameUnique")
    @ResponseBody
    public String checkProjectNameUnique(Project project)
    {
        return projectService.checkProjectNameUnique(project);
    }
    
    /**
     * 校验项目标识唯一性
     */
    @PostMapping("/checkProjectSignUnique")
    @ResponseBody
    public String checkProjectSignUnique(Project project)
    {
        return projectService.checkProjectSignUnique(project);
    }
}
