package com.accp.utils;

import java.io.File;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

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
public class JarClassFind {

	private static void findClassInLocalSystem(String path,String classname) {
		int count = 0;
		//String path = "D:\\web_task\\TestFrame\\lib\\";   //指定查找路径
		classname = classname.replace('.', '/') + ".class";

		//JavaBaseTest.LogUtil.APP.info("即将去服务器路径【" + path + "】下查找类【" + classname + "】 ！");
		System.out.println("即将去服务器路径【" + path + "】下查找类【" + classname + "】 ！");
		if (path.charAt(path.length() - 1) != '\\') {
			path += '\\';
		}
		File file = new File(path);
		if (!file.exists()) {
			//JavaBaseTest.LogUtil.ERROR.error("找不到本地指定包查找路径！");
			System.out.println("找不到本地指定包查找路径！");
			return;
		}
		
		String[] filelist = file.list();
		assert filelist != null;
		for (String s : filelist) {
			File temp = new File(path + s);
			if ((temp.isDirectory() && !temp.isHidden() && temp.exists()) && s.equals(classname)) {
				count++;
				System.out.println("【" + path + s + "】查找到第" + count + "个JAR包存在指定类！");
			} else {
				if (s.endsWith("jar")) {
					try {
						JarFile jarfile = new JarFile(path + s);
						for (Enumeration<JarEntry> e = jarfile.entries(); e.hasMoreElements(); ) {
							String name = e.nextElement().toString();
							if (name.equals(classname) || name.contains(classname)) {
								count++;
								//JavaBaseTest.LogUtil.APP.info("【"+path + filelist[i]+"】查找到第"+count+"个JAR包存在指定类！");
								System.out.println("【" + path + s + "】查找到第" + count + "个JAR包存在指定类！");
							}
						}
						jarfile.close();
					} catch (Exception ignored) {

					}
				}
			}
		}
		
		if(count==0){
			//JavaBaseTest.LogUtil.APP.info("没有在当前路径下找到指定类");
			System.out.println("没有在当前路径下找到指定类");
		}else if(count==1){
			//JavaBaseTest.LogUtil.APP.info("在当前路径下只找到一个存在的指定类，不存在类冲突情况！");
			System.out.println("在当前路径下只找到一个存在的指定类，不存在类冲突情况！");
		}
	}
	
	static public void main(String[] args) {
		String path = args[0];
		String classname = args[1];
		findClassInLocalSystem(path,classname);
	}
	
}
