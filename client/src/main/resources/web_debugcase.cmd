title Web界面用例调试【%1】
set classpath=%CLASSPATH%;.\client;
@echo Web界面调试用例接口
@echo 参数说明 依次为：用例编号 执行者
java -Djava.ext.dirs=./lib;.%3 com.accp.execution.WebDebugExecute %1 %2
@echo 当前用例调试窗口将在90秒后退出
ping 127.0.0.1 -n 90 >nul
exit