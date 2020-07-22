package com.accp.project.qualitymanagmt.qaAccident.mapper;

import java.util.List;
import java.util.Map;

import com.accp.project.qualitymanagmt.qaAccident.domain.QaAccident;
import org.springframework.stereotype.Component;

/**
 * 生产事故登记 数据层
 * 
 * uckyframe
 *
 */
@Component
public interface QaAccidentMapper 
{
	/**
     * 查询生产事故登记信息
     * 
     * @param accidentId 生产事故登记ID
     * @return 生产事故登记信息
     */
	QaAccident selectQaAccidentById(Integer accidentId);
	
	/**
     * 查询生产事故登记列表
     * 
     * @param qaAccident 生产事故登记信息
     * @return 生产事故登记集合
     */
	List<QaAccident> selectQaAccidentList(QaAccident qaAccident);
	
	/**
     * 新增生产事故登记
     * 
     * @param qaAccident 生产事故登记信息
     * @return 结果
     */
	int insertQaAccident(QaAccident qaAccident);
	
	/**
     * 修改生产事故登记
     * 
     * @param qaAccident 生产事故登记信息
     * @return 结果
     */
	int updateQaAccident(QaAccident qaAccident);
	
	/**
     * 删除生产事故登记
     * 
     * @param accidentId 生产事故登记ID
     * @return 结果
     */
	int deleteQaAccidentById(Integer accidentId);
	
	/**
     * 批量删除生产事故登记
     * 
     * @param accidentIds 需要删除的数据ID
     * @return 结果
     */
	int deleteQaAccidentByIds(String[] accidentIds);
	
	/**
	 * 事故根据类型分组统计
	 * @param qaAccident 事故对象
	 * @return 返回LIST
	 *
	 *
	 */
	List<Map<Object,Object>> selectGroupByAccidentType(QaAccident qaAccident);
	
	/**
	 * 事故根据级别分组统计
	 * @param qaAccident 事故对象
	 * @return 返回LIST
	 *
	 *
	 */
	List<Map<Object,Object>> selectGroupByAccidentLevel(QaAccident qaAccident);
}