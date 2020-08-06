package com.accp.project.testmanagmt.projectCase.controller;

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
import org.springframework.web.multipart.MultipartFile;

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
import com.accp.project.system.student.domain.Student;
import com.accp.project.testmanagmt.projectCase.domain.ProjectCase;
import com.accp.project.testmanagmt.projectCase.domain.ProjectCaseSteps;
import com.accp.project.testmanagmt.projectCase.service.IProjectCaseService;
import com.accp.project.testmanagmt.projectCase.service.IProjectCaseStepsService;
import com.accp.project.testmanagmt.projectCaseModule.domain.ProjectCaseModule;
import com.accp.project.testmanagmt.projectCaseModule.service.IProjectCaseModuleService;

/**
 * 项目测试用例管理 信息操作处理
 * 
 * uckyframe
 * 
 */
@Controller
@RequestMapping("/testmanagmt/projectCase")
public class ProjectCaseController extends BaseController
{
	@Autowired
	private IProjectCaseService projectCaseService;
	
	@Autowired
	private IProjectCaseStepsService projectCaseStepsService;
	
	@Autowired
	private IProjectService projectService;
	
	@Autowired
	private IProjectCaseModuleService projectCaseModuleService;
	
	@RequiresPermissions("testmanagmt:projectCase:view")
	@GetMapping()
	public String projectCase(ModelMap mmap)
	{
        List<Project> projects=projectService.selectProjectAll(0);
        mmap.put("projects", projects);
        if(projects.size()>0){
        	ProjectCaseModule projectCaseModule = new ProjectCaseModule();
            if(StringUtils.isNotEmpty(ShiroUtils.getProjectId())){
            	mmap.put("defaultProjectId", ShiroUtils.getProjectId());
                projectCaseModule = projectCaseModuleService.selectProjectCaseModuleParentZeroByProjectId(ShiroUtils.getProjectId());
            }
        	mmap.put("projectCaseModule", projectCaseModule);
        }
        
	    return "testmanagmt/projectCase/projectStudent";
	}
	
	/**
	 * 修改项目测试用例管理
	 */
	@GetMapping("/bind/{moduleId}")
	public String bind(@PathVariable("moduleId") Integer moduleId, ModelMap mmap)
	{
		List<Project> projects=projectService.selectProjectAll(0);
		mmap.put("projects", projects);
		ProjectCaseModule mod = new ProjectCaseModule();
		mod.setModuleId(moduleId);
    	mmap.put("projectCaseModule", mod);
	    return "testmanagmt/projectCase/projectCase";
	}

	/**
	 * 查询项目测试用例管理列表
	 */
	@RequiresPermissions("testmanagmt:projectCase:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(ProjectCase projectCase)
	{
		if(StringUtils.isNotEmpty(projectCase.getModuleId())){
			ProjectCaseModule projectCaseModule = projectCaseModuleService.selectProjectCaseModuleById(projectCase.getModuleId());
			if(projectCaseModule.getParentId().equals(0)){
				projectCase.setModuleId(null);
			}
		}
		
		startPage();
        List<ProjectCase> list = projectCaseService.selectProjectCaseList(projectCase);
		return getDataTable(list);
	}
	
	
	/**
	 * 导出项目测试用例管理列表
	 */
	@RequiresPermissions("testmanagmt:projectCase:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(ProjectCase projectCase)
    {
		if(StringUtils.isNotEmpty(projectCase.getModuleId())){
			ProjectCaseModule projectCaseModule = projectCaseModuleService.selectProjectCaseModuleById(projectCase.getModuleId());
			if(projectCaseModule.getParentId().equals(0)){
				projectCase.setModuleId(null);
			}
		}
		
    	List<ProjectCase> list = projectCaseService.selectProjectCaseList(projectCase);
        ExcelUtil<ProjectCase> util = new ExcelUtil<>(ProjectCase.class);
        return util.exportExcel(list, "测试用例");
    }
	
	/**
	 * 导出项目测试用例步骤列表
	 */
	@RequiresPermissions("testmanagmt:projectCase:export")
    @PostMapping("/exportSteps")
    @ResponseBody
    public AjaxResult exportStep(ProjectCaseSteps projectCaseSteps)
    {
		
    	List<ProjectCaseSteps> list = projectCaseStepsService.selectProjectCaseStepsList(projectCaseSteps);
        ExcelUtil<ProjectCaseSteps> util = new ExcelUtil<>(ProjectCaseSteps.class);
        return util.exportExcel(list, "测试用例步骤");
    }
	
	
	/**
	 * 导入项目测试用例管理列表
	 */

	@Log(title = "测试用例", businessType = BusinessType.IMPORT)
	@RequiresPermissions("testmanagmt:projectCase:import")
    @PostMapping("/import")
    @ResponseBody
    public AjaxResult impor(MultipartFile file) throws Exception
    {
		ExcelUtil<ProjectCase> util = new ExcelUtil<>(ProjectCase.class);
		List<ProjectCase> caseList = util.importExcels(file.getInputStream());
		
		
		ExcelUtil<ProjectCaseSteps> utils = new ExcelUtil<>(ProjectCaseSteps.class);
		List<ProjectCaseSteps> caseStepList = utils.importExcelss(file.getInputStream());
		
		StringBuffer sb = new StringBuffer();

		sb.append(projectCaseService.importProjectCase(caseList));
		sb.append(projectCaseStepsService.importProjectCaseSteps(caseStepList));
		
		return AjaxResult.success(sb.toString());
    }
	
	/**
	 * 新增项目测试用例管理
	 */
	@GetMapping("/add")
	public String add(ModelMap mmap) {
		List<Project> projects = projectService.selectProjectAll(0);
		mmap.put("projects", projects);
		if (projects.size() > 0) {
			ProjectCaseModule projectCaseModule;
			if (StringUtils.isNotEmpty(ShiroUtils.getProjectId())) {
				mmap.put("defaultProjectId", ShiroUtils.getProjectId());
				projectCaseModule = projectCaseModuleService
						.selectProjectCaseModuleParentZeroByProjectId(ShiroUtils.getProjectId());
			} else {
				projectCaseModule = projectCaseModuleService
						.selectProjectCaseModuleParentZeroByProjectId(projects.get(0).getProjectId());
			}
			mmap.put("projectCaseModule", projectCaseModule);
		}
		return "testmanagmt/projectCase/add";
	}
	
	/**
	 * 新增项目测试用例管理
	 */
	@GetMapping("/add/{moduleId}")
	public String addid(@PathVariable("moduleId") Integer moduleId,ModelMap mmap) {
		List<Project> projects = projectService.selectProjectAll(0);
		mmap.put("projects", projects);
		
		ProjectCaseModule mod = projectCaseModuleService.selectProjectCaseModuleById(moduleId);
		mmap.put("projectCaseModule", mod);
		
		return "testmanagmt/projectCase/add";
	}
	
	/**
	 * 新增保存项目测试用例管理
	 */
	@RequiresPermissions("testmanagmt:projectCase:add")
	@Log(title = "项目测试用例管理", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(ProjectCase projectCase)
	{
		if(!PermissionUtils.isProjectPermsPassByProjectId(projectCase.getProjectId())){
			return error("没有此项目保存用例权限");
		}
		
		return toAjax(projectCaseService.insertProjectCase(projectCase));
	}

	/**
	 * 修改项目测试用例管理
	 */
	@GetMapping("/edit/{caseId}")
	public String edit(@PathVariable("caseId") Integer caseId, ModelMap mmap)
	{
		ProjectCase projectCase = projectCaseService.selectProjectCaseById(caseId);
		mmap.put("projectCase", projectCase);
    	mmap.put("projectCaseModule", projectCase.getProjectCaseModule());
	    return "testmanagmt/projectCase/edit";
	}
	

	
	
	/**
	 * 修改保存项目测试用例管理
	 */
	@RequiresPermissions("testmanagmt:projectCase:edit")
	@Log(title = "项目测试用例管理", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(ProjectCase projectCase)
	{		
		if(!PermissionUtils.isProjectPermsPassByProjectId(projectCase.getProjectId())){
			return error("没有此项目修改用例权限");
		}
		
		return toAjax(projectCaseService.updateProjectCase(projectCase));
	}
	
	/**
	 * 复制用例
	 * @param caseId 用例ID
	 * @param mmap 返回数据模型
	 *
	 * 
	 */
	@GetMapping("/copy/{caseId}")
	public String copy(@PathVariable("caseId") String caseId, ModelMap mmap)
	{
		ProjectCase projectCase;
		if(caseId.contains(","))
		{
			String[] caseIdArray=caseId.split(",");
			//批量复制
			projectCase=projectCaseService.selectProjectCaseById(Integer.valueOf(caseIdArray[0]));
			mmap.put("caseIdList", caseId);
		}
		else
		{
			//复制一个
			projectCase=projectCaseService.selectProjectCaseById(Integer.valueOf(caseId));
			projectCase.setCaseName("Copy【"+projectCase.getCaseName()+"】");
		}

        List<Project> projects=projectService.selectProjectAll(projectCase.getProjectId());
        mmap.put("projects", projects);
        if(projects.size()>0){
        	ProjectCaseModule projectCaseModule = projectCaseModuleService.selectProjectCaseModuleParentZeroByProjectId(projects.get(0).getProjectId());
        	mmap.put("projectCaseModule", projectCaseModule);
        }
		mmap.put("projectCase", projectCase);
		mmap.put("projectCaseModule", projectCase.getProjectCaseModule());
	    return "testmanagmt/projectCase/copy";
	}
	
	/**
	 * 下载导入模板
	 * ummer
	 *
	 */
	@RequiresPermissions("testmanagmt:projectCase:view")
	@GetMapping("/importTemplate")
	@ResponseBody
	public AjaxResult importTemplate()
	{
		return AjaxResult.success("导入步骤模板.xlsx");
	}

	/**
	 * 批量复制用例
	 * @param projectCase 测试用例对象
	 * J
	 * 
	 */
	@RequiresPermissions("testmanagmt:projectCase:add")
	@Log(title = "项目测试用例管理", businessType = BusinessType.INSERT)
	@PostMapping("/batchCopy")
	@ResponseBody
	public AjaxResult batchCopy(ProjectCase projectCase)
	{
		if(projectCase.getCaseIdList()==null)
		{
			return error("请先选择用例再复制！");
		}
		String[] ids =projectCase.getCaseIdList().split(",");
		int num=0;
		for (String id : ids) {
			if(StringUtils.isNotEmpty(id))
			{
				Integer caseId=Integer.valueOf(id);
				ProjectCase copyProjectCase=projectCaseService.selectProjectCaseById(caseId);
				if(copyProjectCase==null)
				{
					continue;
				}
				ProjectCaseSteps projectCaseSteps = new ProjectCaseSteps();
				projectCaseSteps.setCaseId(caseId);
				List<ProjectCaseSteps> listSteps = projectCaseStepsService.selectProjectCaseStepsList(projectCaseSteps);
				copyProjectCase.setCaseId(0);
				copyProjectCase.setCaseName("Copy【"+copyProjectCase.getCaseName()+"】");
				copyProjectCase.setProjectId(projectCase.getProjectId());
				copyProjectCase.setModuleId(projectCase.getModuleId());
				num=projectCaseService.insertProjectCase(copyProjectCase);
				for(ProjectCaseSteps step:listSteps){
					step.setStepId(0);
					step.setCaseId(copyProjectCase.getCaseId());
					if(!step.getProjectId().equals(copyProjectCase.getProjectId())){
						step.setProjectId(copyProjectCase.getProjectId());
						step.setExtend(null);
					}
					projectCaseStepsService.insertProjectCaseSteps(step);
				}
			}
		}
		return toAjax(num);
	}

	/**
	 * 复制用例
	 * @param projectCase 测试用例对象
	 *
	 * 
	 */
	@RequiresPermissions("testmanagmt:projectCase:add")
	@Log(title = "项目测试用例管理", businessType = BusinessType.INSERT)
	@PostMapping("/copy")
	@ResponseBody
	public AjaxResult copySave(ProjectCase projectCase)
	{
		if(!PermissionUtils.isProjectPermsPassByProjectId(projectCase.getProjectId())){
			return error("没有此项目复制用例权限");
		}
		
		ProjectCaseSteps projectCaseSteps = new ProjectCaseSteps();
		projectCaseSteps.setCaseId(projectCase.getCaseId());
		List<ProjectCaseSteps> listSteps = projectCaseStepsService.selectProjectCaseStepsList(projectCaseSteps);
		projectCase.setCaseId(0);
		int num=projectCaseService.insertProjectCase(projectCase);
		for(ProjectCaseSteps step:listSteps){
			step.setStepId(0);
			step.setCaseId(projectCase.getCaseId());
			if(!step.getProjectId().equals(projectCase.getProjectId())){
				step.setProjectId(projectCase.getProjectId());
				step.setExtend(null);
			}
			projectCaseStepsService.insertProjectCaseSteps(step);
		}
		return toAjax(num+listSteps.size());
	}
	
	/**
	 * 删除项目测试用例管理
	 */
	@RequiresPermissions("testmanagmt:projectCase:remove")
	@Log(title = "项目测试用例管理", businessType = BusinessType.DELETE)
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(String ids)
	{	
        try
        {
        	return toAjax(projectCaseService.deleteProjectCaseByIds(ids));
        }
        catch (BusinessException e)
        {
            return error(e.getMessage());
        }
	}

    /**
     * 校验测试用例名称唯一性
     * @param projectCase 测试用例对象
     *
     * 
     */
    @PostMapping("/checkProjectCaseNameUnique")
    @ResponseBody
    public String checkProjectCaseNameUnique(ProjectCase projectCase)
    {
        return projectCaseService.checkProjectCaseNameUnique(projectCase);
    }
    
    /**
	 * 找带模块的学生
	 */
	@PostMapping("/getStudentHasMod")
	@ResponseBody
	public TableDataInfo getStudentHasMod(ModelMap mmap)
	{	
		startPage();
		List<Student> list = projectCaseService.getStudentHasMod();
		return getDataTable(list);
	}
	
//	/**
//	 * 批量导入用例模块
//	 * @param file 文件对象
//	 * ummer
//	 *
//	 */
//	@Log(title = "用例管理", businessType = BusinessType.IMPORT)
//	@RequiresPermissions("testmanagmt:projectCase:import")
//	@PostMapping("/import")
//	@ResponseBody
//	public AjaxResult importProjectCase(MultipartFile file) throws Exception
//	{
////		ExcelUtil<ProjectCase> util = new ExcelUtil<>(ProjectCase.class);
////		List<ProjectCase> caseList = util.importExcels(file.getInputStream());
////		ExcelUtil<ProjectCaseSteps> utils = new ExcelUtil<>(ProjectCaseSteps.class);
////		List<ProjectCaseSteps> caseStepList = utils.importExcelss(file.getInputStream());
//		System.out.println();
//		return null;
//	}
}
