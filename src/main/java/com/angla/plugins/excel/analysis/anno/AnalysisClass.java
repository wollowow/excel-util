package com.angla.plugins.excel.analysis.anno;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @program: excel-util
 * @description: excel实体类
 * @author: angla
 * @create: 2018-08-08 09:49
 * @Version 1.0
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface AnalysisClass {
    int maxRow() default Integer.MAX_VALUE;     //最大行数
    int minRow() default 1;                     //最小行数
    int maxColumn() default Integer.MAX_VALUE;  //最大列数
    int minColumn() default 1;                  //最小列数
}
