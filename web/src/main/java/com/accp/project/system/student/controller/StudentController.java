package com.accp.project.system.student.controller;

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
import com.accp.project.system.student.domain.Student;
import com.accp.project.system.student.service.IStudentService;
import com.accp.project.system.clazz.domain.Clazz;
import com.accp.project.system.clazz.service.IClazzService;
import com.accp.project.system.project.domain.Project;
import com.accp.project.system.role.domain.Role;

/**
 * 班级信息
 * 

 */
@Controller
@RequestMapping("/system/student")
public class StudentController extends BaseController
{
    @Autowired
    private IStudentService studentService;
    

    @Autowired
    private IClazzService clazzService;

    @RequiresPermissions("system:student:view")
    @GetMapping()
    public String student(ModelMap mmap)
    {
    	mmap.put("clazzs", clazzService.selectClazzList(new Clazz()));
        return "system/student/student";
    }

    @RequiresPermissions("system:student:list")
    @GetMapping("/list")
    @ResponseBody
    public List<Student> list(Student student)
    {
        return studentService.selectStudentList(student);
    }
    
    @RequiresPermissions("system:student:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo plist(Student student)
    {
    	List<Student> list = studentService.selectStudentList(student);
    	
        return getDataTable(list);
    }
    
    /**
     * 新增学生
     */
    @GetMapping("/add/{clazzId}")
    public String add(@PathVariable("clazzId") Long clazzId, ModelMap mmap)
    {
    	mmap.put("clazz", clazzService.selectClazzById(clazzId));
        return "system/student/add";
    }
    
    /**
     * 修改
     */
    @GetMapping("/edit/{studentNo}")
    public String edit(@PathVariable("studentNo") String studentNo, ModelMap mmap)
    {
        Student stu = studentService.selectStudentByNo(studentNo);
        System.out.println(stu);
        System.out.println(clazzService.selectClazzById(stu.getClazzId()));
        mmap.put("clazz", clazzService.selectClazzById(stu.getClazzId()));
        mmap.put("stu", stu);
        return "system/student/edit";
    }
    /**
     * 保存
     */
    @Log(title = "学生管理", businessType = BusinessType.UPDATE)
    @RequiresPermissions("system:clazz:edit")
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(Student student)
    {
        return toAjax(studentService.updateStudent(student));
    }
    /**
     * 新增保存学生
     */
    @Log(title = "学生管理", businessType = BusinessType.INSERT)
    @RequiresPermissions("system:student:add")
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(Student student)
    {
        return toAjax(studentService.insertStudent(student));
    }

    /**
     * 校验学号
     */
    @PostMapping("/checkStudentNoUnique")
    @ResponseBody
    public String checkStudentNoUnique(Student stu)
    {
        return studentService.checkStudentNoUnique(stu);
    }
    
    /**
     * 删除
     */
    @Log(title = "学生管理", businessType = BusinessType.DELETE)
    @RequiresPermissions("system:student:remove")
    @PostMapping("/remove/{studentNo}")
    @ResponseBody
    public AjaxResult remove(@PathVariable("studentNo") String studentNo)
    {
        
        return toAjax(studentService.deleteStudentByNo(studentNo));
    }
    
    
    
}
