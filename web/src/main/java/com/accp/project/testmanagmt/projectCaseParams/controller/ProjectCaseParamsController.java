package com.accp.project.testmanagmt.projectCaseParams.controller;

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
import com.accp.common.utils.StringUtils;
import com.accp.common.utils.poi.ExcelUtil;
import com.accp.common.utils.security.PermissionUtils;
import com.accp.common.utils.security.ShiroUtils;
import com.accp.framework.aspectj.lang.annotation.Log;
import com.accp.framework.aspectj.lang.enums.BusinessType;
import com.accp.framework.web.controller.BaseController;
import com.accp.framework.web.domain.AjaxResult;
import com.accp.framework.web.page.TableDataInfo;
import com.accp.project.system.project.domain.Project;
import com.accp.project.system.project.service.IProjectService;
import com.accp.project.testmanagmt.projectCaseParams.domain.ProjectCaseParams;
import com.accp.project.testmanagmt.projectCaseParams.service.IProjectCaseParamsService;

/**
 * 用例公共参数 信息操作处理
 * 
 * uckyframe
 * 
 */
@Controller
@RequestMapping("/testmanagmt/projectCaseParams")
public class ProjectCaseParamsController extends BaseController
{
	@Autowired
	private IProjectCaseParamsService projectCaseParamsService;
	
	@Autowired
	private IProjectService projectService;
	
	@RequiresPermissions("testmanagmt:projectCaseParams:view")
	@GetMapping()
	public String projectCaseParams(ModelMap mmap)
	{
        List<Project> projects=projectService.selectProjectAll(0);
        mmap.put("projects", projects);
        if(StringUtils.isNotEmpty(ShiroUtils.getProjectId())){
        	mmap.put("defaultProjectId", ShiroUtils.getProjectId());
        }
	    return "testmanagmt/projectCaseParams/projectCaseParams";
	}
	
	/**
	 * 查询用例公共参数列表
	 */
	@RequiresPermissions("testmanagmt:projectCaseParams:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(ProjectCaseParams projectCaseParams)
	{
		startPage();
        List<ProjectCaseParams> list = projectCaseParamsService.selectProjectCaseParamsList(projectCaseParams);
		return getDataTable(list);
	}
	
	
	/**
	 * 导出用例公共参数列表
	 */
	@RequiresPermissions("testmanagmt:projectCaseParams:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(ProjectCaseParams projectCaseParams)
    {
    	List<ProjectCaseParams> list = projectCaseParamsService.selectProjectCaseParamsList(projectCaseParams);
        ExcelUtil<ProjectCaseParams> util = new ExcelUtil<>(ProjectCaseParams.class);
        return util.exportExcel(list, "projectCaseParams");
    }
	
	/**
	 * 新增用例公共参数
	 */
	@GetMapping("/add")
	public String add(ModelMap mmap)
	{
        List<Project> projects=projectService.selectProjectAll(0);
        mmap.put("projects", projects);
        if(StringUtils.isNotEmpty(ShiroUtils.getProjectId())){
        	mmap.put("defaultProjectId", ShiroUtils.getProjectId());
        }
	    return "testmanagmt/projectCaseParams/add";
	}
	
	/**
	 * 新增保存用例公共参数
	 */
	@RequiresPermissions("testmanagmt:projectCaseParams:add")
	@Log(title = "用例公共参数", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(ProjectCaseParams projectCaseParams)
	{		
		if(!PermissionUtils.isProjectPermsPassByProjectId(projectCaseParams.getProjectId())){
			return error("没有此项目保存用例参数权限");
		}		
		return toAjax(projectCaseParamsService.insertProjectCaseParams(projectCaseParams));
	}

	/**
	 * 修改用例公共参数
	 */
	@GetMapping("/edit/{paramsId}")
	public String edit(@PathVariable("paramsId") Integer paramsId, ModelMap mmap)
	{
        List<Project> projects=projectService.selectProjectAll(0);
        mmap.put("projects", projects);
		ProjectCaseParams projectCaseParams = projectCaseParamsService.selectProjectCaseParamsById(paramsId);
		mmap.put("projectCaseParams", projectCaseParams);
	    return "testmanagmt/projectCaseParams/edit";
	}
	
	/**
	 * 修改保存用例公共参数
	 */
	@RequiresPermissions("testmanagmt:projectCaseParams:edit")
	@Log(title = "用例公共参数", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(ProjectCaseParams projectCaseParams)
	{		
		if(!PermissionUtils.isProjectPermsPassByProjectId(projectCaseParams.getProjectId())){
			return error("没有此项目修改用例参数权限");
		}
		return toAjax(projectCaseParamsService.updateProjectCaseParams(projectCaseParams));
	}
	
	/**
	 * 删除用例公共参数
	 */
	@RequiresPermissions("testmanagmt:projectCaseParams:remove")
	@Log(title = "用例公共参数", businessType = BusinessType.DELETE)
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(String ids)
	{		
        try
        {
        	return toAjax(projectCaseParamsService.deleteProjectCaseParamsByIds(ids));
        }catch (BusinessException e)
        {
            return error(e.getMessage());
        }		
	}
	
    /**
     * 校验用例参数名称唯一性
     * @param projectCaseParams 公共参数对象
     *
     * 
     */
    @PostMapping("/checkProjectCaseParamsNameUnique")
    @ResponseBody
    public String checkProjectCaseParamsNameUnique(ProjectCaseParams projectCaseParams)
    {
        return projectCaseParamsService.checkProjectCaseParamsNameUnique(projectCaseParams);
    }
	
}
