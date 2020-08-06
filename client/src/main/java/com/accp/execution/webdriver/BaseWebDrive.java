package com.accp.execution.webdriver;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import com.accp.utils.LogUtil;
import com.accp.utils.config.SysConfig;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.Augmenter;

import cn.hutool.core.util.BooleanUtil;

/**
 * 
 * 
 * 
 *
 * 
 * 
 *
 * 
 * 
 */
public class BaseWebDrive {

	/**
	 * 进测试结果进行截图
	 * @param driver 驱动
	 * @param imgname 图片名称
	 */
	public static void webScreenShot(WebDriver driver, String imgname) {
		String relativelyPath = System.getProperty("user.dir");
		String pngpath=relativelyPath +File.separator+ "log"+File.separator+"ScreenShot" +File.separator+ imgname + ".png";

		// 对远程系统进行截图
		driver = new Augmenter().augment(driver);
		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(scrFile, new File(pngpath));
		} catch (IOException e) {
			LogUtil.APP.error("截图操作失败，抛出异常请查看日志...", e);
		}
		scrFile.deleteOnExit();
		LogUtil.APP
				.info("已对当前界面进行截图操作，可通过用例执行界面的日志明细查看，也可以前往客户端上查看...【{}】",pngpath);
	}

	/**
	 * 在自动化过程中加入点击显示效果
	 * @param driver 驱动
	 * @param element 定位元素
	 *
	 * 
	 */
    public static void highLightElement(WebDriver driver, WebElement element){
    	Properties properties = SysConfig.getConfiguration();
    	boolean highLight = BooleanUtil.toBoolean(properties.getProperty("webdriver.highlight"));

    	if(highLight){
            JavascriptExecutor js = (JavascriptExecutor) driver;
            /*调用js将传入参数的页面元素对象的背景颜色和边框颜色分别设定为黄色和红色*/
            js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, "background: yellow; border:2px solid red;");
    	}
    }

}
