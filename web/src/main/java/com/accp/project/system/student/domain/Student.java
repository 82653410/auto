package com.accp.project.system.student.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.accp.project.system.clazz.domain.Clazz;

public class Student {
	/**学号**/
	private String studentNo;
	/**学生姓名**/
	private String studentName;
	/**手机号码**/
	private String studentPhone;
	/**班级id**/
	private Long clazzId;
	/** 删除标志（0代表存在 2代表删除） */
    private String delFlag;
    
    private Clazz clazz;
    
    
    
	public Clazz getClazz() {
		return clazz;
	}
	public void setClazz(Clazz clazz) {
		this.clazz = clazz;
	}
	public String getDelFlag() {
		return delFlag;
	}
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
	public String getStudentNo() {
		return studentNo;
	}
	public void setStudentNo(String studentNo) {
		this.studentNo = studentNo;
	}
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public String getStudentPhone() {
		return studentPhone;
	}
	public void setStudentPhone(String studentPhone) {
		this.studentPhone = studentPhone;
	}
	public Long getClazzId() {
		return clazzId;
	}
	public void setClazzId(Long clazzId) {
		this.clazzId = clazzId;
	}
	
	@Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("studentNo", getStudentNo())
            .append("studentName", getStudentName())
            .append("studentPhone", getStudentPhone())
            .append("clazzId", getClazzId())
            .toString();
    }
}
