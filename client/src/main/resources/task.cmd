title ��������ִ�С�%1�� 
set classpath=%CLASSPATH%;.\client;
@echo ָ������ִ��
@echo ��Ŀ���� ����ID
java -Djava.ext.dirs=./lib;.%2 com.accp.execution.RunAutomationTest %1
@echo ��ǰ��������ִ�д��ڽ���90����˳�
ping 127.0.0.1 -n 90 >nul
exit