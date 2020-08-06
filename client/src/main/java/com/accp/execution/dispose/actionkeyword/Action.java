package com.accp.execution.dispose.actionkeyword;

import java.lang.annotation.*;

/**
 * 动作关键字注解定义
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
