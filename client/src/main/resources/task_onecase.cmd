title ��������ִ�С�%2�� 
set classpath=%CLASSPATH%;.\client;
@echo ��Ŀ��������ִ��
@echo ����˵�� ����Ϊ����Ŀ���� tastId ������� �����汾�� 
java -Djava.ext.dirs=./lib;.%4 com.accp.execution.OneCaseExecute %1 %2 %3
@echo ��ǰ��������ִ�д��ڽ���90����˳�
ping 127.0.0.1 -n 90 >nul
exit
