package com.accp.project.monitor.job.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.accp.common.constant.ClientConstants;
import com.accp.common.utils.client.HttpRequest;
import com.accp.framework.config.AppConfig;
import com.accp.project.system.client.domain.Client;
import com.accp.project.system.client.service.IClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * 客户端心跳监听 
 * 
 * 
 * 
 *
 *
 * 
 */
@Component("clientHeart")
public class ClientHeart {
    @Autowired
    private IClientService clientService;

    @Autowired
    private AppConfig lfConfig;

    public static ClientHeart clientHeart;

    @PostConstruct
    public void init() {
        clientHeart = this;
    }

    /* 避免频繁查询数据库，记录客户端状态 0 正常 1 链接失败 2 状态未知 */
    private static Map<String, Integer> clientListen = new HashMap<>(0);

    public static void iniClientListen(String clientIp) {
        clientListen.put(clientIp, 2);
    }

    public static void removeClientListen(String clientIp) {
        clientListen.remove(clientIp);
    }

    public static Integer getClientStatus(String clientIp) {
        return clientListen.get(clientIp);
    }

    public static void setClientStatus(String clientIp, Integer status) {
        clientListen.put(clientIp, status);
    }

    public void heartTask(String params) {
        try {
            String result = HttpRequest.httpClientGet(
                    "http://" + params + ":" + ClientConstants.CLIENT_MONITOR_PORT + "/getClientStatus",new Client(),
					new HashMap<>(0), 3000);
            JSONObject jsonObject = JSON.parseObject(result);

            if (null == ClientHeart.getClientStatus(params)) {
                ClientHeart.setClientStatus(params, 1);
            }

            if ("success".equals(jsonObject.get("status"))) {
                if (ClientHeart.getClientStatus(params) != 0) {
                    Client client = new Client();
                    if (lfConfig.getVersion().equals(jsonObject.get("version"))) {
                        client.setClientIp(params);
                        client.setRemark("检测客户端状态成功");
                        client.setStatus(0);
                        clientService.updateClientStatusByIp(client);
                    } else {
                        client.setClientIp(params);
                        client.setRemark(
                                "客户端(" + jsonObject.get("version") + ")与服务器(" + lfConfig.getVersion() + ")版本不一致");
                        client.setStatus(1);
                        clientService.updateClientStatusByIp(client);
                    }
                }
            } else {
                if (ClientHeart.getClientStatus(params) != 1) {
                    Client client = new Client();
                    client.setClientIp(params);
                    client.setRemark("客户端返回状态异常");
                    client.setStatus(1);
                    clientService.updateClientStatusByIp(client);
                }
            }
        } catch (RuntimeException e) {
            if (null == ClientHeart.getClientStatus(params) || ClientHeart.getClientStatus(params) != 1) {
                Client client = new Client();
                client.setClientIp(params);
                client.setRemark("检测客户端远程异常");
                client.setStatus(1);
                clientService.updateClientStatusByIp(client);
            }
        }
    }

}
