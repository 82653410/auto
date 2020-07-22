package com.accp.framework.config;

import java.io.File;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 读取项目中相关的配置
 *
 *
 *
 *
 *
 *
 */
@Component
@ConfigurationProperties(prefix = "app")
public class AppConfig
{
    /** 项目名称 */
    private String name;
    /** 版本 */
    private String version;
    /** 版权年份 */
    private String copyrightYear;
    /** 上传路径 */
    private static String profile;
    /** 获取地址开关 */
    private static boolean addressEnabled;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getVersion()
    {
        return version;
    }

    public void setVersion(String version)
    {
        this.version = version;
    }

    public String getCopyrightYear()
    {
        return copyrightYear;
    }

    public void setCopyrightYear(String copyrightYear)
    {
        this.copyrightYear = copyrightYear;
    }

    public static String getProfile()
    {
    	setProfile();
        return profile;
    }

    public static void setProfile()
    {
		// 文件目录
		String pathName = System.getProperty("user.dir")+File.separator+"profile";
		File file = new File(pathName);
		if (file.exists()) {
			if (!file.isDirectory()) {
				file.mkdir();
			}
		} else {
				file.mkdir();
		}
    	AppConfig.profile = file.getPath()+File.separator;
    }

    public static boolean isAddressEnabled()
    {
        return addressEnabled;
    }

    public void setAddressEnabled(boolean addressEnabled)
    {
    	AppConfig.addressEnabled = addressEnabled;
    }

    public static String getAvatarPath()
    {
        return profile + "avatar"+File.separator;
    }

    public static String getDownloadPath()
    {
        return profile + "download"+File.separator;
    }
    
    public static String getUploadPath()
    {
        return profile + "upload"+File.separator;
    }
}
