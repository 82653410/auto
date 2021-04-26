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
		//String path = "D:\\web_task\\TestFrame\\lib\\";   //ָ������·��
		classname = classname.replace('.', '/') + ".class";

		//JavaBaseTest.LogUtil.APP.info("����ȥ������·����" + path + "���²����ࡾ" + classname + "�� ��");
		System.out.println("����ȥ������·����" + path + "���²����ࡾ" + classname + "�� ��");
		if (path.charAt(path.length() - 1) != '\\') {
			path += '\\';
		}
		File file = new File(path);
		if (!file.exists()) {
			//JavaBaseTest.LogUtil.ERROR.error("�Ҳ�������ָ��������·����");
			System.out.println("�Ҳ�������ָ��������·����");
			return;
		}
		
		String[] filelist = file.list();
		assert filelist != null;
		for (String s : filelist) {
			File temp = new File(path + s);
			if ((temp.isDirectory() && !temp.isHidden() && temp.exists()) && s.equals(classname)) {
				count++;
				System.out.println("��" + path + s + "�����ҵ���" + count + "��JAR������ָ���࣡");
			} else {
				if (s.endsWith("jar")) {
					try {
						JarFile jarfile = new JarFile(path + s);
						for (Enumeration<JarEntry> e = jarfile.entries(); e.hasMoreElements(); ) {
							String name = e.nextElement().toString();
							if (name.equals(classname) || name.contains(classname)) {
								count++;
								//JavaBaseTest.LogUtil.APP.info("��"+path + filelist[i]+"�����ҵ���"+count+"��JAR������ָ���࣡");
								System.out.println("��" + path + s + "�����ҵ���" + count + "��JAR������ָ���࣡");
							}
						}
						jarfile.close();
					} catch (Exception ignored) {

					}
				}
			}
		}
		
		if(count==0){
			//JavaBaseTest.LogUtil.APP.info("û���ڵ�ǰ·�����ҵ�ָ����");
			System.out.println("û���ڵ�ǰ·�����ҵ�ָ����");
		}else if(count==1){
			//JavaBaseTest.LogUtil.APP.info("�ڵ�ǰ·����ֻ�ҵ�һ�����ڵ�ָ���࣬���������ͻ�����");
			System.out.println("�ڵ�ǰ·����ֻ�ҵ�һ�����ڵ�ָ���࣬���������ͻ�����");
		}
	}
	
	static public void main(String[] args) {
		String path = args[0];
		String classname = args[1];
		findClassInLocalSystem(path,classname);
	}
	
}