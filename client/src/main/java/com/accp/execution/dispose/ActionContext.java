package com.accp.execution.dispose;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.accp.execution.dispose.actionkeyword.Action;
import com.accp.execution.dispose.actionkeyword.ActionKeyWordParser;
import com.accp.utils.LogUtil;
import org.reflections.Reflections;

/**
 * ���趯�����������ݲ������ɲ�ͬ�Ķ����ؼ������ͣ�ִ����Ӧ�Ľ���
 *
 * 
 */
@SuppressWarnings("rawtypes")
public class ActionContext {

    private static Map<String, Class> allActions;

    static {
        Reflections reflections = new Reflections("com.accp.execution.dispose.actionkeyword");
        Set<Class<?>> annotatedClasses =
                reflections.getTypesAnnotatedWith(Action.class);
        allActions = new ConcurrentHashMap<>();
        for (Class<?> classObject : annotatedClasses) {
            Action action = classObject
                    .getAnnotation(Action.class);
            allActions.put(action.name(), classObject);
        }
        allActions = Collections.unmodifiableMap(allActions);
    }

    private ActionKeyWordParser action;

    public ActionContext(String name){

        if (allActions.containsKey(name)) {
        	LogUtil.APP.info("Created Action name is {}", name);
            try {
                action = (ActionKeyWordParser) allActions.get(name).newInstance();
            } catch (InstantiationException | IllegalAccessException ex) {
            	LogUtil.APP.error("Instantiate Action failed", ex);
            }
        } else {
        	LogUtil.APP.warn("Specified Action name {} does not exist", name);
        }
    }

    public String parse(String actionParams,String testResult,String actionKeyWord) {
        if(null != action){
            testResult = action.parse(actionParams, testResult);
        }else {
            testResult="δ��������Ӧ�����ؼ��֣�ֱ�������˶���������ؼ��֣�"+actionKeyWord;
            LogUtil.APP.warn("δ��������Ӧ�����ؼ��֣�ֱ�������˶���������ؼ��֣�{}",actionKeyWord);
        }
        return testResult;
    }

}
