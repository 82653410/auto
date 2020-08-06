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
	 * �����Խ�����н�ͼ
	 * @param driver ����
	 * @param imgname ͼƬ����
	 */
	public static void webScreenShot(WebDriver driver, String imgname) {
		String relativelyPath = System.getProperty("user.dir");
		String pngpath=relativelyPath +File.separator+ "log"+File.separator+"ScreenShot" +File.separator+ imgname + ".png";

		// ��Զ��ϵͳ���н�ͼ
		driver = new Augmenter().augment(driver);
		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(scrFile, new File(pngpath));
		} catch (IOException e) {
			LogUtil.APP.error("��ͼ����ʧ�ܣ��׳��쳣��鿴��־...", e);
		}
		scrFile.deleteOnExit();
		LogUtil.APP
				.info("�ѶԵ�ǰ������н�ͼ��������ͨ������ִ�н������־��ϸ�鿴��Ҳ����ǰ���ͻ����ϲ鿴...��{}��",pngpath);
	}

	/**
	 * ���Զ��������м�������ʾЧ��
	 * @param driver ����
	 * @param element ��λԪ��
	 *
	 * 
	 */
    public static void highLightElement(WebDriver driver, WebElement element){
    	Properties properties = SysConfig.getConfiguration();
    	boolean highLight = BooleanUtil.toBoolean(properties.getProperty("webdriver.highlight"));

    	if(highLight){
            JavascriptExecutor js = (JavascriptExecutor) driver;
            /*����js�����������ҳ��Ԫ�ض���ı�����ɫ�ͱ߿���ɫ�ֱ��趨Ϊ��ɫ�ͺ�ɫ*/
            js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, "background: yellow; border:2px solid red;");
    	}
    }

}
