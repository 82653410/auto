package com.accp.execution.dispose.actionkeyword;

import java.lang.annotation.*;

/**
 * �����ؼ���ע�ⶨ��
 *
 * 
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Action {
    String name() default "";
}
