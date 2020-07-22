package com.accp.project.system.clazz.controller;

import java.util.List;
import java.util.Map;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.accp.common.utils.StringUtils;
import com.accp.framework.aspectj.lang.annotation.Log;
import com.accp.framework.aspectj.lang.enums.BusinessType;
import com.accp.framework.web.controller.BaseController;
import com.accp.framework.web.domain.AjaxResult;
import com.accp.framework.web.page.TableDataInfo;
import com.accp.project.system.clazz.domain.Clazz;
import com.accp.project.system.clazz.service.IClazzService;
import com.accp.project.system.project.domain.Project;
import com.accp.project.system.role.domain.Role;
import com.accp.project.system.student.domain.Student;
import com.accp.project.testmanagmt.projectCaseModule.domain.ProjectCaseModule;

/**
 * 班级信息
 * 

 */
@Controller
@RequestMapping("/system/clazz")
public class ClazzController extends BaseController
{
    @Autowired
    private IClazzService clazzService;

    @RequiresPermissions("system:clazz:view")
    @GetMapping()
    public String clazz()
    {
        return "system/clazz/clazz";
    }

    @RequiresPermissions("system:clazz:list")
    @GetMapping("/list")
    @ResponseBody
    public List<Clazz> list(Clazz clazz)
    {
        return clazzService.selectClazzList(clazz);
    }
    
    @RequiresPermissions("system:clazz:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo lists(Clazz clazz)
    {
    	List<Clazz> list = clazzService.selectClazzList(clazz);
    	return getDataTable(list);
    }
    
    

    /**
     * 新增班级
     */
    @GetMapping("/add/{parentId}")
    public String add(@PathVariable("parentId") Long parentId, ModelMap mmap)
    {
        mmap.put("clazz", clazzService.selectClazzById(parentId));
        return "system/clazz/add";
    }

    /**
     * 新增保存班级
     */
    @Log(title = "班级管理", businessType = BusinessType.INSERT)
    @RequiresPermissions("system:clazz:add")
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(Clazz clazz)
    {
        return toAjax(clazzService.insertClazz(clazz));
    }

    /**
     * 修改
     */
    @GetMapping("/edit/{clazzId}")
    public String edit(@PathVariable("clazzId") Long clazzId, ModelMap mmap)
    {
        Clazz clazz = clazzService.selectClazzById(clazzId);
        if (StringUtils.isNotNull(clazz) && 100L == clazzId)
        {
            clazz.setParentName("无");
        }
        mmap.put("clazz", clazz);
        return "system/clazz/edit";
    }

    /**
     * 保存
     */
    @Log(title = "班级管理", businessType = BusinessType.UPDATE)
    @RequiresPermissions("system:clazz:edit")
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(Clazz clazz)
    {
        return toAjax(clazzService.updateClazz(clazz));
    }

    /**
     * 删除
     */
    @Log(title = "班级管理", businessType = BusinessType.DELETE)
    @RequiresPermissions("system:clazz:remove")
    @PostMapping("/remove/{clazzId}")
    @ResponseBody
    public AjaxResult remove(@PathVariable("clazzId") Long clazzId)
    {
        if (clazzService.selectClazzCount(clazzId) > 0)
        {
            return error(1, "存在下级班级,不允许删除");
        }
        if (clazzService.checkClazzExistStudent(clazzId))
        {
            return error(1, "班级存在学生,不允许删除");
        }
        return toAjax(clazzService.deleteClazzById(clazzId));
    }

    /**
     * 校验班级名称
     */
    @PostMapping("/checkClazzNameUnique")
    @ResponseBody
    public String checkClazzNameUnique(Clazz clazz)
    {
        return clazzService.checkClazzNameUnique(clazz);
    }

    /**
     * 选择班级树
     */
    @GetMapping("/selectClazzTree/{clazzId}")
    public String selectClazzTree(@PathVariable("clazzId") Long clazzId, ModelMap mmap)
    {
        mmap.put("clazz", clazzService.selectClazzById(clazzId));
        return "system/clazz/tree";
    }
    


    

    /**
     * 加载班级列表树
     */
    @GetMapping("/treeData")
    @ResponseBody
    public List<Map<String, Object>> treeData()
    {
        return clazzService.selectClazzTree(new Clazz());
    }

    /**
     * 加载角色班级（数据权限）列表树
     */
    @GetMapping("/roleClazzTreeData")
    @ResponseBody
    public List<Map<String, Object>> clazzTreeData(Role role)
    {
        return clazzService.roleClazzTreeData(role);
    }
    

    /**
     * 加载项目班级（数据权限）列表树
     */
    @GetMapping("/projectClazzTreeData")
    @ResponseBody
    public List<Map<String, Object>> projectClazzTreeData(Project pro)
    {
        return clazzService.projectClazzTreeData(pro);
    }
    
    /**
     * 加载模块学生（数据权限）列表树
     */
    @GetMapping("/moduleStudentTreeData")
    @ResponseBody
    public List<Map<String, Object>> moduleStudentTreeData(ProjectCaseModule mod)
    {
        return clazzService.moduleStudentTreeData(mod);
    }
    

    /**
     * 根据班级id查学生
     */
    @GetMapping("/selectClazzStudent/{clazzId}")
    @ResponseBody
    public List<Student> selectClazzStudent(@PathVariable("clazzId") Integer clazzId, ModelMap mmap)
    {
    	
        return clazzService.selectStudentByClazzId(clazzId);
    }
    
}
