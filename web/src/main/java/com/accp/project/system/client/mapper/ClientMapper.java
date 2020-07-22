package com.accp.project.system.client.mapper;

import com.accp.project.system.client.domain.Client;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 客户端管理 数据层
 * 
 * uckyframe
 *
 */
@Component
public interface ClientMapper 
{
	/**
     * 查询客户端管理信息
     * 
     * @param clientId 客户端管理ID
     * @return 客户端管理信息
     */
	public Client selectClientById(Integer clientId);
	
	/**
     * 查询客户端管理列表
     * 
     * @param client 客户端管理信息
     * @return 客户端管理集合
     */
	public List<Client> selectClientList(Client client);
	
	/**
     * 新增客户端管理
     * 
     * @param client 客户端管理信息
     * @return 结果
     */
	public int insertClient(Client client);
	
	/**
     * 修改客户端管理
     * 
     * @param client 客户端管理信息
     * @return 结果
     */
	public int updateClient(Client client);
	
	/**
	 * 根据客户端IP修改状态以及备注
	 * @param client
	 * @return
	 *
	 *
	 */
	public int updateClientStatusByIp(Client client);
	
	/**
     * 删除客户端管理
     * 
     * @param clientId 客户端管理ID
     * @return 结果
     */
	public int deleteClientById(Integer clientId);
	
	/**
     * 批量删除客户端管理
     * 
     * @param clientIds 需要删除的数据ID
     * @return 结果
     */
	public int deleteClientByIds(String[] clientIds);
	
	/**
	 * 检查客户端名称唯一性
	 * @param clientName
	 * @return
	 *
	 *
	 */
	public Client checkClientNameUnique(String clientName);
	
	/**
	 * 检查客户端IP唯一性
	 * @param clientIp
	 * @return
	 *
	 *
	 */
	public Client checkIpUnique(String clientIp);


	/**
	 * 根据客户端名称查找客户端(netty方式)
	 * @param clientIP
	 * @return
	 */
	public Client selectClientByClientIP(String clientIP);
}