package com.accp.project.system.student.mapper;

import java.util.List;
import com.accp.project.system.student.domain.Student;

import org.springframework.stereotype.Component;

/**
 * 部门管理 数据层
 * 

 */
@Component
public interface StudentMapper
{
	/**
	 * 查询学生list
	 * @param student
	 * @return
	 */
	List<Student> selectStudentList(Student student);
	
	/**
     * 新增学生信息
     * 
     * @param student 部门信息
     * @return 结果
     */
    int insertStudent(Student student);
    
    /**
     * 检查学号是否存在
     * @param student
     * @return
     */
    int checkStudentNo(Student student);
    
    /**
     * 删除学生
     * 
     * @param 学号
     * @return 结果
     */
    int deleteStudentByNo(String studentNo);
    
    /**
     * 学号找学生
     * @param studentNo
     * @return
     */
    Student selectStudentByNo(String studentNo);
    
    int updateStudent(Student stu);
}
