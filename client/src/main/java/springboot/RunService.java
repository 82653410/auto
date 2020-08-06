package springboot;

import java.io.File;
import java.net.InetAddress;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import cn.hutool.core.util.BooleanUtil;
import com.accp.netty.NettyClient;
import com.accp.utils.config.SysConfig;

/**
 * 
 * 
 * 
 *
 * 
 *
 *
 */
@SpringBootApplication
public class RunService {
	/*
	 * 注意：请不要在此处使用IDEA运行（Run）客户端，客户端的启动方式，请参照官网文档
	 * 如果IDEA工具上运行时出现乱码，请设置VM options,添加-Dfile.encoding=GBK即可
	 * */

	private static final Logger log = LoggerFactory.getLogger(RunService.class);
	private static final Boolean NETTY_MODEL= BooleanUtil.toBoolean(SysConfig.getConfiguration().getProperty("netty.model"));
	public static String CLIENT_IP = "";
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PropertyConfigurator.configure(System.getProperty("user.dir") + File.separator +"bootlog4j.conf");
		SpringApplication.run(RunService.class, args);
		try {
			CLIENT_IP = InetAddress.getLocalHost().getHostAddress();
			log.info("启动客户端监听,请稍后......监听IP:{}",CLIENT_IP);
		} catch (Exception e) {
			log.error("获取服务IP出现异常......", e);
		}
		if(NETTY_MODEL){
			try {
				log.info("##################客户端netty开启#################");
				NettyClient.start();
			} catch (Exception e) {
				log.error("连接服务端netty异常......");
				e.printStackTrace();
			}
		}else{
			HttpImpl.checkHostNet();
		}
	}

}
