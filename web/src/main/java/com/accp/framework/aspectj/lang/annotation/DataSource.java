package com.accp.framework.aspectj.lang.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.accp.framework.aspectj.lang.enums.DataSourceType;

/**
 * 自定义多数据源切换注解
 * 
 
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DataSource
{
    /**
     * 切换数据源名称
     */
    DataSourceType value() default DataSourceType.MASTER;
}
