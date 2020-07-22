package com.accp.project.testmanagmt.projectCaseModule.controller;

import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
import com.accp.project.system.clazz.service.IClazzService;
import com.accp.project.system.project.domain.Project;
import com.accp.project.system.project.service.IProjectService;
import com.accp.project.system.student.domain.Student;
import com.accp.project.testmanagmt.projectCaseModule.domain.ProjectCaseModule;
import com.accp.project.testmanagmt.projectCaseModule.service.IProjectCaseModuleService;
import org.springframework.web.multipart.MultipartFile;

/**
 * 测试用例模块管理 信息操作处理
 * 
 * uckyframe
 *
 */
@Controller
@RequestMapping("/testmanagmt/projectCaseModule")
public class ProjectCaseModuleController extends BaseController
{
	@Autowired
	private IProjectCaseModuleService projectCaseModuleService;
	
	@Autowired
	private IProjectService projectService;
	
	@Autowired
	private IClazzService clazzService;
	
	@RequiresPermissions("testmanagmt:projectCaseModule:view")
	@GetMapping()
	public String projectCaseModule(ModelMap mmap)
	{
        List<Project> projects=projectService.selectProjectAll(0);
        mmap.put("projects", projects);
	    return "testmanagmt/projectCaseModule/projectCaseModule";
	}
	
	/**
	 * 查询测试用例模块管理列表
	 * 旧
	 */
	@RequiresPermissions("testmanagmt:projectCaseModule:list")
	@GetMapping("/list")
	@ResponseBody
	public List<ProjectCaseModule> list(ProjectCaseModule projectCaseModule)
	{
		return projectCaseModuleService.selectProjectCaseModuleList(projectCaseModule);
	}
	
	/**
	 * 查询测试用例模块管理列表
	 * 新
	 */
	@RequiresPermissions("testmanagmt:projectCaseModule:list")
	@GetMapping("/lists")
	@ResponseBody
	public String lists(ProjectCaseModule projectCaseModule)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		List<Object> list = projectCaseModuleService.selectProjectCaseModuleLists(projectCaseModule);
		Integer ii=0;
		for (int i = 0; i < list.size(); i++) {
			sb.append("{");
			if(list.get(i) instanceof Project) {

				Project p =(Project) list.get(i) ;
				ii=p.getProjectId();
				sb.append("\"moduleId\":"+ii+ii.toString()+",");
				sb.append("\"moduleName\":\""+p.getProjectName()+"\",");
				sb.append("\"parentId\":"+0+",");
				sb.append("\"orderNum\":"+0+",");
				sb.append("\"updateTime\":\""+p.getUpdateTime()+"\",");
				sb.append("\"updateBy\":\""+p.getUpdateBy()+"\",");
				sb.append("\"mid\":"+p.getProjectId()+",");
				sb.append("\"pid\":"+p.getProjectId());
			}else if(list.get(i) instanceof Clazz) {
				Clazz c =(Clazz) list.get(i) ;
				sb.append("\"moduleId\":"+ii+c.getClazzId().toString()+",");
				sb.append("\"moduleName\":\""+c.getClazzName()+"\",");
				sb.append("\"parentId\":"+ii+c.getParentId().toString()+",");
				sb.append("\"orderNum\":"+c.getOrderNum()+",");
				sb.append("\"updateTime\":\""+c.getUpdateTime()+"\",");
				sb.append("\"updateBy\":\""+c.getUpdateBy()+"\",");
				sb.append("\"mid\":"+c.getClazzId()+",");
				sb.append("\"pid\":"+ii);
			}

			sb.append("}");
			if(i<list.size()-1) {
				sb.append(",");
			}
		}
		sb.append("]");
		return sb.toString();
	}
	
	
	/**
	 * 导出测试用例模块管理列表
	 */
	@RequiresPermissions("testmanagmt:projectCaseModule:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(ProjectCaseModule projectCaseModule)
    {
    	List<ProjectCaseModule> list = projectCaseModuleService.selectProjectCaseModuleList(projectCaseModule);
        ExcelUtil<ProjectCaseModule> util = new ExcelUtil<>(ProjectCaseModule.class);
        return util.exportExcel(list, "projectCaseModule");
    }
	
	/**
	 * 新增测试用例模块管理
	 */
	@GetMapping("/add/{moduleId}")
	public String add(@PathVariable("moduleId") int moduleId, ModelMap mmap)
	{
		ProjectCaseModule projectCaseModule = projectCaseModuleService.selectProjectCaseModuleById(moduleId);
		mmap.put("projectCaseModule",projectCaseModule);
		mmap.put("projectId",projectCaseModule.getProjectId());
		mmap.put("maxOrderNum", projectCaseModuleService.selectProjectCaseModuleMaxOrderNumByParentId(moduleId));
	    return "testmanagmt/projectCaseModule/add";
	}
	
	/**
	 * 新增保存测试用例模块管理
	 */
	@RequiresPermissions("testmanagmt:projectCaseModule:add")
	@Log(title = "测试用例模块管理", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(ProjectCaseModule projectCaseModule)
	{
		if(!PermissionUtils.isProjectPermsPassByProjectId(projectCaseModule.getProjectId())){
			return error("没有此项目保存用例模块权限");
		}
		
		return toAjax(projectCaseModuleService.insertProjectCaseModule(projectCaseModule));
	}

	/**
	 * 修改测试用例模块管理
	 */
	@GetMapping("/edit/{moduleId}")
	public String edit(@PathVariable("moduleId") Integer moduleId, ModelMap mmap)
	{
		ProjectCaseModule projectCaseModule=projectCaseModuleService.selectProjectCaseModuleById(moduleId);
		mmap.put("projectCaseModule", projectCaseModule);
		mmap.put("parentProjectCaseModule", projectCaseModuleService.selectProjectCaseModuleById(projectCaseModule.getParentId()));
	    return "testmanagmt/projectCaseModule/edit";
	}
	
	/**
	 * 修改保存测试用例模块管理
	 */
	@RequiresPermissions("testmanagmt:projectCaseModule:edit")
	@Log(title = "测试用例模块管理", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(ProjectCaseModule projectCaseModule)
	{	
		if(!PermissionUtils.isProjectPermsPassByProjectId(projectCaseModule.getProjectId())){
			return error("没有此项目修改用例模块权限");
		}
		
		return toAjax(projectCaseModuleService.updateProjectCaseModule(projectCaseModule));
	}
	
	/**
	 * 删除测试用例模块管理
	 */
	@RequiresPermissions("testmanagmt:projectCaseModule:remove")
	@Log(title = "测试用例模块管理", businessType = BusinessType.DELETE)
	@PostMapping( "/remove/{moduleId}")
	@ResponseBody
	public AjaxResult remove(@PathVariable("moduleId") Integer moduleId)
	{
        if (projectCaseModuleService.selectProjectCaseModuleByParentId(moduleId).size() > 0)
        {
            return error(1, "存在下级模块,不允许删除");
        }
        try
        {
        	return toAjax(projectCaseModuleService.deleteProjectCaseModuleById(moduleId));
        }
        catch (BusinessException e)
        {
            return error(e.getMessage());
        }
	}
	
    /**
     * 选择模块树
     * @param moduleId 模块ID
     * @param mmap 返回数据模型
     *
     *
     */
    @GetMapping("/selectProjectCaseModuleTree/{moduleId}")
    public String selectProjectCaseModuleTree(@PathVariable("moduleId") Integer moduleId, ModelMap mmap)
    {
        mmap.put("projectCaseModule", projectCaseModuleService.selectProjectCaseModuleById(moduleId));
        return "testmanagmt/projectCaseModule/tree";
    }

    /**
     * 加载模块列表树
     */
    @GetMapping("/treeData/{projectId}")
    @ResponseBody
    public List<Map<String, Object>> treeData(@PathVariable("projectId") Integer projectId)
    {
    	ProjectCaseModule projectCaseModule=new ProjectCaseModule();
    	projectCaseModule.setProjectId(projectId);
		return projectCaseModuleService.selectProjectCaseModuleTree(projectCaseModule);
    }

    /**
     * 根据项目ID获取根节点模块实体
     */
    @GetMapping("/getModuleByProjectId/{projectId}")
    @ResponseBody
    public ProjectCaseModule getModuleByProjectId(@PathVariable("projectId") Integer projectId)
    {
		return projectCaseModuleService.selectProjectCaseModuleParentZeroByProjectId(projectId);
    }

	/**
	 * 批量导入用例模块
	 * @param file 文件对象
	 * ummer
	 *
	 */
	@Log(title = "用例模块", businessType = BusinessType.IMPORT)
	@RequiresPermissions("testmanagmt:projectCaseModule:import")
	@PostMapping("/import")
	@ResponseBody
	public AjaxResult importProjectCaseModules(MultipartFile file) throws Exception
	{
		ExcelUtil<ProjectCaseModule> util = new ExcelUtil<>(ProjectCaseModule.class);
		List<ProjectCaseModule> modulesList = util.importExcel(file.getInputStream());
		String message = projectCaseModuleService.importProjectCaseModules(modulesList);
		return AjaxResult.success(message);
	}

	/**
	 * 下载导入模板
	 * ummer
	 *
	 */
	@RequiresPermissions("testmanagmt:projectCaseModule:view")
	@GetMapping("/importTemplate")
	@ResponseBody
	public AjaxResult importTemplate()
	{
		ExcelUtil<ProjectCaseModule> util = new ExcelUtil<>(ProjectCaseModule.class);
		return util.importTemplateExcel("用例模块");
	}
	
	
	
	
	
	
//	/**
//	 * 加载班级树
//	 * @param projectId
//	 * @param mmap
//	 * @return
//	 */
//	@GetMapping("/bind/{projectId}/{clazzId}")
//    public String selectClazzTree(@PathVariable("projectId") Integer projectId,@PathVariable("clazzId") Integer clazzId, ModelMap mmap)
//    {
//        mmap.put("clazzId", clazzId);
//        
//		//mmap.put("parentProjectCaseModule", projectCaseModuleService.selectProjectCaseModuleById(projectCaseModule.getParentId()));
//	    
//		mmap.put("projectId", projectId);
//        //根据查询到的已绑定的班级list ,clazzId查询clazz的详细信息
//        
//        return "testmanagmt/projectModuleStudent/tree";
//    }
	
	
	
	
	/**
	 * 绑定保存测试用例模块管理
	 */
	@GetMapping("/bind/{projectId}/{clazzId}")
	public String bindSave(@PathVariable("projectId") Integer projectId,@PathVariable("clazzId") Integer clazzId,String StudentNo,ModelMap mmap)
	{	
		mmap.put("projectId", projectId);
		mmap.put("clazzId", clazzId);
		return "testmanagmt/projectModuleStudent/bindtree";
	}
	
	/**
	 * 班级id查学生
	 */
	@PostMapping("/getStudent/{clazzId}")
	@ResponseBody
	public TableDataInfo bindClazz(@PathVariable("clazzId") Integer clazzId,ModelMap mmap)
	{	
		startPage();
		List<Student> list = clazzService.selectStudentByClazzId(clazzId);
		return getDataTable(list);
	}
	
	
	
	 
	/**
	 * 新增测试用例模块管理
	 */
	@GetMapping("/add/{projectId}/{studentNo}")
	public String addtwo(@PathVariable("projectId") Integer projectId,@PathVariable("studentNo") String studentNo, ModelMap mmap)
	{mmap.put("projectId",projectId);
		mmap.put("studentNo",studentNo);
		mmap.put("parentId", projectCaseModuleService.findModByProjectId(projectId));
	    return "testmanagmt/projectModuleStudent/add";
	}
	
	/**
	 * 学号和项目id查模块
	 */
	@PostMapping("/modlist/{projectId}/{studentNo}")
	@ResponseBody
	public List<ProjectCaseModule> findMod(@PathVariable("projectId") Integer projectId,@PathVariable("studentNo") String studentNo,ModelMap mmap)
	{	
		
		List<ProjectCaseModule> list = projectCaseModuleService.findModByProjectIdAndStuNo(projectId, studentNo);
		return list;
	}

	/**
	 * 学号查模块
	 */
	@PostMapping("/modlist/{studentNo}")
	@ResponseBody
	public List<ProjectCaseModule> findModByStuNo(@PathVariable("studentNo") String studentNo,ModelMap mmap)
	{	
		System.out.println(studentNo);
		List<ProjectCaseModule> list = projectCaseModuleService.selectModuleAndProject(studentNo);
		return list;
	}
	
	/**
	 * 新增保存测试用例模块管理
	 */
	@RequiresPermissions("testmanagmt:projectCaseModule:add")
	@Log(title = "测试用例模块管理", businessType = BusinessType.INSERT)
	@PostMapping("/adds")
	@ResponseBody
	public AjaxResult addSaveTwo(ProjectCaseModule projectCaseModule,String studentNo)
	{
		if(!PermissionUtils.isProjectPermsPassByProjectId(projectCaseModule.getProjectId())){
			return error("没有此项目保存用例模块权限");
		}
		int ret = projectCaseModuleService.insertProjectCaseModule(projectCaseModule);
		if(ret>0) {

			return toAjax(projectCaseModuleService.bindProjectCaseModuleStu(projectCaseModule.getModuleId(), studentNo));
		}
		else {
			return toAjax(0);
		}
	}

}
